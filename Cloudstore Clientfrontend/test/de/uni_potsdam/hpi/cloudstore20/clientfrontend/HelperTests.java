package de.uni_potsdam.hpi.cloudstore20.clientfrontend;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.helper.DatabaseRequestTest;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.helper.ReflectorTest;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.helper.ServletCommunicatorTest;

@RunWith(Suite.class)
@SuiteClasses({ DatabaseRequestTest.class, ReflectorTest.class, ServletCommunicatorTest.class })
public class HelperTests {

}
