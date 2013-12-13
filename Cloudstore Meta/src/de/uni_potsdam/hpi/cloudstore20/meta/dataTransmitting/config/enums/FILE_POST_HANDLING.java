package de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.enums;

public enum FILE_POST_HANDLING {

	DontMove("Change anything", "The uploaded file will not be touched or moved after a successfull upload."), Stub("Stub File",
			"After a successfull upload the file will be deleted and replaced by a stub, containing information about the uploading process."), Delete(
			"Delete File", "After a successfull upload the file will be removed from the local file system.");

	private String displayString;
	private String detailedDescription;

	private FILE_POST_HANDLING(String ds, String descr) {

		this.displayString = ds;
		this.detailedDescription = descr;
	}

	public String getDisplayString() {

		return this.displayString;
	}

	public String getDetailedDescription() {

		return this.detailedDescription;
	}

}
