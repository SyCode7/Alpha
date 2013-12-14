package de.uni_potsdam.hpi.cloudstore20.clientfrontend.view.tab;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.helper.ServletCommunicationException;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.helper.ServletCommunicator;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.view.DefaultWindow;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.view.TabElement;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.dataList.DataList;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.dataList.DataListElement;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.dataList.DataListException;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.dataList.DataListTest;

public class DataListTab extends TabElement {

	public DataListTab(TabFolder tabFolder, DefaultWindow window) {

		super(tabFolder, window);
	}

	@Override
	protected void createContent() {

		TabItem tbtmDateiliste = new TabItem(tabFolder, SWT.NONE);
		tbtmDateiliste.setText("Dateiliste");

		SashForm sashForm = new SashForm(this.tabFolder, SWT.VERTICAL);
		tbtmDateiliste.setControl(sashForm);

		Tree tree = new Tree(sashForm, SWT.BORDER);
		this.buildFileTree(tree);

		SashForm sashForm_1 = new SashForm(sashForm, SWT.NONE);

		Button btnNewButton_1 = new Button(sashForm_1, SWT.NONE);
		btnNewButton_1.setText("Eintrag öffnen");

		new Composite(sashForm_1, SWT.NONE);
		sashForm_1.setWeights(new int[] { 1, 5 });
		sashForm.setWeights(new int[] { 325, 27 });

	}

	private DataList loadContent() throws DataListException {

		DataList dl = null;
		if (DefaultWindow.config.getLoadFromServer()) {
			try {
				dl = ServletCommunicator.getDataList();
			} catch (ServletCommunicationException e) {
				dl = DataListTest.getSampleDataList("fe_excep");
			}
		} else {
			dl = DataListTest.getSampleDataList("frontend");
		}

		return dl;
	}

	private void loadDefault(Tree tree) {

		for (int i = 0; i < 4; i++) {
			TreeItem item0 = new TreeItem(tree, 0);
			item0.setText("Item " + i);
			for (int j = 0; j < 4; j++) {
				TreeItem item1 = new TreeItem(item0, 0);
				item1.setText("SubItem " + i + " " + j);
				for (int k = 0; k < 4; k++) {
					TreeItem item2 = new TreeItem(item1, 0);
					item2.setText("SubItem " + i + " " + j + " " + k);
				}
			}
		}
	}

	private void buildTreeContent(Tree tree, DataList content) throws DataListException {

		for (DataListElement dle : content.getContent()) {

			TreeItem item = new TreeItem(tree, 0);
			item.setText(dle.getName());
			if (dle.isFolder()) {
				this.buildTreeContent(item, dle.getFolderContent());
			}

		}

	}

	private void buildTreeContent(TreeItem treeItem, DataList content) throws DataListException {

		for (DataListElement dle : content.getContent()) {

			TreeItem item = new TreeItem(treeItem, 0);
			item.setText(dle.getName());
			if (dle.isFolder()) {
				this.buildTreeContent(item, dle.getFolderContent());
			}

		}

	}

	private void buildFileTree(final Tree tree) {

		try {
			DataList content = this.loadContent();
			this.buildTreeContent(tree, content);
		} catch (DataListException e1) {
			e1.printStackTrace();

			this.loadDefault(tree);

		}

		tree.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event e) {

				String string = "";
				TreeItem[] selection = tree.getSelection();
				for (int i = 0; i < selection.length; i++)
					string += selection[i] + " ";
				System.out.println("Selection={" + string + "}");
			}
		});
		tree.addListener(SWT.DefaultSelection, new Listener() {

			public void handleEvent(Event e) {

				String string = "";
				TreeItem[] selection = tree.getSelection();
				for (int i = 0; i < selection.length; i++)
					string += selection[i] + " ";
				System.out.println("DefaultSelection={" + string + "}");
			}
		});
		tree.addListener(SWT.Expand, new Listener() {

			public void handleEvent(Event e) {

				System.out.println("Expand={" + e.item + "}");
			}
		});
		tree.addListener(SWT.Collapse, new Listener() {

			public void handleEvent(Event e) {

				System.out.println("Collapse={" + e.item + "}");
			}
		});
		tree.getItems()[0].setExpanded(true);
	}

	@Override
	protected void performContentUpdate() {

		// TODO Auto-generated method stub

	}

}
