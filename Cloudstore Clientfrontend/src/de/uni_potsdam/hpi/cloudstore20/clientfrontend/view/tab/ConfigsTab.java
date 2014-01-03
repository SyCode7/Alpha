package de.uni_potsdam.hpi.cloudstore20.clientfrontend.view.tab;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.view.DefaultWindow;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.view.TabElement;
import de.uni_potsdam.hpi.cloudstore20.meta.CloudstoreException;

public class ConfigsTab extends TabElement {

	private List<TabElement> includingTabs;

	public ConfigsTab(TabFolder tabFolder, DefaultWindow window) {

		super(tabFolder, window);
	}

	@Override
	protected void createContent() {

		this.includingTabs = new LinkedList<TabElement>();

		TabItem tbtmConfigTab = new TabItem(this.tabFolder, SWT.NONE);
		tbtmConfigTab.setText("Konfigurationen");

		TabFolder tabFolder_AllConfigs = new TabFolder(tabFolder, SWT.NONE);
		tbtmConfigTab.setControl(tabFolder_AllConfigs);

		this.includingTabs.add(new ProviderConfigTab(tabFolder_AllConfigs, this.window));

		this.includingTabs.add(new BackupConfigTab(tabFolder_AllConfigs, this.window));

		this.includingTabs.add(new UploadConfigTab(tabFolder_AllConfigs, this.window));
	}

	@Override
	protected void performContentUpdate() throws CloudstoreException {

		for (TabElement te : this.includingTabs) {
			te.updateContent();
		}

	}

}
