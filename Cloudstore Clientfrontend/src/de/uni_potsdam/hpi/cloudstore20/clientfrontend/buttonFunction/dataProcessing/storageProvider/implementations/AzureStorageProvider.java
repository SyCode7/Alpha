package de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.implementations;

import java.io.*;
import java.math.BigInteger;
import java.net.URISyntaxException;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.StorageProvider;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.StorageProviderException;


import com.microsoft.windowsazure.services.blob.client.CloudBlobClient;
import com.microsoft.windowsazure.services.blob.client.CloudBlockBlob;
import com.microsoft.windowsazure.services.core.storage.StorageException;
import com.microsoft.windowsazure.services.core.storage.utils.Base64;

public class AzureStorageProvider extends StorageProvider {


	protected CloudBlobClient serviceClient;
	private boolean connected;
	
	
	public AzureStorageProvider(String providerName, String location)
			throws StorageProviderException {
		super(providerName, location);
	}

	public AzureStorageProvider(String providerName)
			throws StorageProviderException {
		super(providerName);
	}

	@Override
	public String uploadFile(File file) throws StorageProviderException {

		FileInputStream fis = null;
		try {
			String remoteFolderName = this.getRemoteFolderName();
			long fileSize = file.length();
			this.getServiceClient().getContainerReference(remoteFolderName).createIfNotExist();
			CloudBlockBlob blob = this.getBlob(file.getName());
			fis = new FileInputStream(file);

			blob.upload(fis, fileSize);

			HashCode hash = Files.hash(file, Hashing.md5());
			String md5 = Base64.encode(hash.asBytes());
			blob.getProperties().setContentMD5(md5);
			blob.uploadProperties();
			return file.getName();
		} catch (FileNotFoundException e) {
			throw new StorageProviderException(this.providerName, e);
		} catch (IOException e) {
			throw new StorageProviderException(this.providerName, e);
		} catch (StorageException e) {
			throw new StorageProviderException(this.providerName, e);
		} catch (URISyntaxException e) {
			throw new StorageProviderException(this.providerName, e);
		} finally {
			this.closeStream(fis);
		}
	}


	@Override
	public File downloadFile(String fileID) throws StorageProviderException {
		
		String destPath = downloadFolder + File.separator + fileID;
		CloudBlockBlob blob = this.getBlob(fileID);

		if (blob == null) {
			throw new StorageProviderException(
					"Azure >> downloadFile: could not access blob with following params: " +
					String.format("remoteFolder: %s, fileId: %s", getRemoteFolderName(), fileID));
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(destPath);
			blob.download(fos);
			fos.close();
		} catch (FileNotFoundException e) {
			throw new StorageProviderException(this.providerName, e);
		} catch (StorageException e) {
			throw new StorageProviderException(this.providerName, e);
		} catch (IOException e) {
			throw new StorageProviderException(this.providerName, e);
		} finally {
			this.closeStream(fos);
		}
		
		return new File(destPath);
	}

	@Override
	public void deleteFile(String fileID) throws StorageProviderException {
		CloudBlockBlob blob = this.getBlob(fileID);
		try {
			if (blob != null && blob.exists()) {
				blob.delete();
			}
		} catch (StorageException e) {
			throw new StorageProviderException(this.providerName, e);
		}
	}

	@Override
	public String getFileHash(String fileID) throws StorageProviderException {

		try {
			CloudBlockBlob blob = this.getBlob(fileID);
			if (blob == null) {
				throw new StorageProviderException(
						"Azure >> getFileHash: could not access blob with following params: " +
						String.format("remoteFolder: %s, fileId: %s", getRemoteFolderName(), fileID));
			}
			blob.downloadAttributes();
			byte[] hash = Base64.decode(blob.getProperties().getContentMD5());

			BigInteger bigInt = new BigInteger(1, hash);
			String hashString = bigInt.toString(16);
			while (hashString.length() < 32) {
				hashString = "0" + hashString;
			}
			return hashString;
		} catch (StorageException e) {
			throw new StorageProviderException(this.providerName, e);
		}
	}
	
	// private
	


	protected String getRemoteFolderName() {

		return this.remoteFolderName.toLowerCase();
	}

	private CloudBlobClient getServiceClient() {
		return this.serviceClient;
	}
	
	public CloudBlockBlob getBlob(String fileID) 
			throws StorageProviderException {

		if (this.connected) {
			try {
				return this.serviceClient.getBlockBlobReference(getRemoteFolderName() + "/" + fileID);
			} catch (URISyntaxException e) {
				throw new StorageProviderException(this.providerName, e);
			} catch (StorageException e) {
				throw new StorageProviderException(this.providerName, e);
			}
		}
		
		throw new StorageProviderException("Azure >> getBlob: provider is not connected!");
	}

}
