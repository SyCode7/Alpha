package de.uni_potsdam.hpi.cloudstore20.clientfrontend.view;

import java.util.LinkedList;

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
import de.uni_potsdam.hpi.cloudstore20.meta.CloudstoreException;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.CloudstoreConfig;

public class DefaultWindow {

	public Shell shell;
	public static Display display;
	private TabFolder tabFolder;
	private static LinkedList<TabElement> tabsToUpdate = new LinkedList<TabElement>();
	private long updatingIntervall = 100l;

	public static CloudstoreConfig config;

	
	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			DefaultWindow window = new DefaultWindow();
			window.loadConfig();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadConfig() {

		DefaultWindow.config = CloudstoreConfig.loadDefault();
	}

	/**
	 * Open the window.
	 */
	public void open() {

		DefaultWindow.display = Display.getDefault();
		this.createContents();
		this.startContentUpdater();
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
		this.shell = new Shell(DefaultWindow.display, SWT.NO_TRIM | SWT.ON_TOP);

		this.shell.setSize(643, 447);
		this.shell.setText("Cloudstore 2.0");
		this.shell.setLayout(new FillLayout(SWT.HORIZONTAL));

		SashForm sashForm_MainFrame = new SashForm(this.shell, SWT.VERTICAL);

		Label lblHeaderText = new Label(sashForm_MainFrame, SWT.NONE);
		lblHeaderText.setFont(SWTResourceManager.getFont("Segoe UI", 13, SWT.BOLD));
		lblHeaderText.setText("CloudRaid - Futuristic Online Storage for now");

		this.tabFolder = new TabFolder(sashForm_MainFrame, SWT.NONE);

		this.loadTabElements();

		this.closeButton(sashForm_MainFrame);

		this.loginButton(sashForm_MainFrame);

		SashForm sashForm_LoginFrame = new SashForm(sashForm_MainFrame, SWT.NONE);
		Button btnLogin = new Button(sashForm_LoginFrame, SWT.NONE);
		btnLogin.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(final SelectionEvent e) {

				final LoginDialog dialog = new LoginDialog(shell, 0);
				dialog.open();

			}
		});
		btnLogin.setText("Login");

		final Button btnLoadfromserver = new Button(sashForm_LoginFrame, SWT.CHECK);
		btnLoadfromserver.setSelection(config.getLoadFromServer());
		btnLoadfromserver.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {

				config.setLoadFromServer(btnLoadfromserver.getSelection());

			}
		});
		btnLoadfromserver.setText("loadFromServer");
		sashForm_LoginFrame.setWeights(new int[] { 1, 1 });

		sashForm_MainFrame.setWeights(new int[] { 2, 20, 1, 1 });

	}

	private void loginButton(SashForm sashForm) {

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

		DefaultWindow.tabsToUpdate.add(new DataListTab(tabFolder, this));

		DefaultWindow.tabsToUpdate.add(new MapTab(tabFolder, this));

		DefaultWindow.tabsToUpdate.add(new ProviderConfigTab(tabFolder, this));

		DefaultWindow.tabsToUpdate.add(new BackupConfigTab(tabFolder, this));

		DefaultWindow.tabsToUpdate.add(new UploadTab(tabFolder, this));

		DefaultWindow.tabsToUpdate.add(new UploadConfigTab(tabFolder, this));

		this.createOnly4DesignTabContent();
	}

	private void startContentUpdater() {

		Thread t = new Thread() {

			@Override
			public void run() {

				while (true) {
					try {
						Thread.sleep(updatingIntervall);
					} catch (InterruptedException e1) {}

					DefaultWindow.display.syncExec(new Thread() {

						public void run() {

							for (TabElement tab : DefaultWindow.tabsToUpdate) {
								try {
									tab.updateContent();
								} catch (CloudstoreException e) {
									// TODO POPUP wegen fehlermeldung
									// Das Popup darf auch nur einmal kommen und am besten muss die refreshingRoutine pausieren
									e.printStackTrace();
								}
							}
						}
					});
				}
			}
		};
		t.start();

	}

	private void createOnly4DesignTabContent() {

		TabItem tbtmProviderKonf = new TabItem(tabFolder, SWT.NONE);
		tbtmProviderKonf.setText("Only4Designer");

	}
}
