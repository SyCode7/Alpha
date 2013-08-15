package de.uni_potsdam.hpi.cloudstore20.clientfrontend;

public abstract class Cloudstore20Exception extends Exception {

	private static final long serialVersionUID = -6733328178501379785L;

	private String message;

	public Cloudstore20Exception(String message) {

		this.message = message;

	}

	public Cloudstore20Exception(String message, Exception e) {

		this.message = message;
		this.setStackTrace(e.getStackTrace());
	}

	@Override
	public String getMessage() {

		return this.message;
	}
}
