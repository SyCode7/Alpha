package de.uni_potsdam.hpi.cloudstore20.clientfrontend.clientfrontend;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class DefaultWindow {

	protected Shell shell;

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
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(643, 447);
		shell.setText("SWT Application");

		Button btnTest = new Button(shell, SWT.NONE);
		btnTest.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {

				Methods.testMethod();

			}
		});
		btnTest.setBounds(10, 10, 75, 25);
		btnTest.setText("Test");

	}
}
