package de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.configuration;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.enums.PROVIDER_ENUM;

public class CloudraidNode {

	private int k;
	private int m;
	private Set<PROVIDER_ENUM> provider;
	private Set<PROVIDER_ENUM> provider4Download;
	private long originalFileSize;
	private HasseDiagramm hasseDiagramm;

	@Override
	public String toString() {

		double costs = ((double) ((int) (this.getCostsInComparisonToBestSingleUpload() * 100000))) / 1000d;
		double performance = ((double) ((int) (this.getPerformanceInComparisonToBestSingleUpload() * 100000))) / 1000d;

		StringBuilder sb = new StringBuilder("CR-Config[");

		sb.append("K: ").append(this.k);
		sb.append(" - M: ").append(this.m);
		sb.append(" - up: ").append(this.provider);
		sb.append(" - down: ").append(this.provider4Download);
		sb.append(" - avail: ").append(this.getAvailabilityAsString());
		sb.append(" - costs: ").append(costs).append("%");
		sb.append(" - perf: ").append(performance).append("%");

		sb.append("]");
		return sb.toString();
	}

	public CloudraidNode(int k, int m, Set<PROVIDER_ENUM> provider, Set<PROVIDER_ENUM> provider4download, long originalFileSize,
			HasseDiagramm hasseDiagramm) {

		if (provider.size() != (k + m)) {
			throw new IllegalArgumentException("Die Anzahl der Provider stimmt nicht mit n überein.");
		}

		if (provider4download.size() != k) {
			throw new IllegalArgumentException("Die Anzahl der Provider für den Download stimmt nicht mit k überein.");
		}

		if (!provider.containsAll(provider4download)) {
			throw new IllegalArgumentException(
					"Die Provider für den Download sind nicht in dem vollständigen ProviderSet enthalten.");
		}

		this.k = k;
		this.m = m;
		this.provider = provider;
		this.provider4Download = provider4download;
		this.originalFileSize = originalFileSize;
		this.hasseDiagramm = hasseDiagramm;

	}

	public int getK() {

		return this.k;
	}

	public int getM() {

		return this.m;
	}

	public Set<PROVIDER_ENUM> getProviderSet() {

		return this.provider;
	}

	public double calculateTime(double performance) {

		return (this.originalFileSize / this.k) / performance;

	}

	public double getUploadPerformance() {

		double value = Double.MAX_VALUE;

		for (PROVIDER_ENUM prov : this.provider) {

			double temp = InMemDatabase.getInstance().getUploadSpeedFor(prov, this.originalFileSize / this.k);

			if (temp < value) {
				value = temp;
			}
		}

		return value;

	}

	public double getDownloadPerformance() {

		double value = Double.MAX_VALUE;

		for (PROVIDER_ENUM prov : this.provider4Download) {

			double temp = InMemDatabase.getInstance().getDownloadSpeedFor(prov, this.originalFileSize / this.k);

			if (temp < value) {
				value = temp;
			}
		}

		return value;

	}

	public String getAvailabilityAsString() {

		String result = "0." + this.getAvailability();

		int last0 = 0;
		for (int i = result.length() - 1; i >= 0; i--) {
			char c = result.charAt(i);
			if (c != '0') {
				last0 = i + 1;
				break;
			}
		}
		result = result.substring(0, last0);

		return result;

	}

	public BigInteger getAvailability() {

		Set<AvailiablityPropNode> probs = new HashSet<AvailiablityPropNode>();

		for (PROVIDER_ENUM prov : this.provider) {
			AvailiablityPropNode apn;
			if (probs.isEmpty()) {
				apn = new AvailiablityPropNode();
				apn.addNewProvider(prov, true);
				probs.add(apn);
				apn = new AvailiablityPropNode();
				apn.addNewProvider(prov, false);
				probs.add(apn);
			} else {
				Set<AvailiablityPropNode> newNodes = new HashSet<AvailiablityPropNode>();
				for (AvailiablityPropNode node : probs) {
					apn = node.clone();
					apn.addNewProvider(prov, true);
					newNodes.add(apn);
					apn = node.clone();
					apn.addNewProvider(prov, false);
					newNodes.add(apn);
				}
				probs = newNodes;
			}
		}

		BigInteger value = new BigInteger("0");

		for (AvailiablityPropNode apn : probs) {

			if (apn.getSuccessfull() >= this.k) {
				value = value.add(apn.getProb());
			}
		}

		return value;

	}

	public double getReliability() {

		double value = 1;
		for (PROVIDER_ENUM prov : this.provider4Download) {
			value *= InMemDatabase.getInstance().getAvailability(prov);
		}

		return value;
	}

	public double getCostsInComparisonToBestSingleUpload() {

		double ownValue = this.calcOwnCosts();

		return ownValue / this.hasseDiagramm.bestCosts;

	}

	public double getPerformanceInComparisonToBestSingleUpload() {

		double ownValue = this.calculateTime(this.getDownloadPerformance()) + this.calculateTime(this.getUploadPerformance());

		return ownValue / this.hasseDiagramm.bestPerformance;
	}

	public String getCostsInComparisonToBestSingleUploadAsString(int numberOfDigits) {

		double value = this.getCostsInComparisonToBestSingleUpload();
		return this.doubleToString(value, numberOfDigits);
	}

	private String doubleToString(double value, int numberOfDigits) {

		double factor = Math.pow(10, numberOfDigits);
		int i = (int) (value * factor);
		double newValue = Double.valueOf(i) / factor;
		String s = String.valueOf(newValue);
		return s.replace(".", ",");

	}

	private double calcOwnCosts() {

		double thisCosts = 0;
		for (PROVIDER_ENUM prov : this.provider) {
			thisCosts += (InMemDatabase.getInstance().getCostsPerWriteFor(prov) / 1024 / 1024) * (this.originalFileSize / this.k);
		}

		for (PROVIDER_ENUM prov : this.provider) {
			thisCosts += InMemDatabase.getInstance().getCostsForStorage(prov, (this.originalFileSize / this.k));
		}

		for (PROVIDER_ENUM prov : this.provider) {
			thisCosts += InMemDatabase.getInstance().getCostsPerRequestFor(prov) / 100000;
		}

		for (PROVIDER_ENUM prov : this.provider4Download) {
			thisCosts += (InMemDatabase.getInstance().getCostsPerReadFor(prov) / 1024 / 1024) * (this.originalFileSize / this.k);
		}
		return thisCosts;
	}

}
