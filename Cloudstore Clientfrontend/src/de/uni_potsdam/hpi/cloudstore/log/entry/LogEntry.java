package de.uni_potsdam.hpi.cloudstore.log.entry;

import de.uni_potsdam.hpi.cloudstore.log.Logger.LOG;

public class LogEntry extends BaseEntry {


	public LogEntry(String id, LOG log, String attribute) {
		super(id);
		this.type = log;
		this.attr = attribute;
	}
	
	public LogEntry(String id, LOG log, String attribute, long timestamp) {
		super(id, timestamp);
		this.type = log;
		this.attr = attribute;
	}

};