package com.fxms.ui.biz.menu.mng;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.bas.menu.FxMenuButton;
import com.fxms.ui.css.image.ImagePointer;
import com.fxms.ui.dx.item.chart.ViewChartPane;

import javafx.scene.Parent;
import javafx.scene.image.ImageView;

public class ViewPerfButton extends FxMenuButton {

	public ViewPerfButton() {
		getCenterButton().setGraphic(new ImageView(ImagePointer.getImage("s48x48/line-chart.png")));
	}

	@Override
	protected OP_NAME getContentOpName() {
		return OP_NAME.PsView;
	}

	@Override
	protected Parent makeScreen(UiOpCodeVo op) {
		ViewChartPane pane =  new ViewChartPane();
		pane.setPrefSize(op.getUiWidth(), op.getUiHeight());		
		return pane;
	}
}