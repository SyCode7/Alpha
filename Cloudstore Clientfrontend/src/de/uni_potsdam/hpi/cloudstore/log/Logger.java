package de.uni_potsdam.hpi.cloudstore.log;

import java.util.LinkedList;
import java.util.Queue;

import de.uni_potsdam.hpi.cloudstore.log.entry.*;
import de.uni_potsdam.hpi.cloudstore.log.threads.*;
import de.uni_potsdam.hpi.cloudstore.provider.helper.ProviderReturnValueBoolean;
import de.uni_potsdam.hpi.cloudstore.provider.helper.ProviderReturnValueString;

public class Logger {

	public enum Method {
		t_erasure, t_native, t_parallel, t_opt_erasure, t_unknown
	};

	public static enum LOG {

		/** the whole test start-timestamp */
		START_TEST, END_TEST,

		/** one run in the big test */
		START_RUN, END_RUN,

		/* Die wichtigsten LOGS: */

		/** Error appeared timestamp */
		ERROR,

		/*
		 * UPLOAD
		 */

		/** Task - uploading start-timestamp */
		START_TASK, // is a task
		/** Task - uploading end-timestamp */
		END_TASK, // is a task

		/** Thread task - uploading start-timestamp (one packets to one providers) */
		START_UPLOAD, // is a thread task
		/** Thread task - uploading end-timestamp (one packets to one providers) */
		END_UPLOAD, // is a thread task

		/*
		 * DOWNLOAD
		 */

		/** Task - downloading start-timestamp */
		// START_TASK2, // is a task
		// /** Task - downloading end-timestamp */
		// END_TASK2, // is a task

		/** Thread task - uploading start-timestamp (one packets to one providers) */
		START_DOWNLOAD, // is a thread task
		/** Thread task - uploading end-timestamp (one packets to one providers) */
		END_DOWNLOAD, // is a thread task

		/* Nebenlogs: */

		/*
		 * ENCODING
		 */

		/** encoding start-timestamp */
		START_ENCODING, // is a task
		/** encoding end-timestamp */
		END_ENCODING, // is a task

		/*
		 * ENCRYPTION
		 */
		/** encryption start-timestamp */
		START_ENCRYPTION_PACKET, // is a thread task
		/** encryption end-timestamp */
		END_ENCRYPTION_PACKET, // is a thread task
		/** encryption start-timestamp */
		START_ENCRYPTION_FILE, // is a task
		/** encryption end-timestamp */
		END_ENCRYPTION_FILE, // is a task

		START_DECRYPTION_FILE, END_DECRYPTION_FILE, // is a task

		START_DECRYPTION, END_DECRYPTION, START_ENCRYPTION, END_ENCRYPTION,

		/*
		 * HASHING
		 */

		/** checking hash start-timestamp (only one file from one provider) */
		START_HASHING_FILE, // is a task
		/** checking hash end-timestamp (only one file from one provider) */
		END_HASHING_FILE, // is a task

		/** checking hash start-timestamp (various packets from many providers) */
		START_HASHING_PACKETS, // is a task

		/** checking hash start-timestamp (one packet from one providers) */
		START_HASHING_PACKET, // is a thread task
		/** checking hash end-timestamp (one packet from one providers) */
		END_HASHING_PACKET, // is a thread task

		/** checking hash end-timestamp (various packets from many providers) */
		END_HASHING_PACKETS, // is a task

		/*
		 * DELETING
		 */

		/** deleting start-timestamp (only one file from one provider) */
		START_DELETING_FILE, // is a task
		/** deleting end-timestamp (only one file from one provider) */
		END_DELETING_FILE, // is a task

		/** deleting start-timestamp (various packets from many providers) */
		START_DELETING_PACKETS, // is a task

		/** deleting start-timestamp (one packet from one providers) */
		START_DELETING_PACKET, // is a thread task
		/** deleting end-timestamp (one packet from one providers) */
		END_DELETING_PACKET, // is a thread task

		/** deleting end-timestamp (various packets from many providers) */
		END_DELETING_PACKETS, // is a task

		/*
		 * Optimamization
		 */
		START_OPTIMIZATION, END_OPTIMIZATION,

		/*
		 * DECODING
		 */

		/** decoding end-timestamp */
		START_DECODING,
		/** decoding end-timestamp */
		END_DECODING;

