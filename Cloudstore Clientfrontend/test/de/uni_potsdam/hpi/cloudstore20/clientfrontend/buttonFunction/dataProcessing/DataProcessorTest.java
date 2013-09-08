package de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing;

import static org.junit.Assert.fail;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.DataProcessTask;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.DataProcessingException;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.DataProcessor;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.ProviderFileContainer;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.CloudstoreConfig;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.DATA_PROCESS_METHOD;
import de.uni_potsdam.hpi.cloudstore20.meta.helper.FileHelper;

public class DataProcessorTest {

	public File fileToTest;
	private long fileSize = 100 * 1024 * 1024;
	private float percentage = 0.05f;

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
	public void testProcessFile() throws DataProcessingException {

		CloudstoreConfig config = CloudstoreConfig.loadDefault();

		DataProcessTask dpt = DataProcessor.processFile(config, this.fileToTest);

		long min = Long.MAX_VALUE;
		long max = Long.MIN_VALUE;
		for (ProviderFileContainer pfc : dpt.getProviderFileListFor(DATA_PROCESS_METHOD.test)) {

			long value = pfc.getUploadTimeForFileList();

			if (value > max) {
				max = value;
			}
			if (value < min) {
				min = value;
			}

		}

		long diff = max - min;
		if (max * this.percentage < diff) {
			fail("Uploadoptimierungsgrenze überschritten");
		}

	}

}
