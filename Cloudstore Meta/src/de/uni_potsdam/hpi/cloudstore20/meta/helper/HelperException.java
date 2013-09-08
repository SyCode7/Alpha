package de.uni_potsdam.hpi.cloudstore20.meta.helper;

import de.uni_potsdam.hpi.cloudstore20.meta.CloudstoreException;

public class HelperException extends CloudstoreException {

	private static final long serialVersionUID = -694216218243726162L;

	public HelperException(String message) {

		super(message);
	}

	public HelperException(Throwable cause) {

		super(cause);
	}

	public HelperException(String message, Throwable cause) {

		super(message, cause);
	}

}
