package de.uni_potsdam.hpi.cloudstore20.clientfrontend.view.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class YesNoDialog extends Dialog {

	protected boolean result = false;
	protected Shell shell;
	protected String question;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public YesNoDialog(Shell parent, String question) {

		super(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		setText("SWT Dialog");
		this.question = question;
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public boolean open() {

		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {

		shell = new Shell(getParent(), getStyle());
		shell.setSize(268, 175);
		shell.setText(getText());

		SashForm sashForm = new SashForm(shell, SWT.NONE);
		sashForm.setLocation(0, 0);

		Label label = new Label(sashForm, SWT.NONE);
		label.setText(question);

		SashForm sashForm_1 = new SashForm(sashForm, SWT.VERTICAL);

		Button btnJa = new Button(sashForm_1, SWT.NONE);
		btnJa.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseUp(MouseEvent e) {

				result = true;
			}
		});
		btnJa.setText("Ja");

		Button btnNein = new Button(sashForm_1, SWT.NONE);
		btnNein.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseUp(MouseEvent e) {

				result = false;
			}
		});
		btnNein.setText("Nein");
		sashForm_1.setWeights(new int[] { 1, 1 });
		sashForm.setWeights(new int[] { 1, 1 });

	}
}
