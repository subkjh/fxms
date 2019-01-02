package com.fxms.ui.bas.pane;

import java.util.Map;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.bas.pane.list.ListPaneBase;
import com.fxms.ui.bas.property.FxUi;
import com.fxms.ui.biz.action.FxDataMenuItem;
import com.fxms.ui.dx.DxCallback;

import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;

public class AlarmCfgPane extends BorderPane implements FxUi {

	private EditorPane cfg;
	private EditorPane mem;
	private ListPaneBase list;
	private UiOpCodeVo opCode;

	public AlarmCfgPane() {

	}

	@Override
	public UiOpCodeVo getOpCode() {
		return opCode;
	}

	@Override
	public void init(UiOpCodeVo opcode) {

		this.opCode = opcode;

		UiOpCodeVo memAddOpCode = CodeMap.getMap().getOpCode(OP_NAME.AlarmCfgMemAdd);

		mem = makeAlarmMemEditor(memAddOpCode);
		list = makeAlarmMemList();

		mem.setDisable(opcode.getOpName().equals(OP_NAME.AlarmCfgInfoAdd.getOpName()));
		list.setDisable(opcode.getOpName().equals(OP_NAME.AlarmCfgInfoAdd.getOpName()));

		if (opcode.getOpName().equals(OP_NAME.AlarmCfgAdd.getOpName())) {
			cfg = makeAlarmCfgInfo(CodeMap.getMap().getOpCode(OP_NAME.AlarmCfgInfoAdd));
		} else {
			cfg = makeAlarmCfgInfo(CodeMap.getMap().getOpCode(OP_NAME.AlarmCfgInfoUpdate));
		}
		
		list.setPadding(new Insets(10, 10, 10, 10));

		setTop(cfg);
		setLeft(mem);
		setCenter(list);

	}

	@Override
	public void initData(Map<String, Object> data) {

		cfg.initData(data);

		if (data != null && data.size() > 0) {
			mem.getDefaultData().put("alarmCfgNo", data.get("alarmCfgNo"));
			list.getDefaultData().put("alarmCfgNo", data.get("alarmCfgNo"));
			list.doSearch();
		} else {
			mem.getDefaultData().clear();
			list.getDefaultData().clear();
		}

	}

	private EditorPane makeAlarmCfgInfo(UiOpCodeVo opcode) {
		EditorPane node = new EditorPane(opcode.getOpTypeText(), null, true);

		node.setCallback(new DxCallback() {
			@Override
			public void onCallback(Map<String, Object> data) {
				cfg.setDisable(true);
				mem.setDisable(false);
				mem.getDefaultData().put("alarmCfgNo", data.get("alarmCfgNo"));
			}
		});

		node.init(opcode);
		return node;
	}

	private EditorPane makeAlarmMemEditor(UiOpCodeVo opcode) {
		EditorPane mem = new EditorPane(opcode.getOpTypeText(), null, true);
		mem.setCallback(new DxCallback() {
			@Override
			public void onCallback(Map<String, Object> data) {
				list.getDefaultData().put("alarmCfgNo", data.get("alarmCfgNo"));
				list.doSearch();
			}
		});
		mem.init(opcode);
		mem.setPrefWidth(350);

		return mem;
	}

	private ListPaneBase makeAlarmMemList() {
		ListPaneBase list = new ListPaneBase();
		list.init(CodeMap.getMap().getOpCode(OP_NAME.AlarmCfgMemList));
		list.getTableContextMenu().getItems()
				.add(new FxDataMenuItem(list, OP_NAME.AlarmCfgMemDelete, list.getSearchCallback()));
		list.setPrefWidth(800);
		return list;
	}

}
