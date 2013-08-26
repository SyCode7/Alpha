package de.uni_potsdam.hpi.cloudstore20.meta.dataList;

import de.uni_potsdam.hpi.cloudstore20.meta.CloudstoreException;

public class DataListException extends CloudstoreException {

	private static final long serialVersionUID = -8034493376388837853L;

	public DataListException(String message) {
		super(message);
	}

	public DataListException(Throwable cause) {
		super(cause);
	}

	public DataListException(String message, Throwable cause) {
		super(message, cause);
	}

}
