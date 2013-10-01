package de.uni_potsdam.hpi.cloudstore20.clientfrontend.view.tab;

import java.io.File;
import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.DataProcessingException;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.DataProcessor;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.StorageProvider;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.view.DefaultWindow;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.view.tab.helper.UploadProgressbarContainer;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.CloudstoreConfig;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.DATA_PROCESS_METHOD;

public class UploadTab extends TabElement {

	private List todoFileList;
	private List doneFileList;
	private ProgressBar progressbar;
	private Label lblStatus;
	private DataProcessor currentContentInProgress;
	private LinkedList<UploadProgressbarContainer> uploadBars = new LinkedList<UploadProgressbarContainer>();
	private SashForm sashForm_progressBars;
	private SashForm sashForm_UploadProgressBars;
	private DATA_PROCESS_METHOD lastMethod = null;
	private String lastFile = "";

	public UploadTab(TabFolder tabFolder, DefaultWindow window) {

		super(tabFolder, window);

		DataProcessor.getInstance().addToNoticeList(this);
	}

	@Override
	protected void createContent() {

		TabItem tbtmUpload = new TabItem(this.tabFolder, SWT.NONE);
		tbtmUpload.setText("Upload");

		SashForm sashForm_main = new SashForm(tabFolder, SWT.VERTICAL);
		tbtmUpload.setControl(sashForm_main);

		new Composite(sashForm_main, SWT.NONE);

		SashForm sashForm_content = new SashForm(sashForm_main, SWT.NONE);

		SashForm sashForm_upload_dataAndMap = new SashForm(sashForm_content, SWT.VERTICAL);

		SashForm sashForm_Buttons4UploadSelection = new SashForm(sashForm_upload_dataAndMap, SWT.NONE);

		Button btnNewButton = new Button(sashForm_Buttons4UploadSelection, SWT.NONE);
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
				if (f.isFile()) {
					// TODO: Config auslesen
					DataProcessor.getInstance().addNewTask(f, CloudstoreConfig.loadDefault());
				}

			}
		});
		btnNewButton.setText("Datei ausw\u00E4hlen");

		btnNewButton = new Button(sashForm_Buttons4UploadSelection, SWT.NONE);
		btnNewButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseUp(MouseEvent e) {

				DirectoryDialog dialog = new DirectoryDialog(window.shell);
				String path = dialog.open();
				if (path == null) {
					path = "";
				}
				File f = new File(path);

				if (!f.exists()) {
					return;
				}

				if (f.isDirectory()) {
					// TODO: Ordnerstruktur sollte in Dateiliste erkennbarsei
					for (File f_ : f.listFiles()) {
						// TODO: Config auslesen
						DataProcessor.getInstance().addNewTask(f_, CloudstoreConfig.loadDefault());
					}
				}
			}
		});
		btnNewButton.setText("Ordner ausw\u00E4hlen");

		new Composite(sashForm_Buttons4UploadSelection, SWT.NONE);
		sashForm_Buttons4UploadSelection.setWeights(new int[] { 1, 1, 3 });

		Label todo = new Label(sashForm_upload_dataAndMap, SWT.NONE);
		todo.setText("Noch abzuarbeitende Dateien:");
		todoFileList = new List(sashForm_upload_dataAndMap, SWT.BORDER | SWT.V_SCROLL);
		Label done = new Label(sashForm_upload_dataAndMap, SWT.NONE);
		done.setText("Abgearbeitete Dateien:");
		doneFileList = new List(sashForm_upload_dataAndMap, SWT.BORDER | SWT.V_SCROLL);

		sashForm_progressBars = new SashForm(sashForm_upload_dataAndMap, SWT.NONE);
		progressbar = new ProgressBar(sashForm_progressBars, SWT.SMOOTH);
		sashForm_UploadProgressBars = new SashForm(sashForm_progressBars, SWT.NONE);
		sashForm_progressBars.setWeights(new int[] { 1, 0 });

		lblStatus = new Label(sashForm_upload_dataAndMap, SWT.NONE);
		lblStatus.setText("");

		sashForm_upload_dataAndMap.setWeights(new int[] { 3, 2, 9, 2, 9, 4, 2 });

		Browser browser = new Browser(sashForm_content, SWT.NONE);
		browser.setUrl("maps.google.com");
		sashForm_content.setWeights(new int[] { 1, 0 });

		new Composite(sashForm_main, SWT.NONE);
		sashForm_main.setWeights(new int[] { 1, 50, 1 });

	}

	@Override
	public void updateContent(DataProcessor content) {

		this.currentContentInProgress = content;
		DefaultWindow.updateContent();

	}

	@Override
	protected void performContentUpdate() throws DataProcessingException {

		String file = this.currentContentInProgress.getCurrentFile();
		DATA_PROCESS_METHOD dpm = this.currentContentInProgress.getCurrentMethod();
		if (dpm != null) {
			String description = dpm.getShortDescription();
			this.lblStatus.setText(file + "\t" + description);
		}
		this.updateProgressbar(dpm);

		this.updateFileLists(file);

		this.lastMethod = dpm;
		this.lastFile = file;

	}

	private void updateFileLists(String file) {

		if (!this.lastFile.equals(file)) {

			this.todoFileList.removeAll();
			for (String s : this.currentContentInProgress.getWorkList()) {
				this.todoFileList.add(s);
			}
			this.doneFileList.removeAll();
			for (String s : this.currentContentInProgress.getDoneWork()) {
				this.doneFileList.add(s);
			}
		}
	}

	private void updateProgressbar(DATA_PROCESS_METHOD dpm) throws DataProcessingException {

		boolean upload = (dpm == DATA_PROCESS_METHOD.upload);

		if (upload) {

			if (this.lastMethod != DATA_PROCESS_METHOD.upload) {
				this.generateUploadProgressbars();
			}

			for (UploadProgressbarContainer upc : this.uploadBars) {
				upc.update();
			}

		} else {

			this.progressbar.setSelection(this.currentContentInProgress.getCurrentStatus());
		}

		this.setOneProgressbar(!upload);

	}

	private void generateUploadProgressbars() throws DataProcessingException {

		// Altes aufräumen
		for (UploadProgressbarContainer upc : this.uploadBars) {
			upc.remove();
		}
		this.uploadBars.clear();

		for (StorageProvider sp : this.currentContentInProgress.getCurrentProvider()) {

			ProgressBar bar = new ProgressBar(sashForm_UploadProgressBars, SWT.SMOOTH);

			this.uploadBars.add(new UploadProgressbarContainer(bar, sp));

		}

		int[] weights = new int[this.uploadBars.size()];
		for (int i = 0; i < weights.length; i++) {
			weights[i] = 1;
		}
		sashForm_UploadProgressBars.setWeights(weights);

	}

	private void setOneProgressbar(boolean onlyOne) {

		if (onlyOne) {
			sashForm_progressBars.setWeights(new int[] { 1, 0 });
		} else {
			sashForm_progressBars.setWeights(new int[] { 0, 1 });
		}

	}

}
