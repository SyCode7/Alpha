package de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.Elements;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.DataProcessElement;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.DataProcessTask;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.DataProcessingException;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.UsedCloudstoreConfig;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.enums.FILE_POST_HANDLING;

public class FilePostProcessing extends DataProcessElement {

	public FilePostProcessing(UsedCloudstoreConfig config) {

		super(config);
	}

	@Override
	public DataProcessTask doProcessing(DataProcessTask task) throws DataProcessingException {

		FILE_POST_HANDLING fph = this.config.getFilePostHandling();

		// TODO: mit richtiger Funktionalität versehen.
		switch (fph) {
		case Delete:
			System.out.println("Datei würde gelöscht werden.");
			break;
		case DontMove:
			System.out.println("Nichts passiert");
			break;
		case Stub:
			System.out.println("Datei würde gelöscht werden und durch Stub ersetzt.");
			break;
		default:
			throw new DataProcessingException("Keine Nachverarbeitung gesetzt gesetzt.");
		}

		return task;
	}

	@Override
	public int getStatus() {

		// TODO Auto-generated method stub
		return 0;
	}

}
