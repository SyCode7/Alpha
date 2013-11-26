package de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config;


public enum OPTIMIZATION_FUNCTION {
	
	PRICE("Preis"), PERFORMANCE("Performance"), AVAILABILITY("Verfügbarkeit");

	private String displayString;

	private OPTIMIZATION_FUNCTION(String displayString) {

		this.displayString = displayString;
	}

	public String getDisplayString() {

		return this.displayString;
	}

}
