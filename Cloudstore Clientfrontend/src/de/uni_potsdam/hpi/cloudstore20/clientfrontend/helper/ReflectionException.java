package de.uni_potsdam.hpi.cloudstore20.clientfrontend.helper;

import de.uni_potsdam.hpi.cloudstore20.meta.CloudstoreException;

public class ReflectionException extends CloudstoreException {

	private static final long serialVersionUID = -5323491267965598113L;

	public ReflectionException(String message) {

		super(message);
	}

	public ReflectionException(Throwable cause) {

		super(cause);
	}

	public ReflectionException(String message, Throwable cause) {

		super(message, cause);
	}

}
