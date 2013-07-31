package de.uni_potsdam.hpi.cloudstore.provider.helper;

import java.util.HashMap;
import java.util.Map;

import de.uni_potsdam.hpi.cloudstore.log.Logger.LOG;

public abstract class ProviderReturnValue {

	private Map<String, Long> values = new HashMap<String, Long>();

	public void addValue(LOG type, long value) {

		this.values.put(type.toString(), value);
	}

	public long getValue(LOG type) {

		return this.values.get(type.toString());
	}

}
