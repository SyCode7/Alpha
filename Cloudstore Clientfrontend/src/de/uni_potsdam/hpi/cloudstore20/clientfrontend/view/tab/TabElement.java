package de.uni_potsdam.hpi.cloudstore20.clientfrontend.view.tab;

import org.eclipse.swt.widgets.TabFolder;

public abstract class TabElement {

	protected TabFolder tabFolder;

	public TabElement(TabFolder tabFolder) {

		this.tabFolder = tabFolder;

		this.createContent();
	}

	protected abstract void createContent();

}
