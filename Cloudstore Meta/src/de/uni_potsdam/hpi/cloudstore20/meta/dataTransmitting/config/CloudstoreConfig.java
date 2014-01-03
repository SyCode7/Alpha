package de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.DataTransmittingClass;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.DataTransmittingException;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.enums.DATA_PROCESS_METHOD;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.enums.FILE_POST_HANDLING;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.enums.OPTIMIZATION_FUNCTION;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.enums.PROVIDER_ENUM;

public class CloudstoreConfig extends DataTransmittingClass {

	private static final long serialVersionUID = -6408120828047873381L;

	private List<DATA_PROCESS_METHOD> methods;
	private OPTIMIZATION_FUNCTION[] optimizationOrdering;
	private FILE_POST_HANDLING filePostHandling;
	private double maxCosts;
	private int numberOfNines;
	private double maxPerformance;

	// TODO: Datenshema für gespeicherte Passwörter etc.
	// aufpassen, dass Zugangsdaten NICHT zum Serverübertragen werden
	private Set<PROVIDER_ENUM> configuredProvider;

	private boolean loadFromServer = false;
	private boolean decideAlone4BestConfigToUse = true;

	protected CloudstoreConfig(List<DATA_PROCESS_METHOD> methods, Set<PROVIDER_ENUM> provider,
			OPTIMIZATION_FUNCTION[] optimizationOrdering, int numberOfNines, double maxCosts, FILE_POST_HANDLING fph,
			double maxPerformance) {

		this.optimizationOrdering = optimizationOrdering;
		this.configuredProvider = provider;
		this.methods = methods;
		this.numberOfNines = numberOfNines;
		this.maxCosts = maxCosts;
		this.filePostHandling = fph;
		this.maxPerformance = maxPerformance;
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

	public Set<PROVIDER_ENUM> getConfiguredProvider() {

		Set<PROVIDER_ENUM> list = new HashSet<PROVIDER_ENUM>();
		list.addAll(this.configuredProvider);
		return list;

	}

	public void setLoadFromServer(boolean bool) {

		this.loadFromServer = bool;
	}

	public boolean getLoadFromServer() {

		return this.loadFromServer;
	}

	public boolean getDecideAlone4BestConfigToUse() {

		return this.decideAlone4BestConfigToUse;

	}

	public OPTIMIZATION_FUNCTION[] getOptimizationOrdering() {

		OPTIMIZATION_FUNCTION[] t = new OPTIMIZATION_FUNCTION[this.optimizationOrdering.length];
		for (int i = 0; i < this.optimizationOrdering.length; i++) {
			t[i] = this.optimizationOrdering[i];
		}
		return t;
	}

	public double getMaxCosts() {

		return this.maxCosts;
	}

	public int getNumberOfNines() {

		return this.numberOfNines;
	}

	public static CloudstoreConfig loadDefault() {

		List<DATA_PROCESS_METHOD> methods = new LinkedList<DATA_PROCESS_METHOD>();
		methods.add(DATA_PROCESS_METHOD.erasure);
		methods.add(DATA_PROCESS_METHOD.upload);
		methods.add(DATA_PROCESS_METHOD.filePostProcessing);

		Set<PROVIDER_ENUM> provider = new HashSet<PROVIDER_ENUM>();
		for (PROVIDER_ENUM pe : PROVIDER_ENUM.values()) {
			provider.add(pe);
		}

		OPTIMIZATION_FUNCTION[] opti = new OPTIMIZATION_FUNCTION[3];
		opti[0] = OPTIMIZATION_FUNCTION.PERFORMANCE;
		opti[1] = OPTIMIZATION_FUNCTION.PRICE;
		opti[2] = OPTIMIZATION_FUNCTION.AVAILABILITY;

		double maxCosts = 2;
		int numberOfNines = 5;
		double maxPerformance = 5;

		return new CloudstoreConfig(methods, provider, opti, numberOfNines, maxCosts, FILE_POST_HANDLING.Stub, maxPerformance);

	}

	public void setNumberOfNines(Integer value) {

		this.numberOfNines = value;

	}

	public void setMaxCosts(Double value) {

		this.maxCosts = value;

	}

	public void setFilePostHandling(FILE_POST_HANDLING fph) {

		this.filePostHandling = fph;
	}

	public FILE_POST_HANDLING getFilePostHandling() {

		return this.filePostHandling;
	}

	public void setMaxPerformance(double maxPerformance) {

		this.maxPerformance = maxPerformance;

	}

	public double getMaxPerformance() {

		return this.maxPerformance;
	}

}
