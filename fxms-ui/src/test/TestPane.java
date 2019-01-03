package test;

import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.css.CssPointer;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

public class TestPane extends Application {

	private double width = 800;
	private double height = 600;

	public static void main(String[] args) {
//		Font font = Font.font("맑은고딕", FontWeight.BOLD, 12);
//		System.out.println(Font.getFontNames());
//		System.out.println(Font.getFamilies());
//		System.out.println(Font.font("Malgun Gothic", FontWeight.EXTRA_BOLD, 12).toString());
		System.out.println(FontWeight.valueOf("Bold"));
		System.out.println(FontWeight.valueOf("Regular"));


		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {

		// this.initialize();
		//
		// CdOp op = CodeMap.getMap().getCdOp("list-alarm-hst");
		// Parent parent = new SearchPane(op, null);
		// Parent parent = new ListPane(op, true, null);
		// Parent parent = new ViewChartPane();
		// Parent parent = new CounterPane("Major", 100);
		// Pane parent = new ViewPsPane();
		// ((ViewPsPane) parent).set(DxAsyncSelector.getSelector().getMo(1000136),
		// CodeMap.getMap().getPsItem("TEMP"));
		// window title 제거
		// primaryStage.initStyle(StageStyle.UNDECORATED);w

		Scale scale = new Scale();
		ScrollPane sp = new ScrollPane();
		sp.setFitToHeight(true);
		sp.setFitToWidth(true);
		VBox parent = new VBox(10);

		sp.setContent(parent);

		parent.getTransforms().add(scale);

		parent.getChildren().add(new TextField("Thingspire Text 1"));
		parent.getChildren().add(new TextField("Thingspire Text 2"));

		try {

			Scene scene = new Scene(sp);
			scene.getStylesheets().add(CssPointer.class.getResource("fxms.css").toExternalForm());

			primaryStage.setTitle("TEST");
			primaryStage.setScene(scene);

			primaryStage.show();

			width = scene.getWidth();
			height = scene.getHeight();

			double translateX = parent.getTranslateX();
			double translateY = parent.getTranslateY();
			double baseX = parent.getBoundsInParent().getMinX();
			double baseY = parent.getBoundsInParent().getMinY();

			System.out.println(width + ", " + height);
			System.out.println("base : " + baseX + ", " + baseY);
			System.out.println("base : " + translateX + ", " + translateY);
			System.out.println(parent.getLayoutBounds().getWidth() + ", " + parent.getLayoutBounds().getHeight());

			parent.widthProperty().addListener(new ChangeListener<Number>() {
				@Override
				public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth,
						Number newSceneWidth) {
					System.out.println("width: " + width + "-> " + newSceneWidth + ", "
							+ (newSceneWidth.doubleValue() / width) + ", " + parent.getLayoutX());

					// parent.setScaleX(newSceneWidth.doubleValue() / ( 1 * width ) );

					double scaleX = newSceneWidth.doubleValue() / width;
					scale.setX(scaleX);
					// parent.setScaleX(scaleX);
					// for ( Node node : parent.getChildren()) {
					// node.setScaleX(scaleX);
					// }

					System.out.println(baseX + ", " + translateX + ", "
							+ (translateX + (baseX * newSceneWidth.doubleValue() / width) - baseX));

					// parent.setScaleShape(true);
					// parent.setTranslateX(100);
					// parent.setTranslateX(translateX + (baseX * newSceneWidth.doubleValue() /
					// width) - baseX);
				}
			});
			parent.heightProperty().addListener(new ChangeListener<Number>() {
				@Override
				public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight,
						Number newSceneHeight) {
					System.out.println("height: " + newSceneHeight + ", " + (newSceneHeight.doubleValue() / height)
							+ ", " + parent.getLayoutX());

					double scaleY = newSceneHeight.doubleValue() / height;
					scale.setY(scaleY);

					// parent.setScaleY(scaleY);
					// parent.setTranslateY(translateY + (baseY * newSceneHeight.doubleValue() /
					// height) - baseY);

				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	synchronized void initialize() {
		CodeMap.getMap().reload(null);

		while (CodeMap.getMap().isLoaded() == false) {
			Thread.yield();
		}

	}
}