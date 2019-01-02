package com.fxms.ui.bas.renderer.column;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class NoTableColumn extends TableColumn<Object[], Number> {

	
	public NoTableColumn(final int index, String text, int width) {
		
		super(text);
		
		setPrefWidth(width);

		setStyle("-fx-alignment: center-right;");

		setCellValueFactory(new Callback<CellDataFeatures<Object[], Number>, ObservableValue<Number>>() {
			public ObservableValue<Number> call(CellDataFeatures<Object[], Number> p) {
				Object value = p.getValue()[index];
				if ( value == null) {
					return new SimpleLongProperty(0);
				}
				
				long v = 0;
				try {
					v = Double.valueOf(value.toString()).longValue();
				} catch (Exception e) {
				}
				
				return new SimpleLongProperty(v);

			}
		});
	}
}
