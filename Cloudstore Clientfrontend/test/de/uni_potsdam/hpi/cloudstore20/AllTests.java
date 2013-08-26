package de.uni_potsdam.hpi.cloudstore20;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.DataProcessingTests;
import de.uni_potsdam.hpi.cloudstore20.meta.MetaTests;

@RunWith(Suite.class)
@SuiteClasses({ MetaTests.class, DataProcessingTests.class })
public class AllTests {

}
