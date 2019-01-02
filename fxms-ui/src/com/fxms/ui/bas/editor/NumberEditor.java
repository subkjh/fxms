package com.fxms.ui.bas.editor;

import java.util.Map;
import java.util.function.UnaryOperator;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;

public class NumberEditor extends TextField implements FxEditor {

	public NumberEditor() {
		UnaryOperator<Change> integerFilter = change -> {
			String newText = change.getControlNewText();
			if (newText.matches("-?([1-9][0-9]*)?")) {
				return change;
			} else if ("-".equals(change.getText())) {
				if (change.getControlText().startsWith("-")) {
					change.setText("");
					change.setRange(0, 1);
					change.setCaretPosition(change.getCaretPosition() - 2);
					change.setAnchor(change.getAnchor() - 2);
					return change;
				} else {
					change.setRange(0, 0);
					return change;
				}
			}
			return null;
		};

		// modified version of standard converter that evaluates an empty string
		// as zero instead of null:
		StringConverter<Integer> converter = new IntegerStringConverter() {
			@Override
			public Integer fromString(String s) {
				if (s.isEmpty())
					return 0;
				return super.fromString(s);
			}
		};

		TextFormatter<Integer> textFormatter = new TextFormatter<Integer>(converter, 0, integerFilter);
		setTextFormatter(textFormatter);
	}

	@Override
	public void clearEditor() {
		this.clear();
	}

	@Override
	public String getAttrId() {
		return this.getText();
	}

	@Override
	public void init(String attrValueList, String promptText, int width) {
		setPromptText(promptText);
		setPrefWidth(width);
	}
	
	@Override
	public void setAttrId(Object id, Map<String, Object> objectData) {
		this.setText(id == null ? "" : id.toString());
	}
}
