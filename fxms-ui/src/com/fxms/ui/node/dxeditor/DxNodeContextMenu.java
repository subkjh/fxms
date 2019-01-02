package com.fxms.ui.node.dxeditor;

import com.fxms.ui.bas.FxWindow;
import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.bas.lang.Lang;
import com.fxms.ui.bas.vo.ui.UiBasicVo;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class DxNodeContextMenu extends ContextMenu {

	private UiBasicVo uiBasicVo;
	private MenuItem openNewMi;

	public DxNodeContextMenu() {

		openNewMi = new MenuItem(Lang.getText(Lang.Type.menu, "Open New"));
		openNewMi.setOnAction((ActionEvent e) -> {
			Node node = null;
			try {
				node = uiBasicVo.makeNode();
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			if (node instanceof Parent) {
				UiOpCodeVo opcode = CodeMap.getMap().getOpCode(uiBasicVo.getOpNo());
				FxWindow.showStage(getOwnerNode(), (Parent) node, opcode.getOpTitle());
			}
		});

		getItems().add(openNewMi);
	}

	public void setUiBasicVo(UiBasicVo uiBasicVo) {
		this.uiBasicVo = uiBasicVo;
	}
}
