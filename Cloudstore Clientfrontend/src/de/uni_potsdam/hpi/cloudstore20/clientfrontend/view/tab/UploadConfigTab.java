package de.uni_potsdam.hpi.cloudstore20.clientfrontend.view.tab;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.view.DefaultWindow;

public class UploadConfigTab extends TabElement {

	public UploadConfigTab(TabFolder folder, DefaultWindow window) {

		super(folder, window);
	}

	@Override
	protected void createContent() {

		TabItem tbtmUploadkonf = new TabItem(this.tabFolder, SWT.NONE);
		tbtmUploadkonf.setText("Uploadkonfiguration");

		SashForm sashForm_5 = new SashForm(this.tabFolder, SWT.NONE);
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

		new Composite(sashForm_5, SWT.NONE);
		sashForm_5.setWeights(new int[] { 1, 1, 1 });
	}

	@Override
	public void updateContent() {

		// TODO Auto-generated method stub

	}

}
