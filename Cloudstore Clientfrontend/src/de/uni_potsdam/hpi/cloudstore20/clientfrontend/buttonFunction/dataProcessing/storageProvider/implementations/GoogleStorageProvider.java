package de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.implementations;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.jets3t.service.ServiceException;
import org.jets3t.service.impl.rest.httpclient.GoogleStorageService;
import org.jets3t.service.model.GSBucket;
import org.jets3t.service.model.GSObject;
import org.jets3t.service.security.GSCredentials;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.StorageProvider;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.StorageProviderConfig;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.StorageProviderException;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.enums.PROVIDER_ENUM;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.enums.STORAGE_PROVIDER_CONFIG;
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

	public GoogleStorageProvider() throws StorageProviderException {

		super("Google", defaultLocation());
		initBucket();
	}

	public GoogleStorageProvider(String location) throws StorageProviderException {

		super("Google", location);
		initBucket();
	}

	@Override
	public String uploadFile(File file) throws StorageProviderException {

		FileInputStream fis = null;
		try {
			class Uploader implements Callable<Boolean> {
				GoogleStorageService service = null;
				String remoteFolder = null;
				GSObject s3File = null;
				
				Uploader(GoogleStorageService service, String remoteFolder, GSObject gsObject) { 
		        	this.service = service; this.remoteFolder = remoteFolder; this.s3File = gsObject;}
		        public Boolean call() throws ServiceException{
		        	service.putObject(remoteFolder, s3File);
		            return true; 
		        }
		    }
			fis = new FileInputStream(file);
			this.waitAndUpdateStatus(file.length(), fis.getChannel(), 
					new Uploader(this.getService(), this.getRemoteFolderName(), new GSObject(file)));
			
			return file.getName();
		} catch (NoSuchAlgorithmException e) {
			throw new StorageProviderException(this.providerName, e);
		} catch (IOException e) {
			throw new StorageProviderException(this.providerName, e);
		} catch (InterruptedException e) {
			throw new StorageProviderException(this.providerName, e);
		} catch (ExecutionException e) {
			throw new StorageProviderException(this.providerName, e);
		} finally {
			this.closeStream(fis);
		}
	}

	@Override
	public File downloadFile(String fileID) throws StorageProviderException {

		FileOutputStream fos = null;
		try {
			class Downloader implements Callable<Boolean> {
				InputStream is = null; 
				FileOutputStream fos = null;
				public Downloader(InputStream fis, FileOutputStream fos) { this.is = fis; this.fos = fos;}
				@Override
				public Boolean call() throws Exception {
					FileHelper.copyStream(is, fos, 3);
					return true;
				}
			}

			final GSObject gsFile = this.getService().getObject(this.getRemoteFolderName(), fileID);
			File localFile = new File(downloadFolder + File.separator + fileID);
			fos = new FileOutputStream(localFile);

			this.waitAndUpdateStatus(gsFile.getContentLength(), fos.getChannel(), 
					new Downloader(gsFile.getDataInputStream(), fos));
			
			return localFile;
		} catch (ServiceException e) {
			throw new StorageProviderException(this.providerName, e);
		} catch (IOException e) {
			throw new StorageProviderException(this.providerName, e);
		} catch (InterruptedException e) {
			throw new StorageProviderException(this.providerName, e);
		} catch (ExecutionException e) {
			throw new StorageProviderException(this.providerName, e);
		} finally {
			this.closeStream(fos);
		}
	}

	@Override
	public void deleteFile(String fileID) throws StorageProviderException {

		try {
			this.getService().deleteObject(this.getRemoteFolderName(), new GSObject(fileID).getKey());
			// return true;
		} catch (ServiceException e) {
			throw new StorageProviderException(this.providerName, e);
		}
	}

	@Override
	public String getFileHash(String fileID) throws StorageProviderException {

		try {
			return this.getService().getObjectDetails(BUCKET_NAME + "/" + remoteFolderName, fileID).getMd5HashAsHex();
		} catch (ServiceException e) {
			throw new StorageProviderException(this.providerName, e);
		}
	}

	// private methods

	protected String getRemoteFolderName() {

		return BUCKET_NAME + "/" + remoteFolderName;
	}

	private GoogleStorageService getService() {

		return this.gsService;
	}

	private void initBucket() {

		try {
			this.gsCredentials = new GSCredentials(this.getAccessKey(), this.getSecretKey());
			this.gsService = new GoogleStorageService(this.gsCredentials);
			String localBucket = location.toLowerCase() + BUCKET_NAME;
			// this.setProviderName("Google");
			GSBucket bucket = null;
			if (this.projectId == null) {
				bucket = (GSBucket) gsService.getBucket(localBucket);
			} else {
				GSBucket[] buckets = gsService.listAllBuckets(this.projectId);
				for (GSBucket b : buckets) {
					if (b.getName().equals(localBucket)) {
						bucket = b;
					}
				}
			}
			if (bucket == null) {
				if (this.projectId == null) {
					GSBucket newBucket = new GSBucket(localBucket);
					newBucket.setLocation(location);
					gsService.createBucket(newBucket);
				} else {
					gsService.createBucket(localBucket, location, null, this.projectId);
				}
			}
			BUCKET_NAME = localBucket; // update the bucketname for the chosen region
		} catch (ServiceException e) {
			// TODO
			// this.lastError = new StorageProviderException(this.providerName, e);
		}
	}

	private String getAccessKey() {

		return StorageProviderConfig.getInstance().get(
				PROVIDER_ENUM.fromString(this.getCompleteProviderName()).getConfigCategory(), STORAGE_PROVIDER_CONFIG.Username);
	}

	private String getSecretKey() {

		return StorageProviderConfig.getInstance().get(
				PROVIDER_ENUM.fromString(this.getCompleteProviderName()).getConfigCategory(), STORAGE_PROVIDER_CONFIG.Password);
	}

}
