package FX.MS;

import java.util.Map;

import com.fxms.ui.bas.FxDialog;
import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.bas.lang.Lang;
import com.fxms.ui.bas.property.FxUi;
import com.fxms.ui.css.CssPointer;
import com.fxms.ui.node.dxeditor.DxEditor;
import com.fxms.ui.node.dxeditor.DxPane;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class FxMain implements FxUi {
	
	public FxMain(){
		
	}

	private void showMain(DxPane node) {

		UI.dxPane = node;

		try {
			Rectangle2D bounds = Screen.getPrimary().getVisualBounds();

			Scene scene = new Scene(node);
			scene.getStylesheets().add(CssPointer.getStyleSheet("fxms.css"));
			scene.getStylesheets().add(CssPointer.getStyleSheet("dx-context-menu.css"));

			// node.getMainPane().prefHeightProperty().bind(scene.heightProperty());
			// node.getMainPane().prefWidthProperty().bind(scene.widthProperty());

			double width = bounds.getWidth() - 4;
			double height = bounds.getHeight() - 4;

			// node.getMainPane().setPrefWidth(width);
			// node.getMainPane().setPrefHeight(height);

			UI.primaryStage.initStyle(StageStyle.DECORATED);
			UI.primaryStage.setFullScreen(UiData.getConfig(UiData.fullscreen, "N").equalsIgnoreCase("y"));
			UI.primaryStage.setFullScreenExitHint("");
			UI.primaryStage.setX(0);
			UI.primaryStage.setY(0);
			UI.primaryStage.setWidth(width);
			UI.primaryStage.setHeight(height);
			UI.primaryStage.setTitle("Fx Management System");
			UI.primaryStage.setScene(scene);
			UI.primaryStage.show();

			UI.primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				public void handle(WindowEvent we) {
					System.exit(0);
				}
			});

			UI.primaryStage.widthProperty().addListener(new ChangeListener<Number>() {
				@Override
				public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth,
						Number newSceneWidth) {
					// node.getMainPane().setPrefWidth(newSceneWidth.doubleValue());
					double scaleX = newSceneWidth.doubleValue() / width;
					if (scaleX > 1) {
						node.getMainPane().setScaleX(1);
					} else {
						node.getMainPane().setScaleX(scaleX);
					}
				}
			});
			UI.primaryStage.heightProperty().addListener(new ChangeListener<Number>() {
				@Override
				public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight,
						Number newSceneHeight) {
					// node.getMainPane().setPrefHeight(newSceneHeight.doubleValue());

					double scaleY = newSceneHeight.doubleValue() / height;
					if (scaleY > 1) {
						node.getMainPane().setScaleY(1);
					} else {
						node.getMainPane().setScaleY(scaleY);
					}
				}
			});

			// primaryStage.setHeight(300);
			// primaryStage.setWidth(1.6181 * 300);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public UiOpCodeVo getOpCode() {
		return null;
	}

	@Override
	public void init(UiOpCodeVo opcode) {
	}

	@Override
	public void initData(Map<String, Object> data) {
		
		DxPane pane = null;

		if (CodeMap.getMap().getUiList().size() == 0) {
			FxDialog.showDialog(null, new DxEditor(),
					Lang.getText(Lang.Type.msg, "생성된 화면이 존재하지 않습니다. 먼저 화면을 생성하기 바랍니다."));
			if (CodeMap.getMap().getUiList().size() > 0) {
				pane = new DxPane(DxPane.FIRST_GROUP);
			}
		} else {
			int uiGroupNo = CodeMap.getMap().getUserProperty(CodeMap.USER_PROPERTY_DX_INDEX, DxPane.FIRST_GROUP);
			pane = new DxPane(uiGroupNo);
		}

		if (pane != null) {
			showMain(pane);
		}
		
	}

}