		public static boolean includes(String log) {

			for (LOG l : LOG.values()) {
				if (l.toString().equals(log))
					return true;
			}

			return false;
		}

	}

	public enum LoggerType {
		File, MySQL, SysOut;
	}

	public void writeBeforeExit() throws Throwable {

		// this.thread.work = false;
		this.writeOutNow();
		// this.thread.interrupt();
		// this.thread.join();

	};

	public static final int WRITE_OUT_SIZE = 1000;

	public static final String DELIMITER = null;

	private static boolean isSetup = false;
	/* Settings */
	private static long WAITING_TIME_WITHOUT_WRITE_OUT = 0L;
	private static String LOCATION_OR_DB_NAME = null;
	private static LoggerType TYPE = null;
	/* Settings end */

	private static Logger instance = new Logger();
	private LogThread thread;
	private Queue<BaseEntry> log = null;

	private Logger() {

		this.log = new LinkedList<BaseEntry>();

	}

	/**
	 * Before calling this method, call {@link Logger#setUp(long, String, LoggerType)} to pass needed settings for the Logger.
	 * 
	 * */
	public static Logger getInstance() {

		if (!Logger.isSetup) {
			System.err.println("The Logger is not set up yet!");
			return null;
		}
		instance.startThread();
		return instance;
	}

	private void startThread() {

		if (this.thread != null)
			return;

		this.thread = this.newThread();
		this.thread.setLogger(this);
		this.thread.start();
	}

	/**
	 * Call this method to setup the Logger before calling {@link Logger#getInstance()}. Otherwise {@link Logger#getInstance()}
	 * will return <code>null</code>.
	 * 
	 * */
	public static void setUp(long timeOut, String locationOrDbName, LoggerType type) {

		if (timeOut < 0 || locationOrDbName == null || locationOrDbName.length() == 0) {
			System.err.println("Parameter for setUp are not valid!");
			return;
		}

		Logger.isSetup = false;

		Logger.WAITING_TIME_WITHOUT_WRITE_OUT = timeOut;
		Logger.LOCATION_OR_DB_NAME = locationOrDbName;
		Logger.TYPE = type;

		Logger.isSetup = true;
	}

	/**
	 * Resets all settings. See also {@link Logger#setUp(long, String, LoggerType)}
	 */
	public static void reset() {

		Logger.WAITING_TIME_WITHOUT_WRITE_OUT = 0l;
		Logger.LOCATION_OR_DB_NAME = null;
		Logger.TYPE = null;

		Logger.isSetup = false;
	}

	/* Settings getter */
	public static long getTimeOut() {

		return Logger.WAITING_TIME_WITHOUT_WRITE_OUT;
	}

	public static String getDBName() {

		if (Logger.TYPE != LoggerType.MySQL) {
			return null;
		}
		return Logger.LOCATION_OR_DB_NAME;
	}

	public static String getLocation() {

		if (Logger.TYPE != LoggerType.File) {
			return null;
		}
		return Logger.LOCATION_OR_DB_NAME;
	}

	public static LoggerType getType() {

		return Logger.TYPE;
	}

	/* Settings getter end */

	private LogThread newThread() {

		switch (Logger.TYPE) {
		case File:
			return new LogToFileThread(Logger.getTimeOut(), Logger.getLocation());
		case SysOut:
		default:
			return new LogToSysOutThread(Logger.getTimeOut());
		}
	}

	// ist zum zwischen speichern der Logs gedacht, die nicht gleich geschrieben werden sollen
	public LogEntry getLogEntry(String id, LOG log, String attribute) {

		return new LogEntry(id, log, attribute);
	}

	public LogEntry getLogEntry(String id, LOG log, String attribute, long timestamp) {

		return new LogEntry(id, log, attribute, timestamp);
	}

	private LogEntry getProviderLogEntry(String id, LOG log, long timeStamp, String providerName, String location, long fileSize) {

		LogEntry ent = new LogEntry(id, log, null, timeStamp);
		ent.setProvName(providerName);
		ent.setProvLocation(location);
		ent.setFileSize(fileSize);
		return ent;
	}

	public void addRawLog(String id, long timestamp, LOG log, String attribute) {

		this.addThreadSafe(new LogEntry(id, log, attribute, timestamp));

		writeOutIfNeeded();

	}

