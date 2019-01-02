package com.fxms.ui.biz.main;

import com.fxms.ui.UiCode;
import com.fxms.ui.dx.DxAsyncSelector;

import javafx.application.Application;
import javafx.stage.Stage;

public class DxMain extends Application {

	public static Stage primaryStage;
	public static DxMain fxMain;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {

		try {
			DxAsyncSelector.getSelector().login("SYSTEM", "SYSTEM");
		} catch (Exception e1) {
			e1.printStackTrace();
			System.exit(0);
		}

		UiCode.reload(null);

		DxMain.primaryStage = primaryStage;
		DxMain.fxMain = this;

		try {
			new DxMainRun().run(primaryStage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}