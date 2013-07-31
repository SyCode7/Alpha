package de.uni_potsdam.hpi.cloudstore.helper.Exceptions;

public class TestFailedException extends Exception {

	private static final long serialVersionUID = 6392661252449009757L;
	
	private String reason;

	public TestFailedException(String reason) {

		this.reason = reason;
	}

	public String getReason() {

		return this.reason;
	}

}
