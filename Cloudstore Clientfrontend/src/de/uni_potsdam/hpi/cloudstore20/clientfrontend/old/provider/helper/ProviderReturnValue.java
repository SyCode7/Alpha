package de.uni_potsdam.hpi.cloudstore20.clientfrontend.old.provider.helper;

import java.util.HashMap;
import java.util.Map;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.old.log.Logger.LOGABLE_LOGS;

public abstract class ProviderReturnValue {

	private Map<String, Long> values = new HashMap<String, Long>();

	public void addValue(LOGABLE_LOGS type, long value) {

		this.values.put(type.toString(), value);
	}

	public long getValue(LOGABLE_LOGS type) {

		return this.values.get(type.toString());
	}

}
