package com.fxms.ui.bas.pane;

import java.util.List;
import java.util.Map;

import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.bas.property.FxEditorNode;
import com.fxms.ui.bas.property.FxUi;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class EditorTabPane extends TabPane implements FxEditorNode, FxUi  {

	private EditData editorBox;
	private UiOpCodeVo opCode;

	public EditorTabPane() {
		setMinSize(300, 200);
	}

	@Override
	public Map<String, Object> getInputData() {
		return editorBox.getInputData(EditorTabPane.this);
	}

	@Override
	public UiOpCodeVo getOpCode() {
		return opCode;
	}

	@Override
	public Map<String, Object> getDefaultData() {
		return editorBox.getParameters();
	}

	@Override
	public void init(UiOpCodeVo opcode) {

		this.opCode = opcode;
		editorBox = new EditData(opcode);

		getTabs().clear();

		if (opcode != null) {
			List<String> groupList = opcode.getAttrGroupList();
			for (String group : groupList) {
				showGroup(group);
			}
		}
	}

	@Override
	public void initData(Map<String, Object> data) {

		System.out.println(getClass().getSimpleName() + " :: " + data);
		editorBox.initEditor(data);
	}

	protected void showGroup(String groupName) {

		Tab tab = new Tab();
		tab.setClosable(false);
		tab.setText(groupName);
		tab.setContent(editorBox.makePane(groupName, false));
		getTabs().add(tab);

	}

}
