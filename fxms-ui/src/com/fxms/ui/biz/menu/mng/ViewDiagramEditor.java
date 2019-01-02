package com.fxms.ui.biz.menu.mng;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.bas.menu.FxMenuButton;
import com.fxms.ui.css.image.ImagePointer;
import com.fxms.ui.node.diagram.FxDiagramEdit;

import javafx.scene.Parent;
import javafx.scene.image.ImageView;

public class ViewDiagramEditor extends FxMenuButton {

	public ViewDiagramEditor() {
		getCenterButton().setGraphic(new ImageView(ImagePointer.getImage("s48x48/line-chart.png")));
	}

	@Override
	protected OP_NAME getContentOpName() {
		return OP_NAME.DiagramEdit;
	}

	@Override
	protected Parent makeScreen(UiOpCodeVo op) {
		return new FxDiagramEdit();
	}
}
