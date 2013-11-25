package de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.implementations;

import java.io.File;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.StorageProvider;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.StorageProviderException;

public class AzureStorageProvider extends StorageProvider {

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File downloadFile(String fileID) throws StorageProviderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteFile(String fileID) throws StorageProviderException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getFileHash(String fileID) throws StorageProviderException {
		// TODO Auto-generated method stub
		return null;
	}

}
