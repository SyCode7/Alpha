package de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.DATA_PROCESS_METHOD;

public class DataProcessTask {

	private List<ProviderFileContainer> providerAndFiles = new LinkedList<ProviderFileContainer>();
	private List<DATA_PROCESS_METHOD> methods = new LinkedList<DATA_PROCESS_METHOD>();
	private File originalFile;

	public DataProcessTask(File originalFile) {

		this.originalFile = originalFile;
	}

	public File getOriginalFile() {

		return this.originalFile;
	}

	public List<DATA_PROCESS_METHOD> getMethods() {

		List<DATA_PROCESS_METHOD> temp = new LinkedList<DATA_PROCESS_METHOD>();
		for (DATA_PROCESS_METHOD dpm : this.methods) {
			temp.add(dpm);
		}

		return temp;
	}

	public List<ProviderFileContainer> getProviderFileListFor(DATA_PROCESS_METHOD method) throws DataProcessingException {

		if ((method != DATA_PROCESS_METHOD.test) && this.methods.contains(method)) {
			throw new DataProcessingException("Duplicate Method on same Task");
		}

		this.methods.add(method);
		return this.providerAndFiles;
	}

	public void addProviderFileContainer(ProviderFileContainer pfc) {

		this.providerAndFiles.add(pfc);
	}

}
