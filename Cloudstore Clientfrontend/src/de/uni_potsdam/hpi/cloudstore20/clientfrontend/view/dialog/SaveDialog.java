package de.uni_potsdam.hpi.cloudstore20.clientfrontend.view.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

public class SaveDialog implements SelectionListener {

	private Shell s;

	public SaveDialog(Shell s) {

		this.s = s;
	}

	@Override
	public void widgetSelected(SelectionEvent event) {

		FileDialog fd = new FileDialog(s, SWT.SAVE);
		fd.setText("Save");
		fd.setFilterPath("C:/");
		String[] filterExt = { "*.txt", "*.doc", ".rtf", "*.*" };
		fd.setFilterExtensions(filterExt);
		String selected = fd.open();
		System.out.println("Datei speichern: " + selected);
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {

		// TODO Auto-generated method stub

	}

}
