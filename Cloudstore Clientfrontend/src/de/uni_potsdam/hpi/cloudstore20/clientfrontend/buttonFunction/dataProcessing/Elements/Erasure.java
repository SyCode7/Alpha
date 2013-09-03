package de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.Elements;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.DataProcessElement;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.DataProcessTask;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.DataProcessingException;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.CloudstoreConfig;

public class Erasure extends DataProcessElement {

	public Erasure(CloudstoreConfig config) {

		super(config);
		// TODO Auto-generated constructor stub
	}

	@Override
	public DataProcessTask doProcessing(DataProcessTask task) throws DataProcessingException {

		// TODO Auto-generated method stub
		return task;
	}

	//Bitte verwenden! Wird u.a. von Uploadoptimierung gebraucht
	public List<File> doErasure(File file, int k, int m) {

		List<File> returnValue = new LinkedList<File>();

		// TODO: Erasure implementieren

		return returnValue;

	}

}
