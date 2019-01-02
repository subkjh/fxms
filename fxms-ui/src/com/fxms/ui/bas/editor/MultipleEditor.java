package com.fxms.ui.bas.editor;

import java.util.Map;

import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.bas.vo.Attr;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.ComboBoxListCell;

public class MultipleEditor extends ListView<String> implements FxEditor {

	private String qid;

	public MultipleEditor() {
		setEditable(true);
		this.setPrefHeight(120);
	}

	@Override
	public void clearEditor() {
	}

	@Override
	public String getAttrId() {

		StringBuffer ret = new StringBuffer();

		for (String s : getItems()) {
			if (s.length() > 0) {
				if (ret.length() > 0) {
					ret.append(",");
				}
				ret.append(s);
			}
		}

		return ret.toString();
	}

	@Override
	public void init(String attrValueList, String promptText, int width) {
		this.qid = attrValueList;
		initItems(qid, null);
		this.setPrefWidth(width);

	}

	@Override
	public void setAttrId(Object id, Map<String, Object> objectData) {

		initItems(qid, objectData);

		try {
			String ss[] = id.toString().split(",");
			for (int i = ss.length - 1; i >= 0; i--) {
				getItems().add(0, ss[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void initItems(String qid, Map<String, Object> objectData) {

		if (qid == null) {
			return;
		}

		getItems().clear();

		final ObservableList<String> names = FXCollections.observableArrayList();

		for (Attr attr : CodeMap.getMap().getAttrList(qid, objectData)) {
			getItems().add("");
			names.add(attr.getAttrId());
		}

		setCellFactory(ComboBoxListCell.forListView(names));

	}

}
