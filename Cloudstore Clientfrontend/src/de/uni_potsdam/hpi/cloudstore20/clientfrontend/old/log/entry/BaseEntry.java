package de.uni_potsdam.hpi.cloudstore20.clientfrontend.old.log.entry;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.old.log.Logger.LOGABLE_LOGS;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.old.log.Logger.LOGABLE_METHODS;

public abstract class BaseEntry {

	public static final String DELIMITER = " :: ";

	String attr = null;
	String id = null;
	long timestamp = 0L;
	LOGABLE_LOGS type = null;

	/* optional */
	// for Tasks: type
	int k = -1;
	int m = -1;
	LOGABLE_METHODS method = null;
	String action = null;

	// for Tests: test class
	String classNameUnderTest = null;

	// for ThreadTasks: provider name and file size
	String providerName = null;
	String providerLocation = null;
	long fileSize = -1;
	private String fileName = null;

	// for ThreadTasks: threadID
	String threadID = null;

	public BaseEntry(String id) {

		this.id = id;
		this.timestamp = System.currentTimeMillis();
	}

	public BaseEntry(String id, long timestamp) {

		this.id = id;
		this.timestamp = timestamp;
	}

	// provider name and location setter and getter

	public String getProvName() {

		return providerName;
	}

	public String getProvLocation() {

		return providerLocation;
	}

	public void setProvName(String name) {

		this.providerName = name;
	}

	public void setProvLocation(String location) {

		this.providerLocation = location;
	}

	// file size getter and setter
	public long getFileSize() {

		return fileSize;
	}

	public void setFileSize(long size) {

		fileSize = size;
	}

	public void setFileName(String fName) {
		this.fileName = fName;
	}

	public String getFileName() {

		return fileName;
	}

	// thread id getter and setter

	public String getThreadID() {

		return this.threadID;
	}

	public void setTreadID(String id) {

		this.threadID = id;
	}

	// test class setter and getter
	public String getTestClass() {

		return this.classNameUnderTest;
	}

	public void setTestClass(String className) {

		this.classNameUnderTest = className;
	}

	// type setter and getter
	public void set_type(int k, int m, LOGABLE_METHODS method, String action) {

		this.k = k;
		this.m = m;
		this.method = method;
		this.action = action;
	}

	public int get_m() {

		return this.m;
	}

	public int get_k() {

		return this.k;
	}

	public LOGABLE_METHODS get_method() {

		return this.method;
	}

	public String get_action() {

		return this.action;
	}

	public BaseEntry() {

	}

	public String getType() {

		return this.type.toString();
	}

	public LOGABLE_LOGS type() {

		return this.type;
	}

	public String getTimestamp() {

		return String.valueOf(this.timestamp);
	}

	public String getId() {

		return this.id;
	}

	public String getAttr() {

		if (attr == null)
			return "";
		return this.attr;
	}

	public String toString() {

		String dynamicLog = "";
		if (this.attr != null && this.attr != "") {
			dynamicLog = "#" + this.attr;
		}
		return this.id + BaseEntry.DELIMITER + this.timestamp
				+ BaseEntry.DELIMITER + this.getType() + dynamicLog;
	}

}
