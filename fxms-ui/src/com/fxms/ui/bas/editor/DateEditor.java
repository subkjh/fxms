package com.fxms.ui.bas.editor;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

import javafx.scene.control.DatePicker;
import javafx.util.StringConverter;

public class DateEditor extends DatePicker implements FxEditor {

	private final String pattern = "yyyyMMdd";
	private static final SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");

	public DateEditor() {
		StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

			@Override
			public String toString(LocalDate date) {
				if (date != null) {
					return dateFormatter.format(date);
				} else {
					return "";
				}
			}

			@Override
			public LocalDate fromString(String string) {
				if (string != null && !string.isEmpty()) {
					return LocalDate.parse(string, dateFormatter);
				} else {
					return null;
				}
			}
		};
		setConverter(converter);

		setAttrId(yyyyMMdd.format(new Date(System.currentTimeMillis())), null);

	}

	@Override
	public void clearEditor() {
	}

	@Override
	public String getAttrId() {
		if (getValue() == null) {
			return null;
		}

		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
		return dateFormatter.format(getValue());
	}

	@Override
	public void setAttrId(Object id, Map<String, Object> objectData) {
		
		if (id != null && id.toString().length() >= 8) {
			try {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
				LocalDate localDate = LocalDate.parse(id.toString().substring(0, 8), formatter);
				this.setValue(localDate);
			} catch (Exception e) {
			}
		}
		
	}

	@Override
	public void init(String attrValueList, String promptText, int width) {
		setPromptText(promptText);
		setPrefWidth(width);
	}

}
