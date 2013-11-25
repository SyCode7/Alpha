package de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.implementations;

import java.io.*;

import org.apache.http.HttpException;

import com.rackspacecloud.client.cloudfiles.FilesAuthorizationException;
import com.rackspacecloud.client.cloudfiles.FilesClient;
import com.rackspacecloud.client.cloudfiles.FilesConstants;
import com.rackspacecloud.client.cloudfiles.FilesException;
import com.rackspacecloud.client.cloudfiles.FilesInvalidNameException;
import com.rackspacecloud.client.cloudfiles.FilesNotFoundException;
import com.rackspacecloud.client.cloudfiles.FilesObjectMetaData;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.StorageProvider;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.StorageProviderException;
import de.uni_potsdam.hpi.cloudstore20.meta.helper.FileHelper;

public class RackspaceStorageProvider extends StorageProvider {

	FilesClient client = null;
	
	public RackspaceStorageProvider(String providerName, String location)
			throws StorageProviderException {
		super(providerName, location);
	}

	public RackspaceStorageProvider(String providerName)
			throws StorageProviderException {
		super(providerName);
	}

	@Override
	public String uploadFile(File file) throws StorageProviderException {

		try {
			String extension = "";
			
			int dotLocation = file.getName().lastIndexOf('.');
			if (dotLocation > 0) {
				extension = file.getName().substring(dotLocation + 1);
			}
			String mimeType = FilesConstants.getMimetype(extension);
			FilesClient client = this.getClient();
			this.createContainerIfNeeded();

			String result = client.storeObject(this.getRemoteFolderName(), file, mimeType);
			
			if (result != null) {
				throw new StorageProviderException(
						"Rackspace >> uploadFile: result from upload was null!");
			}
			
			return file.getName();
		}
		catch (IOException e) {
			throw new StorageProviderException(this.providerName, e);
		} catch (FilesException e) {
			throw new StorageProviderException(this.providerName, e);
		} catch (HttpException e) {
			throw new StorageProviderException(this.providerName, e);
		}
	}


	@Override
	public File downloadFile(String fileID) throws StorageProviderException {

		FileOutputStream fos = null;
		InputStream is = null;
		try {
			String destination = downloadFolder + File.separator + fileID;
			fos = new FileOutputStream(destination);
			is = this.getObjectAsStream(fileID);

			FileHelper.copyStream(is, fos);
			File file = new File(destination);
			if (!file.exists()) {
				throw new StorageProviderException(
					String.format("Rackspace >> downloadFile: file %s does not exist!", 
						file.getAbsolutePath())
					);
			}
			return file;
		}
		catch (IOException e) {
			throw new StorageProviderException(this.providerName, e);
		} catch (FilesNotFoundException e) {
			throw new StorageProviderException(this.providerName, e);
		} catch (FilesAuthorizationException e) {
			throw new StorageProviderException(this.providerName, e);
		} catch (FilesInvalidNameException e) {
			throw new StorageProviderException(this.providerName, e);
		} catch (HttpException e) {
			throw new StorageProviderException(this.providerName, e);
		} finally{
			this.closeStream(is);
			this.closeStream(fos);
		}
	}


	@Override
	public void deleteFile(String fileID) throws StorageProviderException {
		try {
			this.getClient().deleteObject(this.getRemoteFolderName(), fileID);
		} catch (IOException e) {
			throw new StorageProviderException(this.providerName, e);
		} catch (FilesNotFoundException e) {
			throw new StorageProviderException(this.providerName, e);
		} catch (FilesException e) {
			throw new StorageProviderException(this.providerName, e);
		} catch (HttpException e) {
			throw new StorageProviderException(this.providerName, e);
		}
	}

	@Override
	public String getFileHash(String fileID) throws StorageProviderException {
		try {
			FilesObjectMetaData foMetaData;
			foMetaData = this.getClient().getObjectMetaData(this.getRemoteFolderName(), fileID);
			if(foMetaData != null)
				return foMetaData.getETag();
			else
				throw new StorageProviderException(
						"Rackspace >> getFileHash: meta data object was null!");
		} catch (FilesNotFoundException e){
			throw new StorageProviderException(this.providerName, e);
		} catch (FilesAuthorizationException e) {
			throw new StorageProviderException(this.providerName, e);
		} catch (FilesInvalidNameException e) {
			throw new StorageProviderException(this.providerName, e);
		} catch (IOException e) {
			throw new StorageProviderException(this.providerName, e);
		} catch (HttpException e) {
			throw new StorageProviderException(this.providerName, e);
		}
	}
	
	
	// private methods

	@Override
	protected String getRemoteFolderName() {
		return this.remoteFolderName;
	}

	private void createContainerIfNeeded() throws 
			StorageProviderException, FilesAuthorizationException, 
			FilesException, IOException, HttpException {

		FilesClient client = this.getClient();
		String containerName = this.getRemoteFolderName();
		if (!client.containerExists(containerName)){
			client.createContainer(containerName);
			if (!client.containerExists(containerName)){
				throw new StorageProviderException(
					String.format(
						"Rackspace >> createContainerIfNeeded: was not able to create a container %s!",
						containerName));
			}
		}
	}

	private FilesClient getClient() throws StorageProviderException {
		try {
			if(this.client == null){
				client = new FilesClient();   
				//zugangsdaten werden aus rackspace.jar -> cloudfiles.properties ausgelesen!
				client.login();
			}
		} catch (IOException e) {
			throw new StorageProviderException(this.providerName, e);
		} catch (HttpException e) {
			throw new StorageProviderException(this.providerName, e);
		}
		return this.client;
	}
	

	private InputStream getObjectAsStream(String fileID) throws 
		FilesAuthorizationException, FilesInvalidNameException, 
		FilesNotFoundException, IOException, HttpException, 
		StorageProviderException {
		
		return this.getClient().getObjectAsStream(this.getRemoteFolderName(), fileID);
	}
}
