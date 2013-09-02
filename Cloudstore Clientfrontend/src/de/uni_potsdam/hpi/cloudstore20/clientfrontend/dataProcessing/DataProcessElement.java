package de.uni_potsdam.hpi.cloudstore20.clientfrontend.dataProcessing;

public interface DataProcessElement {

	public enum DATA_PROCESS_METHOD {
		test, erasure, preChunking, preSlicing, uploadOptimization;
	}

	public abstract DataProcessTask doProcessing(DataProcessTask task) throws DataProcessingException;

}
