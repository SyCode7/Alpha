package de.uni_potsdam.hpi.cloudstore20.meta;

public abstract class CloudstoreException extends Exception {

	private static final long serialVersionUID = -1658741804319470061L;

	public CloudstoreException(String message) {
		super(message);
	}

	public CloudstoreException(Throwable cause) {
		super(cause);
	}

	public CloudstoreException(String message, Throwable cause) {
		super(message, cause);
	}

}
