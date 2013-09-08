package de.uni_potsdam.hpi.cloudstore20.meta;

public enum CommunicationInformation {

	dataList("requests the whole datalist");

	private String description;

	private CommunicationInformation(String description) {

		this.description = description;
	}

	public String getDescription() {

		return this.description;
	}

};