	// ist zum schreiben der log gedacht, die nicht gleich geschrieben werden sollen
	public void addRawLog(BaseEntry logEntry) {

		this.addThreadSafe(logEntry);

		writeOutIfNeeded();
	}

	private synchronized void addThreadSafe(BaseEntry logEntry) {

		this.log.add(logEntry);
	}

	/**
	 * adding Task-Logs (<code>LOG.REQUEST_...</code> and <code>LOG.RESPONSE_...</code>)
	 * */
	public void addLog(String id, LOG log, String attribute, int k, int m, Method method, String action, String fileName) {

		LogEntry entry = this.getLogEntry(id, log, attribute);
		entry.set_type(k, m, method, action);
		entry.setFileName(fileName);
		this.addThreadSafe(entry);
		writeOutIfNeeded();
	}

	public void addLog(String id, LOG log, String attribute, int k, int m, Method method, String action, String fileName, long timestamp) {

		LogEntry entry = this.getLogEntry(id, log, attribute, timestamp);
		entry.set_type(k, m, method, action);
		entry.setFileName(fileName);
		this.addThreadSafe(entry);
		writeOutIfNeeded();
	}

	/**
	 * Adding Test-Logs (<code>LOG.START_TEST</code> and <code>LOG.END_TEST</code>)
	 * */
	public void addLog(String id, LOG log, String attribute, String testClass) {

		LogEntry entry = this.getLogEntry(id, log, attribute);
		entry.setTestClass(testClass);

		this.addThreadSafe(entry);
		writeOutIfNeeded();
	}

	public void addLog(String id, LOG log) {

		this.addLog(id, log, null);
	}

	public void addLog(String id, LOG log, String attribute) {

		this.addThreadSafe(this.getLogEntry(id, log, attribute));

		writeOutIfNeeded();

	}

	public void addLog(String id, ProviderReturnValueBoolean value, String providerName, String location, long fileSize) {

		this.addThreadSafe(getProviderLogEntry(id, LOG.START_DOWNLOAD, value.getValue(LOG.START_DOWNLOAD), providerName,
				location, fileSize));
		this.addThreadSafe(getProviderLogEntry(id, LOG.END_DOWNLOAD, value.getValue(LOG.END_DOWNLOAD), providerName, location,
				fileSize));

		writeOutIfNeeded();

	}

	public void addLog(String id, ProviderReturnValueBoolean value, String providerName) {

		this.addLog(id, value, providerName, null, -1);

	}

	public void addLog(String id, ProviderReturnValueString value, String providerName, String location, long fileSize) {

		this.addThreadSafe(getProviderLogEntry(id, LOG.START_UPLOAD, value.getValue(LOG.START_UPLOAD), providerName, location,
				fileSize));
		this.addThreadSafe(getProviderLogEntry(id, LOG.END_UPLOAD, value.getValue(LOG.END_UPLOAD), providerName, location,
				fileSize));

		writeOutIfNeeded();

	}

	public void addLog(String id, ProviderReturnValueString value, String providerName) {

		this.addLog(id, value, providerName, null, -1);
	}

	public void addError(String id, String message, String providerName, String location, long size) {

		BaseEntry b = this.getLogEntry(id, LOG.ERROR, message);
		b.setProvName(providerName);
		b.setProvLocation(location);
		b.setFileSize(size);
		this.addThreadSafe(b);

		writeOutIfNeeded();

	}

	public void addError(String id, String message, String providerName, long size) {

		this.addError(id, message, providerName, "", size);
	}

	private void writeOutIfNeeded() {

		if (this.getSize() >= Logger.WRITE_OUT_SIZE) {
			this.writeOutNow(false);
		}

	}

	public synchronized int getSize() {

		return this.log.size();
	}

	public void writeOutNow() {

		this.writeOutNow(true);
	}

	public void writeOutNow(boolean wait) {

		this.thread.interrupt();
		if (!wait)
			return;
		while (this.thread.writing) {
			try {
				Thread.sleep(100l);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	public synchronized LinkedList<BaseEntry> getCompleteList() {

		LinkedList<BaseEntry> copy = new LinkedList<BaseEntry>(this.log);
		// Iterator<BaseEntry> returnValue = this.log.iterator();
		this.log = new LinkedList<BaseEntry>();

		return copy;
	}

}
