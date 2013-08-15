package de.uni_potsdam.hpi.cloudstore20.webfrontend.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	
	
	public String example(String name) throws IllegalArgumentException;
	
}
