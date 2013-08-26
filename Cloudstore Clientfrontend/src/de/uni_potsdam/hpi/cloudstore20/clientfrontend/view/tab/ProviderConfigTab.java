package de.uni_potsdam.hpi.cloudstore20.clientfrontend.view.tab;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

public class ProviderConfigTab extends TabElement {

	public ProviderConfigTab(TabFolder tabFolder) {
		super(tabFolder);
	}

	@Override
	protected void createContent() {

		TabItem tbtmProviderKonf = new TabItem(this.tabFolder, SWT.NONE);
		tbtmProviderKonf.setText("Providerkonfiguration");

		SashForm sashForm_1 = new SashForm(this.tabFolder, SWT.VERTICAL);
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

		Text username = new Text(sashForm_4, SWT.BORDER);

		Label lblPasswordOder = new Label(sashForm_4, SWT.NONE);
		lblPasswordOder.setText("Passwort oder Schl\u00FCssel");

		Text password = new Text(sashForm_4, SWT.BORDER);

		Button btnProvider = new Button(sashForm_4, SWT.NONE);
		btnProvider.setText("Provider updaten");
		sashForm_4.setWeights(new int[] { 1, 1, 1, 1, 1, 1, 1, 1 });

		Composite composite_1 = new Composite(sashForm_3, SWT.NONE);
		sashForm_3.setWeights(new int[] { 1, 1 });

		Composite composite_3 = new Composite(sashForm_1, SWT.NONE);
		sashForm_1.setWeights(new int[] { 16, 302, 31 });

	}

}
