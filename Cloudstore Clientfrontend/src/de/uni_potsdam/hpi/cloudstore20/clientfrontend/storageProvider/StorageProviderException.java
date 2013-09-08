package de.uni_potsdam.hpi.cloudstore20.clientfrontend.storageProvider;

import de.uni_potsdam.hpi.cloudstore20.meta.CloudstoreException;

public class StorageProviderException extends CloudstoreException {

	private static final long serialVersionUID = 6525837026931751974L;

	public StorageProviderException(String message) {

		super(message);
	}

	public StorageProviderException(Throwable cause) {

		super(cause);
	}

	public StorageProviderException(String message, Throwable cause) {

		super(message, cause);
	}

}
