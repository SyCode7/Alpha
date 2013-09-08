package de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.dataList;

import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uni_potsdam.hpi.cloudstore20.meta.CloudstoreException;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.DataTransmittingException;

public class DataListTest {

	private DataList dl;
	private String content;

	@Before
	public void setUp() throws Exception {

		this.content = "file1#0#file2#0#folder={file1#1#folder={file1}}#0#file3";

		this.dl = DataListTest.getSampleDataList("setUp");
	}

	public static DataList getSampleDataList(String prefix) throws DataListException {

		List<DataListElement> content1 = new LinkedList<DataListElement>();
		List<DataListElement> content2 = new LinkedList<DataListElement>();
		List<DataListElement> content3 = new LinkedList<DataListElement>();

		content3.add(new DataListElement(prefix + "_file1"));

		content2.add(new DataListElement(prefix + "_file1"));
		content2.add(new DataListElement(prefix + "_folder", DataListTest.getDataList(content3)));

		content1.add(new DataListElement(prefix + "_file1"));
		content1.add(new DataListElement(prefix + "_file2"));
		content1.add(new DataListElement(prefix + "_folder", DataListTest.getDataList(content2)));
		content1.add(new DataListElement(prefix + "_file3"));

		return DataListTest.getDataList(content1);

	}

	private static DataList getDataList(List<DataListElement> content) throws DataListException {

		Constructor<?> constructor;
		try {
			constructor = DataList.class.getDeclaredConstructor(List.class);
			constructor.setAccessible(true);
			return (DataList) constructor.newInstance(content);
		} catch (Exception e) {
			throw new DataListException(e.getMessage(), e.getCause());
		}

	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testDataList() throws DataTransmittingException {

		this.dl = new DataList(this.content);
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

		if (this.content.equals(con)) {
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
