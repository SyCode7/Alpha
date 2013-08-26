package de.uni_potsdam.hpi.cloudstore20.clientfrontend.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

public class OpenDialog implements SelectionListener {

	private Shell s;

	public OpenDialog(Shell s) {

		this.s = s;
	}

	@Override
	public void widgetSelected(SelectionEvent event) {

		FileDialog fd = new FileDialog(s, SWT.OPEN);
		fd.setText("Open");
		fd.setFilterPath("C:/");
		String[] filterExt = { "*.txt", "*.doc", ".rtf", "*.*" };
		fd.setFilterExtensions(filterExt);
		String selected = fd.open();
		System.out.println("Datei ausgewählt: " + selected);
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {

		// TODO Auto-generated method stub

	}

}
