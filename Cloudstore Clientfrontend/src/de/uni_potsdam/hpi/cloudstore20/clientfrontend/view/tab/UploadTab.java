package de.uni_potsdam.hpi.cloudstore20.clientfrontend.view.tab;

import java.io.File;
import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
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
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.view.TabElement;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.view.tab.helper.UploadProgressbarContainer;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.DATA_PROCESS_METHOD;

public class UploadTab extends TabElement {

	private List todoFileList;
	private List doneFileList;
	private ProgressBar progressbar;
	private LinkedList<UploadProgressbarContainer> uploadBars = new LinkedList<UploadProgressbarContainer>();
	private SashForm sashForm_progressBars;
	private SashForm sashForm_UploadProgressBars;
	private DATA_PROCESS_METHOD lastMethod = null;
	private String lastFile = "";
	private PaintListener paintListener;

	public UploadTab(TabFolder tabFolder, DefaultWindow window) {

		super(tabFolder, window);

	}

	@Override
	protected void createContent() {

		TabItem tbtmUpload = new TabItem(this.tabFolder, SWT.NONE);
		tbtmUpload.setText("Upload");

		SashForm sashForm_main = new SashForm(tabFolder, SWT.VERTICAL);
		tbtmUpload.setControl(sashForm_main);

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
					DataProcessor.getInstance().addNewTask(f, DefaultWindow.config);
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
						DataProcessor.getInstance().addNewTask(f_, DefaultWindow.config);
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

		SashForm sashForm_pictureProgessbar = new SashForm(sashForm_upload_dataAndMap, SWT.NONE);

		Canvas canvas1 = new Canvas(sashForm_pictureProgessbar, SWT.NONE);

		canvas1.addPaintListener(new PaintListener() {

			public void paintControl(PaintEvent e) {

				// Image image = new Image(display, "D:\\Workspaces\\cloudstore_2-0\\Cloudstore Clientfrontend\\pic\\file.jpg");
				Image image = new Image(DefaultWindow.display, new File("pic\\test.png").getAbsolutePath());

				e.gc.drawImage(image, 0, 0);

				image.dispose();
			}
		});

		sashForm_progressBars = new SashForm(sashForm_pictureProgessbar, SWT.NONE);

		SashForm sashForm_standardProgressBar = new SashForm(sashForm_progressBars, SWT.VERTICAL);

		new Composite(sashForm_standardProgressBar, SWT.NONE);
		progressbar = new ProgressBar(sashForm_standardProgressBar, SWT.SMOOTH);
		new Composite(sashForm_standardProgressBar, SWT.NONE);

		sashForm_standardProgressBar.setWeights(new int[] { 1, 1, 1 });
		sashForm_UploadProgressBars = new SashForm(sashForm_progressBars, SWT.VERTICAL);
		sashForm_progressBars.setWeights(new int[] { 1, 0 });

		Canvas canvas2 = new Canvas(sashForm_pictureProgessbar, SWT.NONE);

		canvas2.addPaintListener(new PaintListener() {

			public void paintControl(PaintEvent e) {

				// Image image = new Image(display, "D:\\Workspaces\\cloudstore_2-0\\Cloudstore Clientfrontend\\pic\\cloud.jpg");
				Image image = new Image(DefaultWindow.display, new File("pic\\test.png").getAbsolutePath());

				e.gc.drawImage(image, 0, 0);

				image.dispose();
			}
		});
		sashForm_pictureProgessbar.setWeights(new int[] { 1, 10, 1 });

		sashForm_upload_dataAndMap.setWeights(new int[] { 2, 2, 7, 2, 7, 6 });

		Browser browser = new Browser(sashForm_content, SWT.NONE);
		browser.setUrl("maps.google.com");
		sashForm_content.setWeights(new int[] { 1, 0 });
		sashForm_main.setWeights(new int[] { 50 });

	}

	@Override
	protected void performContentUpdate() throws DataProcessingException {

		String file = DataProcessor.getInstance().getCurrentFile();
		DATA_PROCESS_METHOD dpm = DataProcessor.getInstance().getCurrentMethod();
		if (dpm != null) {
			String description = dpm.getShortDescription();
			this.updateProgressbar(dpm, file + "\t" + description);
		}

		this.updateFileLists(file);

		this.lastMethod = dpm;
		this.lastFile = file;

	}

	private void updateFileLists(String file) {

		if (!this.lastFile.equals(file)) {

			this.todoFileList.removeAll();
			for (String s : DataProcessor.getInstance().getWorkList()) {
				this.todoFileList.add(s);
			}
			this.doneFileList.removeAll();
			for (String s : DataProcessor.getInstance().getDoneWork()) {
				this.doneFileList.add(s);
			}
		}
	}

	private void updateProgressbar(DATA_PROCESS_METHOD dpm, final String message) throws DataProcessingException {

		boolean upload = (dpm == DATA_PROCESS_METHOD.upload);

		if (upload) {

			if (this.lastMethod != DATA_PROCESS_METHOD.upload) {
				this.generateUploadProgressbars();
			}

			for (UploadProgressbarContainer upc : this.uploadBars) {
				upc.update();
			}

		} else {

			if (this.paintListener != null) {
				this.progressbar.removePaintListener(this.paintListener);
			}
			if (this.lastMethod != dpm) {
				this.paintListener = new PaintListener() {

					public void paintControl(PaintEvent e) {

						if (message == null || message.length() == 0)
							return;

						e.gc.setFont(e.display.getSystemFont());
						e.gc.setForeground(e.display.getSystemColor(SWT.COLOR_BLACK));

						Rectangle rec = progressbar.getBounds();
						e.gc.drawString(message, rec.x + 5, (rec.height - e.gc.getFontMetrics().getHeight()) / 2, true);
					}

				};
			}
			this.progressbar.setSelection(DataProcessor.getInstance().getCurrentStatus());
			this.progressbar.addPaintListener(this.paintListener);
		}

		this.setOneProgressbar(!upload);

	}

	private void generateUploadProgressbars() throws DataProcessingException {

		// Altes aufräumen
		for (UploadProgressbarContainer upc : this.uploadBars) {
			upc.remove();
		}
		this.uploadBars.clear();

		for (StorageProvider sp : DataProcessor.getInstance().getCurrentProvider()) {

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
