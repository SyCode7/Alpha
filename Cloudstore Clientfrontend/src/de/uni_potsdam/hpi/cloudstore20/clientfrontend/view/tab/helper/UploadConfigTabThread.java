package de.uni_potsdam.hpi.cloudstore20.clientfrontend.view.tab.helper;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.configuration.CloudraidNode;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.configuration.HasseDiagramm;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.DataProcessor;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.view.DefaultWindow;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.view.tab.UploadConfigTab;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.CloudstoreConfig;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.CloudstoreConfigException;

public class UploadConfigTabThread extends Thread {

	private UploadConfigTab uct;

	public UploadConfigTabThread(UploadConfigTab uct) {

		this.uct = uct;
	}

	@Override
	public void run() {

		List<String> listContent = new LinkedList<String>();

		CloudstoreConfig config = DefaultWindow.config;
		HasseDiagramm hd = new HasseDiagramm(config.getConfiguredProvider(), 100 * 1024 * 1024);
		Set<CloudraidNode> nodes;
		try {
			nodes = hd.filterNodes(config.getMaxCosts(), config.getNumberOfNines(), config.getMaxPerformance());
		} catch (CloudstoreConfigException e1) {
			listContent.add("An error occured: ");
			for (StackTraceElement ste : e1.getStackTrace()) {
				listContent.add(ste.toString());
			}
			return;
		}

		nodes = DataProcessor.applyOptimization(config, nodes);

		if (nodes.size() <= 0) {
			listContent.add("Keine Nodes mit gewählter Konfiguration vorhanden.");
		} else {
			for (CloudraidNode cn : nodes) {
				listContent.add(cn.toString());
			}
		}

		this.uct.writeToList(listContent);
	}

}
