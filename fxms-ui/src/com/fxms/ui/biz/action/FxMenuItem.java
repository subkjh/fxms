package com.fxms.ui.biz.action;

import java.util.HashMap;
import java.util.Map;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.bas.code.UiOpCodeVo;

import FX.MS.UI;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.stage.Window;

public abstract class FxMenuItem extends MenuItem {

	private UiOpCodeVo opcode;
	private Node ownerPane;
	private Map<String, Object> parameters;

	public FxMenuItem(OP_NAME name) {

		this(CodeMap.getMap().getOpCode(name));

		if (opcode == null) {
			setText(name.getOpName());
		}
	}

	public FxMenuItem(UiOpCodeVo opcode) {
		
		this.opcode = opcode;

		if (opcode == null) {
			this.setDisable(true);
			return;
		}

		setText(opcode.getOpTitle());

		setOnAction((ActionEvent e) -> {
			onAction();
		});
	}

	public UiOpCodeVo getOpCode() {
		return opcode;
	}

	public Node getOwnerPane() {
		return ownerPane;
	}

	public Map<String, Object> getParameters() {
		if (parameters == null) {
			parameters = new HashMap<String, Object>();
		}
		return parameters;
	}

	public void setOpCode(UiOpCodeVo opcode) {
		this.opcode = opcode;
		this.setDisable(opcode == null);
		setText(opcode == null ? "--------------" : opcode.getOpTitle());
	}

	protected long getLong(Map<String, Object> parameters, String name, long defVal) {

		Object value = parameters.get(name);
		if (value == null) {
			return defVal;
		}

		if (value instanceof Number) {
			return ((Number) value).longValue();
		}

		try {
			return Double.valueOf(value.toString()).longValue();
		} catch (Exception e) {
			e.printStackTrace();
			return defVal;
		}

	}

	protected Window getWindow() {
		return ownerPane != null ? ownerPane.getScene().getWindow() : UI.primaryStage;
	}

	protected abstract void onAction();

	protected void setOwnerPane(Node parent) {
		this.ownerPane = parent;
	}
}
