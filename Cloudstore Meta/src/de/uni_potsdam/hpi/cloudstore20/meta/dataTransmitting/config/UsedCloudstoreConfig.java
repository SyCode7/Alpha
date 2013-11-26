package de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config;

import java.util.Set;

public class UsedCloudstoreConfig extends CloudstoreConfig {

	private static final long serialVersionUID = 2834799719659816267L;

	private int k;
	private int m;

	public UsedCloudstoreConfig(int k, int m, Set<PROVIDER_ENUM> provider, CloudstoreConfig base) {

		super(base.getMethods(), provider, base.getOptimizationOrdering(), base.getNumberOfNines(), base.getMaxCosts());
		this.k = k;
		this.m = m;
	}

	public int getK() {

		return this.k;
	}

	public int getM() {

		return this.m;
	}

}
