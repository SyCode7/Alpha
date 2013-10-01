package de.uni_potsdam.hpi.cloudstore20.clientfrontend.view;

import org.eclipse.swt.widgets.TabFolder;

import de.uni_potsdam.hpi.cloudstore20.meta.CloudstoreException;

public abstract class TabElement {

	protected TabFolder tabFolder;
	protected DefaultWindow window;

	public TabElement(TabFolder tabFolder, DefaultWindow window) {

		this.tabFolder = tabFolder;
		this.window = window;

		this.createContent();
	}

	protected abstract void createContent();

	public void updateContent() throws CloudstoreException {

		this.performContentUpdate();
	}

	protected abstract void performContentUpdate() throws CloudstoreException;

}
