package de.uni_potsdam.hpi.cloudstore20.clientfrontend.log.entry;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.log.Logger.LOGABLE_LOGS;

public class LogEntry extends BaseEntry {

	public LogEntry(String id, LOGABLE_LOGS log, String attribute) {
		super(id);
		this.type = log;
		this.attr = attribute;
	}

	public LogEntry(String id, LOGABLE_LOGS log, String attribute, long timestamp) {
		super(id, timestamp);
		this.type = log;
		this.attr = attribute;
	}

};