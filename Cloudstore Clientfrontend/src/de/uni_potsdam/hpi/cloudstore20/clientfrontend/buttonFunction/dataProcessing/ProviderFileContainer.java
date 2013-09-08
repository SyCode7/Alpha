package de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.helper.DatabaseRequest;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.storageProvider.StorageProvider;

public class ProviderFileContainer {

	private List<File> files = new LinkedList<File>();
	private StorageProvider provider;
	private long speed = 0;

	public ProviderFileContainer(StorageProvider provider) {

		this.provider = provider;
		this.speed = DatabaseRequest.getProviderSpeed(this.provider.getCompleteProviderName());
	}

	private ProviderFileContainer(StorageProvider provider, long speed) {

		this.provider = provider;
		this.speed = speed;
	}

	public long getProviderSpeed() {

		return this.speed;
	}

	public void addFile(File f) {

		this.files.add(f);
	}

	public void replaceFile(File oldFile, File newFile) throws ProviderFileContainerException {

		if (oldFile == null || newFile == null) {
			throw new NullPointerException();
		}

		if (!this.files.remove(oldFile)) {
			throw new ProviderFileContainerException("Datei ist nicht in der Liste enthalten: " + oldFile.getName());
		}

		this.addFile(newFile);
	}

	public long getUploadTimeForFileList() {

		long time = 0;
		for (File f : this.files) {
			long tempTime = (f.length() / this.speed);
			if (tempTime < 1) {
				tempTime = 1;
			}
			time += tempTime;
		}

		return time;
	}

	public File getBiggestFile() throws ProviderFileContainerException {

		if (this.files.size() == 0) {
			throw new ProviderFileContainerException("Noch keine Datei in der Liste!");
		}

		File temp = this.files.get(0);

		for (File f : this.files) {
			if (temp.length() < f.length()) {
				temp = f;
			}
		}

		return temp;

	}

	public List<File> getFileList() {

		return this.files;
	}

	public StorageProvider getProvider() {

		return this.provider;
	}

	public String getProviderName() {

		return this.provider.getCompleteProviderName();
	}

	public ProviderFileContainer copyContainer() {

		ProviderFileContainer pfc = new ProviderFileContainer(this.provider, this.speed);

		for (File f : this.files) {
			pfc.addFile(f);
		}

		return pfc;
	}

}
