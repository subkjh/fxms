package com.fxms.ui.bas.renderer.column;

import com.fxms.ui.UiCode;

import javafx.scene.control.TableCell;

public class AlarmLevelTableColumn extends NoTableColumn {

	public AlarmLevelTableColumn(final int index, String text, int width) {

		super(index, text, width);

		setCellFactory(e -> new TableCell<Object[], Number>() {
			@Override
			public void updateItem(Number item, boolean empty) {

				super.updateItem(item, empty);

				if (item == null || empty) {

					setText(null);

				} else {

					setText(UiCode.alarmName[item.intValue()]);

					// if (getStyleClass().contains("alarm-level-" + item) == false) {
					// getStyleClass().add("alarm-level-" + item);
					// }

					if (item.intValue() == 1) {
						this.setStyle("-fx-background-color: #f26a6a; -fx-alignment: center;");
					} else if (item.intValue() == 2) {
						this.setStyle("-fx-background-color: #f2c16a; -fx-alignment: center; -fx-text-fill: #215968;");
					} else if (item.intValue() == 3) {
						this.setStyle("-fx-background-color: #f9f5ad; -fx-alignment: center; -fx-text-fill: #8081B6;");
					} else if (item.intValue() == 4) {
						this.setStyle("-fx-background-color: #d9f9b9; -fx-alignment: center;");
					}

				}
			}
		});

	}
}