package com.fxms.ui.bas.renderer.column;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class DateTableColumn extends TableColumn<Object[], String> {

	public DateTableColumn(final int index, String text, int width) {

		super(text);

		setPrefWidth(width);

		setStyle("-fx-alignment: CENTER;");

		setCellValueFactory(new Callback<CellDataFeatures<Object[], String>, ObservableValue<String>>() {

			public ObservableValue<String> call(CellDataFeatures<Object[], String> p) {
				Object value = p.getValue()[index];

				String ret = "";

				if (value instanceof Number) {
					ret = makeDate(String.valueOf(((Number) value).longValue()));
				} else if (value != null) {
					ret = makeDate(value.toString());
				}

				return new SimpleStringProperty(ret);
			}
		});
	}

	private String makeDate(String s) {

		if (s == null || s.length() < 8) {
			return "";
		}

		StringBuffer sb = new StringBuffer();
		for (char ch : s.toCharArray()) {
			if (sb.length() == 4 || sb.length() == 7) {
				sb.append("-");
			} else if (sb.length() == 10) {
				sb.append(" ");
			} else if (sb.length() == 13 || sb.length() == 16) {
				sb.append(":");
			}

			sb.append(ch);
		}

		return sb.toString();
	}
}