package com.fxms.ui.bas.renderer.column;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class StringTableColumn extends TableColumn<Object[], String> {

	public StringTableColumn(final int index, String text, int width) {

		super(text);

		setPrefWidth(width);

		setCellValueFactory(new Callback<CellDataFeatures<Object[], String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<Object[], String> p) {
				Object value = p.getValue()[index];
				return new SimpleStringProperty(value == null ? "" : String.valueOf(value));
			}
		});
	}
}
