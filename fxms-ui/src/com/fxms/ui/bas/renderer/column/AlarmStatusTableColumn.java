package com.fxms.ui.bas.renderer.column;

import com.fxms.ui.css.image.ImagePointer;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

public class AlarmStatusTableColumn extends TableColumn<Object[], ImageView> {

	public AlarmStatusTableColumn(final int index, String text, int width) {

		super(text);

		setPrefWidth(width);

		setCellValueFactory(new Callback<CellDataFeatures<Object[], ImageView>, ObservableValue<ImageView>>() {
			public ObservableValue<ImageView> call(CellDataFeatures<Object[], ImageView> p) {
				Object value = p.getValue()[index];

				ImageView imageView;

				if ("y".equalsIgnoreCase(String.valueOf(value))) {
					imageView = new ImageView(new Image(ImagePointer.class.getResourceAsStream("s16x16/alarm-acked.png")));
				} else {
					imageView = new ImageView(new Image(ImagePointer.class.getResourceAsStream("s16x16/alarm-on.png")));
				}

				return new SimpleObjectProperty<ImageView>(imageView);
			}
		});

		// setCellFactory(e -> new TableCell<Object[], String>() {
		// @Override
		// public void updateItem(String item, boolean empty) {
		//
		// super.updateItem(item, empty);
		//
		// if (item == null || empty) {
		//
		// setText(null);
		//
		// } else {
		//
		// setText("");
		//
		//// if (getStyleClass().contains("alarm-level-" + item) == false) {
		//// getStyleClass().add("alarm-level-" + item);
		//// }
		//
		// this.setStyle("-fx-background-image: url(\"image/alarm-on.png\");");
		//
		// }
		// }
		// });
	}
}