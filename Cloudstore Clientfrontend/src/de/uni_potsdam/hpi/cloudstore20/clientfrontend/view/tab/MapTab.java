package de.uni_potsdam.hpi.cloudstore20.clientfrontend.view.tab;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.DataProcessor;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.view.DefaultWindow;

public class MapTab extends TabElement {

	public MapTab(TabFolder tabFolder, DefaultWindow window) {

		super(tabFolder, window);
	}

	@Override
	protected void createContent() {

		TabItem tbtmKarte = new TabItem(tabFolder, SWT.NONE);
		tbtmKarte.setText("Karte");

		Browser browser = new Browser(tabFolder, SWT.NONE);
		browser.setUrl("maps.google.com");
		tbtmKarte.setControl(browser);

	}

	@Override
	public void updateContent(DataProcessor content) {

		// TODO Auto-generated method stub

	}

	@Override
	protected void performContentUpdate() {

		// TODO Auto-generated method stub
		
	}

}
