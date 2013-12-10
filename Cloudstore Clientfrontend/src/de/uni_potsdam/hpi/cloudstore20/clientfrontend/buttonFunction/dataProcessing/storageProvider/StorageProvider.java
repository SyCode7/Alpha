package de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider;

import java.io.*;

public abstract class StorageProvider implements StorageProviderInterface{

	protected String providerName;
	protected String location = "";
	protected StorageProviderConfig config;
	private int processStatus = 0;
	
	
	protected String keys[] = {null, null};
	
	protected static String downloadFolder = "download"; // temp-ordner!
	protected String remoteFolderName = "cloudstore20";
	protected boolean doNotLoadConfig = false;
	
	private static String config_file = "keys.conf";
	private static String config_separator = ":::";
		
	public StorageProvider(String providerName, String location) throws StorageProviderException {

		this.providerName = providerName;
		this.location = location;

		try {
			this.loadConfig();
		} catch (IOException e) {
			e.printStackTrace();
			throw new StorageProviderException(e);
		}

	}

	public StorageProvider(String providerName) throws StorageProviderException {

		this.providerName = providerName;

		try {
			this.loadConfig();
		} catch (IOException e) {
			e.printStackTrace();
			throw new StorageProviderException(e);
		}

	}

	private void loadConfig() throws StorageProviderException, IOException {
		if(this.doNotLoadConfig) return;
		InputStream fis;
		BufferedReader br;
		String line;
		File configFile = null;
		
		configFile = new File(StorageProvider.config_file);
		if(!configFile.exists()){
			throw new StorageProviderException("Please create a configuration file for the Providers!");
		}
		
		fis = new FileInputStream(configFile);
		br = new BufferedReader(new InputStreamReader(fis));
		boolean configFound = false;
		while ((line = br.readLine()) != null) {
			if(line.length() == 0) continue; 
		    String split[] = line.split(StorageProvider.config_separator);
		    assert split.length == 3: "invalid config line: " + line;
		    
		    if(!split[0].equals(this.configKey())) continue;
		    this.keys[0] = split[1];
		    this.keys[1] = split[2];
		    
		    configFound = true;
		    break;
		}

		br.close();
		
		if(!configFound){
			throw new StorageProviderException(
					String.format(
							"no config was found for %s in %s", 
							this.configKey(),
							configFile.getAbsolutePath()));
		}
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
	
	protected abstract String getRemoteFolderName();
	protected static String defaultLocation(){
		return "EU";
	}
	
	protected String configKey(){
		return this.providerName;
	}
	
	static {
		File downFolder = new File(downloadFolder);
		if(!downFolder.isDirectory()){
			downFolder.mkdir();
		}
	}
}
