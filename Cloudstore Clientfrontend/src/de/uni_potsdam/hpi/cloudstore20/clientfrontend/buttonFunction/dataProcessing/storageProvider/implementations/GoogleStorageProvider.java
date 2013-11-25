package de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.implementations;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;


import org.jets3t.service.ServiceException;
import org.jets3t.service.impl.rest.httpclient.GoogleStorageService;
import org.jets3t.service.model.GSBucket;
import org.jets3t.service.model.GSObject;
import org.jets3t.service.security.GSCredentials;


import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.StorageProvider;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.StorageProviderException;
import de.uni_potsdam.hpi.cloudstore20.meta.helper.FileHelper;

public class GoogleStorageProvider extends StorageProvider {


	protected GSCredentials gsCredentials;
	
	// To communicate with Google Storage use the GoogleStorageService.
	protected GoogleStorageService gsService;

	/**
	 * Application Name of Google account
	 */
	protected String appName = "cloudstore";
	protected String projectId = null;

	/**
	 * Bucketname, where the data will be stored
	 */
	/* https://developers.google.com/storage/docs/bucketnaming?hl=de-DE#requirements */
	protected String BUCKET_NAME = Integer.toHexString(appName.hashCode());
	
	
	public GoogleStorageProvider(String providerName)
			throws StorageProviderException {
		super(providerName);
		initBucket();
	}
	
	public GoogleStorageProvider(String providerName, String location) 
			throws StorageProviderException {
		super(providerName, location);
		initBucket();
	}

	@Override
	public String uploadFile(File file) throws StorageProviderException {

		try {
			GSObject gsFile = new GSObject(file);
//			long t1,t2;
//			t1 = System.currentTimeMillis();
			gsFile = this.getService().putObject(this.getRemoteFolderName(), gsFile);
//			t2 = System.currentTimeMillis();
			
			return file.getName();//this.returnValue(file.getName(), t1, t2, remoteFolderName);
		} catch (ServiceException e) {
			throw new StorageProviderException(this.providerName, e);
		} catch (NoSuchAlgorithmException e) {
			throw new StorageProviderException(this.providerName, e);
		} catch (IOException e) {
			throw new StorageProviderException(this.providerName, e);
		}
	}

	@Override
	public File downloadFile(String fileID) throws StorageProviderException {
		try {
//			long t1,t2;

			GSObject gsFile = this.getService().getObject(this.getRemoteFolderName(), fileID);
			File localFile = new File(downloadFolder + File.separator + fileID);
			FileOutputStream fos = new FileOutputStream(localFile);
			
//			t1 = System.currentTimeMillis();
			FileHelper.copyStream(gsFile.getDataInputStream(), fos);
//			t2 = System.currentTimeMillis();
			
			fos.close();
			return localFile;//this.returnValue(true, t1, t2, fileID, remoteFolderName);
		} catch (ServiceException e) {
			throw new StorageProviderException(this.providerName, e);
		} catch (IOException e) {
			throw new StorageProviderException(this.providerName, e);
		}
	}

	@Override
	public void deleteFile(String fileID) throws StorageProviderException {

		try {
			this.getService().deleteObject(this.getRemoteFolderName(), new GSObject(fileID).getKey());
//			return true;
		} catch (ServiceException e) {
			throw new StorageProviderException(this.providerName, e);
		}
	}

	@Override
	public String getFileHash(String fileID) throws StorageProviderException {
		try {
			return this.getService().getObjectDetails(BUCKET_NAME + "/"+ remoteFolderName, fileID).getMd5HashAsHex();
		} catch (ServiceException e) {
			throw new StorageProviderException(this.providerName, e);
		}
	}
	
	
	
	// private methods
	
	protected String getRemoteFolderName(){
		return BUCKET_NAME + "/" + remoteFolderName;
	}
	
	private GoogleStorageService getService(){
		return this.gsService;
	}
	
	private void initBucket(){

		try {
			this.gsCredentials = new GSCredentials(this.getAccessKey(), this.getSecretKey());
			this.gsService = new GoogleStorageService(this.gsCredentials);
			String localBucket = location.toLowerCase() + BUCKET_NAME;
//			this.setProviderName("Google");
			GSBucket bucket = null;
			if (this.projectId == null) {
				bucket = (GSBucket) gsService.getBucket(localBucket);
			} else {
				GSBucket[] buckets = gsService.listAllBuckets(this.projectId);
				for (GSBucket b: buckets) {
					if (b.getName().equals(localBucket)) {
						bucket = b;
					}
				}				
			}
			if (bucket == null){
				if (this.projectId == null) {
					GSBucket newBucket = new GSBucket(localBucket);
					newBucket.setLocation(location);
					gsService.createBucket(newBucket);					
				} else {					
					gsService.createBucket(localBucket, location, null, this.projectId);					
				}
			}
			BUCKET_NAME = localBucket;	// update the bucketname for the chosen region
		} catch (ServiceException e) {
			// TODO
//			this.lastError = new StorageProviderException(this.providerName, e);
		}
	}
	
	private String getAccessKey(){
		// TODO
		return "";
	}
	
	private String getSecretKey(){
		// TODO
		return "";
	}
	
}
