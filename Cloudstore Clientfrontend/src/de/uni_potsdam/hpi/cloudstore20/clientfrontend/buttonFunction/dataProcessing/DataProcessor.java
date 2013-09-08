package de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing;

import java.io.File;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.helper.ReflectionException;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.helper.Reflector;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.CloudstoreConfig;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.DATA_PROCESS_METHOD;

public class DataProcessor {

	private static String packageName = "de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.Elements.";

	public static DataProcessTask processFile(CloudstoreConfig config, File file) throws DataProcessingException {

		DataProcessTask dpt = new DataProcessTask(file);

		for (DATA_PROCESS_METHOD dpm : config.getMethods()) {

			Object[] param = { config };
			try {
				DataProcessElement dpe = (DataProcessElement) Reflector.reflectClass(
						(DataProcessor.packageName + dpm.getClassName()), param);
				dpe.doProcessing(dpt);
			} catch (ReflectionException e) {
				throw new DataProcessingException(e.getMessage(), e.getCause());
			}
		}

		return dpt;
	}
}
