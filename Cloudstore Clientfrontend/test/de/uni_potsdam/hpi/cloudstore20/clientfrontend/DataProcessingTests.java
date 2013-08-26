package de.uni_potsdam.hpi.cloudstore20.clientfrontend;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.dataProcessing.DataProcessingTaskTest;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.dataProcessing.Elements.ErasureTest;

@RunWith(Suite.class)
@SuiteClasses({ ErasureTest.class, DataProcessingTaskTest.class })
public class DataProcessingTests {

}
