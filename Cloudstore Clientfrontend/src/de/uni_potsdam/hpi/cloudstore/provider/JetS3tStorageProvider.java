package de.uni_potsdam.hpi.cloudstore.provider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import de.uni_potsdam.hpi.cloudstore.provider.helper.ProviderException;
import de.uni_potsdam.hpi.cloudstore.provider.helper.ProviderReturnValueBoolean;
import de.uni_potsdam.hpi.cloudstore.provider.helper.ProviderReturnValueString;

import org.jets3t.service.S3ServiceException;
import org.jets3t.service.ServiceException;


import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.S3Bucket;
import org.jets3t.service.model.S3Object;

import org.jets3t.service.security.AWSCredentials;

import de.uni_potsdam.hpi.cloudstore.helper.FileHelper;

/**
 * Represents a connection to the Amazon S3 cloud storage provider
 */
public class JetS3tStorageProvider extends StorageProvider {

	String awsAccessKey = "AKIAJGQTGT7SGZIL3PSQ";
	String awsSecretKey = "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";
	String appName = "cloudstore";
	String BUCKET_NAME = "cloudstoretest";//Integer.toHexString(appName.hashCode());
		
	/**
	 * The hook into the JetS3t framework
	 */
	RestS3Service s3Service;
	AWSCredentials s3Credentials;


	/**
	 * Constructor.
	 * @param options Geographic location of server site
	 * @throws ProviderException 
	 */
	public JetS3tStorageProvider(String options) {
		try {
			location = this.checkLocation(options);
			this.s3Credentials = new AWSCredentials(this.awsAccessKey, this.awsSecretKey);
			this.s3Service = new RestS3Service(this.s3Credentials);
			
			// create main bucket for each region one
			String localeBucket = location.toLowerCase() + BUCKET_NAME;
			this.setProviderName("Amazon");
			S3Bucket bucket = (S3Bucket) s3Service.getBucket(localeBucket);
			if(bucket == null){
				S3Bucket newBucket = new S3Bucket(localeBucket, location);
				newBucket.setLocation(location);
				s3Service.createBucket(newBucket);
			}
			BUCKET_NAME = localeBucket;	// update the bucketname for the chosen region
		} catch (ServiceException e) {
			this.lastError =  new ProviderException(this.providerName, e);
		}
	}
	
	@SuppressWarnings("unused")
	private S3Bucket getBucketOrCreateNew(String bucketName) throws ProviderException{
		S3Bucket bucket;
		try {
			bucket = (S3Bucket) s3Service.getBucket(BUCKET_NAME + "/" + bucketName);
			if(bucket == null){
				S3Bucket newBucket = new S3Bucket(BUCKET_NAME + "/" + bucketName, location);
				s3Service.createBucket(newBucket);
				return newBucket;
			}
			return bucket;
		} catch (S3ServiceException e) {
			throw new ProviderException(this.providerName, e);
		}
	}

	
	/* Interface - methods */
	/**
	 * Uploads a file to the storage provider
	 * @param remoteFolderName Folder to put the data in
	 * @param fileName Path of the file to upload
	 * @return
	 * @throws ProviderException 
	 */
	@Override
	public ProviderReturnValueString putFile(String remoteFolderName, String filePath) throws ProviderException {

		try {
			long t1,t2;
			java.io.File file = new java.io.File(filePath);
			S3Object s3File = new S3Object(file);
			
			t1 = System.currentTimeMillis();
			s3File = s3Service.putObject(BUCKET_NAME,  s3File);
			t2 = System.currentTimeMillis();
			
			return this.returnValue(file.getName(), t1, t2);
		} catch (ServiceException e) {
			throw new ProviderException(this.providerName, e);
		} catch (NoSuchAlgorithmException e) {
			throw new ProviderException(this.providerName, e);
		} catch (IOException e) {
			throw new ProviderException(this.providerName, e);
		}
	}

	/**
	 * Deletes a file from the storage provider
	 * @param user
	 * @param remoteFolderName Remote folder to find the file
	 * @param fileID Name of the uploaded file
	 * @return True on success and false on error
	 * @throws ProviderException 
	 */
	@Override
	public boolean deleteFile(String remoteFolderName, String fileID) throws ProviderException {
		try {
			s3Service.deleteObject(BUCKET_NAME, new S3Object(fileID).getKey());
			return true;
		} catch (ServiceException e) {
			throw new ProviderException(this.providerName, e);
		}
	}
	/**
	 * Retrieves the remote hash of a file
	 * @param remoteFolderName Remote folder to find the file
	 * @param fileID Name of the uploaded file
	 * @return MD5 hash of the file
	 * @throws ProviderException 
	 */
	@Override
	public String getHash(String remoteFolderName, String fileID) throws ProviderException {
		try {
			S3Object obj = s3Service.getObject(BUCKET_NAME, fileID);
			return obj.getMd5HashAsHex();
		} catch (ServiceException e) {
			throw new ProviderException(this.providerName, e);
		}
	}

	/**
	 * Downloads a file from the storage provider
	 * @param user User who requested the download
	 * @param remoteFolderName Folder to get the data from
	 * @param filePath Destination Path of the file to download
	 * @return True on success and false on error
	 * @throws ProviderException 
	 */
	@Override
	public ProviderReturnValueBoolean popFile(String remoteFolderName, String fileID, String destPath) throws ProviderException {

		try {
			long t1,t2;
			S3Object gsFile;
			gsFile = s3Service.getObject(BUCKET_NAME, fileID);
			
			File localFile = new File(destPath);
			FileOutputStream fos = new FileOutputStream(localFile);
			
			t1 = System.currentTimeMillis();
			FileHelper.copyStream(gsFile.getDataInputStream(), fos);
			t2 = System.currentTimeMillis();
			
			fos.close();
			return this.returnValue(true, t1, t2);
		} catch (ServiceException e) {
			throw new ProviderException(this.providerName, e);
		} catch (IOException e) {
			throw new ProviderException(this.providerName, e);
		}
	}


	/**
	 * Removes all files from the storage provider
	 * @return True on success and false on error
	 * @throws ProviderException 
	 */
	@Override
	public boolean deleteAllFiles() throws ProviderException {
		try {
			S3Bucket[] buckets = s3Service.listAllBuckets();
			if(buckets.length != 0){
				for (int i = 0; i < buckets.length; i++) {
					String bucketName = buckets[i].getName();
					S3Object[] objects = s3Service.listObjects(bucketName);
					for (int j = 0; j < objects.length; j++) {
						String objectKey = objects[j].getKey();
						s3Service.deleteObject(bucketName, objectKey);
					}
				}
			}
			return true;
		} catch (ServiceException e) {
			throw new ProviderException(this.providerName, e);
		}
	}
	
	
	/**
	 * Retrieves a list of all files stored in the storage provider
	 * @param remoteFolderName Remote folder to find the files
	 * @return A list of filenames
	 * @throws ProviderException 
	 */
	@Override
	public String[] listFiles(String remoteFolderName) throws ProviderException {
		try {
			S3Bucket bucket;
			bucket = (S3Bucket) s3Service.getBucket(remoteFolderName);
			S3Object[] objects = s3Service.listObjects(bucket.getName());
			String[] output = new String[objects.length];
			for (int i = 0; i < objects.length; i++) {
				output[i] = objects[i].getName();
			}
			return output;
		} catch (ServiceException e) {
			throw new ProviderException(this.providerName, e);
		}
	}

	@Override
	public boolean fileExists(String remoteFolder, String fileID) {

		// TODO Auto-generated method stub
		return false;
	}


}