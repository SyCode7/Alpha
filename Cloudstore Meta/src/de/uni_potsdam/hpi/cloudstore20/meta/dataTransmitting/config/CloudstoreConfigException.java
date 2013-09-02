package de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config;

import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.DataTransmittingException;

public class CloudstoreConfigException extends DataTransmittingException {

	private static final long serialVersionUID = 7446048827065919408L;

	public CloudstoreConfigException(String message) {

		super(message);
	}

	public CloudstoreConfigException(Throwable cause) {

		super(cause);
	}

	public CloudstoreConfigException(String message, Throwable cause) {

		super(message, cause);
	}

}
