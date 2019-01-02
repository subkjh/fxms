package com.fxms.ui.bas.pane;

import java.util.Map;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.bas.pane.list.ListPaneBase;
import com.fxms.ui.bas.property.FxUi;
import com.fxms.ui.biz.action.FxDataMenuItem;
import com.fxms.ui.dx.DxAsyncSelector;
import com.fxms.ui.dx.DxCallback;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;

public class TableCfgPane extends TabPane implements FxUi {

	private EditorPane tabEditorPane;
	private ListPaneBase colListPane;
	private EditorPane colEditorPane;
	private ListPaneBase idxListPane;
	private EditorPane idxEditorPane;

	public TableCfgPane() {
		// setPrefHeight(680);
	}

	@Override
	public void initData(Map<String, Object> parameters) {

		Map<String, Object> data = null;
		if (parameters == null) {
			data = null;
		} else {
			Map<String, Object> result = DxAsyncSelector.getSelector().callMethod(null,
					CodeMap.getMap().getOpCode(OP_NAME.EtcTableGet), "tabName", parameters.get("tabName"));
			data = DxAsyncSelector.getData(result, 0);
		}

		tabEditorPane.initData(data);

		if (data == null) {
			tabEditorPane.setDisable(false);
			colEditorPane.setDisable(true);
			idxEditorPane.setDisable(true);
		} else {
			showTableData(data);
			colListPane.doSearch();
			idxListPane.doSearch();
		}

	}

	private UiOpCodeVo opCode;

	@Override
	public UiOpCodeVo getOpCode() {
		return opCode;
	}

	@Override
	public void init(UiOpCodeVo opcode) {

		this.opCode = opcode;

		UiOpCodeVo colOpCode;
		UiOpCodeVo idxOpCode;
		UiOpCodeVo colListOpCode = CodeMap.getMap().getOpCode(OP_NAME.EtcTableColumnUi);
		UiOpCodeVo idxListOpCode = CodeMap.getMap().getOpCode(OP_NAME.EtcTableIndexUi);
		colOpCode = CodeMap.getMap().getOpCode(OP_NAME.EtcTableColumnAdd);
		idxOpCode = CodeMap.getMap().getOpCode(OP_NAME.EtcTableIndexAdd);

		Tab tab = new Tab();
		tab.setClosable(false);
		tab.setText("Repository");
		tab.setContent(makeTablePane(opcode));
		getTabs().add(tab);

		tab = new Tab();
		tab.setClosable(false);
		tab.setText("Attributes");
		tab.setContent(makeColumnsPane(colOpCode, colListOpCode));
		getTabs().add(tab);

		tab = new Tab();
		tab.setClosable(false);
		tab.setText("Indexes");
		tab.setContent(makeIndexPane(idxOpCode, idxListOpCode));
		getTabs().add(tab);

	}

	private BorderPane makeColumnsPane(UiOpCodeVo colOpCode, UiOpCodeVo colListOpCode) {

		colEditorPane = new EditorPane(colOpCode.getOpTypeText(), null, false);
		colEditorPane.setCallback(new DxCallback() {
			@Override
			public void onCallback(Map<String, Object> data) {
				colListPane.doSearch();
			}
		});
		colEditorPane.init(colOpCode);
		colListPane = new ListPaneBase();
		colListPane.init(colListOpCode);

		setContextMenu(colListPane, OP_NAME.EtcTableColumnDelete);

		BorderPane pane = new BorderPane();
		pane.setTop(colEditorPane);
		pane.setCenter(colListPane);

		return pane;
	}

	private BorderPane makeIndexPane(UiOpCodeVo idxOpCode, UiOpCodeVo idxListOpCode) {
		idxEditorPane = new EditorPane(idxOpCode.getOpTypeText(), null, false);
		idxEditorPane.setCallback(new DxCallback() {
			@Override
			public void onCallback(Map<String, Object> data) {
				idxListPane.doSearch();
			}
		});
		idxEditorPane.init(idxOpCode);
		idxListPane = new ListPaneBase();
		idxListPane.init(idxListOpCode);

		setContextMenu(idxListPane, OP_NAME.EtcTableIndexDelete);

		BorderPane pane = new BorderPane();
		pane.setTop(idxEditorPane);
		pane.setCenter(idxListPane);

		return pane;
	}

	private EditorPane makeTablePane(UiOpCodeVo tabOpCode) {
		tabEditorPane = new EditorPane(tabOpCode.getOpTypeText(), null, true);
		tabEditorPane.setCallback(new DxCallback() {
			@Override
			public void onCallback(Map<String, Object> data) {
				tabEditorPane.setDisable(true);
				showTableData(data);
			}
		});
		tabEditorPane.init(tabOpCode);
		tabEditorPane.setDisable(true);
		return tabEditorPane;
	}

	private void setContextMenu(ListPaneBase parent, OP_NAME opName) {
		FxDataMenuItem miDel = new FxDataMenuItem(parent, opName, parent.getSearchCallback());
		parent.getTableContextMenu().getItems().add(miDel);
	}

	private void showTableData(Map<String, Object> data) {
		colEditorPane.setDisable(false);
		colEditorPane.initData(data);
		colListPane.getDefaultData().put("tabName", data.get("tabName"));

		idxEditorPane.setDisable(false);
		idxEditorPane.initData(data);
		idxListPane.getDefaultData().put("tabName", data.get("tabName"));
	}

}
