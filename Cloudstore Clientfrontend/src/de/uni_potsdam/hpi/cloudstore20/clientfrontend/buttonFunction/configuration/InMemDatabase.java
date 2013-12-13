package de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.enums.PROVIDER_ENUM;

public class InMemDatabase {

	private static InMemDatabase instance = new InMemDatabase();

	private InMemDatabase() {

		this.refreshContent();
	}

	public static InMemDatabase getInstance() {

		return instance;
	}

	private Map<String, Map<String, Object>> content;
	private long lastTimeUpdated = 0;

	public double getUploadSpeedFor(PROVIDER_ENUM provider, long fileSizeInKB) {

		this.refreshContent();

		@SuppressWarnings("unchecked")
		Map<String, Double> speeds = (Map<String, Double>) this.content.get(provider.toString()).get("performance_write");

		Map<Long, Double> sizePerformanceMap = new HashMap<Long, Double>();
		for (String key : speeds.keySet()) {
			Long fileSize = 0l;
			if (key.contains("kb")) {
				fileSize = Long.valueOf(key.replace("kb", ""));
			}
			if (key.contains("mb")) {
				fileSize = Long.valueOf(key.replace("mb", "")) * 1024;
			}
			if (key.contains("gb")) {
				fileSize = Long.valueOf(key.replace("gb", "")) * 1024 * 1024;
			}
			if (fileSize == 0) {
				throw new RuntimeException("fileSize darf nicht 0 sein.");
			}
			sizePerformanceMap.put(fileSize, fileSize / speeds.get(key));
		}

		return this.calcPerformance(fileSizeInKB, sizePerformanceMap);
	}

	public double getDownloadSpeedFor(PROVIDER_ENUM provider, long fileSizeInMB) {

		this.refreshContent();

		@SuppressWarnings("unchecked")
		Map<String, Double> speeds = (Map<String, Double>) this.content.get(provider.toString()).get("performance_read");

		Map<Long, Double> sizePerformanceMap = new HashMap<Long, Double>();
		for (String key : speeds.keySet()) {
			Long fileSize = 0l;
			if (key.contains("kb")) {
				fileSize = Long.valueOf(key.replace("kb", ""));
			}
			if (key.contains("mb")) {
				fileSize = Long.valueOf(key.replace("mb", "")) * 1024;
			}
			if (key.contains("gb")) {
				fileSize = Long.valueOf(key.replace("gb", "")) * 1024 * 1024;
			}
			if (fileSize == 0) {
				throw new RuntimeException("fileSize darf nicht 0 sein.");
			}
			sizePerformanceMap.put(fileSize, fileSize / speeds.get(key));
		}

		return this.calcPerformance(fileSizeInMB, sizePerformanceMap);
	}

	private double calcPerformance(long fileSizeInKB, Map<Long, Double> sizePerformanceMap) {

		boolean valueAvailable = sizePerformanceMap.containsKey(fileSizeInKB);

		Map<Long, Double> differencesMap = new HashMap<Long, Double>();
		for (long fSize : sizePerformanceMap.keySet()) {
			differencesMap.put(Math.abs(fSize - fileSizeInKB), sizePerformanceMap.get(fSize));
		}

		Long[] orderedDifferences = differencesMap.keySet().toArray(new Long[0]);
		Arrays.sort(orderedDifferences);

		double performanceSum = 0;
		int c = 0;

		double bestPerformance = 0;
		for (int i = 0; i < 7 && i < orderedDifferences.length; i++) {
			long diff = orderedDifferences[i];
			performanceSum += differencesMap.get(diff);
			c++;
		}
		bestPerformance = performanceSum / c;

		if (valueAvailable) {
			bestPerformance = (bestPerformance + sizePerformanceMap.get(fileSizeInKB)) / 2;
		}

		return bestPerformance;

	}

	public double getCostsPerRequestFor(PROVIDER_ENUM provider) {

		this.refreshContent();

		double priceFor100k = Double.valueOf((String) this.content.get(provider.toString()).get("costs_request_costs"));
		double numberOfRequests = Double.valueOf((String) this.content.get(provider.toString()).get("costs_num_requests"));

		return priceFor100k / numberOfRequests;
	}

	public double getCostsPerReadFor(PROVIDER_ENUM provider) {

		this.refreshContent();

		return Double.valueOf((String) this.content.get(provider.toString()).get("costs_read"));
	}

	@SuppressWarnings("deprecation")
	public double getCostsForStorage(PROVIDER_ENUM provider, long fileSizeInKB) {

		return provider.getStorageCostsFor(fileSizeInKB);
	}

	public double getCostsPerWriteFor(PROVIDER_ENUM provider) {

		this.refreshContent();

		return Double.valueOf((String) this.content.get(provider.toString()).get("costs_write"));
	}

	public double getAvailability(PROVIDER_ENUM provider) {

		this.refreshContent();

		return Double.valueOf((String) this.content.get(provider.toString()).get("availability"));
	}

	private Map<String, Map<String, Object>> loadFile() {

		FileInputStream fis;
		try {
			ObjectMapper mapper = new ObjectMapper();
			fis = new FileInputStream(new File("provider.conf.off"));
			Map<String, Map<String, Object>> providers = mapper.readValue(fis,
					new TypeReference<Map<String, Map<String, Object>>>() {});
			return providers;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new HashMap<String, Map<String, Object>>();

	}

	private synchronized void refreshContent() {

		if ((System.currentTimeMillis() - this.lastTimeUpdated) > 360000) {
			this.lastTimeUpdated = System.currentTimeMillis();
			this.content = this.loadFile();
		}

	}

}
