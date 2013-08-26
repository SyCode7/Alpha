package de.uni_potsdam.hpi.cloudstore20.meta;

public enum CommunicationInformation {

	dataReq("requests the whole datalist"),
	dataAns("datalist as answer");

	private String description;

	private CommunicationInformation(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

};
