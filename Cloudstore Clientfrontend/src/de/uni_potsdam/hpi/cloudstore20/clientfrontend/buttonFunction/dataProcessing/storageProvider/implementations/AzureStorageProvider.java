package de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.implementations;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.nio.channels.FileChannel;
import java.security.InvalidKeyException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.microsoft.windowsazure.services.blob.client.BlobOutputStream;
import com.microsoft.windowsazure.services.blob.client.CloudBlobClient;
import com.microsoft.windowsazure.services.blob.client.CloudBlockBlob;
import com.microsoft.windowsazure.services.core.storage.CloudStorageAccount;
import com.microsoft.windowsazure.services.core.storage.StorageException;
import com.microsoft.windowsazure.services.core.storage.utils.Base64;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.StorageProvider;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.StorageProviderConfig;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.StorageProviderException;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.enums.PROVIDER_ENUM;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.enums.STORAGE_PROVIDER_CONFIG;
import de.uni_potsdam.hpi.cloudstore20.meta.helper.FileHelper;

public class AzureStorageProvider extends StorageProvider {

	protected CloudBlobClient serviceClient = null;

	public AzureStorageProvider() throws StorageProviderException {

		super("Azure", defaultLocation());
	}

	public AzureStorageProvider(String location) throws StorageProviderException {

		super("Azure", location);
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

			class Uploader implements Callable<Boolean> {
		        CloudBlockBlob blob;
		        FileInputStream fis;
		        long fileSize;
		        Uploader(CloudBlockBlob blob, FileInputStream fis, long size) { 
		        	this.blob = blob; this.fis = fis; this.fileSize = size;}
		        public Boolean call() throws StorageException, IOException{
		        	blob.upload(fis, fileSize);
		            return true; 
		        }
		    }
			
			Callable<Boolean> uploader = new Uploader(blob, fis, fileSize);
			ExecutorService executor = Executors.newSingleThreadExecutor();
			Future<Boolean> res = executor.submit(uploader);
		    
			FileChannel fc = fis.getChannel();
					
			long oldPos = 0;
			while(!executor.isTerminated()){
				if(oldPos != fc.position()){
					oldPos = fc.position();
					System.out.println(String.format("%d / %d = %d", fileSize, oldPos, oldPos * 100 / fileSize));
				}
				if(fileSize <= oldPos)
					break;
				try {
					res.get(10, TimeUnit.MILLISECONDS);
					break;
				} catch (TimeoutException e) {continue;}
			}
			
			res.get();
			//TODO!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
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

		String destPath = downloadFolder + File.separator + fileID;
		CloudBlockBlob blob = this.getBlob(fileID);

		if (blob == null) {
			throw new StorageProviderException("Azure >> downloadFile: could not access blob with following params: "
					+ String.format("remoteFolder: %s, fileId: %s", getRemoteFolderName(), fileID));
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
				throw new StorageProviderException("Azure >> getFileHash: could not access blob with following params: "
						+ String.format("remoteFolder: %s, fileId: %s", getRemoteFolderName(), fileID));
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

	protected String getRemoteFolderName() {

		return this.remoteFolderName.toLowerCase();
	}

	private CloudBlobClient getServiceClient() {

		if (this.serviceClient == null) {

			CloudStorageAccount account;
			try {
				account = CloudStorageAccount.parse(this.getConnectionString());
				this.serviceClient = account.createCloudBlobClient();
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		return this.serviceClient;
	}

	private String getConnectionString() {

		String user = StorageProviderConfig.getInstance().get(
				PROVIDER_ENUM.fromString(this.getCompleteProviderName()).getConfigCategory(), STORAGE_PROVIDER_CONFIG.Username);
		String pwd = StorageProviderConfig.getInstance().get(
				PROVIDER_ENUM.fromString(this.getCompleteProviderName()).getConfigCategory(), STORAGE_PROVIDER_CONFIG.Password);
		return String.format("DefaultEndpointsProtocol=http;AccountName=%s;AccountKey=%s", user, pwd);
	}

	public CloudBlockBlob getBlob(String fileID) throws StorageProviderException {

		try {
			return this.getServiceClient().getBlockBlobReference(getRemoteFolderName() + "/" + fileID);
		} catch (URISyntaxException e) {
			throw new StorageProviderException(this.providerName, e);
		} catch (StorageException e) {
			throw new StorageProviderException(this.providerName, e);
		}
	}

}
