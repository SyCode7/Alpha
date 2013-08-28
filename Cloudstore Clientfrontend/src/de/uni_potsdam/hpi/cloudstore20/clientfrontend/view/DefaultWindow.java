package de.uni_potsdam.hpi.cloudstore20.clientfrontend.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.wb.swt.SWTResourceManager;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.view.dialog.LoginDialog;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.view.tab.BackupConfigTab;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.view.tab.DataListTab;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.view.tab.MapTab;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.view.tab.ProviderConfigTab;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.view.tab.UploadConfigTab;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.view.tab.UploadTab;

public class DefaultWindow {

	private Shell shell;
	private Display display;
	private TabFolder tabFolder;

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

		this.display = Display.getDefault();
		createContents();
		this.shell.open();
		this.shell.layout();
		while (!this.shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {

		/*
		 * Hier können Systemleisten u.a. ausgeschaltet werden
		 */
		this.shell = new Shell(this.display, SWT.NO_TRIM | SWT.ON_TOP);

		this.shell.setSize(643, 447);
		this.shell.setText("Cloudstore 2.0");
		this.shell.setLayout(new FillLayout(SWT.HORIZONTAL));

		SashForm sashForm = new SashForm(this.shell, SWT.VERTICAL);

		Label lblHeaderText = new Label(sashForm, SWT.NONE);
		lblHeaderText.setFont(SWTResourceManager.getFont("Segoe UI", 13,
				SWT.BOLD));
		lblHeaderText.setText("CloudRaid - Futuristic Online Storage for now");

		this.tabFolder = new TabFolder(sashForm, SWT.NONE);

		this.loadTabElements();

		TabItem tbtmOnlydesigner = new TabItem(tabFolder, SWT.NONE);
		tbtmOnlydesigner.setText("Only4Designer");

		this.closeButton(sashForm);

		this.loginButton(sashForm);

		sashForm.setWeights(new int[] { 2, 20, 1, 1 });

	}

	private void loginButton(SashForm sashForm) {
		Button btnLogin = new Button(sashForm, SWT.NONE);
		btnLogin.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(final SelectionEvent e) {

				final LoginDialog dialog = new LoginDialog(shell, 0);
				dialog.open();

			}
		});
		btnLogin.setText("Login");
	}

	private void closeButton(SashForm sashForm) {
		Button btnBeenden = new Button(sashForm, SWT.NONE);
		btnBeenden.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseUp(MouseEvent e) {

				System.exit(0);
			}
		});
		btnBeenden.setText("Beenden");
	}

	private void loadTabElements() {

		new DataListTab(tabFolder);

		new MapTab(tabFolder);

		new ProviderConfigTab(tabFolder);

		new BackupConfigTab(tabFolder, shell);

		new UploadTab(tabFolder);

		new UploadConfigTab(tabFolder);

	}

}
