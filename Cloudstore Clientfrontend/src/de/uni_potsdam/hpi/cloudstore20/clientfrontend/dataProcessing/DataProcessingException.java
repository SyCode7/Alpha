package de.uni_potsdam.hpi.cloudstore20.clientfrontend.dataProcessing;

import de.uni_potsdam.hpi.cloudstore20.meta.CloudstoreException;

public class DataProcessingException extends CloudstoreException {

	private static final long serialVersionUID = -7065916029596735583L;

	public DataProcessingException(String message) {

		super(message);
	}

	public DataProcessingException(String message, Exception e) {

		super(message);
	}

}
