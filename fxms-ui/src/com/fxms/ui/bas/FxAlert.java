package com.fxms.ui.bas;

import com.fxms.ui.bas.lang.Lang;
import com.fxms.ui.bas.window.FxWindowTitle;
import com.fxms.ui.css.CssPointer;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class FxAlert extends StackPane {

	class StageEvent implements EventHandler<MouseEvent> {

		private double x;
		private double y;
		private double windowX;
		private double windowY;

		public StageEvent() {
		}

		public void addEvent(Node node) {
			node.addEventHandler(MouseEvent.MOUSE_DRAGGED, this);
			node.addEventHandler(MouseEvent.MOUSE_PRESSED, this);
		}

		@Override
		public void handle(MouseEvent e) {

			Label label = (Label) e.getSource();
			Window window = label.getScene().getWindow();

			if (e.getEventType() == MouseEvent.MOUSE_PRESSED && e.getButton() == MouseButton.PRIMARY) {
				x = e.getScreenX();
				y = e.getScreenY();
				windowX = window.getX();
				windowY = window.getY();
			} else if (e.getEventType() == MouseEvent.MOUSE_DRAGGED && e.getButton() == MouseButton.PRIMARY) {
				window.setX(windowX + (e.getScreenX() - x));
				window.setY(windowY + (e.getScreenY() - y));
			}
		}
	}

	public static void showAlert(FxAlertType alertType, Node ownerPane, String title, String... msg) {
		FxAlert alert = new FxAlert(alertType, title, msg);
		if (ownerPane.getScene() != null) {
			alert.show(ownerPane.getScene().getWindow(), Modality.WINDOW_MODAL);
		} else {
			alert.show(null, Modality.WINDOW_MODAL);
		}
	}

	public static void showAlert(FxAlertType alertType, Window parent, String title, String... msg) {
		FxAlert alert = new FxAlert(alertType, title, msg);
		alert.show(parent, Modality.WINDOW_MODAL);
	}

	private Label alertContext = new Label();
	private BorderPane pane = new BorderPane();
	private final Border contextBorder = new Border(
			new BorderStroke(Color.rgb(4, 67, 144), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3)));
	private final Border outBorder = new Border(
			new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1)));

	public static enum FxAlertType {
		ok, error;
	}

	private FxAlert(FxAlertType alertTtype, String title, String... msg) {

		StringBuffer sb = new StringBuffer();
		for (String s : msg) {
			if (sb.length() > 0) {
				sb.append("\n");
			}
			sb.append(s);
		}

		alertContext.setText(sb.toString());
		alertContext.setWrapText(true);
		alertContext.setMinSize(200, 100);
		alertContext.getStyleClass().add("fx-window-alert-" + alertTtype.name().toLowerCase());

		pane.setTop(makeTop(title));
		pane.setCenter(alertContext);
		pane.setBottom(makeBottom());
		pane.setPadding(new Insets(0, 0, 0, 0));
		pane.setBorder(contextBorder);

		getChildren().add(pane);
		setBorder(outBorder);
		setPadding(new Insets(0, 0, 0, 0));

	}

	private Node makeBottom() {

		HBox hBox = new HBox(8);
		hBox.setAlignment(Pos.CENTER_RIGHT);
		hBox.setPadding(new Insets(8, 8, 8, 8));

		Button cancelButton = new Button(Lang.getText(Lang.Type.button, "Close"));
		cancelButton.getStyleClass().add(CssPointer.FxWindowButton);
		cancelButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				cancelButton.getScene().getWindow().hide();
			}
		});

		hBox.getChildren().add(cancelButton);

		return hBox;
	}

	private Node makeTop(String title) {
		FxWindowTitle windowTitle = new FxWindowTitle(title);
		return windowTitle;
	}

	private void show(Window parent, Modality modality) {

		Stage stage = new Stage();
		stage.initModality(modality);
		stage.initStyle(StageStyle.TRANSPARENT);

		if (parent != null) {
			stage.initOwner(parent);
		}

		Scene scene = new Scene(this);
		scene.getStylesheets().add(CssPointer.getStyleSheet("fxms.css"));
		stage.setScene(scene);

		stage.showAndWait();
		stage.toFront();

	}

}
