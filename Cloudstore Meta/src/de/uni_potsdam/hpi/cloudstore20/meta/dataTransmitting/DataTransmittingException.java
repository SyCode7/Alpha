package de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting;

import de.uni_potsdam.hpi.cloudstore20.meta.CloudstoreException;

public class DataTransmittingException extends CloudstoreException {

	private static final long serialVersionUID = 5386002638573129349L;

	public DataTransmittingException(String message) {

		super(message);
	}

	public DataTransmittingException(Throwable cause) {

		super(cause);
	}

	public DataTransmittingException(String message, Throwable cause) {

		super(message, cause);
	}

}
