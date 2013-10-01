package de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.dataList;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.DataTransmittingClass;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.DataTransmittingException;

public class DataList extends DataTransmittingClass {

	private static final long serialVersionUID = 6881275138376341048L;

	private List<DataListElement> content = new LinkedList<DataListElement>();

	// public DataList(String content) throws DataTransmittingException {
	//
	// super(content);
	// this.content = this.buildDataListFromString(content, 0);
	// }

	public String getClassAsString() throws DataListException {

		try {
			return DataTransmittingClass.toString(this);
		} catch (IOException e) {
			throw new DataListException(e.getMessage(), e.getCause());
		}
		// return this.getDataListAsString(this, 0);
	}

	private DataList(List<DataListElement> content) throws DataTransmittingException {

		this.content = content;
	}

	// private String getDataListAsString(DataList dataList, int level) throws DataListException {
	//
	// StringBuilder content = new StringBuilder();
	//
	// for (DataListElement dle : dataList.getContent()) {
	//
	// if (content.length() != 0) {
	// content.append("#").append(level).append("#");
	// }
	//
	// if (dle.isFolder()) {
	// content.append(dle.getName()).append("#fa" + level + "#")
	// .append(this.getDataListAsString(dle.getFolderContent(), level + 1)).append("#fz" + level + "#");
	// } else {
	// content.append(dle.getName());
	// }
	//
	// }
	//
	// return content.toString();
	// }

	// private List<DataListElement> buildDataListFromString(String s, int level) throws DataTransmittingException {
	//
	// List<DataListElement> returnValue = new LinkedList<DataListElement>();
	// String splitter = "#" + level + "#";
	//
	// for (String element : s.split(splitter)) {
	//
	// DataListElement dle;
	//
	// if (element.contains("#fa" + level + "#")) {
	// String[] splitted = element.split("#fa" + level + "#");
	// dle = new DataListElement(splitted[0], new DataList(this.buildDataListFromString(
	// splitted[1].replace("#fz" + level + "#", ""), level + 1)));
	// } else {
	// dle = new DataListElement(element);
	// }
	//
	// returnValue.add(dle);
	// }
	//
	// return returnValue;
	//
	// }

	public List<DataListElement> getContent() {

		List<DataListElement> temp = new LinkedList<DataListElement>();

		for (DataListElement dle : this.content) {
			temp.add(dle);
		}

		return temp;
	}

}
