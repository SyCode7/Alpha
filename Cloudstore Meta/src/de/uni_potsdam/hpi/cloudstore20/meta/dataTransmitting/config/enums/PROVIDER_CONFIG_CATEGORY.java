package de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.enums;

public enum PROVIDER_CONFIG_CATEGORY {

	AzureUS("AccountName", "AccountKey"), 
	AzureEU("AccountName", "AccountKey"), 
	Google("AccessKey", "SecretKey"), 
	Amazon("AccessKey", "SecretKey"), 
	Rackspace("Username", "Password"), 
	HPStorage("ApiKey", "ApiSecret");

	private String pwd;
	private String username;

	private PROVIDER_CONFIG_CATEGORY(String username, String pwd) {

		this.pwd = pwd;
		this.username = username;
	}

	public static PROVIDER_CONFIG_CATEGORY getFromString(String string) {

		for (PROVIDER_CONFIG_CATEGORY pcc : PROVIDER_CONFIG_CATEGORY.values()) {
			if (pcc.toString().equals(string)) {
				return pcc;
			}
		}

		throw new IllegalArgumentException("No matching enum to given string found");
	}

	public String getUserNameCredentialDescription() {

		return this.username;
	}

	public String getPasswordCredentialsDescription() {

		return this.pwd;
	}

}
