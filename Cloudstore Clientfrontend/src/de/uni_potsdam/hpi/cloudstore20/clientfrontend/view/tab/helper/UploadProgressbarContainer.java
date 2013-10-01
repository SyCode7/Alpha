package de.uni_potsdam.hpi.cloudstore20.clientfrontend.view.tab.helper;

import org.eclipse.swt.widgets.ProgressBar;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.StorageProvider;

public class UploadProgressbarContainer {

	private StorageProvider provider;
	private ProgressBar bar;

	public UploadProgressbarContainer(ProgressBar progressbar, StorageProvider provider) {

		this.bar = progressbar;
		this.provider = provider;

		this.bar.setToolTipText(this.provider.getCompleteProviderName());

	}

	public void update() {

		this.bar.setSelection(this.provider.getProcessStatus());

	}

	public String getProviderName() {

		return this.provider.getCompleteProviderName();

	}

	public void remove() {

		this.provider = null;
		this.bar.dispose();
		this.bar = null;
	}

}
