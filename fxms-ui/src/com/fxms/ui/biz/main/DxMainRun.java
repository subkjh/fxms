package com.fxms.ui.biz.main;

import com.fxms.ui.bas.grid.GridCfg;
import com.fxms.ui.biz.menu.DxContextMenu;
import com.fxms.ui.css.CssPointer;
import com.fxms.ui.dx.DxAlarmReceiver;

import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class DxMainRun {

	public void run(Stage stage) {

		if (DxMainPane.dxStage != null) {
			if (DxMainPane.dxStage.isShowing()) {
				DxMainPane.dxStage.toFront();
			} else {
				DxMainPane.dxStage.show();
			}
			return;
		}

		DxMainPane.dxStage = stage;

		Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
		GridCfg cfg = GridCfg.getGridCfg(bounds);
		cfg.gridLineVisible = false;
		cfg.hgap = 9;
		cfg.vgap = 9;

		if (stage == null) {
			stage = new Stage();
		}

		DxAlarmReceiver.getBorader().reload();

		DxMainPane pane = new DxMainPane(stage, null, cfg);

		Scene scene = new Scene(pane, bounds.getWidth(), bounds.getHeight());
		scene.getStylesheets().add(CssPointer.getStyleSheet("fxms.css"));

		stage.setX(bounds.getMinX());
		stage.setY(bounds.getMinY());
		stage.setWidth(bounds.getWidth());
		stage.setHeight(bounds.getHeight());

		stage.setTitle("Fxms Dashboard");
		stage.setScene(scene);
		stage.show();

		new DxContextMenu(pane).addMenu(stage);

	}
}
