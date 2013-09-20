package de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.ButtonThread;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.helper.ReflectionException;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.helper.Reflector;
import de.uni_potsdam.hpi.cloudstore20.meta.CloudstoreException;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.CloudstoreConfig;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.DATA_PROCESS_METHOD;

public class DataProcessor {

	private static DataProcessor instance = new DataProcessor();

	public static DataProcessor getInstance() {

		return instance;
	}

	private static String packageName = "de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.Elements.";

	private List<DataProcessorUpdateInterface> toInform = new LinkedList<DataProcessorUpdateInterface>();
	private Map<File, CloudstoreConfig> workingList = new HashMap<File, CloudstoreConfig>();
	private List<DataProcessTask> doneWork = new LinkedList<DataProcessTask>();
	private boolean blockWorkingList = false;
	private ButtonThread worker;

	public void addToNoticeList(DataProcessorUpdateInterface clazz) {

		this.toInform.add(clazz);

	}

	public void addNewTask(File toUpload, CloudstoreConfig configUsed) {

		this.waitForFreeList();

		this.blockWorkingList = true;
		this.workingList.put(toUpload, configUsed);
		this.blockWorkingList = false;
		try {
			if (!this.worker.isRunning()) {
				this.processFile();
			}
		} catch (CloudstoreException e) {
			// TODO Im Thread ist ein Fehler aufgetretten! EXCEPTION HANDLING MEHR ALS NÖTIG
			e.printStackTrace();
		}

	}

	private void waitForFreeList() {

		while (this.blockWorkingList) {
			try {
				Thread.sleep(50l);
			} catch (InterruptedException e) {}
		}

	}

	private void processFile() {

		this.worker = new ButtonThread() {

			@Override
			protected void doTask() throws CloudstoreException {

				doProcessing();

			}

		};
		this.worker.start();

		try {
			while (this.worker.isRunning()) {
				try {
					Thread.sleep(1000l);
				} catch (InterruptedException e) {}
				this.updateAll();
			}
		} catch (CloudstoreException e) {
			// TODO Innerhalb des Runners ist ein Fehler aufgetretten. Hier muss nen DICKES handling hin!
			e.printStackTrace();
		}

	}

	private void doProcessing() throws DataProcessingException {

		Entry<File, CloudstoreConfig> entry = null;
		while ((entry = this.getNextTask()) != null) {

			DataProcessTask dpt = new DataProcessTask(entry.getKey());

			for (DATA_PROCESS_METHOD dpm : entry.getValue().getMethods()) {

				Object[] param = { entry.getValue() };
				try {
					DataProcessElement dpe = (DataProcessElement) Reflector.reflectClass(
							(DataProcessor.packageName + dpm.getClassName()), param);
					dpe.doProcessing(dpt);
				} catch (ReflectionException e) {
					throw new DataProcessingException(e.getMessage(), e.getCause());
				}
			}

		}

	}

	private Entry<File, CloudstoreConfig> getNextTask() {

		Entry<File, CloudstoreConfig> entry = null;

		this.blockWorkingList = true;
		if (!this.workingList.entrySet().isEmpty()) {
			for (Entry<File, CloudstoreConfig> entry_ : this.workingList.entrySet()) {
				entry = entry_;
				break;
			}
		}
		this.workingList.remove(entry.getKey());
		this.blockWorkingList = false;

		return entry;

	}

	private void updateAll() {

		for (DataProcessorUpdateInterface dpui : this.toInform) {
			dpui.updateContent(this);
		}

	}
}
