package de.uni_potsdam.hpi.cloudstore20.clientfrontend.view.tab.helper;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.ProgressBar;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.StorageProvider;

public class UploadProgressbarContainer {

	private StorageProvider prov;
	private ProgressBar bar;

	public UploadProgressbarContainer(ProgressBar progressbar, StorageProvider provider) {

		this.bar = progressbar;
		this.prov = provider;

		this.bar.setToolTipText(this.prov.getCompleteProviderName());
		this.bar.addPaintListener(new PaintListener() {

			public void paintControl(PaintEvent e) {

				e.gc.setFont(e.display.getSystemFont());
				e.gc.setForeground(e.display.getSystemColor(SWT.COLOR_BLACK));

				Rectangle rec = bar.getBounds();
				e.gc.drawString(prov.getCompleteProviderName(), rec.x + 5, (rec.height - e.gc.getFontMetrics().getHeight()) / 2,
						true);
			}
		});

	}

	public void update() {

		this.bar.setSelection(this.prov.getProcessStatus());

	}

	public String getProviderName() {

		return this.prov.getCompleteProviderName();

	}

	public void remove() {

		this.prov = null;
		this.bar.dispose();
		this.bar = null;
	}

}
