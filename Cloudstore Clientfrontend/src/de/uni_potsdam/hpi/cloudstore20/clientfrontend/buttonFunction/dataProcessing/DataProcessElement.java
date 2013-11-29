package de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing;

import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.UsedCloudstoreConfig;

public abstract class DataProcessElement {

	protected UsedCloudstoreConfig config;

	public abstract DataProcessTask doProcessing(DataProcessTask task) throws DataProcessingException;

	public abstract int getStatus();

	public DataProcessElement(UsedCloudstoreConfig config) {

		this.config = config;
	}
}
