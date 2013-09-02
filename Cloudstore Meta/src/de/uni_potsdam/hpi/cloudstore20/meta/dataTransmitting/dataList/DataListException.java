package de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.dataList;

import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.DataTransmittingException;

public class DataListException extends DataTransmittingException {

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
