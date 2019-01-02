package com.fxms.ui.bas.renderer.column;

import com.fxms.ui.css.image.ImagePointer;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

public class YnTableColumn extends TableColumn<Object[], ImageView> {

	public YnTableColumn(final int index, String text, int width) {

		super(text);

		setPrefWidth(width);
	
		setCellValueFactory(new Callback<CellDataFeatures<Object[], ImageView>, ObservableValue<ImageView>>() {
			public ObservableValue<ImageView> call(CellDataFeatures<Object[], ImageView> p) {
				Object value = p.getValue()[index];

				ImageView imageView;

				if ("y".equalsIgnoreCase(String.valueOf(value))) {
					imageView = new ImageView(ImagePointer.getImage("s16x16/check.png"));
				} else {
					imageView = new ImageView(ImagePointer.getImage("s16x16/uncheck.png"));
				}

				return new SimpleObjectProperty<ImageView>(imageView);
			}
		});

	
	}
}