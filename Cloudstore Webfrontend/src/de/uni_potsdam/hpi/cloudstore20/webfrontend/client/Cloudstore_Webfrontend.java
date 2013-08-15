package de.uni_potsdam.hpi.cloudstore20.webfrontend.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Cloudstore_Webfrontend implements EntryPoint {

	private final GreetingServiceAsync service = GWT
			.create(GreetingService.class);

	public void onModuleLoad() {

		Panel root = RootPanel.get();

		Button btnTest = new Button("Test");
		btnTest.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				service.example("", new AsyncCallback<String>() {

					@Override
					public void onSuccess(String result) {

						Window.alert("Erfolgreich");

					}

					@Override
					public void onFailure(Throwable caught) {
						
						Window.alert("Es ist ein Fehler aufgetreten.");

					}
				});

			}
		});
		root.add(btnTest);

	}
}
