package de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.enums;

public enum DATA_PROCESS_METHOD {

	test("Test", "only for testing"), erasure("Erasure", "Erasure-Encoding"), preChunking("PreChunking", "Chunking Data"), preSlicing(
			"PreSlicing", "Slicing Data"), uploadOptimization("UploadOptimizer", "optimize Data for Upload"), upload("Upload", "Uploading Data"), filePostProcessing("FilePostProcessing","Post-Processing Data");

	private String className;
	private String desc;

	private DATA_PROCESS_METHOD(String className, String desc) {

		this.className = className;
		this.desc = desc;
	}

	public String getClassName() {

		return this.className;
	}

	public String getShortDescription() {

		return this.desc;
	}
}
