package de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.Elements;

import static org.junit.Assert.fail;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.DataProcessElement;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.DataProcessTask;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.DataProcessingException;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.ProviderFileContainer;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.implementations.MockupStorageProvider;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.CloudstoreConfig;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.DATA_PROCESS_METHOD;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.UsedCloudstoreConfig;
import de.uni_potsdam.hpi.cloudstore20.meta.helper.FileHelper;

public class UploadOptimizerTest {

	private float percentage = 0.05f;
	private long fileSize = 100 * 1024 * 1024;

	private UsedCloudstoreConfig conf;

	private DataProcessTask startContent;
	private DataProcessTask resultContent;

	@Before
	public void setUp() throws Exception {

		// TODO: richtige config erzeugen:
		this.conf = CloudstoreConfig.loadDefault();

		String filePath = System.getProperty("java.io.tmpdir") + "tempFile";
		String fileToTestPath = filePath + System.currentTimeMillis();
		File fileToTest = new File(fileToTestPath);
		fileToTest.deleteOnExit();
		FileHelper.generateRandomContentFile(fileToTestPath, this.fileSize);
		this.startContent = new DataProcessTask(fileToTest);

		// TODO: Provider auswählen und zuweisen. NICHT MOCKUP!!!
		for (int i = 0; i < this.conf.getM() + this.conf.getK(); i++) {

			String fileToTestPath_ = filePath + System.currentTimeMillis();
			File fileToTest_ = new File(fileToTestPath);
			FileHelper.generateRandomContentFile(fileToTestPath_, this.fileSize / (this.conf.getM() + this.conf.getK()));

			ProviderFileContainer pfc = new ProviderFileContainer(new MockupStorageProvider());
			pfc.addFile(fileToTest_);
			this.startContent.addProviderFileContainer(pfc);
		}

	}

	@After
	public void tearDown() throws Exception {

		for (ProviderFileContainer pfc : this.resultContent.getProviderFileListFor(DATA_PROCESS_METHOD.test)) {
			for (File f : pfc.getFileList()) {
				f.delete();
			}
		}

	}

	@Test
	public void testDoProcessing() throws DataProcessingException {

		DataProcessElement dpe = new UploadOptimizer(this.conf);

		this.resultContent = dpe.doProcessing(this.startContent);

		long min = Long.MAX_VALUE;
		long max = Long.MIN_VALUE;
		for (ProviderFileContainer pfc : this.resultContent.getProviderFileListFor(DATA_PROCESS_METHOD.test)) {

			if (pfc.getProvider() instanceof MockupStorageProvider) {
				fail("MockupProvider wird zum Testen der Uploadoptmierung verwendet.");
			}

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
