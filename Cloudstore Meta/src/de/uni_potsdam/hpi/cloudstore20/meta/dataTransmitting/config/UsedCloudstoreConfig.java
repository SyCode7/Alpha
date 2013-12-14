package de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config;

import java.util.HashSet;
import java.util.Set;

import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.enums.PROVIDER_ENUM;

public class UsedCloudstoreConfig extends CloudstoreConfig {

	private static final long serialVersionUID = 2834799719659816267L;

	private int k;
	private int m;

	public UsedCloudstoreConfig(int k, int m, Set<PROVIDER_ENUM> provider, CloudstoreConfig base) {

		super(base.getMethods(), provider, base.getOptimizationOrdering(), base.getNumberOfNines(), base.getMaxCosts(), base
				.getFilePostHandling());
		this.k = k;
		this.m = m;
	}

	public int getK() {

		return this.k;
	}

	public int getM() {

		return this.m;
	}

	public static UsedCloudstoreConfig loadTestConfig() {

		int k = 3;
		int m = 1;
		Set<PROVIDER_ENUM> provider = new HashSet<PROVIDER_ENUM>();
		provider.add(PROVIDER_ENUM.AMAZON_EU);
		provider.add(PROVIDER_ENUM.AMAZON_US);
		provider.add(PROVIDER_ENUM.GOOGLE_EU);
		provider.add(PROVIDER_ENUM.GOOGLE_US);
		return new UsedCloudstoreConfig(k, m, provider, CloudstoreConfig.loadDefault());
	}

}
