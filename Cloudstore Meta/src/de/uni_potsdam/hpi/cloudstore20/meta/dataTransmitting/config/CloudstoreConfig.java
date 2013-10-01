package de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.DataTransmittingClass;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.DataTransmittingException;

public class CloudstoreConfig extends DataTransmittingClass {

	private static final long serialVersionUID = -6408120828047873381L;

	// private enum CloudstoreConfigElements {
	// dataProcessConfig, erasureConfig;
	// }

	private List<DATA_PROCESS_METHOD> methods = new LinkedList<DATA_PROCESS_METHOD>();
	private int k;
	private int m;

	private boolean loadFromServer = true;

	private CloudstoreConfig(List<DATA_PROCESS_METHOD> methods, int k, int m) {

		this.methods = methods;
		this.k = k;
		this.m = m;
	}

	@Override
	public String getClassAsString() throws DataTransmittingException {

		try {
			return DataTransmittingClass.toString(this);
		} catch (IOException e) {
			throw new DataTransmittingException(e.getMessage(), e.getCause());
		}

	}

	public List<DATA_PROCESS_METHOD> getMethods() {

		List<DATA_PROCESS_METHOD> temp = new LinkedList<DATA_PROCESS_METHOD>();

		for (DATA_PROCESS_METHOD dpm : this.methods) {
			temp.add(dpm);
		}

		return temp;
	}

	public int getK() {

		return this.k;
	}

	public int getM() {

		return this.m;
	}

	public void setLoadFromServer(boolean bool) {

		this.loadFromServer = bool;
	}

	public boolean getLoadFromServer() {

		return this.loadFromServer;
	}

	public static CloudstoreConfig loadDefault() {

		List<DATA_PROCESS_METHOD> methods = new LinkedList<DATA_PROCESS_METHOD>();
		methods.add(DATA_PROCESS_METHOD.erasure);
		methods.add(DATA_PROCESS_METHOD.upload);

		return new CloudstoreConfig(methods, 4, 2);

	}

	public long getSlicingNumber() {

		// TODO Auto-generated method stub
		return 0;
	}

}
