package de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.implementations;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.jets3t.service.S3Service;
import org.jets3t.service.S3ServiceException;
import org.jets3t.service.ServiceException;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.S3Bucket;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.security.AWSCredentials;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.StorageProvider;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.StorageProviderConfig;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.StorageProviderException;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.enums.PROVIDER_ENUM;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.enums.STORAGE_PROVIDER_CONFIG;
import de.uni_potsdam.hpi.cloudstore20.meta.helper.FileHelper;

public class AmazonStorageProvider extends StorageProvider {

	protected String appName = "cloudstore";
	protected String BUCKET_NAME = Integer.toHexString(appName.hashCode());

	protected RestS3Service awsService = null;
	protected AWSCredentials awsCredentials;

	public AmazonStorageProvider() throws StorageProviderException {

		super("Amazon", defaultLocation());
		initBucket();
	}

	public AmazonStorageProvider(String location) throws StorageProviderException {

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
			if (bucket == null) {
				S3Bucket newBucket = new S3Bucket(localBucket, location);
				newBucket.setLocation(location);
				awsService.createBucket(newBucket);
			}
			BUCKET_NAME = localBucket; // update the bucketname for the chosen region
		} catch (ServiceException e) {
			// this.lastError = new ProviderException(this.providerName, e);
		}
	}

	@Override
	public String uploadFile(File file) throws StorageProviderException {

		FileInputStream fis = null;
		try {
			class Uploader implements Callable<Boolean> {
				S3Service service = null;
				String remoteFolder = null;
				S3Object s3File = null;
				
				Uploader(S3Service service, String remoteFolder, S3Object s3File) { 
		        	this.service = service; this.remoteFolder = remoteFolder; this.s3File = s3File;}
		        public Boolean call() throws S3ServiceException{
		        	service.putObject(remoteFolder, s3File);
		            return true; 
		        }
		    }
			
			fis = new FileInputStream(file);
			this.waitAndUpdateStatus(file.length(), fis.getChannel(), 
					new Uploader(this.getService(), this.getRemoteFolderName(), new S3Object(file)));
			
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
			S3Object gsFile = this.getService().getObject(this.getRemoteFolderName(), fileID);

			File localFile = new File(downloadFolder + File.separator + fileID);
			fos = new FileOutputStream(localFile);

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
			long fileSize = gsFile.getContentLength();
			this.waitAndUpdateStatus(fileSize, fos.getChannel(), new Downloader(gsFile.getDataInputStream(), fos));
			
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

	protected String getRemoteFolderName() {

		return BUCKET_NAME;// + "/" + remoteFolderName;
	}

	private S3Service getService() {

		return this.awsService;
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
