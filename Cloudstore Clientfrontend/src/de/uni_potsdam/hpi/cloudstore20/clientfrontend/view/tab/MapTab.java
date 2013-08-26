package de.uni_potsdam.hpi.cloudstore20.clientfrontend.view.tab;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public class MapTab extends TabElement {

	public MapTab(TabFolder tabFolder) {
		super(tabFolder);
	}

	@Override
	protected void createContent() {
		TabItem tbtmKarte = new TabItem(tabFolder, SWT.NONE);
		tbtmKarte.setText("Karte");

		Browser browser = new Browser(tabFolder, SWT.NONE);
		browser.setUrl("maps.google.com");
		tbtmKarte.setControl(browser);

	}

}
