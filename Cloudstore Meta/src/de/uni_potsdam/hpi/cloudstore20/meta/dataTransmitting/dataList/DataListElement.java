package de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.dataList;

import java.io.Serializable;

public class DataListElement implements Serializable {

	private static final long serialVersionUID = 9160413449819252106L;
	private String name;
	private boolean isFolder;
	private DataList folderContent;

	public String getName() {

		return this.name;
	}

	public boolean isFolder() {

		return this.isFolder;
	}

	public DataList getFolderContent() throws DataListException {

		if (this.isFolder) {
			return this.folderContent;
		}

		throw new DataListException("this is not a folder");
	}

	public DataListElement(String name, DataList list) {

		this.name = name;
		this.isFolder = true;
		this.folderContent = list;
	}

	public DataListElement(String name) {

		this.name = name;
		this.isFolder = false;
		this.folderContent = null;
	}

}
