package de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.implementations;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.StorageProvider;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.StorageProviderException;
import de.uni_potsdam.hpi.cloudstore20.meta.helper.FileHelper;


import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.security.AWSCredentials;
import org.jets3t.service.S3Service;
import org.jets3t.service.ServiceException;
import org.jets3t.service.model.S3Bucket;
import org.jets3t.service.model.S3Object;


public class AmazonStorageProvider extends StorageProvider {

	protected String appName = "cloudstore";
	protected String BUCKET_NAME = Integer.toHexString(appName.hashCode());
		

	protected RestS3Service awsService = null;
	protected AWSCredentials awsCredentials;
	
	public AmazonStorageProvider()
			throws StorageProviderException {
		super("Amazon", defaultLocation());
		initBucket();
	}

	public AmazonStorageProvider(String location)
			throws StorageProviderException {
		super("Amazon", location);
		initBucket();
	}
	
	
	private void initBucket() {
		try {
			this.awsCredentials = new AWSCredentials(this.getAccessKey(), this.getSecretKey());
			this.awsService = new RestS3Service(this.awsCredentials);
			
			// create main bucket for each region one
			String localBucket = location.toLowerCase() + BUCKET_NAME;
			S3Bucket bucket = (S3Bucket) awsService.getBucket(localBucket);
			if(bucket == null){
				S3Bucket newBucket = new S3Bucket(localBucket, location);
				newBucket.setLocation(location);
				awsService.createBucket(newBucket);
			}
			BUCKET_NAME = localBucket;	// update the bucketname for the chosen region
		} catch (ServiceException e) {
//			this.lastError =  new ProviderException(this.providerName, e);
		}
	}

	@Override
	public String uploadFile(File file) throws StorageProviderException {
		try {
			S3Object s3File = new S3Object(file);
			s3File = this.getService().putObject(this.getRemoteFolderName(),  s3File);
			return file.getName();
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

		FileOutputStream fos = null;
		try {
//			long t1,t2;
			S3Object gsFile;
			gsFile = this.getService().getObject(this.getRemoteFolderName(), fileID);
			
			File localFile = new File(downloadFolder + File.separator + fileID);
			fos = new FileOutputStream(localFile);
			
//			t1 = System.currentTimeMillis();
			FileHelper.copyStream(gsFile.getDataInputStream(), fos, 3);
//			t2 = System.currentTimeMillis();
			
			return localFile;
		} catch (ServiceException e) {
			throw new StorageProviderException(this.providerName, e);
		} catch (IOException e) {
			throw new StorageProviderException(this.providerName, e);
		} finally {
			this.closeStream(fos);
		}
	}

	@Override
	public void deleteFile(String fileID) throws StorageProviderException {
		try {
			this.getService().deleteObject(this.getRemoteFolderName(), new S3Object(fileID).getKey());
		} catch (ServiceException e) {
			throw new StorageProviderException(this.providerName, e);
		}
	}

	@Override
	public String getFileHash(String fileID) throws StorageProviderException {
		try {
			S3Object obj = this.getService().getObject(BUCKET_NAME, fileID);
			return obj.getMd5HashAsHex();
		} catch (ServiceException e) {
			throw new StorageProviderException(this.providerName, e);
		}
	}
	
	
	// private methods

	protected String getRemoteFolderName(){
		return BUCKET_NAME;// + "/" + remoteFolderName;
	}
	
	
	private S3Service getService() {
		return this.awsService;
	}

	private String getAccessKey(){
		return this.keys[0];
	}
	
	private String getSecretKey(){
		return this.keys[1];
	}

}
