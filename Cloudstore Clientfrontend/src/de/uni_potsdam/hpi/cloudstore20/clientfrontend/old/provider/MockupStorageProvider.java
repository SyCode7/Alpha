package de.uni_potsdam.hpi.cloudstore20.clientfrontend.old.provider;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import com.google.common.io.Files;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.old.Settings;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.old.helper.FileHelper;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.old.provider.helper.ProviderReturnValueBoolean;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.old.provider.helper.ProviderReturnValueString;

/**
 * Represents a mockup storage provider only writing on disk rather than on the network.
 */
public class MockupStorageProvider extends StorageProvider {

	private final double failure = Settings.failure;
	private boolean simulateErrors = false;

	/**
	 * Id of the actual provider
	 */
	private String storageProviderId;
	/**
	 * Name of the destination folder
	 */
	private String folder;
	private int speed = Settings.speed;
	/*
	 * simulating real upload and download
	 */
	private boolean simulate = false;

	private String getDestFilePath(String fileName) {

		return this.folder + File.separator + fileName;
	}

	public MockupStorageProvider(String providerId) {

		this.storageProviderId = providerId;
		this.folder = System.getProperty("java.io.tmpdir") + File.separator + providerId;

		this.setProviderName("MockupProvider");
		this.location = providerId;
	}

	/**
	 * Uploads a file to the storage provider
	 * 
	 * @param user
	 *            User who requested the upload
	 * @param remoteFolderName
	 *            Folder to put the data in
	 * @param filePath
	 *            Path of the file to upload
	 * @return Filename
	 */
	@Override
	public ProviderReturnValueString putFile(String remoteFolderName, String filePath) {

		this.simulateError();

		File file = new File(filePath);

		FileHelper.createDir(this.folder);
		File newFile = new File(this.getDestFilePath(file.getName()));
		try {
			Files.copy(file, newFile);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		if (!newFile.exists()) {
			return null;
		}

		long time = file.length() / this.speed;
		try {
			if (this.simulate)
				Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return this.returnValue(file.getName(), 0, time);
	}

	private void simulateError() {

		if (this.isSimulateErrors() && Math.random() <= this.failure) {
			System.err.println("MockupStorageProvider.simulateError() >> It is a Simulated error!");
			throw new RuntimeException("This is an simulated error");
		}
	}

	/**
	 * Deletes a file from the storage provider
	 * 
	 * @param user
	 * @param remoteFolderName
	 *            Remote folder to find the file
	 * @param fileID
	 *            Name of the uploaded file
	 * @return True on success and false on error
	 */
	@Override
	public boolean deleteFile(String remoteFolderName, String fileName) {

		File file = new File(this.getDestFilePath(fileName));
		if (!file.exists()) {
			return false;
		}
		if (!file.delete()) {
			return false;
		}
		return true;
	}

	/**
	 * Retrieves the remote hash of a file
	 * 
	 * @param remoteFolderName
	 *            Remote folder to find the file
	 * @param fileID
	 *            Name of the uploaded file
	 * @return MD5 hash of the file
	 */
	@Override
	public String getHash(String remoteFolderName, String fileName) {

		File currentFile = new File(this.getDestFilePath(fileName));
		if (!currentFile.exists())
			return null;
		try {
			return FileHelper.getHashMD5(currentFile);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Downloads a file from the storage provider
	 * 
	 * @param user
	 *            User who requested the download
	 * @param remoteFolderName
	 *            Folder to get the data from
	 * @param filePath
	 *            Destination Path of the file to download
	 * @return True on success and false on error
	 */
	@Override
	public ProviderReturnValueBoolean popFile(String remoteFolderName, String fileName, String destPath) {

		File currentFile = new File(this.getDestFilePath(fileName)); // suche die Datei im lokalen Verzeichnis
		if (currentFile.exists()) {
			File out = new File(destPath);
			try {
				Files.copy(currentFile, out);
			} catch (IOException e) {
				e.printStackTrace();
				return this.returnValue(false, 0, 0);
			}
			if (!out.exists()) {
				return this.returnValue(false, 0, 0);
			}
		} else {
			return this.returnValue(false, 0, 0);
		}

		long time = currentFile.length() / this.speed;
		try {
			if (this.simulate)
				Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return this.returnValue(true, 0, time);
	}

	/**
	 * Retrieves a list of all files stored in the storage provider
	 * 
	 * @param remoteFolderName
	 *            Remote folder to find the files
	 * @return A list of filenames
	 */
	@Override
	public String[] listFiles(String remoteFolderName) {

		File[] files = new File(folder).listFiles();
		String[] result = new String[files.length];
		for (int i = 0; i < files.length; i++) {
			result[i] = files[i].getName();
		}
		return result;

	}

	/**
	 * Removes all files from the storage provider
	 * 
	 * @return True on success and false on error
	 */
	@Override
	public boolean deleteAllFiles() {

		return new File(this.folder).delete();
	}

	@Override
	public boolean isSimulateErrors() {

		return this.simulateErrors;
	}

	@Override
	public void setSimulateErrors(boolean simulateErrors) {

		this.simulateErrors = simulateErrors;
	}

	public String getStorageProviderId() {

		return this.storageProviderId;
	}

	@Override
	public boolean fileExists(String remoteFolder, String fileID) {

		// TODO Auto-generated method stub
		return false;
	}

}
