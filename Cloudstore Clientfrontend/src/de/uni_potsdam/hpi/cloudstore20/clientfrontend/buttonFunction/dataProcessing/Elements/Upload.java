package de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.Elements;

import java.util.LinkedList;
import java.util.List;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.ButtonThread;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.DataProcessElement;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.DataProcessTask;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.DataProcessingException;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.ProviderFileContainer;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.StorageProvider;
import de.uni_potsdam.hpi.cloudstore20.meta.CloudstoreException;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.UsedCloudstoreConfig;

public class Upload extends DataProcessElement {

	private DataProcessTask dpt;

	public Upload(UsedCloudstoreConfig config) {

		super(config);
		// TODO Auto-generated constructor stub
	}

	@Override
	public DataProcessTask doProcessing(DataProcessTask task) throws DataProcessingException {

		this.dpt = task;

		List<Thread> threads = new LinkedList<Thread>();

		for (final ProviderFileContainer pfc : task.getProviderFileListForUploading()) {

			ButtonThread t = new ButtonThread() {

				@Override
				protected void doTask() throws CloudstoreException {

					pfc.uploadData();

				}
			};

			t.start();
			threads.add(t);

		}

		for (Thread t : threads) {
			try {
				t.join();
			} catch (InterruptedException e) {}
		}

		return task;
	}

	@Override
	public int getStatus() {

		int status = 0;

		for (ProviderFileContainer pfc : this.dpt.getProviderFileListForUploading()) {

			status += pfc.getProvider().getProcessStatus();

		}

		return (int) (status / this.dpt.getProviderFileListForUploading().size());

	}

	public List<StorageProvider> getDetailedStatus() {

		List<StorageProvider> returnValue = new LinkedList<StorageProvider>();

		for (ProviderFileContainer pfc : this.dpt.getProviderFileListForUploading()) {

			returnValue.add(pfc.getProvider());

		}

		return returnValue;
	}

}
