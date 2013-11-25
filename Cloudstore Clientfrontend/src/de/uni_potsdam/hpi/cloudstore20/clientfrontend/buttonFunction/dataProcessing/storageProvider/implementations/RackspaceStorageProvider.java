package de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.implementations;

import java.io.File;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.StorageProvider;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.StorageProviderException;

public class RackspaceStorageProvider extends StorageProvider {

	public RackspaceStorageProvider(String providerName, String location)
			throws StorageProviderException {
		super(providerName, location);
		// TODO Auto-generated constructor stub
	}

	public RackspaceStorageProvider(String providerName)
			throws StorageProviderException {
		super(providerName);
		// TODO Auto-generated constructor stub
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
