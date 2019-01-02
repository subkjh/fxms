package com.fxms.ui.biz.main;

import com.fxms.ui.UiCode;
import com.fxms.ui.dx.DxAsyncSelector;

import javafx.application.Application;
import javafx.stage.Stage;

public class MxMain extends Application {

	public static Stage primaryStage;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {

		MxMain.primaryStage = primaryStage;

		try {
			DxAsyncSelector.getSelector().login("SYSTEM", "SYSTEM");
		} catch (Exception e1) {
			e1.printStackTrace();
			System.exit(0);
		}

		UiCode.reload(null);

		// window title 제거
		// primaryStage.initStyle(StageStyle.UNDECORATED);

		try {
			new MxMainRun().run(primaryStage);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
