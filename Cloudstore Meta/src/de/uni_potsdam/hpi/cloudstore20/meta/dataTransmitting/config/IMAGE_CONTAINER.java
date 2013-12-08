package de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config;

import java.io.File;

public enum IMAGE_CONTAINER {

	Test("pic\\", "test.png"), Cloud("pic\\", "cloud.jpg"), File("pic\\", "file.jpg"), Clear("pic\\", "clear.png");

	private String path;

	private IMAGE_CONTAINER(String folderPath, String fileName) {

		File f = new File(folderPath + fileName);

		if (!f.exists()) {
			throw new RuntimeException("ImageLoading-Error");
		}

		this.path = f.getAbsolutePath();
	}

	public String getPath() {

		return this.path;
	}

}
