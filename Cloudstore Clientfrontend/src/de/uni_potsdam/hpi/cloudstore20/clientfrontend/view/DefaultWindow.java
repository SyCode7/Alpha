package de.uni_potsdam.hpi.cloudstore20.clientfrontend.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.Scale;

public class DefaultWindow {

	protected Shell shell;
	private TabFolder tabFolder_1;
	private SashForm sashForm_2;
	private Button btnCheckButton;
	private Text username;
	private Text password;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			DefaultWindow window = new DefaultWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {

		Display display = Display.getDefault();
		createContents(display);
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents(Display display) {

		/*
		 * Hier können Systemleisten u.a. ausgeschaltet werden
		 */
		shell = new Shell(display, SWT.NO_TRIM | SWT.ON_TOP);

		shell.setSize(643, 447);
		shell.setText("SWT Application");
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));

		SashForm sashForm = new SashForm(shell, SWT.VERTICAL);

		Label lblHeaderText = new Label(sashForm, SWT.NONE);
		lblHeaderText.setFont(SWTResourceManager.getFont("Segoe UI", 13, SWT.BOLD));
		lblHeaderText.setText("CloudRaid - Futuristic Online Storage for now");

		tabFolder_1 = new TabFolder(sashForm, SWT.NONE);

		this.makeDateilistenTab(tabFolder_1);

		this.makeKartenTab(tabFolder_1);

		this.makeProviderkonfiguration();

		this.makeOrdnerUndBackupEinstellungTab();

		TabItem tbtmUpload = new TabItem(tabFolder_1, SWT.NONE);
		tbtmUpload.setText("Upload");

		TabItem tbtmUploadkonf = new TabItem(tabFolder_1, SWT.NONE);
		tbtmUploadkonf.setText("Uploadkonfiguration");

		SashForm sashForm_5 = new SashForm(tabFolder_1, SWT.NONE);
		tbtmUploadkonf.setControl(sashForm_5);

		SashForm sashForm_6 = new SashForm(sashForm_5, SWT.VERTICAL);

		Label lblNewLabel_3 = new Label(sashForm_6, SWT.NONE);
		lblNewLabel_3.setText("Erasure [k:m]");

		SashForm sashForm_1 = new SashForm(sashForm_6, SWT.NONE);

		Button uploadopti = new Button(sashForm_6, SWT.CHECK);
		uploadopti.setText("Uploadoptimierung");
		Button uploadspli = new Button(sashForm_6, SWT.CHECK);
		uploadspli.setText("Uploadsplitting");
		Button dataEncr = new Button(sashForm_6, SWT.CHECK);
		dataEncr.setText("Daten verschl\u00FCsseln");

		sashForm_6.setWeights(new int[] { 1, 1, 1, 1, 1 });

		SashForm sashForm_7 = new SashForm(sashForm_5, SWT.VERTICAL);

		Label lblAusf = new Label(sashForm_7, SWT.NONE);
		lblAusf.setText("Ausfallsicherheit");
		Scale scaleAusf = new Scale(sashForm_7, SWT.NONE);

		Label lblKosten = new Label(sashForm_7, SWT.NONE);
		lblAusf.setText("Kosten");
		Scale scaleKosten = new Scale(sashForm_7, SWT.NONE);

		Label lblGeschw = new Label(sashForm_7, SWT.NONE);
		lblAusf.setText("Geschwindigkeit");
		Scale scaleGeschw = new Scale(sashForm_7, SWT.NONE);

		sashForm_7.setWeights(new int[] { 1, 1, 1, 1, 1, 1 });

		Composite composite_2 = new Composite(sashForm_5, SWT.NONE);
		sashForm_5.setWeights(new int[] { 1, 1, 1 });

