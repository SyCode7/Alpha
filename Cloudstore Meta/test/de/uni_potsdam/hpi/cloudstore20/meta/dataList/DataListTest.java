package de.uni_potsdam.hpi.cloudstore20.meta.dataList;

import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DataListTest {

	private DataList dl;
	private String content;

	@Before
	public void setUp() throws Exception {
		this.content = "file1#0#file2#0#folder={file1#1#folder={file1}}#0#file3";

		List<DataListElement> content1 = new LinkedList<DataListElement>();
		List<DataListElement> content2 = new LinkedList<DataListElement>();
		List<DataListElement> content3 = new LinkedList<DataListElement>();

		content3.add(new DataListElement("file1"));

		content2.add(new DataListElement("file1"));
		content2.add(new DataListElement("folder", this.getDataList(content3)));

		content1.add(new DataListElement("file1"));
		content1.add(new DataListElement("file2"));
		content1.add(new DataListElement("folder", this.getDataList(content2)));
		content1.add(new DataListElement("file3"));

		this.dl = this.getDataList(content1);
	}

	private DataList getDataList(List<DataListElement> content)
			throws NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {

		Constructor<?> constructor = DataList.class
				.getDeclaredConstructor(List.class);
		constructor.setAccessible(true);
		return (DataList) constructor.newInstance(content);

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDataList() {
		this.dl = new DataList(this.content);
		try {
			if (!this.content.equals(this.dl.getDataListAsString())) {
				fail("DataList out of String doesn't work");
			}
		} catch (DataListException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testGetDataListAsString() {

		String con = "";
		try {
			con = this.dl.getDataListAsString();
		} catch (DataListException e) {
			fail(e.getMessage());
		}

		if (this.content.equals(con)) {
			fail("content as String doesn't work");
		}

	}

}
