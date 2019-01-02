package com.fxms.ui.biz.menu.mng;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.bas.menu.FxMenuButton;
import com.fxms.ui.node.tree.FxTreePane;
import com.fxms.ui.node.tree.vo.TreeItemVo;

import javafx.scene.Parent;
import javafx.stage.Screen;

public class ViewUserTreeButton extends FxMenuButton {

	@Override
	protected OP_NAME getContentOpName() {
		return OP_NAME.Tree;
	}

	@Override
	protected Parent makeScreen(UiOpCodeVo op) {

		FxTreePane pane = new FxTreePane(null) {
			@Override
			protected void onSelected(TreeItemVo treeItemVo) {
				// nothing
			}
		};

		pane.setPrefSize(360, Screen.getPrimary().getVisualBounds().getHeight() - 200);

		return pane;
	}

}
