package de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.enums;

public enum DATA_PROCESS_METHOD {

	test("Test", "only for testing", IMAGE_CONTAINER.Test, IMAGE_CONTAINER.Test), erasure("Erasure", "Erasure-Encoding",
			IMAGE_CONTAINER.Test, IMAGE_CONTAINER.Test), preChunking("PreChunking", "Chunking Data", IMAGE_CONTAINER.Test,
			IMAGE_CONTAINER.Test), preSlicing("PreSlicing", "Slicing Data", IMAGE_CONTAINER.Test, IMAGE_CONTAINER.Test), uploadOptimization(
			"UploadOptimizer", "optimize Data for Upload", IMAGE_CONTAINER.Test, IMAGE_CONTAINER.Test), upload("Upload",
			"Uploading Data", IMAGE_CONTAINER.File, IMAGE_CONTAINER.Cloud), filePostProcessing("FilePostProcessing", "Post-Processing original File", IMAGE_CONTAINER.Test, IMAGE_CONTAINER.Test);

	private String className;
	private String desc;
	private IMAGE_CONTAINER sourcePic;
	private IMAGE_CONTAINER targetPic;

	private DATA_PROCESS_METHOD(String className, String desc, IMAGE_CONTAINER source, IMAGE_CONTAINER target) {

		this.className = className;
		this.desc = desc;
		this.sourcePic = source;
		this.targetPic = target;
	}

	public String getClassName() {

		return this.className;
	}

	public String getShortDescription() {

		return this.desc;
	}

	public IMAGE_CONTAINER getSourcePic() {

		return this.sourcePic;
	}

	public IMAGE_CONTAINER getTargetPic() {

		return this.targetPic;
	}
}
