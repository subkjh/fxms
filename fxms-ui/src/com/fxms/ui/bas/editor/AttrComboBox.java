package com.fxms.ui.bas.editor;

import java.util.List;
import java.util.Map;

import com.fxms.ui.bas.vo.Attr;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;

public abstract class AttrComboBox extends ComboBox<Attr> implements FxListEditor<Attr> {

	private EditorCallback<Attr> callback;
	private boolean ignoreCase = true;

	public AttrComboBox() {
	}

	@Override
	public void clearEditor() {
		this.getSelectionModel().clearSelection();
	}

	@Override
	public String getAttrId() {
		Attr attr = getSelectionModel().getSelectedItem();
		return attr == null ? null : attr.getAttrId();
	}

	@Override
	public void init(String attrValueList, String promptText, int width) {
		
		initList(attrValueList);
		
		setPromptText(promptText);
		
		if (width > 0) {
			setPrefWidth(width);
		}
	}

	public void initAttrList(List<Attr> attrList) {

		getItems().clear();

		for (Attr attr : attrList) {
			getItems().add(attr);
		}
	}

	public void select(int index) {
		try {
			getSelectionModel().select(index);
		} catch (Exception e) {
		}
	}

	@Override
	public void setAttrId(Object id, Map<String, Object> objectData) {

		// if (id != null) {
		// System.out.println(id + ", " + id.getClass().getName());
		// }

		if (id instanceof Number) {
			for (Attr cd : getItems()) {
				try {

					// System.out.println(Double.valueOf(cd.getAttrId()).doubleValue() + ", " +
					// ((Number) id).doubleValue());
					if (Double.valueOf(cd.getAttrId()).doubleValue() == ((Number) id).doubleValue()) {
						getSelectionModel().select(cd);
						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			String attrId = String.valueOf(id);
			for (Attr cd : getItems()) {
				if (ignoreCase) {
					if (attrId.equalsIgnoreCase(cd.getAttrId())) {
						getSelectionModel().select(cd);
						return;
					}
				} else {
					if (attrId.equals(cd.getAttrId())) {
						getSelectionModel().select(cd);
						return;
					}
				}
			}
		}
	}

	@Override
	public void setCallback(EditorCallback<Attr> callback) {

		if (this.callback == null && callback != null) {
			valueProperty().addListener(new ChangeListener<Attr>() {
				@Override
				public void changed(ObservableValue<? extends Attr> observable, Attr oldValue, Attr newValue) {
					AttrComboBox.this.callback.onSelected(AttrComboBox.this, newValue);
				}
			});
		}

		this.callback = callback;
	}

	public void setIgnoreCase(boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}

	public abstract void initList(String attrValueList);
}
