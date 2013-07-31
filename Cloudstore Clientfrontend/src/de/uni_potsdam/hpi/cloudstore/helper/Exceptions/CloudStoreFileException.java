package de.uni_potsdam.hpi.cloudstore.helper.Exceptions;


public class CloudStoreFileException extends Exception {

	private static final long serialVersionUID = 3337335668422608431L;
	
	private String message = "";
	
	public CloudStoreFileException(String reason){
		this.message = reason;
	}
	
	public String getMessage(){
		return this.message;
	}

}
