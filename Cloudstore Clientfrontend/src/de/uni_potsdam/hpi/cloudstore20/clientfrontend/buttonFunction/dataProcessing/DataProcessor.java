package de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing;

import java.io.File;
import java.math.BigInteger;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.ButtonThread;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.configuration.CloudraidNode;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.configuration.HasseDiagramm;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.Elements.Upload;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.StorageProvider;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.StorageProviderException;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.implementations.MockupStorageProvider;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.helper.ReflectionException;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.helper.Reflector;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.helper.ServletCommunicationException;
import de.uni_potsdam.hpi.cloudstore20.meta.CloudstoreException;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.CloudstoreConfig;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.CloudstoreConfigException;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.DATA_PROCESS_METHOD;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.OPTIMIZATION_FUNCTION;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.PROVIDER_ENUM;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.UsedCloudstoreConfig;

public class DataProcessor {

	private static DataProcessor instance = new DataProcessor();

	public static DataProcessor getInstance() {

		return instance;
	}

	private static String packageName = "de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.Elements.";

	private Map<File, CloudstoreConfig> workingList = new HashMap<File, CloudstoreConfig>();
	private Entry<File, UsedCloudstoreConfig> currentJob = null;
	private DataProcessElement dpe = null;
	private DATA_PROCESS_METHOD dpm = null;
	private List<DataProcessTask> doneWork = new LinkedList<DataProcessTask>();
	private boolean blockWorkingList = false;
	private boolean blockDoneList = false;
	private ButtonThread worker;

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

	}

	private void doProcessing() throws DataProcessingException {

		while ((this.currentJob = this.getNextTask()) != null) {

			DataProcessTask dpt = new DataProcessTask(this.currentJob.getKey());

			// TODO: Die richtigen Provider auslesen
			for (PROVIDER_ENUM prov : this.currentJob.getValue().getConfiguredProvider()) {
				try {
					dpt.addProviderFileContainer(new ProviderFileContainer(new MockupStorageProvider(prov.toString())));
				} catch (ServletCommunicationException | StorageProviderException e) {
					e.printStackTrace();
				}
			}

			for (DATA_PROCESS_METHOD dpm : this.currentJob.getValue().getMethods()) {

				Object[] param = { this.currentJob.getValue() };
				try {
					this.dpe = (DataProcessElement) Reflector.reflectClass((DataProcessor.packageName + dpm.getClassName()),
							param);
					this.dpm = dpm;
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

	private Entry<File, UsedCloudstoreConfig> getNextTask() throws DataProcessingException {

		Entry<File, UsedCloudstoreConfig> entry = null;

		this.blockWorkingList = true;
		if (!this.workingList.entrySet().isEmpty()) {
			for (Entry<File, CloudstoreConfig> entry_ : this.workingList.entrySet()) {
				UsedCloudstoreConfig ucc = this.getBestConfig(entry_.getValue(), entry_.getKey().length());
				entry = new AbstractMap.SimpleEntry<File, UsedCloudstoreConfig>(entry_.getKey(), ucc);
				break;
			}
		}
		if (entry != null) {
			this.workingList.remove(entry.getKey());
		}
		this.blockWorkingList = false;

		return entry;

	}

	private UsedCloudstoreConfig getBestConfig(CloudstoreConfig config, long fileSize) throws DataProcessingException {

		UsedCloudstoreConfig ucc = null;

		if (config.getDecideAlone4BestConfigToUse()) {

			HasseDiagramm hd = new HasseDiagramm(config.getConfiguredProvider(), fileSize);
			Set<CloudraidNode> nodes;
			try {
				nodes = hd.filterNodes(config.getMaxCosts(), config.getNumberOfNines());
			} catch (CloudstoreConfigException e) {
				throw new DataProcessingException(e.getMessage(), e.getCause());
			}

			for (OPTIMIZATION_FUNCTION of : config.getOptimizationOrdering()) {
				if (of.equals(OPTIMIZATION_FUNCTION.AVAILABILITY)) {
					nodes = this.getHighestAvail(nodes);
				} else if (of.equals(OPTIMIZATION_FUNCTION.PERFORMANCE)) {
					nodes = this.getHighestPerf(nodes);
				} else if (of.equals(OPTIMIZATION_FUNCTION.PRICE)) {
					nodes = this.getLowestCosts(nodes);
				}
			}

			// TODO: was passiert, wenn es wirklich mehrere wären?
			// gleiches Problem wie manuelle auswahl?
			if (nodes.size() <= 0) {
				throw new DataProcessingException("Es gibt keinen optimalen Knoten!");
			}
			CloudraidNode bestNode4thisProblem = null;
			for (CloudraidNode cn : nodes) {
				bestNode4thisProblem = cn;
				break;
			}
			if (bestNode4thisProblem == null) {
				throw new DataProcessingException("Beim Auslesen des optimalen Knotens ist ein Fehler aufgetreten.");
			}

			ucc = new UsedCloudstoreConfig(bestNode4thisProblem.getK(), bestNode4thisProblem.getM(),
					bestNode4thisProblem.getProviderSet(), config);

		} else {
			throw new DataProcessingException("not implemented yet");
		}

		return ucc;
	}

	private Set<CloudraidNode> getHighestPerf(Set<CloudraidNode> nodes) {

		Set<CloudraidNode> newNodes = new HashSet<CloudraidNode>();

		double bestValue = Double.MAX_VALUE;
		for (CloudraidNode cn : nodes) {

			double value = cn.calculateTime(cn.getUploadPerformance()) + cn.calculateTime(cn.getDownloadPerformance());

			if (value < bestValue) {
				newNodes.clear();
				bestValue = value;
			}
			if (value == bestValue) {
				newNodes.add(cn);
			}
		}

		return newNodes;
	}

	private Set<CloudraidNode> getLowestCosts(Set<CloudraidNode> nodes) {

		Set<CloudraidNode> newNodes = new HashSet<CloudraidNode>();

		double bestValue = Double.MAX_VALUE;
		for (CloudraidNode cn : nodes) {

			double value = cn.getCostsInComparisonToBestSingleUpload();

			if (bestValue > value) {
				newNodes.clear();
				bestValue = value;
			}
			if (bestValue == value) {
				newNodes.add(cn);
			}

		}

		return newNodes;
	}

	private Set<CloudraidNode> getHighestAvail(Set<CloudraidNode> nodes) {

		Set<CloudraidNode> newNodes = new HashSet<CloudraidNode>();

		BigInteger bestValue = new BigInteger("0");
		for (CloudraidNode cn : nodes) {

			BigInteger value = cn.getAvailability();
			// a.compareTo(b); // returns (-1 if a < b), (0 if a == b), (1 if a > b)
			if (value.compareTo(bestValue) == 1) {
				newNodes.clear();
				bestValue = value;
			}
			if (value.compareTo(bestValue) == 0) {
				newNodes.add(cn);
			}
		}

		return newNodes;
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
