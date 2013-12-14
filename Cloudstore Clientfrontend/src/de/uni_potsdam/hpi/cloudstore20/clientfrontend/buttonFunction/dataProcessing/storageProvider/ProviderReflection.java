package de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.enums.PROVIDER_ENUM;

import static de.uni_potsdam.hpi.cloudstore20.clientfrontend.helper.Constants.*;

public class ProviderReflection {

	private static final String classLocation = "de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.implementations.";
	public final static long MAX_PROVIDER_LIVING_TIME = 2 * HOUR;

	private static Map<String, StorageProvider> providerList = new HashMap<String, StorageProvider>();
	private static Map<String, Long> providerLivingTime = new HashMap<String, Long>();

	private ProviderReflection() {
	}
	
	
	
	public static StorageProvider getProvider(PROVIDER_ENUM prov, boolean unique){
		return loadClass(prov, unique);
	}

	public static StorageProvider getProvider(PROVIDER_ENUM prov){
		return loadClass(prov, false);
	}
	

	private static StorageProvider loadClass(PROVIDER_ENUM prov, boolean unique) {

		String key = getKey(prov, unique);

		StorageProvider provider = null;
		if (!providerLivingTime.containsKey(key)) {
			provider = reflectClass(prov);
			providerList.put(key, provider);
			providerLivingTime.put(key, System.currentTimeMillis());
		}

		if ((System.currentTimeMillis() - providerLivingTime.get(key)) > MAX_PROVIDER_LIVING_TIME) {
			if(provider == null){
				provider = reflectClass(prov);
			}
			providerList.put(key, provider);
			providerLivingTime.put(key, System.currentTimeMillis());
		}

		if (provider == null) {
			provider = reflectClass(prov);
			providerList.put(key, provider);
			providerLivingTime.put(key, System.currentTimeMillis());

		}

		return provider;
	}

	private static String getKey(PROVIDER_ENUM prov, boolean unique) {
		if (unique) {
			return prov.toString() + "#" + System.currentTimeMillis();
		} else {
			return prov.toString();
		}

	}

	private static StorageProvider reflectClass(PROVIDER_ENUM prov) {
		try {
			Class<?> cls = Class.forName(ProviderReflection.classLocation
					+ prov.className());
			String location = prov.getLocation();
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
