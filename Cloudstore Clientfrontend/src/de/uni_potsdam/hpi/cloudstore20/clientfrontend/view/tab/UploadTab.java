package de.uni_potsdam.hpi.cloudstore20.clientfrontend.view.tab;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public class UploadTab extends TabElement {

	public UploadTab(TabFolder tabFolder) {
		super(tabFolder);
	}

	@Override
	protected void createContent() {

		TabItem tbtmUpload = new TabItem(this.tabFolder, SWT.NONE);
		tbtmUpload.setText("Upload");

	}

}
