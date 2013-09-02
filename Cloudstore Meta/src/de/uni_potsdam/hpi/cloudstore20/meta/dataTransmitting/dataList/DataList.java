package de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.dataList;

import java.util.LinkedList;
import java.util.List;

public class DataList {

	private List<DataListElement> content = new LinkedList<DataListElement>();

	public DataList(String wholeContent) {
		this.content = this.buildDataListFromString(wholeContent, 0);
	}

	public String getClassAsString() throws DataListException {
		return this.getDataListAsString(this, 0);
	}

	private DataList(List<DataListElement> content) {
		this.content = content;
	}

	private String getDataListAsString(DataList dataList, int level)
			throws DataListException {

		StringBuilder content = new StringBuilder();

		for (DataListElement dle : dataList.getContent()) {

			if (content.length() != 0) {
				content.append("#").append(level).append("#");
			}

			if (dle.isFolder()) {
				content.append(dle.getName())
						.append("#+#")
						.append(this.getDataListAsString(
								dle.getFolderContent(), level + 1))
						.append("#-#");
			} else {
				content.append(dle.getName());
			}

		}

		return content.toString();
	}

	private List<DataListElement> buildDataListFromString(String s, int level) {

		List<DataListElement> returnValue = new LinkedList<DataListElement>();
		String splitter = "#" + level + "#";

		for (String element : s.split(splitter)) {

			DataListElement dle;

			if (element.contains("#+#")) {
				String[] splitted = element.split("#+#");
				dle = new DataListElement(splitted[0], new DataList(
						this.buildDataListFromString(
								splitted[1].replace("#-#", ""), level + 1)));
			} else {
				dle = new DataListElement(element);
			}

			returnValue.add(dle);
		}

		return returnValue;

	}

	public List<DataListElement> getContent() {
		return this.content;
	}

}
