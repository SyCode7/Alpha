package de.uni_potsdam.hpi.cloudstore20.clientfrontend;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.DataProcessingTaskTest;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.DataProcessorTest;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.ElementsTest;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.ProviderFileContainerTest;

@RunWith(Suite.class)
@SuiteClasses({ ElementsTest.class, DataProcessingTaskTest.class, DataProcessorTest.class, ProviderFileContainerTest.class })
public class DataProcessingTests {

}