		Button btnBeenden = new Button(sashForm, SWT.NONE);
		btnBeenden.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseUp(MouseEvent e) {

				System.exit(0);
			}
		});
		btnBeenden.setText("Beenden");

		sashForm.setWeights(new int[] { 2, 20, 1 });

	}

	private void makeProviderkonfiguration() {

		TabItem tbtmProviderKonf = new TabItem(tabFolder_1, SWT.NONE);
		tbtmProviderKonf.setText("Providerkonfiguration");

		SashForm sashForm_1 = new SashForm(tabFolder_1, SWT.VERTICAL);
		tbtmProviderKonf.setControl(sashForm_1);

		Composite composite = new Composite(sashForm_1, SWT.NONE);

		SashForm sashForm_3 = new SashForm(sashForm_1, SWT.NONE);

		SashForm sashForm_4 = new SashForm(sashForm_3, SWT.VERTICAL);

		Label lblNewLabel_1 = new Label(sashForm_4, SWT.NONE);
		lblNewLabel_1.setText("Providerkonfiguration");

		Label lblNewLabel_2 = new Label(sashForm_4, SWT.NONE);
		lblNewLabel_2.setText("Provider ausw\u00E4hlen");

		Combo combo = new Combo(sashForm_4, SWT.NONE);

		Label lblNutzername = new Label(sashForm_4, SWT.NONE);
		lblNutzername.setText("Nutzername");

		username = new Text(sashForm_4, SWT.BORDER);

		Label lblPasswordOder = new Label(sashForm_4, SWT.NONE);
		lblPasswordOder.setText("Passwort oder Schl\u00FCssel");

		password = new Text(sashForm_4, SWT.BORDER);

		Button btnProvider = new Button(sashForm_4, SWT.NONE);
		btnProvider.setText("Provider updaten");
		sashForm_4.setWeights(new int[] { 1, 1, 1, 1, 1, 1, 1, 1 });

		Composite composite_1 = new Composite(sashForm_3, SWT.NONE);
		sashForm_3.setWeights(new int[] { 1, 1 });

		Composite composite_3 = new Composite(sashForm_1, SWT.NONE);
		sashForm_1.setWeights(new int[] { 16, 302, 31 });
	}

	private void makeOrdnerUndBackupEinstellungTab() {

		TabItem tbtmOuBE = new TabItem(tabFolder_1, SWT.NONE);
		tbtmOuBE.setText("Ordner und Backup Einstellungen");

		SashForm sashForm_1 = new SashForm(tabFolder_1, SWT.VERTICAL);
		tbtmOuBE.setControl(sashForm_1);

		new Composite(sashForm_1, SWT.NONE);

		Label lblD = new Label(sashForm_1, SWT.NONE);
		lblD.setText("Datei nach dem Upload ...");

		Button btnLassen = new Button(sashForm_1, SWT.RADIO);
		btnLassen.setText("auf dem Rechner lassen.");
		Button btnRudiment = new Button(sashForm_1, SWT.RADIO);
		btnRudiment.setText("durch Rudiment ersetzen.");
		Button btnLoeschen = new Button(sashForm_1, SWT.RADIO);
		btnLoeschen.setText("löschen.");

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

				DirectoryDialog dialog = new DirectoryDialog(shell);
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

	private void makeKartenTab(TabFolder tabFolder) {

		TabItem tbtmKarte = new TabItem(tabFolder, SWT.NONE);
		tbtmKarte.setText("Karte");

		Browser browser = new Browser(tabFolder, SWT.NONE);
		browser.setUrl("maps.google.com");
		tbtmKarte.setControl(browser);
	}

	private void makeDateilistenTab(TabFolder tabFolder) {

		TabItem tbtmDateiliste = new TabItem(tabFolder, SWT.NONE);
		tbtmDateiliste.setText("Dateiliste");

		SashForm sashForm = new SashForm(tabFolder_1, SWT.VERTICAL);
		tbtmDateiliste.setControl(sashForm);

		Tree tree = new Tree(sashForm, SWT.BORDER);
		this.buildFileTree(tree);

		SashForm sashForm_1 = new SashForm(sashForm, SWT.NONE);

		Button btnNewButton_1 = new Button(sashForm_1, SWT.NONE);
		btnNewButton_1.setText("Eintrag öffnen");

		new Composite(sashForm_1, SWT.NONE);
		sashForm_1.setWeights(new int[] { 1, 5 });
		sashForm.setWeights(new int[] { 325, 27 });

	}

	private void buildFileTree(final Tree tree) {

		for (int i = 0; i < 4; i++) {
			TreeItem item0 = new TreeItem(tree, 0);
			item0.setText("Item " + i);
			for (int j = 0; j < 4; j++) {
				TreeItem item1 = new TreeItem(item0, 0);
				item1.setText("SubItem " + i + " " + j);
				for (int k = 0; k < 4; k++) {
					TreeItem item2 = new TreeItem(item1, 0);
					item2.setText("SubItem " + i + " " + j + " " + k);
				}
			}
		}
		tree.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event e) {

				String string = "";
				TreeItem[] selection = tree.getSelection();
				for (int i = 0; i < selection.length; i++)
					string += selection[i] + " ";
				System.out.println("Selection={" + string + "}");
			}
		});
		tree.addListener(SWT.DefaultSelection, new Listener() {

			public void handleEvent(Event e) {

				String string = "";
				TreeItem[] selection = tree.getSelection();
				for (int i = 0; i < selection.length; i++)
					string += selection[i] + " ";
				System.out.println("DefaultSelection={" + string + "}");
			}
		});
		tree.addListener(SWT.Expand, new Listener() {

			public void handleEvent(Event e) {

				System.out.println("Expand={" + e.item + "}");
			}
		});
		tree.addListener(SWT.Collapse, new Listener() {

			public void handleEvent(Event e) {

				System.out.println("Collapse={" + e.item + "}");
			}
		});
		tree.getItems()[0].setExpanded(true);
	}

}
