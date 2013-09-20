package de.uni_potsdam.hpi.cloudstore20.clientfrontend.view.tab;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.DataProcessor;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.view.DefaultWindow;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.CloudstoreConfig;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.DATA_PROCESS_METHOD;

public class UploadTab extends TabElement {

	protected File selectedFile = null;
	protected List fileList;
	protected ProgressBar progressbar;
	private Label lblStatus;
	private DataProcessor currentContentInProgress;

	/**
	 * @wbp.parser.entryPoint
	 */
	public UploadTab(TabFolder tabFolder, DefaultWindow window) {

		super(tabFolder, window);

		DataProcessor.getInstance().addToNoticeList(this);
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	protected void createContent() {

		TabItem tbtmUpload = new TabItem(this.tabFolder, SWT.NONE);
		tbtmUpload.setText("Upload");

		SashForm sashForm_1 = new SashForm(tabFolder, SWT.VERTICAL);
		tbtmUpload.setControl(sashForm_1);

		new Composite(sashForm_1, SWT.NONE);

		SashForm sashForm_2 = new SashForm(sashForm_1, SWT.NONE);

		SashForm sashForm_3 = new SashForm(sashForm_2, SWT.VERTICAL);

		SashForm sashForm = new SashForm(sashForm_3, SWT.NONE);

		Button btnNewButton = new Button(sashForm, SWT.NONE);
		btnNewButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseUp(MouseEvent e) {

				FileDialog dialog = new FileDialog(window.shell);
				String path = dialog.open();
				if (path == null) {
					path = "";
				}
				File f = new File(path);

				if (!f.exists()) {
					return;
				}

				if (f.isDirectory()) {
					// TODO: implementieren

				} else if (f.isFile()) {
					selectedFile = f;
				}

				// TODO: Config auslesen
				DataProcessor.getInstance().addNewTask(selectedFile, CloudstoreConfig.loadDefault());

			}
		});
		btnNewButton.setText("Datei ausw\u00E4hlen");

		new Composite(sashForm, SWT.NONE);
		sashForm.setWeights(new int[] { 1, 4 });

		Label lblNachHochzuladendeDateien = new Label(sashForm_3, SWT.NONE);
		lblNachHochzuladendeDateien.setText("Noch hochzuladende Dateien:");

		fileList = new List(sashForm_3, SWT.BORDER | SWT.H_SCROLL);

		progressbar = new ProgressBar(sashForm_3, SWT.SMOOTH);

		lblStatus = new Label(sashForm_3, SWT.NONE);
		lblStatus.setText("");
		sashForm_3.setWeights(new int[] { 30, 20, 200, 30, 20 });

		Browser browser = new Browser(sashForm_2, SWT.NONE);
		browser.setUrl("maps.google.com");
		sashForm_2.setWeights(new int[] { 1, 0 });

		new Composite(sashForm_1, SWT.NONE);
		sashForm_1.setWeights(new int[] { 1, 20, 1 });

	}

	@Override
	public void updateContent(DataProcessor content) {

		this.currentContentInProgress = content;
		DefaultWindow.updateContent();

	}

	@Override
	protected void performContentUpdate() {
		
		String file = this.currentContentInProgress.getCurrentFile();
		DATA_PROCESS_METHOD dpm = this.currentContentInProgress.getCurrentMethod();
		if (dpm != null) {
			String description = dpm.getShortDescription();
			this.lblStatus.setText(file + "\t" + description);
		}
		this.progressbar.setSelection(this.currentContentInProgress.getCurrentStatus());

		this.fileList.removeAll();
		for (String s : this.currentContentInProgress.getWorkList()) {
			this.fileList.add(s);
		}
		this.fileList.add("");
		this.fileList.add(file);
		this.fileList.add("");
		for (String s : this.currentContentInProgress.getDoneWork()) {
			this.fileList.add(s);
		}

	}

}
