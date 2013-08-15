package de.uni_potsdam.hpi.cloudstore20.clientfrontend.log.threads;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.log.Logger;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.log.entry.BaseEntry;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.log.entry.LogEntry;

public class LogToFileThread extends LogThread {

	/* maximum filesize in byte */
	public static long MAX_FILE_SIZE = 131072; // 128kB
	public static String META_FILE_NAME_LOG = "meta_log.txt";
	public static String META_FILE_NAME_FILE = "meta_file.txt";

	private File location = new File(System.getProperty("java.io.tmpdir"));

	public LogToFileThread(long waitingTime, String location) {

		super(waitingTime);

		this.location = new File(location);
	}

	private File newFile(String name) {

		return new File(this.location, name);
	}

	private File getMetaLogs() {

		return newFile(META_FILE_NAME_LOG);
	}

	private File getMetaFiles() {

		return newFile(META_FILE_NAME_FILE);
	}

	private void addNewLogToMetaFile(String logtype) throws IOException {

		File meta = this.getMetaLogs();

		FileWriter writer = new FileWriter(meta, true);

		writer.write(logtype + "\n");

		writer.flush();
		writer.close();
	}

	private File getFile() {

		// create a new logfile
		int i = 1;
		File file = newFile(i + ".txt");
		while (file.length() >= LogToFileThread.MAX_FILE_SIZE) {
			i++;
			file = newFile(i + ".txt");
		}
		try {
			if (!getMetaFiles().exists() || !getMetaLogs().exists()) {
				this.createMetaFile();
			}
			if (file.createNewFile()) {
				this.addNewFileToMetaFileList(file);
			}
		} catch (IOException e) {
		}

		return file;
	}

	private void addNewFileToMetaFileList(File file) throws IOException {

		File meta = getMetaFiles();

		FileWriter writer = new FileWriter(meta, true);

		writer.write(file.getName() + "\n");

		writer.flush();
		writer.close();
	}

	private void createMetaFile() throws IOException {

		File meta = getMetaFiles();
		meta.createNewFile();

		File meta_log = getMetaLogs();
		meta_log.createNewFile();

		FileWriter writer = new FileWriter(meta_log, true);

		Logger.LOGABLE_LOGS[] logs = Logger.LOGABLE_LOGS.values();
		for (Logger.LOGABLE_LOGS log : logs) {
			writer.write(log.toString() + "\n");
		}

		writer.flush();
		writer.close();
	}

	@Override
	protected void writeOut(LinkedList<BaseEntry> entryList) {

		try {
			File file = this.getFile();

			FileWriter writer = new FileWriter(file, true);

			for (BaseEntry entry : entryList) {

				String logentry = entry.toString();
				if (logentry != null) {
					writer.write(logentry + "\n");
					String log = logentry.split(LogEntry.DELIMITER)[2];
					if (!Logger.LOGABLE_LOGS.includes(log)) {
						this.addNewLogToMetaFile(logentry
								.split(LogEntry.DELIMITER)[2]);
					}
				}
			}

			writer.flush();
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	boolean checkSettings() {

		if (this.sleepingTime > 0 && this.location != null
				&& this.location.isDirectory()) {
			return true;
		}
		return false;
	}
}
