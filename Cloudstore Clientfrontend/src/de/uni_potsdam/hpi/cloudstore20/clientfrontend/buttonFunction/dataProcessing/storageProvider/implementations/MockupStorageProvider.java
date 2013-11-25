package de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.implementations;

import java.io.File;
import java.io.IOException;

import com.google.common.io.Files;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.StorageProvider;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.StorageProviderException;
import de.uni_potsdam.hpi.cloudstore20.meta.helper.FileHelper;
import de.uni_potsdam.hpi.cloudstore20.meta.helper.HelperException;

public class MockupStorageProvider extends StorageProvider {

	private File workingDir;
	private long sleepingTime = 30l;
	private double delayFactor = (Math.random() * 1.5) + 0.5;
	private long finalSleepingTime = (long) (this.sleepingTime * this.delayFactor);

	public MockupStorageProvider() throws StorageProviderException {

		super("Mockup" + System.currentTimeMillis());

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {}

		this.createTempWorkingDir();
	}

	private void createTempWorkingDir() throws StorageProviderException {

		File workingFolder = new File(System.getProperty("java.io.tmpdir") + "MockupProvider" + System.currentTimeMillis());

		if (!workingFolder.mkdir()) {
			throw new StorageProviderException("init Fehler");
		}

		this.workingDir = workingFolder;

	}

	@Override
	public String uploadFile(File file) throws StorageProviderException {

		File newFile = new File(this.workingDir.getAbsolutePath() + File.separator + file.getName());
		try {
			Files.copy(file, newFile);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		if (!newFile.exists()) {
			throw new StorageProviderException("Upload fehlgeschlagen");
		}

		this.simulateWaitingTime();

		return newFile.getAbsolutePath();
	}

	private void simulateWaitingTime() {

		for (int i = 0; i < 100; i++) {
			try {
				this.updateProcessStatus(i);
				Thread.sleep(this.finalSleepingTime);
			} catch (InterruptedException e) {}
		}
		this.updateProcessStatus(100);

	}

	@Override
	public File downloadFile(String fileID) throws StorageProviderException {

		File currentFile = new File(fileID);

		if (currentFile.exists()) {
			this.simulateWaitingTime();
			return currentFile;
		} else {
			throw new StorageProviderException("Datei nicht gefunden");
		}

	}

	@Override
	public void deleteFile(String fileID) throws StorageProviderException {

		File file = new File(fileID);
		if (!file.exists()) {
			throw new StorageProviderException("Datei nicht gefunden");
		}
		if (!file.delete()) {
			throw new StorageProviderException("Datei konnte nicht gelöscht werden");
		}

	}

	@Override
	public String getFileHash(String fileID) throws StorageProviderException {

		File currentFile = new File(fileID);

		if (!currentFile.exists()) {
			throw new StorageProviderException("Datei nicht gefunden");
		}

		String md5 = null;
		try {
			md5 = FileHelper.getHashMD5(currentFile);
		} catch (HelperException e) {
			new StorageProviderException(e.getMessage(), e.getCause());
		}
		return md5;
	}

	public void cleanUp() {

		this.deleteFolderContent(this.workingDir);

	}

	private void deleteFolderContent(File f) {

		for (File f_ : f.listFiles()) {

			if (f_.isDirectory()) {
				this.deleteFolderContent(f_);
			}

			f_.delete();

		}

		f.delete();

	}

	@Override
	protected String getRemoteFolderName() {
		return this.remoteFolderName;
	}

}
