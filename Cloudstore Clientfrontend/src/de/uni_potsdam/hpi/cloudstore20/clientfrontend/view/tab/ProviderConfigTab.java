package de.uni_potsdam.hpi.cloudstore20.clientfrontend.view.tab;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.StorageProviderConfig;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.view.DefaultWindow;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.view.TabElement;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.enums.PROVIDER_CONFIG_CATEGORY;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.enums.STORAGE_PROVIDER_CONFIG;

public class ProviderConfigTab extends TabElement {

	private Combo combo;
	private Text username;
	private Text password;
	private Label lblPassword;
	private Label lblUsername;

	public ProviderConfigTab(TabFolder tabFolder, DefaultWindow window) {

		super(tabFolder, window);
	}

	@Override
	protected void createContent() {

		TabItem tbtmProviderKonf = new TabItem(this.tabFolder, SWT.NONE);
		tbtmProviderKonf.setText("Providerkonfiguration");

		SashForm sashForm_1 = new SashForm(this.tabFolder, SWT.VERTICAL);
		tbtmProviderKonf.setControl(sashForm_1);

		new Composite(sashForm_1, SWT.NONE);

		SashForm sashForm_4 = new SashForm(sashForm_1, SWT.VERTICAL);

		Label lblNewLabel_1 = new Label(sashForm_4, SWT.NONE);
		lblNewLabel_1.setText("Providerkonfiguration");

		new Composite(sashForm_4, SWT.NONE);

		Label lblNewLabel_2 = new Label(sashForm_4, SWT.NONE);
		lblNewLabel_2.setText("Provider ausw\u00E4hlen");

		combo = new Combo(sashForm_4, SWT.NONE);
		for (PROVIDER_CONFIG_CATEGORY pcc : PROVIDER_CONFIG_CATEGORY.values()) {
			combo.add(pcc.toString());
		}
		combo.select(0);
		combo.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				updateProviderConfigContent();

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

				updateProviderConfigContent();

			}
		});

		new Composite(sashForm_4, SWT.NONE);

		lblUsername = new Label(sashForm_4, SWT.NONE);

		username = new Text(sashForm_4, SWT.BORDER);
		username.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {

				StorageProviderConfig.getInstance().setProviderInformation(getSelectedProviderCat(),
						STORAGE_PROVIDER_CONFIG.Username, username.getText());
				// TODO: entfernen
				// System.out.println(StorageProviderConfig.getInstance().get(getSelectedProviderCat(),
				// STORAGE_PROVIDER_CONFIG.Username));

			}
		});

		new Composite(sashForm_4, SWT.NONE);

		lblPassword = new Label(sashForm_4, SWT.NONE);

		password = new Text(sashForm_4, SWT.BORDER);
		password.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {

				StorageProviderConfig.getInstance().setProviderInformation(getSelectedProviderCat(),
						STORAGE_PROVIDER_CONFIG.Password, password.getText());
				// TODO: entfernen
				// System.out.println(StorageProviderConfig.getInstance().get(getSelectedProviderCat(),
				// STORAGE_PROVIDER_CONFIG.Password));

			}
		});

		sashForm_4.setWeights(new int[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 });

		new Composite(sashForm_1, SWT.NONE);
		sashForm_1.setWeights(new int[] { 10, 100, 10 });

		updateProviderConfigContent();
	}

	private void updateProviderConfigContent() {

		PROVIDER_CONFIG_CATEGORY pcc = this.getSelectedProviderCat();
		this.lblUsername.setText(pcc.getUserNameCredentialDescription());
		this.lblPassword.setText(pcc.getPasswordCredentialsDescription());
		String userName = "";
		String pwd = "";
		if (StorageProviderConfig.getInstance().hasProviderInformation(pcc)) {
			userName = StorageProviderConfig.getInstance().get(pcc, STORAGE_PROVIDER_CONFIG.Password);
			pwd = StorageProviderConfig.getInstance().get(pcc, STORAGE_PROVIDER_CONFIG.Username);
		}
		if (userName == null) {
			userName = "";
		}
		if (pwd == null) {
			pwd = "";
		}
		this.password.setText(pwd);
		this.username.setText(userName);

	}

	private PROVIDER_CONFIG_CATEGORY getSelectedProviderCat() {

		return PROVIDER_CONFIG_CATEGORY.getFromString((this.combo.getItem(this.combo.getSelectionIndex())));
	}

	@Override
	protected void performContentUpdate() {

	}

}
