package com.fxms.ui.node.status;

import com.fxms.ui.bas.property.DxNode;
import com.fxms.ui.bas.vo.ui.UiBasicVo;
import com.fxms.ui.css.CssPointer;

import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class ClockHtml extends BorderPane implements DxNode {

	public ClockHtml() {
		
		final WebView browser = new WebView();
		final WebEngine webEngine = browser.getEngine();
		webEngine.load(CssPointer.class.getResource("clock.html").toExternalForm());

		// setOpacity(0);
//		setPadding(Insets.EMPTY);
//		setBorder(Border.EMPTY);
		setCenter(browser);
	}

	@Override
	public void onAddedInParent() {

	}

	@Override
	public void onRemovedFromParent() {

	}

	@Override
	public boolean initDxNode(UiBasicVo vo) {
		return true;
	}

}
