package com.fxms.ui.bas.editor;

import java.util.List;
import java.util.Map;

import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.bas.code.UiCodeVo;
import com.fxms.ui.bas.vo.Attr;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;

public class CodeEditor extends ComboBox<UiCodeVo> implements FxListEditor<UiCodeVo> {

	private EditorCallback<UiCodeVo> callback;

	public CodeEditor() {

	}

	@Override
	public void clearEditor() {
		getSelectionModel().clearSelection();
	}

	@Override
	public String getAttrId() {
		UiCodeVo attr = getSelectionModel().getSelectedItem();
		return attr == null ? null : attr.getCdCode();
	}

	@Override
	public void init(String attrValueList, String promptText, int width) {

		String ss[] = attrValueList.split(";");
		String cdType = ss[ss.length - 1].trim();

		if (ss.length > 1) {
			List<Attr> attrList = FxEditor.makeAttrList(ss[0]);
			UiCodeVo code;
			for (Attr attr : attrList) {
				code = new UiCodeVo();
				code.setCdCode(attr.getAttrId());
				code.setCdName(attr.getAttrText());
				getItems().add(code);
			}
		}

		int pos = cdType.indexOf('?');
		Map<String, Object> para = null;
		if (pos > 0) {
			String query = cdType.substring(pos + 1);
			cdType = cdType.substring(0, pos);
			para = FxEditor.parseQuery(query);
		}

		List<UiCodeVo> list = CodeMap.getMap().getCodeList(cdType);
		if (list != null) {
			for (UiCodeVo attr : list) {
				if (attr.match(para)) {
					getItems().add(attr);
				}
			}
		}

		setPromptText(promptText);
		setPrefWidth(width);
	}

	@Override
	public void setAttrId(Object id, Map<String, Object> objectData) {

		String attrId = String.valueOf(id);

		if (id instanceof Number) {
			attrId = String.valueOf(((Number) id).longValue());
		}

		for (UiCodeVo cd : getItems()) {
			if (attrId.equals(cd.getCdCode())) {
				getSelectionModel().select(cd);
				return;
			}
		}

		// 없으면 추가하고 보인다.
		UiCodeVo code = new UiCodeVo();
		code.setCdCode(attrId);
		code.setCdName(attrId);
		getItems().add(code);
		getSelectionModel().select(code);
	}

	@Override
	public void setCallback(EditorCallback<UiCodeVo> callback) {

		if (this.callback == null && callback != null) {
			valueProperty().addListener(new ChangeListener<UiCodeVo>() {
				@Override
				public void changed(ObservableValue<? extends UiCodeVo> observable, UiCodeVo oldValue,
						UiCodeVo newValue) {
					CodeEditor.this.callback.onSelected(CodeEditor.this, newValue);
				}
			});
		}

		this.callback = callback;
	}
}
