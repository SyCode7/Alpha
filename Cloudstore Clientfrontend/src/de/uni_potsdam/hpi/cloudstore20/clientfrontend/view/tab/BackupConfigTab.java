package de.uni_potsdam.hpi.cloudstore20.clientfrontend.view.tab;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.view.DefaultWindow;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.view.TabElement;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.enums.FILE_POST_HANDLING;

public class BackupConfigTab extends TabElement {

	private SashForm sashForm_2;
	private Button btnCheckButton;
	private Map<FILE_POST_HANDLING, Button> buttons;

	public BackupConfigTab(TabFolder tabFolder, DefaultWindow window) {

		super(tabFolder, window);

	}

	@Override
	protected void createContent() {

		TabItem tbtmOuBE = new TabItem(this.tabFolder, SWT.NONE);
		tbtmOuBE.setText("Ordner und Backup Einstellungen");

		SashForm sashForm_1 = new SashForm(this.tabFolder, SWT.VERTICAL);
		tbtmOuBE.setControl(sashForm_1);

		new Composite(sashForm_1, SWT.NONE);

		Label lblD = new Label(sashForm_1, SWT.NONE);
		lblD.setText("Datei nach dem Upload ...");

		if (buttons == null) {
			buttons = new HashMap<FILE_POST_HANDLING, Button>();
		}

		FILE_POST_HANDLING selected = DefaultWindow.config.getFilePostHandling();
		for (FILE_POST_HANDLING fph : FILE_POST_HANDLING.values()) {
			Button temp = new Button(sashForm_1, SWT.RADIO);
			temp.setText(fph.getDisplayString());
			temp.setSelection(fph == selected);
			buttons.put(fph, temp);
			temp.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetSelected(SelectionEvent e) {

					Button selected = ((Button) e.getSource());
					if (selected.getSelection()) {
						for (FILE_POST_HANDLING fph : buttons.keySet()) {
							if (buttons.get(fph) == selected) {
								DefaultWindow.config.setFilePostHandling(fph);
							}
						}
					}

				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {

					Button selected = ((Button) e.getSource());
					if (selected.getSelection()) {
						for (FILE_POST_HANDLING fph : buttons.keySet()) {
							if (buttons.get(fph) == selected) {
								DefaultWindow.config.setFilePostHandling(fph);
							}
						}
					}

				}
			});
		}

		new Composite(sashForm_1, SWT.NONE);

		btnCheckButton = new Button(sashForm_1, SWT.CHECK);
		btnCheckButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseUp(MouseEvent e) {

				sashForm_2.setVisible(btnCheckButton.getSelection());
			}
		});
		btnCheckButton.setText("Dateien automatisch aus Ordner hochladen");

		sashForm_2 = new SashForm(sashForm_1, SWT.HORIZONTAL);

		Label lblNewLabel = new Label(sashForm_2, SWT.NONE);
		lblNewLabel.setText("Ordner: ");

		final Text selectedFolder = new Text(sashForm_2, SWT.BORDER);

		Button btnDurchsuchen = new Button(sashForm_2, SWT.NONE);
		btnDurchsuchen.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseUp(MouseEvent e) {

				DirectoryDialog dialog = new DirectoryDialog(window.shell);
				String folderPath = dialog.open();
				if (folderPath == null) {
					folderPath = "";
				}
				selectedFolder.setText(folderPath);
			}
		});
		btnDurchsuchen.setText("Durchsuchen");

		new Composite(sashForm_2, SWT.NONE);
		sashForm_2.setWeights(new int[] { 95, 325, 157, 49 });

		new Composite(sashForm_1, SWT.NONE);
		sashForm_1.setWeights(new int[] { 36, 16, 16, 16, 16, 32, 16, 24, 159 });

	}

	@Override
	protected void performContentUpdate() {

		// TODO Auto-generated method stub

	}

}
