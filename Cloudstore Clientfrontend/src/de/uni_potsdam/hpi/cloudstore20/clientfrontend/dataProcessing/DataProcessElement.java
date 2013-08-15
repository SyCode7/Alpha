package de.uni_potsdam.hpi.cloudstore20.clientfrontend.dataProcessing;

public interface DataProcessElement {
	
	public enum DATA_PROCESS_METHOD{
		erasure, preChunking, preSlicing, uploadOptimization;
	}
	
}
