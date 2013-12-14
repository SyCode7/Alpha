package de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static de.uni_potsdam.hpi.cloudstore20.clientfrontend.helper.Constants.*;

public class ProviderReflection {

	private static final String classLocation = "de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.implementations.";
	public final static long MAX_PROVIDER_LIVING_TIME = 2 * HOUR;

	private static Map<String, StorageProvider> providerList = new HashMap<String, StorageProvider>();
	private static Map<String, Long> providerLivingTime = new HashMap<String, Long>();
	private static Map<String, String> providerClassNames = new HashMap<String, String>();

	private ProviderReflection() {
	}
	
	public static StorageProvider getProvider(String name, String location, boolean unique){
		return loadClass(getClassName(name), location, unique);
	}

	public static StorageProvider getProvider(String name){
		return loadClass(getClassName(name), "", false);
	}
	
	public static StorageProvider getProvider(String name, boolean unique){
		return loadClass(getClassName(name), "", unique);
	}

	public static StorageProvider getProvider(String name, String location){
		return loadClass(getClassName(name), location, false);
	}

	private static StorageProvider loadClass(String name, String location, boolean unique) {

		String key = getKey(name, location, unique);

		StorageProvider prov = null;
		if (!providerLivingTime.containsKey(key)) {
			prov = reflectClass(name, location);
			providerList.put(key, prov);
			providerLivingTime.put(key, System.currentTimeMillis());
		}

		if ((System.currentTimeMillis() - providerLivingTime.get(key)) > MAX_PROVIDER_LIVING_TIME) {
			if(prov == null){
				prov = reflectClass(name, location);
			}
			providerList.put(key, prov);
			providerLivingTime.put(key, System.currentTimeMillis());
		}

		if (prov == null) {
			prov = reflectClass(name, location);
			providerList.put(key, prov);
			providerLivingTime.put(key, System.currentTimeMillis());

		}

		return prov;
	}

	private static String getClassName(String name){
		if(providerClassNames.size() == 0){
			providerClassNames.put("Google", "GoogleStorageProvider");
			providerClassNames.put("Amazon", "AmazonStorageProvider");
			providerClassNames.put("Azure", "AzureStorageProvider");
			providerClassNames.put("Rackspace", "RackspaceStorageProvider");
			providerClassNames.put("HPStorage", "HPStorageProvider");
			providerClassNames.put("Mockup", "MockupStorageProvider");
		}
		
		return providerClassNames.get(name);
	}

	private static String getKey(String name, String location, boolean unique) {
		if (unique) {
			return name + "#" + location + "#" + System.currentTimeMillis();
		} else {
			return name + "#" + location;
		}

	}

	private static StorageProvider reflectClass(String classname, String location) {
		try {
			Class<?> cls = Class.forName(ProviderReflection.classLocation
					+ classname);
			if(location != null && location.length() > 0){
				Constructor<?> constructor = cls
						.getConstructor(new Class[] { String.class });
				return (StorageProvider) constructor.newInstance(location);
			} else {
				Constructor<?> constructor = cls
						.getConstructor(new Class[] {});
				return (StorageProvider) constructor.newInstance();
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
}
