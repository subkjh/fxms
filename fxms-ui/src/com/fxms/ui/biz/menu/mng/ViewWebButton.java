package com.fxms.ui.biz.menu.mng;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.bas.menu.FxMenuButton;
import com.fxms.ui.css.image.ImagePointer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class ViewWebButton extends FxMenuButton {

	public ViewWebButton() {
		getCenterButton().setGraphic(new ImageView(ImagePointer.getImage("s64x64/analyze.png")));
	}

	@Override
	protected OP_NAME getContentOpName() {
		return OP_NAME.EtcWebOpen;
	}

	@Override
	protected Parent makeScreen(UiOpCodeVo op) {
		final WebView browser = new WebView();
		final WebEngine webEngine = browser.getEngine();

		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setContent(browser);
		scrollPane.setFitToHeight(true);
		scrollPane.setFitToWidth(true);

		webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
			@Override
			public void changed(ObservableValue ov, State oldState, State newState) {

				if (newState == Worker.State.SUCCEEDED) {
					// stage.setTitle(webEngine.getLocation());
				}

			}
		});
		// webEngine.load("http://java2s.com");
		webEngine.load("file:///C:/Users/SUBKJH-DEV/Downloads/Highcharts-6.1.0/examples/synchronized-charts/index.htm");
		return scrollPane;
	}

}
