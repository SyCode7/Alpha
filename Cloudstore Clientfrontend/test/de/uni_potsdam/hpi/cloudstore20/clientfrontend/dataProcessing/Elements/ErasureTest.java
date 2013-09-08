package de.uni_potsdam.hpi.cloudstore20.clientfrontend.dataProcessing.Elements;

import static org.junit.Assert.fail;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.DataProcessElement;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.DataProcessTask;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.DataProcessingException;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.ProviderFileContainer;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.Elements.Erasure;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.old.helper.FileHelper;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.old.provider.MockupStorageProvider;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.CloudstoreConfig;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.DATA_PROCESS_METHOD;

public class ErasureTest {

	private String tempFile;
	private long fileSize = 10485760;
	private int k = 4;
	private int m = 2;

	private DataProcessTask startContent;
	private DataProcessTask resultContent;

	@Before
	public void setUp() throws Exception {

		this.tempFile = System.getProperty("java.io.tmpdir") + "tempFile";

		FileHelper.generateRandomContentFile(this.tempFile, this.fileSize);
		this.startContent = new DataProcessTask(new File(this.tempFile));
		for (int i = 0; i < this.m + this.k; i++) {
			ProviderFileContainer pfc = new ProviderFileContainer(new MockupStorageProvider(String.valueOf(System
					.currentTimeMillis())));
			this.startContent.addProviderFileContainer(pfc);
		}
		this.startContent.getProviderFileListFor(DATA_PROCESS_METHOD.test).get(0).addFile(new File(this.tempFile));

	}

	@After
	public void tearDown() throws Exception {

		for (ProviderFileContainer pfc : this.resultContent.getProviderFileListFor(DATA_PROCESS_METHOD.test)) {
			for (File f : pfc.getFileList()) {
				f.delete();
			}
		}

		new File(this.tempFile).delete();

	}

	@Test
	public void testDoProcessing() {

		DataProcessElement dpt = new Erasure(CloudstoreConfig.loadDefault());
		try {
			this.resultContent = dpt.doProcessing(this.startContent);
			for (ProviderFileContainer pfc : this.resultContent.getProviderFileListFor(DATA_PROCESS_METHOD.test)) {
				if (pfc.getFileList().size() != 1) {
					fail("Ein Provider hat nicht genau ein Chunk");
				}
				// TODO: obere Schranke
				if (!(pfc.getFileList().get(0).length() >= (this.fileSize / this.k))) {
					fail("Datei hat die falsche Größe");
				}
				// TODO: Test ob Name der Datei einem Pattern entspricht
				if (false) {
					fail("Datei hat nicht den richtigen Namen");
				}
			}
		} catch (DataProcessingException e) {
			fail("Exception: " + e.getMessage());
		}

	}
}
