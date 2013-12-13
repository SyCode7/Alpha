package de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing;

import static org.junit.Assert.fail;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.CloudstoreConfig;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.enums.DATA_PROCESS_METHOD;
import de.uni_potsdam.hpi.cloudstore20.meta.helper.FileHelper;

public class DataProcessorTest {

	public File fileToTest;
	private long fileSize = 100 * 1024 * 1024;

	@Before
	public void setUp() throws Exception {

		this.fileToTest = new File(System.getProperty("java.io.tmpdir") + "tempFile");
		FileHelper.generateRandomContentFile(this.fileToTest.getAbsolutePath(), this.fileSize);

	}

	@After
	public void tearDown() throws Exception {

		this.fileToTest.delete();

	}

	@Test
	public void testProcessFile() {

		CloudstoreConfig config = CloudstoreConfig.loadDefault();

		DataProcessor dp = DataProcessor.getInstance();

		dp.addNewTask(this.fileToTest, config);
		DataProcessTask dpt = dp.getDoneTask(this.fileToTest);

		if (dpt == null) {
			fail("DataProcessTask nicht gefunden");
		}

		for (DATA_PROCESS_METHOD dpm : config.getMethods()) {

			if (!dpt.getMethods().contains(dpm)) {
				fail("Eine Methode wurde durch den Processor nicht ausgeführt!");
			}

		}

	}
}
