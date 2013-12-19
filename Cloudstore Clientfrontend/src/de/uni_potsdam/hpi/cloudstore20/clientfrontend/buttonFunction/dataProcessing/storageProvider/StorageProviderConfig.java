package de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.enums.PROVIDER_CONFIG_CATEGORY;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.enums.STORAGE_PROVIDER_CONFIG;

public class StorageProviderConfig {

	private static StorageProviderConfig instance = new StorageProviderConfig();

	public static StorageProviderConfig getInstance() {

		return instance;
	}

	public final static String config_file = "keys.conf";
	public final static String config_separator = ":::";

	private Map<PROVIDER_CONFIG_CATEGORY, Map<STORAGE_PROVIDER_CONFIG, String>> configs = new HashMap<PROVIDER_CONFIG_CATEGORY, Map<STORAGE_PROVIDER_CONFIG, String>>();

	private StorageProviderConfig() {

		try {
			this.loadConfig();
		} catch (StorageProviderException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void loadConfig() throws StorageProviderException, IOException {

		String line;
		File configFile = null;

		configFile = new File(StorageProviderConfig.config_file);
		if (!configFile.exists()) {
			throw new StorageProviderException("Please create a configuration file for the Providers!");
		}

		@SuppressWarnings("resource")
		BufferedReader br = new BufferedReader(new FileReader(configFile));

		while ((line = br.readLine()) != null) {

			if (line.length() == 0)
				continue;

			String split[] = line.split(StorageProviderConfig.config_separator);
			assert split.length == 3 : "invalid config line: " + line;

			if (this.configs.containsKey(split[0])) {
				throw new StorageProviderException("invalid provider config file");
			}

			Map<STORAGE_PROVIDER_CONFIG, String> config = new HashMap<STORAGE_PROVIDER_CONFIG, String>();
			this.configs.put(PROVIDER_CONFIG_CATEGORY.getFromString(split[0]), config);
			config.put(STORAGE_PROVIDER_CONFIG.Username, split[1]);
			config.put(STORAGE_PROVIDER_CONFIG.Password, split[2]);

		}

		br.close();

	}

	public String get(PROVIDER_CONFIG_CATEGORY providerCat, STORAGE_PROVIDER_CONFIG config) {

		return this.configs.get(providerCat).get(config);
	}

	public boolean hasProviderInformation(PROVIDER_CONFIG_CATEGORY providerCat) {

		return this.configs.containsKey(providerCat);
	}

	public void setProviderInformation(PROVIDER_CONFIG_CATEGORY providerCat, STORAGE_PROVIDER_CONFIG config, String content) {

		if (!this.hasProviderInformation(providerCat)) {
			this.configs.put(providerCat, new HashMap<STORAGE_PROVIDER_CONFIG, String>());
		}
		this.configs.get(providerCat).put(config, content);

		this.storeConfigToFile();

	}

	private void storeConfigToFile() {

		// TODO Auto-generated method stub

	}

}
