package com.fxms.ui.biz.main;

import com.fxms.ui.bas.grid.GridCfg;
import com.fxms.ui.css.CssPointer;

import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MxMainRun {

	public void run(Stage stage) {


		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		GridCfg cfg = GridCfg.getGridCfg(primaryScreenBounds);
		cfg.gridLineVisible = false;
		cfg.hgap = 9;
		cfg.vgap = 9;

		if (stage == null) {
			stage = new Stage();
		}
		
		Scene scene = new Scene(new MxMainPane(stage, null, cfg), primaryScreenBounds.getWidth(),
				primaryScreenBounds.getHeight());


		// set Stage boundaries to visible bounds of the main screen
		stage.setX(primaryScreenBounds.getMinX());
		stage.setY(primaryScreenBounds.getMinY());
		stage.setWidth(primaryScreenBounds.getWidth());
		stage.setHeight(primaryScreenBounds.getHeight());
		
		scene.getStylesheets().add(CssPointer.getStyleSheet("fxms.css"));

		stage.setTitle("FxMS Management UI");
		stage.setScene(scene);
		stage.show();
	}

}
