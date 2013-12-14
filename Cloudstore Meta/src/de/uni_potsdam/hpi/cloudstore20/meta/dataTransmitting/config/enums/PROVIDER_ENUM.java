package de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.enums;

public enum PROVIDER_ENUM {

	AZURE_US("Azure#US", 0.037d, PROVIDER_CONFIG_CATEGORY.AzureUS, "AzureStorageProvider"), 
	AZURE_EU("Azure#EU", 0.037d, PROVIDER_CONFIG_CATEGORY.AzureEU, "AzureStorageProvider"), 
	GOOGLE_EU("Google#EU", 0.054d, PROVIDER_CONFIG_CATEGORY.Google, "GoogleStorageProvider"), 
	GOOGLE_US("Google#US", 0.054d, PROVIDER_CONFIG_CATEGORY.Google, "GoogleStorageProvider"), 
	AMAZON_EU("Amazon#EU", 0.055d, PROVIDER_CONFIG_CATEGORY.Amazon, "AmazonStorageProvider"), 
	AMAZON_US("Amazon#us-west-1", 0.055d, PROVIDER_CONFIG_CATEGORY.Amazon, "AmazonStorageProvider"), 
	RACKSPACE("Rackspace#EU", 0.075d, PROVIDER_CONFIG_CATEGORY.Rackspace, "RackspaceStorageProvider"), 
	HP("HPStorage#EU", 0.09d, PROVIDER_CONFIG_CATEGORY.HPStorage, "HPStorageProvider");

	private String toString;
	private String className;
	private double storageCosts;
	private PROVIDER_CONFIG_CATEGORY configCategory;

	private PROVIDER_ENUM(String toString, double storageCosts, PROVIDER_CONFIG_CATEGORY configCategory, String className) {

		this.toString = toString;
		this.storageCosts = storageCosts;
		this.configCategory = configCategory;
		this.className = className;
	}

	public String toString() {

		return this.toString;
	}
	
	public String className(){
		
		return this.className;
	}
	
	public String getName(){
		return this.toString.split("#")[0];
	}
	
	public String getLocation(){
		return this.toString.split("#")[1];
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
