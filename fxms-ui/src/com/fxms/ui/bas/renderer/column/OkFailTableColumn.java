package com.fxms.ui.bas.renderer.column;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class OkFailTableColumn extends TableColumn<Object[], String> {

	public OkFailTableColumn(final int index, String text, int width) {

		super(text);

		setPrefWidth(width);

		setStyle("-fx-alignment: CENTER;");

		setCellValueFactory(new Callback<CellDataFeatures<Object[], String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<Object[], String> p) {
				Object value = p.getValue()[index];
				if (value == null) {
					return new SimpleStringProperty("");
				}

				return new SimpleStringProperty(value == null ? "" : String.valueOf(value));
			}
		});

		setCellFactory(e -> new TableCell<Object[], String>() {
			@Override
			public void updateItem(String item, boolean empty) {

				super.updateItem(item, empty);

				if (item == null || empty) {

					setText(null);

				} else {

					setText(item);
					
					getStyleClass().clear();
					getStyleClass().add("work-result-" + item.toLowerCase());
//
//					String color = "ok".equalsIgnoreCase(item) ? "steelblue;" : "salmon;";
//					this.setStyle("-fx-text-fill:" + color);
				}
			}
		});
	}
}
