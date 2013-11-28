package de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.implementations;

import java.io.*;
import java.util.Properties;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.StorageProvider;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.StorageProviderException;
import de.uni_potsdam.hpi.cloudstore20.meta.helper.FileHelper;


import org.jclouds.openstack.keystone.v2_0.config.CredentialTypes;
import org.jclouds.openstack.keystone.v2_0.config.KeystoneProperties;

import org.jclouds.ContextBuilder;
import org.jclouds.blobstore.*;
import org.jclouds.blobstore.domain.Blob;

public class HPStorageProvider extends StorageProvider {

    BlobStoreContext context = null;
    BlobStore store = null;
    
	public HPStorageProvider(String providerName, String location)
	
			throws StorageProviderException {
		super(providerName, location);
		initStore();
		
	}

	public HPStorageProvider(String providerName)
			throws StorageProviderException {
		super(providerName);
		initStore();
	}
	

	@Override
	public String uploadFile(File file) throws StorageProviderException {
		BlobStore store = this.getStore();
		if(!store.containerExists(this.getRemoteFolderName()))
			if(!store.createContainerInLocation(null, this.getRemoteFolderName()))
				throw new StorageProviderException("HP >> uploadFile: cannot create container!");
		

		FileInputStream is = null;
		try {
			is = new FileInputStream(file);
			Blob blobFile2 = store.blobBuilder(file.getName())
					.payload(is)
					.build();

			store.putBlob(remoteFolderName, blobFile2);
			
			return file.getName();
		} catch (FileNotFoundException e) {
			throw new StorageProviderException(this.providerName, e);
		} finally {
			this.closeStream(is);
		}
	}

	@Override
	public File downloadFile(String fileID) throws StorageProviderException {
		FileOutputStream fos = null;
		InputStream is = null;
		try {
			String remoteFolderName = this.getRemoteFolderName();
			String destPath = downloadFolder + File.separator + fileID;
			if(!store.blobExists(remoteFolderName, fileID)){
				throw new StorageProviderException(
					"HP >> downloadFile: blob does not exist!");
			}
			Blob fileBlob = this.getBlob(fileID);

			is = fileBlob.getPayload().getInput();
			fos = new FileOutputStream(destPath);
			// hier passiert der download
			FileHelper.copyStream(is, fos);
			
			return new File(destPath);
		} catch (FileNotFoundException e) {
			throw new StorageProviderException(this.providerName, e);
		} catch (IOException e) {
			throw new StorageProviderException(this.providerName, e);
		} finally {
			this.closeStream(is);
			this.closeStream(fos);
		}
	}

	@Override
	public void deleteFile(String fileID) throws StorageProviderException {
		this.getStore().removeBlob(this.getRemoteFolderName(), fileID);
	}

	@Override
	public String getFileHash(String fileID) throws StorageProviderException {
		return store.getBlob(this.getRemoteFolderName(), fileID).getMetadata().getETag();
	}

	@Override
	protected String getRemoteFolderName() {
		return this.remoteFolderName;
	}
	
	
	// private methods
	
	private Blob getBlob(String fileID){
		return this.getStore().getBlob(this.getRemoteFolderName(), fileID);
	}
	
	private BlobStore getStore(){
		return this.store;
	}
	

	private void initStore(){
		Properties props = new Properties();
		props.setProperty(KeystoneProperties.CREDENTIAL_TYPE, CredentialTypes.API_ACCESS_KEY_CREDENTIALS);
		context = ContextBuilder.newBuilder("hpcloud-objectstorage")
				.overrides(props)
                .credentials(this.getApiKey(), this.getApiSecret())
                .buildView(BlobStoreContext.class);
		store = context.getBlobStore();
	}

	private String getApiSecret() {
		// TODO Auto-generated method stub
		return "";
	}

	private String getApiKey() {
		// TODO Auto-generated method stub
		return "";
	}

}
