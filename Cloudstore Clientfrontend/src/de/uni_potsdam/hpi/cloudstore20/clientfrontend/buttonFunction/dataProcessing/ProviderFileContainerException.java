package de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing;

import de.uni_potsdam.hpi.cloudstore20.meta.CloudstoreException;

public class ProviderFileContainerException extends CloudstoreException {

	private static final long serialVersionUID = 3271819590671905425L;

	public ProviderFileContainerException(String message) {

		super(message);
	}

	public ProviderFileContainerException(Throwable cause) {

		super(cause);
	}

	public ProviderFileContainerException(String message, Throwable cause) {

		super(message, cause);
	}
}
