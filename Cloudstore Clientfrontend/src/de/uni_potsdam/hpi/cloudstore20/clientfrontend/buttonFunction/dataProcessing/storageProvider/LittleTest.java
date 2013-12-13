package de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider;

public class LittleTest {

	public static void main(String[] args) throws StorageProviderException {

		StorageProvider prov = ProviderReflection.getProvider("Azure", "EU");

	}

}
