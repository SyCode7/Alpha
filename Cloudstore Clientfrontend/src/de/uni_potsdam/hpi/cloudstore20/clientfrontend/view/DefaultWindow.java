package de.uni_potsdam.hpi.cloudstore20.clientfrontend.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
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

public class DefaultWindow {

	protected Shell shell;
	private Text txtTopLeft;
	private Text txtTopCenter;
	private Text txtTopRight;
	private TabFolder tabFolder_1;

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
		lblHeaderText.setFont(SWTResourceManager.getFont("Segoe UI", 13,
				SWT.BOLD));
		lblHeaderText.setText("CloudRaid - Futuristic Online Storage for now");

		tabFolder_1 = new TabFolder(sashForm, SWT.NONE);

		makeStartseitenTab(tabFolder_1);

		makeDateilistenTab(tabFolder_1);

		makeKartenTab(tabFolder_1);

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

		final Tree tree = new Tree(tabFolder_1, SWT.BORDER);
		tbtmDateiliste.setControl(tree);
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

	private void makeStartseitenTab(TabFolder tabFolder) {

		TabItem tbtmStartseite = new TabItem(tabFolder, SWT.NONE);
		tbtmStartseite.setText("Startseite");

		ViewForm viewForm = new ViewForm(tabFolder, SWT.NONE);
		tbtmStartseite.setControl(viewForm);

		Button btnTest = new Button(viewForm, SWT.NONE);
		viewForm.setContent(btnTest);
		btnTest.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {

				Methods.testMethod();

			}
		});
		btnTest.setText("Test");

		txtTopLeft = new Text(viewForm, SWT.BORDER);
		txtTopLeft.setText("Top Left");
		viewForm.setTopLeft(txtTopLeft);

		txtTopCenter = new Text(viewForm, SWT.BORDER);
		txtTopCenter.setText("Top Center");
		viewForm.setTopCenter(txtTopCenter);

		txtTopRight = new Text(viewForm, SWT.BORDER);
		txtTopRight.setText("Top Right");
		viewForm.setTopRight(txtTopRight);
	}
}
