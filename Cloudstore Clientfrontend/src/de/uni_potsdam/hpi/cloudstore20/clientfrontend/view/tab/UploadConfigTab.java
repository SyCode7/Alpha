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
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.view.DefaultWindow;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.view.TabElement;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.enums.OPTIMIZATION_FUNCTION;

public class UploadConfigTab extends TabElement {

	public UploadConfigTab(TabFolder folder, DefaultWindow window) {

		super(folder, window);
	}

	@Override
	protected void createContent() {

		TabItem tbtmUploadkonf = new TabItem(this.tabFolder, SWT.NONE);
		tbtmUploadkonf.setText("Uploadkonfiguration");

		SashForm sashForm_MainFrame = new SashForm(this.tabFolder, SWT.VERTICAL);
		tbtmUploadkonf.setControl(sashForm_MainFrame);

		SashForm sashForm_ConfigMainFrame = new SashForm(sashForm_MainFrame, SWT.NONE);

		SashForm sashForm_StandardConfig = new SashForm(sashForm_ConfigMainFrame, SWT.VERTICAL);

		Label lblNewLabel = new Label(sashForm_StandardConfig, SWT.NONE);
		lblNewLabel.setText("Standardeinstellungen");

		SashForm sashForm_AvailabilityFrame = new SashForm(sashForm_StandardConfig, SWT.VERTICAL);

		Label lblMindestverfgbarkeit = new Label(sashForm_AvailabilityFrame, SWT.NONE);
		lblMindestverfgbarkeit.setText("Verf\u00FCgbarkeit (Anzahl Neuner)");
		final Text verfuegbarkeit = new Text(sashForm_AvailabilityFrame, SWT.BORDER);
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
		sashForm_AvailabilityFrame.setWeights(new int[] { 1, 1 });

		new Composite(sashForm_StandardConfig, SWT.NONE);

		SashForm sashForm_CostsFrame = new SashForm(sashForm_StandardConfig, SWT.VERTICAL);

		Label lblMaximaleKostenn = new Label(sashForm_CostsFrame, SWT.NONE);
		lblMaximaleKostenn.setText("Maximale Kosten (1..n)");
		final Text kosten = new Text(sashForm_CostsFrame, SWT.BORDER);
		kosten.setText("1.0");
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
		sashForm_CostsFrame.setWeights(new int[] { 1, 1 });

		new Composite(sashForm_StandardConfig, SWT.NONE);

		SashForm sashForm_PerformanceFrame = new SashForm(sashForm_StandardConfig, SWT.VERTICAL);

		Label lblMaximaleperformancen = new Label(sashForm_PerformanceFrame, SWT.NONE);
		lblMaximaleperformancen.setText("Maximale \"Performance\" (0..n)");

		final Text performance = new Text(sashForm_PerformanceFrame, SWT.BORDER);
		performance.setText("0.0");
		performance.addVerifyListener(new VerifyListener() {

			@Override
			public void verifyText(VerifyEvent e) {

				final String oldS = performance.getText();
				final String newS = oldS.substring(0, e.start) + e.text + oldS.substring(e.end);

				try {
					double value = Double.valueOf(newS);
					if (value < 0.0d) {
						e.doit = false;
					} else {
						DefaultWindow.config.setMaxPerformance(value);
					}
				} catch (final NumberFormatException numberFormatException) {
					e.doit = false;
				}
			}

		});
		sashForm_PerformanceFrame.setWeights(new int[] { 1, 1 });

		new Composite(sashForm_StandardConfig, SWT.NONE);

		sashForm_StandardConfig.setWeights(new int[] { 1, 2, 1, 2, 1, 2, 1 });

		new Composite(sashForm_ConfigMainFrame, SWT.NONE);

		SashForm sashForm_AutomaticOptimizing = new SashForm(sashForm_ConfigMainFrame, SWT.VERTICAL);

		Label lblAutomatischeOptimierung = new Label(sashForm_AutomaticOptimizing, SWT.NONE);
		lblAutomatischeOptimierung.setText("automatische Optimierung");

		Label lblAusf = new Label(sashForm_AutomaticOptimizing, SWT.NONE);
		lblAusf.setText("1. Stufe");
		final Combo ersteStufe = new Combo(sashForm_AutomaticOptimizing, SWT.NONE);
		ersteStufe.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				DefaultWindow.config.getOptimizationOrdering()[0] = OPTIMIZATION_FUNCTION.getValueOutOfDisplayString(ersteStufe
						.getItem(ersteStufe.getSelectionIndex()));
			}
		});

		Label lblKosten = new Label(sashForm_AutomaticOptimizing, SWT.NONE);
		lblKosten.setText("2. Stufe");
		final Combo zweiteStufe = new Combo(sashForm_AutomaticOptimizing, SWT.NONE);
		zweiteStufe.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				DefaultWindow.config.getOptimizationOrdering()[1] = OPTIMIZATION_FUNCTION.getValueOutOfDisplayString(zweiteStufe
						.getItem(zweiteStufe.getSelectionIndex()));
			}
		});

		Label lblGeschw = new Label(sashForm_AutomaticOptimizing, SWT.NONE);
		lblGeschw.setText("3. Stufe");
		final Combo dritteStufe = new Combo(sashForm_AutomaticOptimizing, SWT.NONE);

		new Composite(sashForm_AutomaticOptimizing, SWT.NONE);
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

		sashForm_AutomaticOptimizing.setWeights(new int[] { 1, 1, 1, 1, 1, 1, 1, 1 });
		sashForm_ConfigMainFrame.setWeights(new int[] { 10, 1, 10 });

		new Composite(sashForm_MainFrame, SWT.NONE);

		SashForm sashForm_ExampleMainFrame = new SashForm(sashForm_MainFrame, SWT.VERTICAL);

		Label lblVorschau = new Label(sashForm_ExampleMainFrame, SWT.NONE);
		lblVorschau.setText("Vorschau");

		new Composite(sashForm_ExampleMainFrame, SWT.NONE);

		List list_Examples = new List(sashForm_ExampleMainFrame, SWT.BORDER);

		new Composite(sashForm_ExampleMainFrame, SWT.NONE);

		SashForm sashForm_ExampleButtonFrame = new SashForm(sashForm_ExampleMainFrame, SWT.NONE);

		Button btnSimulate = new Button(sashForm_ExampleButtonFrame, SWT.NONE);
		btnSimulate.setText("Simulate");

		new Composite(sashForm_ExampleButtonFrame, SWT.NONE);
		sashForm_ExampleButtonFrame.setWeights(new int[] { 1, 4 });
		sashForm_ExampleMainFrame.setWeights(new int[] { 2, 1, 10, 1, 3 });

		new Composite(sashForm_MainFrame, SWT.NONE);
		sashForm_MainFrame.setWeights(new int[] { 20, 1, 8, 1 });

	}

	@Override
	protected void performContentUpdate() {

		// TODO Auto-generated method stub

	}

}
