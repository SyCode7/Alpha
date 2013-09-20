package de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.Elements;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.ButtonThread;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.DataProcessElement;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.DataProcessTask;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.DataProcessingException;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.ProviderFileContainer;
import de.uni_potsdam.hpi.cloudstore20.meta.CloudstoreException;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.CloudstoreConfig;

public class Upload extends DataProcessElement {

	private DataProcessTask dpt;

	public Upload(CloudstoreConfig config) {

		super(config);
		// TODO Auto-generated constructor stub
	}

	@Override
	public DataProcessTask doProcessing(DataProcessTask task) throws DataProcessingException {

		this.dpt = task;

		// TODO: Daten werden nicht parallel hochgeladen!
		ButtonThread thread = new ButtonThread() {

			@Override
			protected void doTask() throws CloudstoreException {

				for (ProviderFileContainer pfc : dpt.getProviderFileListForUploading()) {

					pfc.uploadData();

				}

			}
		};

		thread.start();

		try {
			thread.join();
		} catch (InterruptedException e) {}

		return task;
	}

	@Override
	public int getStatus() {

		int status = 0;

		for (ProviderFileContainer pfc : dpt.getProviderFileListForUploading()) {

			status += pfc.getProvider().getProcessStatus();

		}

		return (int) (status / this.dpt.getProviderFileListForUploading().size());

	}

}
