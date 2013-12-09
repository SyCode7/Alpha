package de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.implementations;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.StorageProviderException;

public class RackspaceStorageProvider extends JCloudsProvider {

    public RackspaceStorageProvider(String location)
			throws StorageProviderException {
		super("Rackspace", location);
	}

	public RackspaceStorageProvider()
			throws StorageProviderException {
		super("Rackspace", defaultLocation());
	}

	@Override
	protected String builderString() {

		if(location == "EU"){
			return "cloudfiles-uk";
		} else {
			return "cloudfiles-us";
		}
	}

}
