package com.fxms.ui.node.diagram.menuitem;

import java.util.Map;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.dx.DxCallback;
import com.fxms.ui.node.diagram.FxDiagram;
import com.fxms.ui.node.diagram.node.DiagNode;
import com.fxms.ui.node.diagram.vo.DiagNodeVo;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

public class DiagMenuItem extends Menu {

	private DiagNode node;
	private OP_NAME opName = null;
	private FxDiagram diagram;

	public DiagMenuItem(FxDiagram diagram) {

		super("Item");

		this.diagram = diagram;

		getItems().add(getEditMenuItem());

		getItems().add(new SeparatorMenuItem());

		MenuItem removeMi = new MenuItem("Remove");

		removeMi.setOnAction((ActionEvent e) -> {
			diagram.getChildren().remove((Node) node);
		});

		MenuItem copyMi = new MenuItem("Copy");

		copyMi.setOnAction((ActionEvent e) -> {
			DiagNodeVo newVo = (DiagNodeVo) node.getDiagNodeVo().clone();
			if (newVo != null) {
				newVo.setDiagNodeX(newVo.getDiagNodeX() + 16);
				newVo.setDiagNodeY(newVo.getDiagNodeY() + 16);
			}
			diagram.addDiagNode(newVo);
		});

		getItems().add(removeMi);
		getItems().add(copyMi);

	}

	public void setNode(DiagNode node) {

		this.node = node;
		opName = node.getOpName();
		
		if (opName != null) {
			setText(opName.getOpName());
		} else {
			setText("");
		}

	}

	private MenuItem getEditMenuItem() {
		MenuItem menu = new MenuItem("Edit Properties");
		menu.setOnAction((ActionEvent e) -> {
			UiOpCodeVo opcode = CodeMap.getMap().getOpCode(opName);
			if (opcode != null) {
				opcode.showDialog(diagram, node.getDiagNodeVo().getProperties(), null, new DxCallback() {
					@Override
					public void onCallback(Map<String, Object> data) {
						node.setProperties(data);
					}
				});

			}
		});

		return menu;
	}
}