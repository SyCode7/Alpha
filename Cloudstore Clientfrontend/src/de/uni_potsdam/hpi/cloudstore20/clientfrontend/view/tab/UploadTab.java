package de.uni_potsdam.hpi.cloudstore20.clientfrontend.view.tab;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.view.DefaultWindow;

public class UploadTab extends TabElement {

	public UploadTab(TabFolder tabFolder, DefaultWindow window) {

		super(tabFolder, window);
	}

	@Override
	protected void createContent() {

		TabItem tbtmUpload = new TabItem(this.tabFolder, SWT.NONE);
		tbtmUpload.setText("Upload");

		SashForm sashForm_1 = new SashForm(tabFolder, SWT.VERTICAL);
		tbtmUpload.setControl(sashForm_1);

		Composite composite = new Composite(sashForm_1, SWT.NONE);

		SashForm sashForm_2 = new SashForm(sashForm_1, SWT.NONE);

		SashForm sashForm_3 = new SashForm(sashForm_2, SWT.VERTICAL);

		Label lblNachHochzuladendeDateien = new Label(sashForm_3, SWT.NONE);
		lblNachHochzuladendeDateien.setText("Nach hochzuladende Dateien:");

		Label lblMaikLernt = new Label(sashForm_3, SWT.NONE);
		lblMaikLernt.setText("1. Maik lernt laufen #2");

		Label lblDiesesVideo = new Label(sashForm_3, SWT.NONE);
		lblDiesesVideo.setText("2. Dieses Video ist doof");

		Label lblLustigSehr = new Label(sashForm_3, SWT.NONE);
		lblLustigSehr.setText("3. lustig, sehr sehr lustig");

		final ProgressBar pb2 = new ProgressBar(sashForm_3, SWT.SMOOTH);
		pb2.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseUp(MouseEvent e) {

				pb2.setSelection((int) (Math.random() * 100));
			}
		});

		sashForm_3.setWeights(new int[] { 1, 1, 1, 1, 1 });

		Browser browser = new Browser(sashForm_2, SWT.NONE);
		browser.setUrl("maps.google.com");
		sashForm_2.setWeights(new int[] { 1, 1 });

		Composite composite_1 = new Composite(sashForm_1, SWT.NONE);
		sashForm_1.setWeights(new int[] { 1, 20, 1 });

	}

}
