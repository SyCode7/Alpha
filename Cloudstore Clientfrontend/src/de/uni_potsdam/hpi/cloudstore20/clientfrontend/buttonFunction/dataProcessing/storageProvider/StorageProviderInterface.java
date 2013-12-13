package de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider;

import java.io.File;

public interface StorageProviderInterface {


	// TODO: hash des files hinten dranhängen und bei download dann wieder abschneiden
	public abstract String uploadFile(File file) throws StorageProviderException;

	public abstract File downloadFile(String fileID) throws StorageProviderException;

	public abstract void deleteFile(String fileID) throws StorageProviderException;

	public abstract String getFileHash(String fileID) throws StorageProviderException;

}
