package de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.enums.STORAGE_PROVIDER_CONFIG;

public abstract class StorageProvider implements StorageProviderInterface {

	protected String providerName;
	protected String location = null;
	protected StorageProviderConfig config;
	private int processStatus = 0;

	protected Map<STORAGE_PROVIDER_CONFIG, String> configData = new HashMap<STORAGE_PROVIDER_CONFIG, String>();

	// TODO: diese werte in die Config auslagern!
	protected static String downloadFolder = "download"; // temp-ordner!
	protected String remoteFolderName = "cloudstore20";

	public StorageProvider(String providerName, String location) throws StorageProviderException {

		this.providerName = providerName;
		this.location = location;

		StorageProviderConfig.getInstance();

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

		String name = this.providerName;
		if (this.location != null) {
			name += "#" + this.location;
		}
		return name;
	}

	protected void updateProcessStatus(int status) {

		this.processStatus = status;
		
		System.out.println(String.format("StorageProvider.updateProcessStatus() >> %d%%", status));

	}

	protected void closeStream(OutputStream os) throws StorageProviderException {

		try {
			if (os != null) {
				os.close();
			}
		} catch (IOException e) {
			throw new StorageProviderException(this.providerName, e);
		}
	}

	protected void closeStream(InputStream is) throws StorageProviderException {

		try {
			if (is != null) {
				is.close();
			}
		} catch (IOException e) {
			throw new StorageProviderException(this.providerName, e);
		}
	}
	

	protected void waitAndUpdateStatus(long fileSize, FileChannel fc,
			Callable<Boolean> callable) throws IOException, InterruptedException, ExecutionException {
		int oldStatus = 0, newStatus = 0;

		ExecutorService executor = Executors.newSingleThreadExecutor();
		Future<Boolean> res = executor.submit(callable);
		while(true){
			
			newStatus = (int) (((float)fc.position() / (float)fileSize) * 100);
			if(oldStatus != newStatus){
				oldStatus = newStatus;
				this.updateProcessStatus(newStatus);
			}
			if(newStatus >= 100)
				break;
			try {
				res.get(10, TimeUnit.MILLISECONDS);
				break;
			} catch (TimeoutException e) {continue;} 
		}
		res.get();
		if(newStatus < 100)
			this.updateProcessStatus(100);
	}

	protected abstract String getRemoteFolderName();

	protected static String defaultLocation() {

		return "EU";
	}

	// protected String configKey() {
	//
	// return this.providerName;
	// }

	static {
		File downFolder = new File(downloadFolder);
		if (!downFolder.isDirectory()) {
			downFolder.mkdir();
		}
	}
}
