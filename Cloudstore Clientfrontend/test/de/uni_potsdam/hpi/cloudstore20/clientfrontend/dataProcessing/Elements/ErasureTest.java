package de.uni_potsdam.hpi.cloudstore20.clientfrontend.dataProcessing.Elements;

import static org.junit.Assert.fail;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.dataProcessing.DataProcessElement;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.dataProcessing.DataProcessingException;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.dataProcessing.ProviderFileContainer;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.old.helper.FileHelper;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.old.provider.MockupStorageProvider;

public class ErasureTest {

	private String tempFile;
	private long fileSize = 10485760;
	private int k = 4;
	private int m = 2;

	private List<ProviderFileContainer> startContent;
	private List<ProviderFileContainer> resultContent;

	@Before
	public void setUp() throws Exception {

		this.tempFile = System.getProperty("java.io.tmpdir") + "tempFile";

		FileHelper.generateRandomContentFile(this.tempFile, this.fileSize);
		this.startContent = new LinkedList<ProviderFileContainer>();
		for (int i = 0; i < this.m + this.k; i++) {
			ProviderFileContainer pfc = new ProviderFileContainer(
					new MockupStorageProvider(String.valueOf(System
							.currentTimeMillis())));
			this.startContent.add(pfc);
		}
		this.startContent.get(0).addFile(new File(this.tempFile));

	}

	@After
	public void tearDown() throws Exception {

		for (ProviderFileContainer pfc : this.resultContent) {
			for (File f : pfc.getFileList()) {
				f.delete();
			}
		}

		new File(this.tempFile).delete();

	}

	@Test
	public void testDoProcessing() {

		DataProcessElement dpt = new Erasure();
		try {
			this.resultContent = dpt.doProcessing(this.startContent);
			for (ProviderFileContainer pfc : this.resultContent) {
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
