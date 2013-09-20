package de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing;

import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.CloudstoreConfig;

public abstract class DataProcessElement {

	protected CloudstoreConfig config;

	public abstract DataProcessTask doProcessing(DataProcessTask task) throws DataProcessingException;

	public abstract int getStatus();

	public DataProcessElement(CloudstoreConfig config) {

		this.config = config;
	}
}
