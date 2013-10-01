package de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.ButtonThread;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.Elements.Upload;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.StorageProvider;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.StorageProviderException;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.implementations.MockupStorageProvider;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.helper.ReflectionException;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.helper.Reflector;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.helper.ServletCommunicationException;
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
	private Entry<File, CloudstoreConfig> currentJob = null;
	private DataProcessElement dpe = null;
	private DATA_PROCESS_METHOD dpm = null;
	private List<DataProcessTask> doneWork = new LinkedList<DataProcessTask>();
	private boolean blockWorkingList = false;
	private boolean blockDoneList = false;
	private ButtonThread worker;

	public void addToNoticeList(DataProcessorUpdateInterface clazz) {

		this.toInform.add(clazz);

	}

	public DataProcessTask getDoneTask(File toFind) {

		DataProcessTask dpt = null;

		// Task ist noch in abzuarbeitender Liste
		boolean inWorkingList;
		do {

			inWorkingList = false;

			this.waitForFreeWorkingList();
			this.blockWorkingList = true;
			for (File f : this.workingList.keySet()) {
				if (f.getAbsolutePath().equals(toFind.getAbsolutePath())) {
					inWorkingList = true;
				}
			}
			this.blockWorkingList = false;

			if (inWorkingList) {
				try {
					Thread.sleep(100l);
				} catch (InterruptedException e) {}
			}

		} while (inWorkingList);

		// Job wird gerade bearbeitet
		boolean equals = false;
		while (!equals) {

			if (this.currentJob == null) {
				break;
			}
			equals = this.currentJob.getKey().getAbsolutePath().equals(toFind.getAbsolutePath());

			try {
				Thread.sleep(100l);
			} catch (InterruptedException e) {}
		}

		// Job aus abgearbeiteter Liste raussuchen
		this.waitForFreeDoneList();
		for (DataProcessTask dpt_ : this.doneWork) {

			if (dpt_.getOriginalFile().getAbsolutePath().equals(toFind.getAbsolutePath())) {
				dpt = dpt_;
				break;
			}

		}

		return dpt;

	}

	public void addNewTask(File toUpload, CloudstoreConfig configUsed) {

		this.waitForFreeWorkingList();
		this.blockWorkingList = true;
		this.workingList.put(toUpload, configUsed);
		this.blockWorkingList = false;

		try {
			if (this.worker == null || !this.worker.isRunning()) {
				this.processFile();
			}
		} catch (CloudstoreException e) {
			// TODO Im Thread ist ein Fehler aufgetretten! EXCEPTION HANDLING MEHR ALS NÖTIG
			e.printStackTrace();
		}

	}

	private void waitForFreeWorkingList() {

		while (this.blockWorkingList) {
			try {
				Thread.sleep(50l);
			} catch (InterruptedException e) {}
		}

	}

	private void waitForFreeDoneList() {

		while (this.blockDoneList) {
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

		ButtonThread bt = new ButtonThread() {

			@Override
			protected void doTask() throws CloudstoreException {

				try {
					while (worker.isRunning()) {
						try {
							Thread.sleep(250l);
						} catch (InterruptedException e) {}
						updateAll();
					}
				} catch (CloudstoreException e) {
					// TODO Innerhalb des Runners ist ein Fehler aufgetretten. Hier muss nen DICKES handling hin!
					e.printStackTrace();
				}
			}
		};
		bt.start();

	}

	private void doProcessing() throws DataProcessingException {

		while ((this.currentJob = this.getNextTask()) != null) {

			DataProcessTask dpt = new DataProcessTask(this.currentJob.getKey());

			// TODO: Die richtigen Provider auslesen
			for (int i = 0; i < 6; i++) {
				try {
					dpt.addProviderFileContainer(new ProviderFileContainer(new MockupStorageProvider()));
				} catch (ServletCommunicationException | StorageProviderException e) {
					e.printStackTrace();
				}
			}

			for (DATA_PROCESS_METHOD dpm : this.currentJob.getValue().getMethods()) {

				this.dpm = dpm;

				Object[] param = { this.currentJob.getValue() };
				try {
					this.dpe = (DataProcessElement) Reflector.reflectClass((DataProcessor.packageName + dpm.getClassName()),
							param);
					this.dpe.doProcessing(dpt);
				} catch (ReflectionException e) {
					throw new DataProcessingException(e.getMessage(), e.getCause());
				}
			}

			this.waitForFreeDoneList();
			this.blockDoneList = true;
			this.doneWork.add(dpt);
			this.blockDoneList = false;
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
		if (entry != null) {
			this.workingList.remove(entry.getKey());
		}
		this.blockWorkingList = false;

		return entry;

	}

	private void updateAll() {

		for (DataProcessorUpdateInterface dpui : this.toInform) {
			dpui.updateContent(this);
		}

	}

	public DATA_PROCESS_METHOD getCurrentMethod() {

		return this.dpm;

	}

	public int getCurrentStatus() {

		if (this.dpe == null) {
			return 0;
		}

		return this.dpe.getStatus();
	}

	public String getCurrentFile() {

		if (this.currentJob == null) {
			return "";
		}

		File f = this.currentJob.getKey();

		return f.getName();
	}

	public List<String> getDoneWork() {

		this.waitForFreeDoneList();
		this.blockDoneList = true;

		List<String> copy = new LinkedList<String>();
		for (DataProcessTask dpt : this.doneWork) {
			copy.add(dpt.getOriginalFile().getName());
		}

		this.blockDoneList = false;
		return copy;
	}

	public List<String> getWorkList() {

		this.waitForFreeWorkingList();
		this.blockWorkingList = true;

		List<String> copy = new LinkedList<String>();
		for (Entry<File, CloudstoreConfig> entry : this.workingList.entrySet()) {
			copy.add(entry.getKey().getName());
		}

		this.blockWorkingList = false;
		return copy;
	}

	public List<StorageProvider> getCurrentProvider() throws DataProcessingException {

		if (this.getCurrentMethod() != DATA_PROCESS_METHOD.upload) {
			throw new DataProcessingException("Kein Upload zur Zeit aktiv");
		}

		return ((Upload) this.dpe).getDetailedStatus();
	}

}
