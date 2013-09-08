package de.uni_potsdam.hpi.cloudstore20.clientfrontend.helper;

import java.lang.reflect.Constructor;

public class Reflector {

	public static Object reflectClass(String completeClassPath, Object[] constructorParameter) throws ReflectionException {

		try {

			Class<?> myClass = Class.forName(completeClassPath);

			Class<?>[] constructorParameterTypes = new Class<?>[constructorParameter.length];
			for (int i = 0; i < constructorParameter.length; i++) {
				constructorParameterTypes[i] = constructorParameter[i].getClass();
			}

			Constructor<?> constructor = myClass.getDeclaredConstructor(constructorParameterTypes);

			return constructor.newInstance(constructorParameter);

		} catch (Exception e) {
			throw new ReflectionException(e.getMessage(), e.getCause());
		}

	}

}
