package com.fxms.ui.node.diagram;

import java.util.HashMap;
import java.util.Map;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.UiCode;
import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.css.CssPointer;
import com.fxms.ui.dx.DxAsyncSelector;
import com.fxms.ui.node.diagram.vo.DiagMainVo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class TestFxDiagram extends Application {

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

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("diagNo", 50003);
		UiOpCodeVo opCode = CodeMap.getMap().getOpCode(OP_NAME.DiagramGet);
		Map<String, Object> retMap = DxAsyncSelector.getSelector().callMethod(null, opCode, parameters);

		DiagMainVo mainVo = new DiagMainVo();
		mainVo.setMap(retMap);

		FxDiagramEdit parent = new FxDiagramEdit();
		parent.setDiagMainVo(mainVo);
		parent.setPrefSize(800, 600);
		ScrollPane sp = new ScrollPane();
		sp.setContent(parent);
		sp.setFitToHeight(true);
		sp.setFitToWidth(true);
		sp.setContextMenu(parent.getContextMenu());

		try {

			Scene scene = new Scene(sp);
			scene.getStylesheets().add(CssPointer.getStyleSheet("fxms.css"));

			primaryStage.setTitle("Diagram " + mainVo.getDiagTitle());

			primaryStage.setScene(scene);

			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}