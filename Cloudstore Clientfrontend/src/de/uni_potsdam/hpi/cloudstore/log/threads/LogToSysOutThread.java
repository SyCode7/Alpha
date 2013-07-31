package de.uni_potsdam.hpi.cloudstore.log.threads;

import java.util.LinkedList;

import de.uni_potsdam.hpi.cloudstore.log.entry.BaseEntry;

/**
 * Used for logger tests
 * 
 */
public class LogToSysOutThread extends LogThread {

	public LogToSysOutThread(long sleepTime) {

		this.sleepingTime = sleepTime;
		this.sysOuts = true;
	}

	@Override
	protected void writeOut(LinkedList<BaseEntry> entryList) {
		try{
			for(BaseEntry entry : entryList){
				sysout(entry.toString());
			}
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	@Override
	boolean checkSettings() {

		return true;
	}

}
