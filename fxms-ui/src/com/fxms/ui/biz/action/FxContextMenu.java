package com.fxms.ui.biz.action;

import com.fxms.ui.bas.property.NeedSelectedData;

import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class FxContextMenu<DATA> extends ContextMenu {

	public void onSelectedData(DATA data) {
		setMenu(getItems(), data);
	}

	@SuppressWarnings("unchecked")
	private void setMenu(ObservableList<MenuItem> items, DATA data) {
		if (items == null || items.size() == 0) {
			return;
		}

		for (MenuItem mi : items) {
			if (mi instanceof NeedSelectedData) {
				NeedSelectedData<DATA> fxDataMenuItem = (NeedSelectedData<DATA>) mi;
				fxDataMenuItem.onSelectedData(data);
			}

			if (mi instanceof Menu) {
				setMenu(((Menu) mi).getItems(), data);
			}
		}
	}

}
