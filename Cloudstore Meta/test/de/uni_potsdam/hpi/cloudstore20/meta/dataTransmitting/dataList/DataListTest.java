package de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.dataList;

import static org.junit.Assert.fail;

import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uni_potsdam.hpi.cloudstore20.meta.CloudstoreException;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.DataTransmittingClass;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.DataTransmittingException;

public class DataListTest {

	private DataList dl;
	private String content;

	@Before
	public void setUp() throws Exception {

		this.content = "rO0ABXNyAEdkZS51bmlfcG90c2RhbS5ocGkuY2xvdWRzdG9yZTIwLm1ldGEuZGF0YVRyYW5zbWl0dGluZy5kYXRhTGlzdC5EYXRhTGlzdF9/M0egphY4AgABTAAHY29udGVudHQAEExqYXZhL3V0aWwvTGlzdDt4cgBLZGUudW5pX3BvdHNkYW0uaHBpLmNsb3Vkc3RvcmUyMC5tZXRhLmRhdGFUcmFuc21pdHRpbmcuRGF0YVRyYW5zbWl0dGluZ0NsYXNzVlB3dzdeOXsCAAB4cHNyABRqYXZhLnV0aWwuTGlua2VkTGlzdAwpU11KYIgiAwAAeHB3BAAAAARzcgBOZGUudW5pX3BvdHNkYW0uaHBpLmNsb3Vkc3RvcmUyMC5tZXRhLmRhdGFUcmFuc21pdHRpbmcuZGF0YUxpc3QuRGF0YUxpc3RFbGVtZW50fyBTf6gv5YoCAANaAAhpc0ZvbGRlckwADWZvbGRlckNvbnRlbnR0AElMZGUvdW5pX3BvdHNkYW0vaHBpL2Nsb3Vkc3RvcmUyMC9tZXRhL2RhdGFUcmFuc21pdHRpbmcvZGF0YUxpc3QvRGF0YUxpc3Q7TAAEbmFtZXQAEkxqYXZhL2xhbmcvU3RyaW5nO3hwAHB0AAtzZXRVcF9maWxlMXNxAH4ABgBwdAALc2V0VXBfZmlsZTJzcQB+AAYBc3EAfgAAc3EAfgAEdwQAAAACc3EAfgAGAHB0AAtzZXRVcF9maWxlMXNxAH4ABgFzcQB+AABzcQB+AAR3BAAAAAFzcQB+AAYAcHQAC3NldFVwX2ZpbGUxeHQADHNldFVwX2ZvbGRlcnh0AAxzZXRVcF9mb2xkZXJzcQB+AAYAcHQAC3NldFVwX2ZpbGUzeA==";

		this.dl = DataList.getSampleDataList("setUp");
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testDataList() throws DataTransmittingException {

		this.dl = (DataList) DataTransmittingClass.fromString(this.content);
		try {
			if (!this.content.equals(this.dl.getClassAsString())) {
				fail("DataList out of String doesn't work");
			}
		} catch (CloudstoreException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testGetClassAsString() {

		String con = "";
		try {
			con = this.dl.getClassAsString();
		} catch (CloudstoreException e) {
			fail(e.getMessage());
		}

		if (!this.content.equals(con)) {
			fail("content as String doesn't work");
		}

	}

	@Test
	public void testGetContent() {

		List<DataListElement> list = this.dl.getContent();
		List<DataListElement> list2 = new LinkedList<DataListElement>();

		for (DataListElement dle : list) {

			list2.add(dle);

		}

		for (DataListElement dle : list2) {

			list.remove(dle);
		}

		if (list2.size() != this.dl.getContent().size()) {
			fail("Content-Liste ist von auﬂen manipulierbar");
		}

	}

}
