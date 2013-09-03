package de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting;

public abstract class DataTransmittingClass {

	public DataTransmittingClass(String content) throws DataTransmittingException {

	}

	public abstract String getClassAsString() throws DataTransmittingException;

}
