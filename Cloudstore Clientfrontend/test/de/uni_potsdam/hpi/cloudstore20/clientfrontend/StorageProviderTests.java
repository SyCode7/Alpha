package de.uni_potsdam.hpi.cloudstore20.clientfrontend;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.storageProvider.ProviderImplementationsTests;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.storageProvider.StorageProviderConfigTest;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.storageProvider.StorageProviderTest;

@RunWith(Suite.class)
@SuiteClasses({ ProviderImplementationsTests.class, StorageProviderConfigTest.class, StorageProviderTest.class })
public class StorageProviderTests {

}
