package de.uni_potsdam.hpi.cloudstore20.serverbackend;

import java.util.LinkedList;
import java.util.List;

public class DatabaseStorageFake {

	private static DatabaseStorageFake instance = new DatabaseStorageFake();

	private DatabaseStorageFake() {

	}

	public static DatabaseStorageFake getInstance() {
		return instance;
	}

	// ##################

	private List<String> content = new LinkedList<String>();

	public void addContent(String s) {
		this.content.add(s);
	}

	public List<String> getContent() {
		return this.content;
	}
}
