package de.uni_potsdam.hpi.cloudstore20.clientfrontend.storageProvider;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.io.Files;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.ProviderReflection;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.StorageProvider;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.StorageProviderException;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.enums.PROVIDER_ENUM;
import de.uni_potsdam.hpi.cloudstore20.meta.helper.FileHelper;
import de.uni_potsdam.hpi.cloudstore20.meta.helper.HelperException;


public class StorageProviderTest {

	StorageProvider provider = ProviderReflection.getProvider(PROVIDER_ENUM.AZURE_EU);
	String fName = "1mb";
	String workDirName = "provTest";

	File file = null;

	@Before
	public void setUp() throws Exception {
		
		File origFile = new File("testdata" + File.separator + fName);
		file = new File(workDirName + File.separator + fName);
		File workDir = new File(workDirName);
		if (!workDir.exists() || !workDir.isDirectory()) {
			assertTrue(workDir.mkdir());
		}

		Files.copy(origFile, file);
	}

	@After
	public void tearDown() throws Exception {

		if (file.exists())
			assertTrue(file.delete());
	}

	@Test
	public void testUploadDownload() throws NoSuchAlgorithmException, IOException, StorageProviderException, HelperException {
		String hash = FileHelper.getHashMD5(file);
		assertTrue(file.exists());
		String result = provider.uploadFile(file);
		assertTrue(file.delete());
		
		assertFalse(file.exists());
		file = provider.downloadFile(result);
		assertTrue(file.exists());
		
		String newHash = FileHelper.getHashMD5(file);
		
		assertTrue(hash.equals(newHash));
	}

}
