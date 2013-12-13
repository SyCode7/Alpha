package de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing;

import static org.junit.Assert.fail;

import java.io.File;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.implementations.MockupStorageProvider;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.enums.DATA_PROCESS_METHOD;

public class DataProcessingTaskTest {

	private DataProcessTask dpt;

	@Before
	public void setUp() throws Exception {

		this.dpt = new DataProcessTask(new File(""));
		for (int i = 0; i < 6; i++) {
			ProviderFileContainer pfc = new ProviderFileContainer(new MockupStorageProvider());
			this.dpt.addProviderFileContainer(pfc);
		}

	}

	@After
	public void tearDown() throws Exception {

		for (ProviderFileContainer pfc : this.dpt.getProviderFileListFor(DATA_PROCESS_METHOD.test)) {
			((MockupStorageProvider) pfc.getProvider()).cleanUp();
		}

	}

	@Test
	public void testGetMethods() {

		DATA_PROCESS_METHOD method1 = DATA_PROCESS_METHOD.erasure;
		DATA_PROCESS_METHOD method2 = DATA_PROCESS_METHOD.preSlicing;

		try {
			this.dpt.getProviderFileListFor(method1);
		} catch (DataProcessingException e) {
			fail("exception");
		}
		try {
			this.dpt.getProviderFileListFor(method2);
		} catch (DataProcessingException e) {
			fail("exception");
		}

		List<DATA_PROCESS_METHOD> content = this.dpt.getMethods();
		if (!content.contains(method2)) {
			fail("Methode2 nicht in Liste");
		}
		if (!content.contains(method1)) {
			fail("Methode1 nicht in Liste");
		}

	}

	@Test
	public void testGetProviderFileListFor() {

		try {
			this.dpt.getProviderFileListFor(DATA_PROCESS_METHOD.erasure);
		} catch (DataProcessingException e) {
			fail("exception to early");
		}
		try {
			this.dpt.getProviderFileListFor(DATA_PROCESS_METHOD.erasure);
		} catch (DataProcessingException e) {
			return;
		}

		fail("no exception thrown");
	}

}
