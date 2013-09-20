package de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction;

import de.uni_potsdam.hpi.cloudstore20.meta.CloudstoreException;

public abstract class ButtonThread extends Thread {

	private CloudstoreException e = null;

	public ButtonThread() {

		this.run();
	}

	public void run() {

		try {
			this.doTask();
		} catch (CloudstoreException e) {
			this.e = e;
		}
	}

	public boolean isRunning() throws CloudstoreException {

		if (this.e != null) {
			throw e;
		} else {
			return this.isAlive();
		}

	}

	protected abstract void doTask() throws CloudstoreException;
}
