package de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.implementations;


import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.StorageProviderException;

public class HPStorageProvider extends JCloudsProvider{

	public HPStorageProvider(String location)
			throws StorageProviderException {
		super("HPStorage", location);
	}
	
	public HPStorageProvider()
			throws StorageProviderException {
		super("HPStorage", defaultLocation());
	}

	@Override
	protected String builderString() {
		return "hpcloud-objectstorage";
	}

	protected static String defaultLocation() {
		return "";
	}


}
