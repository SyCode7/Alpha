package de.uni_potsdam.hpi.cloudstore20.clientfrontend.view.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class LoginDialog extends Dialog {

	Object result;
	private Label label1, label2;
	private Text username;
	private Text password;

	public LoginDialog(Shell parent, int style) {

		super(parent, style);
	}

	public LoginDialog(Shell parent) {

		this(parent, 0); // your default style bits go here (not the Shell's
							// style bits)
	}

	public Object open() {

		Shell parent = this.getParent();
		final Shell shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);

		shell.setLayout(new GridLayout(2, false));
		shell.setText("Login form");

		label1 = new Label(shell, SWT.NULL);
		label1.setText("User Name: ");

		username = new Text(shell, SWT.SINGLE | SWT.BORDER);
		username.setText("");
		username.setTextLimit(30);

		label2 = new Label(shell, SWT.NULL);
		label2.setText("Password: ");

		password = new Text(shell, SWT.SINGLE | SWT.BORDER);
		System.out.println(password.getEchoChar());
		password.setEchoChar('*');
		password.setTextLimit(30);

		Button button = new Button(shell, SWT.PUSH);
		button.setText("Submit");
		button.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {

				String usernameString = username.getText();
				String passwordString = password.getText();
				MessageBox messageBox = new MessageBox(shell, SWT.OK | SWT.ICON_WARNING | SWT.CANCEL);
				if (usernameString == "") {

					messageBox.setMessage("Enter the User Name");
					messageBox.open();
				} else if (passwordString == "") {
					messageBox.setMessage("Enter the Password");
					messageBox.open();
				} else {
					//TODO: hier muss die richtige abfrage rein
					messageBox.setText("Login Form");
					messageBox.setMessage("Welcome:" + username.getText());
					messageBox.open();
				}
			}
		});
		username.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		password.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		shell.pack();
		shell.open();

		shell.open();
		Display display = parent.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		return result;
	}
}
