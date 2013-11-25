package de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class StorageProvider {

	protected String providerName;
	protected String location;
	protected StorageProviderConfig config;
	private int processStatus = 0;
	
	protected static String downloadFolder = "download";
	protected String remoteFolderName = "cloudstore20";
	
	public StorageProvider(String providerName, String location) throws StorageProviderException {

		this.providerName = providerName;
		this.location = location;

		this.loadConfig();

	}

	public StorageProvider(String providerName) throws StorageProviderException {

		this.providerName = providerName;

		this.loadConfig();

	}

	private void loadConfig() throws StorageProviderException {

		// TODO: implement

	}

	public int getProcessStatus() {

		if (this.processStatus < 0) {
			return 0;
		}

		if (this.processStatus > 100) {
			return 100;
		}

		return this.processStatus;

	}

	public String getCompleteProviderName() {

		String name = providerName;
		if (this.location != null) {
			name += "#" + this.location;
		}
		return name;
	}

	protected void updateProcessStatus(int status) {

		this.processStatus = status;

	}


	protected void closeStream(OutputStream os) throws StorageProviderException {
		try {
			if(os != null){
				os.close();
			}
		} catch (IOException e) {
			throw new StorageProviderException(this.providerName, e);
		}
	}
	
	protected void closeStream(InputStream is) throws StorageProviderException {
		try {
			if(is != null){
				is.close();
			}
		} catch (IOException e) {
			throw new StorageProviderException(this.providerName, e);
		}
	}
	
	public abstract String uploadFile(File file) throws StorageProviderException;

	public abstract File downloadFile(String fileID) throws StorageProviderException;

	public abstract void deleteFile(String fileID) throws StorageProviderException;

	public abstract String getFileHash(String fileID) throws StorageProviderException;

	protected abstract String getRemoteFolderName();
	
	
	static {
		File downFolder = new File(downloadFolder);
		if(!downFolder.isDirectory()){
			downFolder.mkdir();
		}
	}
}
