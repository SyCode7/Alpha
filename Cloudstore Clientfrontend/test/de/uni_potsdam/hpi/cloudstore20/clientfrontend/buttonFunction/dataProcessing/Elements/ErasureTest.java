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
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.UsedCloudstoreConfig;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.enums.DATA_PROCESS_METHOD;
import de.uni_potsdam.hpi.cloudstore20.meta.helper.FileHelper;

public class ErasureTest {

	private String tempFile;
	private long fileSize = 10485760;
	private UsedCloudstoreConfig config = UsedCloudstoreConfig.loadTestConfig();

	private DataProcessTask startContent;
	private DataProcessTask resultContent;

	@Before
	public void setUp() throws Exception {

		this.tempFile = System.getProperty("java.io.tmpdir") + "tempFile";

		FileHelper.generateRandomContentFile(this.tempFile, this.fileSize);
		this.startContent = new DataProcessTask(new File(this.tempFile));
		for (int i = 0; i < this.config.getM() + this.config.getK(); i++) {
			ProviderFileContainer pfc = new ProviderFileContainer(new MockupStorageProvider());
			this.startContent.addProviderFileContainer(pfc);
		}

	}

	@After
	public void tearDown() throws Exception {

		for (ProviderFileContainer pfc : this.resultContent.getProviderFileListFor(DATA_PROCESS_METHOD.test)) {
			for (File f : pfc.getFileList()) {
				f.delete();
			}

			((MockupStorageProvider) pfc.getProvider()).cleanUp();
		}

		new File(this.tempFile).delete();

	}

	@Test
	public void testDoProcessing() {

		DataProcessElement dpt = new Erasure(this.config);
		try {
			this.resultContent = dpt.doProcessing(this.startContent);
			for (ProviderFileContainer pfc : this.resultContent.getProviderFileListFor(DATA_PROCESS_METHOD.test)) {
				if (pfc.getFileList().size() != 1) {
					fail("Ein Provider hat nicht genau ein Chunk");
				}
				// TODO: obere Schranke
				if (!(pfc.getFileList().get(0).length() >= (this.fileSize / this.config.getK()))) {
					fail("Datei hat die falsche Größe");
				}
				// TODO: Test ob Name der Datei einem Pattern entspricht
				if (true) {
					fail("Datei hat nicht den richtigen Namen");
				}
			}
		} catch (DataProcessingException e) {
			fail("Exception: " + e.getMessage());
		}

	}
}
