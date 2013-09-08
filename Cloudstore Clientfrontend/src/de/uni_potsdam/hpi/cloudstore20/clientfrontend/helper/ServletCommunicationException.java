package de.uni_potsdam.hpi.cloudstore20.clientfrontend.helper;

import de.uni_potsdam.hpi.cloudstore20.meta.CloudstoreException;

public class ServletCommunicationException extends CloudstoreException {

	private static final long serialVersionUID = -7319653131673621896L;

	public ServletCommunicationException(String message) {

		super(message);
	}

	public ServletCommunicationException(Throwable cause) {

		super(cause);
	}

	public ServletCommunicationException(String message, Throwable cause) {

		super(message, cause);
	}

}
