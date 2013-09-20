package de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.Elements;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.DataProcessElement;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.DataProcessTask;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.DataProcessingException;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.CloudstoreConfig;

public class Test extends DataProcessElement {

	public Test(CloudstoreConfig config) {

		super(config);
	}

	@Override
	public DataProcessTask doProcessing(DataProcessTask task) throws DataProcessingException {

		return task;

	}

	@Override
	public int getStatus() {

		return ((int) (Math.random() * 100));
	}

}
