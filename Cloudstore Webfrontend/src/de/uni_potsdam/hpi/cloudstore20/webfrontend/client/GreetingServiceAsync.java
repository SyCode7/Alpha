package de.uni_potsdam.hpi.cloudstore20.webfrontend.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {

	public void example(String input, AsyncCallback<String> callback)
			throws IllegalArgumentException;

}
