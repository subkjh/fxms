package com.fxms.ui.node.diagram;

import com.fxms.ui.css.CssPointer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class TestDxDiagram extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {

//		try {
//			DxAsyncSelector.getSelector().setServer("125.7.128.42", 10005);
//			DxAsyncSelector.getSelector().login("SYSTEM", "SYSTEM");
//		} catch (Exception e1) {
//			e1.printStackTrace();
//			System.exit(0);
//		}

//		UiCode.reload(null);
		
		DxDiagram parent = new DxDiagram();
		parent.setDiagNo(50003);
		parent.setPrefSize(800, 600);
		parent.onAddedInParent();

		ScrollPane sp = new ScrollPane();
		sp.setContent(parent);
		sp.setFitToHeight(true);
		sp.setFitToWidth(true);
		sp.setContextMenu(parent.getContextMenu());

		try {

			Scene scene = new Scene(sp);
			scene.getStylesheets().add(CssPointer.getStyleSheet("fxms.css"));

			primaryStage.setTitle("Diagram " + parent.getDiagMainVo().getDiagTitle());

			primaryStage.setScene(scene);

			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}