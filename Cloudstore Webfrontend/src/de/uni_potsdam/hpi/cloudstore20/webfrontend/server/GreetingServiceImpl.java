package de.uni_potsdam.hpi.cloudstore20.webfrontend.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.uni_potsdam.hpi.cloudstore20.webfrontend.client.GreetingService;

/**
 * The server side implementation of the RPC service.
 */
public class GreetingServiceImpl extends RemoteServiceServlet implements
		GreetingService {

	private static final long serialVersionUID = 2884075971941175119L;

	@Override
	public String example(String name) throws IllegalArgumentException {

		return null;
	}

}
