package de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config;

public enum DATA_PROCESS_METHOD {

	test("Test"), erasure("Erasure"), preChunking("PreChunking"), preSlicing("PreSlicing"), uploadOptimization("UploadOptimizer");

	private String className;

	private DATA_PROCESS_METHOD(String className) {

		this.className = className;
	}

	public String getClassName() {

		return this.className;
	}
}
