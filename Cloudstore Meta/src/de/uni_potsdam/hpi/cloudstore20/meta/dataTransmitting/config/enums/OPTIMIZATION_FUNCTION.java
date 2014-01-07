package de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.enums;

public enum OPTIMIZATION_FUNCTION {

	PRICE("Preis"), PERFORMANCE("Performance"), AVAILABILITY("Verfügbarkeit");

	private String displayString;

	private OPTIMIZATION_FUNCTION(String displayString) {

		this.displayString = displayString;
	}

	public String getDisplayString() {

		return this.displayString;
	}

	public static OPTIMIZATION_FUNCTION getValueOutOfDisplayString(String item) {

		for (OPTIMIZATION_FUNCTION of : OPTIMIZATION_FUNCTION.values()) {
			if (of.getDisplayString().equals(item)) {
				return of;
			}
		}

		throw new IllegalArgumentException("Keine Funktion f�r diesen String gefunden");
	}

}
