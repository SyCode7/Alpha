package de.uni_potsdam.hpi.cloudstore20.clientfrontend.dataProcessing;

import java.util.List;

public interface DataProcessElement {

	public enum DATA_PROCESS_METHOD {
		erasure, preChunking, preSlicing, uploadOptimization;
	}

	public abstract List<ProviderFileContainer> doProcessing(List<ProviderFileContainer> taskContent)
			throws DataProcessingException;

}
