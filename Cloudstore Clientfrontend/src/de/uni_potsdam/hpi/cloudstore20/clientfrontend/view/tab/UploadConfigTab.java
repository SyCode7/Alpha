package de.uni_potsdam.hpi.cloudstore20.clientfrontend.view.tab;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.view.DefaultWindow;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.view.TabElement;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.OPTIMIZATION_FUNCTION;

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

		new Composite(sashForm_6, SWT.NONE);

		Button uploadopti = new Button(sashForm_6, SWT.CHECK);
		uploadopti.setText("Uploadoptimierung");
		Button uploadspli = new Button(sashForm_6, SWT.CHECK);
		uploadspli.setText("Uploadsplitting");
		Button dataEncr = new Button(sashForm_6, SWT.CHECK);
		dataEncr.setText("Daten verschl\u00FCsseln");

		sashForm_6.setWeights(new int[] { 1, 1, 1, 1, 1 });

		new Composite(sashForm_5, SWT.NONE);

		SashForm sashForm = new SashForm(sashForm_5, SWT.VERTICAL);

		Label lblNewLabel = new Label(sashForm, SWT.NONE);
		lblNewLabel.setText("Standardeinstellungen");

		SashForm sashForm_2 = new SashForm(sashForm, SWT.VERTICAL);

		Label lblMindestverfgbarkeit = new Label(sashForm_2, SWT.NONE);
		lblMindestverfgbarkeit.setText("Verf\u00FCgbarkeit (Anzahl Neuner)");
		final Text verfuegbarkeit = new Text(sashForm_2, SWT.BORDER);
		verfuegbarkeit.setText(String.valueOf(DefaultWindow.config.getNumberOfNines()));
		verfuegbarkeit.addVerifyListener(new VerifyListener() {

			public void verifyText(VerifyEvent e) {

				final String oldS = verfuegbarkeit.getText();
				final String newS = oldS.substring(0, e.start) + e.text + oldS.substring(e.end);

				try {
					int value = Integer.valueOf(newS);
					DefaultWindow.config.setNumberOfNines(value);
				} catch (final NumberFormatException numberFormatException) {
					e.doit = false;
				}
			}
		});
		sashForm_2.setWeights(new int[] { 1, 1 });

		new Composite(sashForm, SWT.NONE);

		SashForm sashForm_3 = new SashForm(sashForm, SWT.VERTICAL);

		Label lblMaximaleKostenn = new Label(sashForm_3, SWT.NONE);
		lblMaximaleKostenn.setText("Maximale Kosten (1..n)");
		final Text kosten = new Text(sashForm_3, SWT.BORDER);
		kosten.setText(String.valueOf(DefaultWindow.config.getMaxCosts()));
		kosten.addVerifyListener(new VerifyListener() {

			@Override
			public void verifyText(VerifyEvent e) {

				final String oldS = kosten.getText();
				final String newS = oldS.substring(0, e.start) + e.text + oldS.substring(e.end);

				try {
					double value = Double.valueOf(newS);
					if (value < 1.0d) {
						e.doit = false;
					} else {
						DefaultWindow.config.setMaxCosts(value);
					}
				} catch (final NumberFormatException numberFormatException) {
					e.doit = false;
				}
			}

		});
		sashForm_3.setWeights(new int[] { 1, 1 });

		new Composite(sashForm, SWT.NONE);

		sashForm.setWeights(new int[] { 1, 1, 1, 1, 1 });

		new Composite(sashForm_5, SWT.NONE);

		SashForm sashForm_7 = new SashForm(sashForm_5, SWT.VERTICAL);

		Label lblAutomatischeOptimierung = new Label(sashForm_7, SWT.NONE);
		lblAutomatischeOptimierung.setText("automatische Optimierung");

		Label lblAusf = new Label(sashForm_7, SWT.NONE);
		lblAusf.setText("1. Stufe");
		final Combo ersteStufe = new Combo(sashForm_7, SWT.NONE);
		ersteStufe.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				DefaultWindow.config.getOptimizationOrdering()[0] = OPTIMIZATION_FUNCTION.getValueOutOfDisplayString(ersteStufe
						.getItem(ersteStufe.getSelectionIndex()));
			}
		});

		Label lblKosten = new Label(sashForm_7, SWT.NONE);
		lblKosten.setText("2. Stufe");
		final Combo zweiteStufe = new Combo(sashForm_7, SWT.NONE);
		zweiteStufe.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				DefaultWindow.config.getOptimizationOrdering()[1] = OPTIMIZATION_FUNCTION.getValueOutOfDisplayString(zweiteStufe
						.getItem(zweiteStufe.getSelectionIndex()));
			}
		});

		Label lblGeschw = new Label(sashForm_7, SWT.NONE);
		lblGeschw.setText("3. Stufe");
		final Combo dritteStufe = new Combo(sashForm_7, SWT.NONE);

		new Composite(sashForm_7, SWT.NONE);
		dritteStufe.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				DefaultWindow.config.getOptimizationOrdering()[2] = OPTIMIZATION_FUNCTION.getValueOutOfDisplayString(dritteStufe
						.getItem(dritteStufe.getSelectionIndex()));
			}
		});

		// Alle auswahlmöglichkeiten laden
		for (OPTIMIZATION_FUNCTION of : OPTIMIZATION_FUNCTION.values()) {
			ersteStufe.add(of.getDisplayString());
			zweiteStufe.add(of.getDisplayString());
			dritteStufe.add(of.getDisplayString());
		}
		// aktuelle Konfiguration einstellen
		OPTIMIZATION_FUNCTION[] current = DefaultWindow.config.getOptimizationOrdering();
		OPTIMIZATION_FUNCTION[] possible = OPTIMIZATION_FUNCTION.values();
		for (int j = 0; j < possible.length; j++) {

			if (current[0].equals(possible[j])) {
				ersteStufe.select(j);
			}
			if (current[1].equals(possible[j])) {
				zweiteStufe.select(j);
			}
			if (current[2].equals(possible[j])) {
				dritteStufe.select(j);
			}

		}

		sashForm_7.setWeights(new int[] { 1, 1, 1, 1, 1, 1, 1, 1 });
		sashForm_5.setWeights(new int[] { 100, 10, 100, 10, 100 });
	}

	@Override
	protected void performContentUpdate() {

		// TODO Auto-generated method stub

	}

}
