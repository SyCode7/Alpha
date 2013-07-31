package de.uni_potsdam.hpi.cloudstore.log.threads;

import java.util.LinkedList;

import de.uni_potsdam.hpi.cloudstore.log.Logger;
import de.uni_potsdam.hpi.cloudstore.log.entry.BaseEntry;

public abstract class LogThread extends Thread {

	protected boolean sysOuts = false;
//	private int id = 0;

	/* variables for writing after certain time of inactivity */
	protected long sleepingTime;


	private static long DEFAULT_WRITE_OUT_TIMEOUT = 30000; // 30sec

	private Logger logger = null;

	public boolean work = true;
	public boolean writing = false;


	public LogThread(long waitingTime) {

		this.sleepingTime = waitingTime;
	}

	public LogThread() {

		this.sleepingTime = LogThread.DEFAULT_WRITE_OUT_TIMEOUT;
	}

	public void run() {

		if (logger == null) {
			System.err.println("LogThread.run()>> no logger was been set!");
			return;
		}
		if (!this.checkSettings()) {
			System.err.println("LogThread.run() >> settings were not set properly!");
			return;
		}

		do {
			try {
				Thread.sleep(this.sleepingTime);
			} catch (InterruptedException e) {
				this.writing = true;
			}
			this.writeOut(logger.getCompleteList());
			this.writing = false;
		} while (this.work);
		
		sysout("Writer thread terminated");

	}

	public void setLogger(Logger logger) {

		this.logger = logger;
	}

	abstract void writeOut(LinkedList<BaseEntry> list);

	abstract boolean checkSettings();

	void sysout(String message) {

		if (this.sysOuts)
			System.out.println(message);
	}
}
