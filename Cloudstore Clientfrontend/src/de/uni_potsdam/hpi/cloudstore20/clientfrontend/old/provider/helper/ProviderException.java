package de.uni_potsdam.hpi.cloudstore20.clientfrontend.old.provider.helper;

import java.io.PrintStream;
import java.io.PrintWriter;

public class ProviderException extends Exception {

	private static final long serialVersionUID = 3782228517671217251L;

	private Exception e;
	private String name;

	public ProviderException(String providerName, Exception catchedException) {

		this.e = catchedException;
		this.name = providerName;
	}

	public Exception getException() {

		return this.e;
	}

	public String getName() {

		return this.name;
	}

	/* Delegations */

	public boolean equals(Object obj) {

		return e.equals(obj);
	}

	public Throwable fillInStackTrace() {
		if (e == null)
			e = new Exception("No exception given!");
		return e.fillInStackTrace();
	}

	public Throwable getCause() {

		return e.getCause();
	}

	public String getLocalizedMessage() {

		return e.getLocalizedMessage();
	}

	public StackTraceElement[] getStackTrace() {

		return e.getStackTrace();
	}

	public int hashCode() {

		return e.hashCode();
	}

	public Throwable initCause(Throwable cause) {

		return e.initCause(cause);
	}

	public void printStackTrace() {

		e.printStackTrace();
	}

	public void printStackTrace(PrintStream s) {

		e.printStackTrace(s);
	}

	public void printStackTrace(PrintWriter s) {

		e.printStackTrace(s);
	}

	public void setStackTrace(StackTraceElement[] stackTrace) {

		e.setStackTrace(stackTrace);
	}

	public String toString() {

		return e.toString();
	}

	public String getMessage() {

		return this.e.getMessage();
	}

}
