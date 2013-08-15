package de.uni_potsdam.hpi.cloudstore20.clientfrontend.dataProcessing;

import java.util.LinkedList;
import java.util.List;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.dataProcessing.DataProcessElement.DATA_PROCESS_METHOD;

public class DataProcessTask {

	private List<ProviderFileContainer> providerAndFiles = new LinkedList<ProviderFileContainer>();
	private List<DATA_PROCESS_METHOD> methods = new LinkedList<DATA_PROCESS_METHOD>();

	public List<DATA_PROCESS_METHOD> getMethods() {

		List<DATA_PROCESS_METHOD> temp = new LinkedList<DATA_PROCESS_METHOD>();
		for (DATA_PROCESS_METHOD dpm : this.methods) {
			temp.add(dpm);
		}

		return temp;
	}

	public void addMethod(DATA_PROCESS_METHOD method) {

		this.methods.add(method);

	}
}
