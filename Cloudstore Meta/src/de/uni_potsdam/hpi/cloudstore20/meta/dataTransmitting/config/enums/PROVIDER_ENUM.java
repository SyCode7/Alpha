package de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.enums;

public enum PROVIDER_ENUM {

	AZURE_US("Azure#US", 0.037d, PROVIDER_CONFIG_CATEGORY.AzureUS), AZURE_EU("Azure#EU", 0.037d, PROVIDER_CONFIG_CATEGORY.AzureEU), GOOGLE_US(
			"Google#US", 0.054d, PROVIDER_CONFIG_CATEGORY.Google), RACKSPACE("Rackspace#EU", 0.075d,
			PROVIDER_CONFIG_CATEGORY.Rackspace), AMAZON_US("Amazon#us-west-1", 0.055d, PROVIDER_CONFIG_CATEGORY.Amazon), AMAZON_EU(
			"Amazon#EU", 0.055d, PROVIDER_CONFIG_CATEGORY.Amazon), GOOGLE_EU("Google#EU", 0.054d, PROVIDER_CONFIG_CATEGORY.Google), HP(
			"HPStorage#EU", 0.09d, PROVIDER_CONFIG_CATEGORY.HPStorage);

	private String toString;
	private double storageCosts;
	private PROVIDER_CONFIG_CATEGORY configCategory;

	private PROVIDER_ENUM(String toString, double storageCosts, PROVIDER_CONFIG_CATEGORY configCategory) {

		this.toString = toString;
		this.storageCosts = storageCosts;
		this.configCategory = configCategory;
	}

	public String toString() {

		return this.toString;
	}

	@Deprecated
	public double getStorageCostsFor(long fileSizeinKB) {

		// TODO: das ist nur ein stub. müsste in die datenbank...
		return (this.storageCosts / 1024 / 1024) * fileSizeinKB;

	}

	public static PROVIDER_ENUM fromString(String s) {

		for (PROVIDER_ENUM prov : PROVIDER_ENUM.values()) {
			if (prov.toString.equals(s)) {
				return prov;
			}
		}
		throw new IllegalArgumentException("Dieser String ist kein PROVIDER_ENUM.");
	}

	public static String[] getProvidersAsStringArray() {

		String[] result = new String[PROVIDER_ENUM.values().length];
		for (int i = 0; i < PROVIDER_ENUM.values().length; i++) {
			result[i] = PROVIDER_ENUM.values()[i].toString;
		}

		return result;

	}

	public PROVIDER_CONFIG_CATEGORY getConfigCategory() {

		return this.configCategory;
	}

}
