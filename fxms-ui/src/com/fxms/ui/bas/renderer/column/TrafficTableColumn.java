package com.fxms.ui.bas.renderer.column;

import com.fxms.ui.bas.code.UiPsItemVo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class TrafficTableColumn extends TableColumn<Object[], String> {

	public TrafficTableColumn(final int index, String text, int width) {

		super(text);

		setPrefWidth(width);

		setStyle("-fx-alignment: center-right;");

		setCellFactory(e -> new TableCell<Object[], String>() {
			@Override
			public void updateItem(String item, boolean empty) {

				super.updateItem(item, empty);

				if (item == null || empty) {

					setText(null);

				} else {

					setText(item);

					if (item.contains("M")) {
						this.setStyle("-fx-background-color:lightgreen; -fx-alignment: center-right;");
					} else if (item.contains("G")) {
						this.setStyle("-fx-background-color:mediumseagreen; -fx-alignment: center-right;");
					} else if (item.contains("T")) {
						this.setStyle("-fx-background-color:green; -fx-alignment: center-right;");
					} else {
						this.setStyle("-fx-alignment: center-right;");
					}

				}
			}
		});

		setCellValueFactory(new Callback<CellDataFeatures<Object[], String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<Object[], String> p) {
				Object value = p.getValue()[index];
				String str = (value == null ? "" : UiPsItemVo.makeAutoUnit(Double.valueOf(value.toString()), 1));
				return new SimpleStringProperty(str);
			}
		});
	}
}
