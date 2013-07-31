package de.uni_potsdam.hpi.cloudstore.clientfrontend;

import java.io.IOException;

import de.uni_potsdam.hpi.cloudstore.helper.FileHelper;
import de.uni_potsdam.hpi.cloudstore.provider.JetS3tStorageProvider;
import de.uni_potsdam.hpi.cloudstore.provider.StorageProvider;
import de.uni_potsdam.hpi.cloudstore.provider.helper.ProviderException;
import de.uni_potsdam.hpi.cloudstore.provider.helper.ProviderReturnValueString;

public class Methods {

	public static void testMethod() {

		StorageProvider sp = new JetS3tStorageProvider("EU");
		String testFile = "/testfile";
		FileHelper.generateRandomContentFile(testFile, 1000l);
		ProviderReturnValueString prvs = null;
		try {
			prvs = sp.putFile("test", testFile);
		} catch (ProviderException e1) {
			e1.printStackTrace();
		}
		if (prvs == null) {
			return;
		}
		System.out.println("Erfolgreich");
		try {
			ServletCommunicator.sendGetRequestToServlet(System
					.currentTimeMillis() + "Erfolgreichhochgeladen");
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		try {
			if (sp.deleteFile("test", prvs.getContent())) {
				System.out.println("Erfolgreich wieder gel�scht");
				try {
					ServletCommunicator
							.sendGetRequestToServlet(System.currentTimeMillis()
									+ "Erfolgreichwiedergeloescht");
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			}
		} catch (ProviderException e1) {
			e1.printStackTrace();
		}

	}

}
