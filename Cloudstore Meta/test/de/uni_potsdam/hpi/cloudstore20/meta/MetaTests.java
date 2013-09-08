package de.uni_potsdam.hpi.cloudstore20.meta;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.CloudstoreConfigTest;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.dataList.DataListElementTest;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.dataList.DataListTest;
import de.uni_potsdam.hpi.cloudstore20.meta.helper.FileHelperTest;

@RunWith(Suite.class)
@SuiteClasses({ DataListTest.class, DataListElementTest.class, CloudstoreConfigTest.class, FileHelperTest.class })
public class MetaTests {

}
