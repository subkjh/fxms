package com.fxms.ui.biz.pane.tree;

import com.fxms.ui.bas.mo.Mo;
import com.fxms.ui.bas.vo.LocationVo;

import javafx.scene.control.TreeCell;

public final class FxTreeCell extends TreeCell<Object> {

	public FxTreeCell() {

	}

	@Override
	public void updateItem(Object item, boolean empty) {

		super.updateItem(item, empty);

		if (empty) {
			setText(null);
			setGraphic(null);
		} else {
			setText(getString());
			setGraphic(getTreeItem().getGraphic());
		}
	}

	private String getString() {
		Object item = getItem();
		if (item == null) {
			return "";
		}

		if (item instanceof LocationVo) {
			return ((LocationVo) item).getInloName();
		} else if (item instanceof Mo) {
			return ((Mo) item).getMoName();
		}

		return item.toString();
	}

}
