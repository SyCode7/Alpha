package de.uni_potsdam.hpi.cloudstore20.clientfrontend.old.helper.Exceptions;

public class InterruptException extends Exception {

	private static final long serialVersionUID = -3137538604421001304L;

	private String reason;

	public InterruptException(String reason) {

		this.reason = reason;
	}

	public String getReason() {

		return this.reason;
	}

}
