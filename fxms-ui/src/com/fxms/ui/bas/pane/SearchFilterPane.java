package com.fxms.ui.bas.pane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fxms.ui.bas.code.UiOpCodeAttrVo;
import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.bas.editor.FxEditor;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 * 검색 조건 Pane
 * 
 * @author SUBKJH-DEV
 *
 */
public class SearchFilterPane extends BorderPane {

	private final HBox filterBox = new HBox(10);
	private final HBox btnBox = new HBox(5);
	private UiOpCodeVo opcode;

	public SearchFilterPane() {

		filterBox.setPadding(new Insets(5, 5, 5, 5));
		btnBox.setPadding(new Insets(5, 5, 5, 5));

		setCenter(filterBox);
		setRight(btnBox);
	}

	public FxEditor getFxEditor(String attrName) {

		for (Node node : filterBox.getChildren()) {
			if (node instanceof FxEditor) {
				if (attrName.equals(node.getId())) {
					return ((FxEditor) node);
				}
			}
		}

		return null;
	}

	public void clearEditor() {
		for (Node node : filterBox.getChildren()) {
			if (node instanceof FxEditor) {
				((FxEditor) node).clearEditor();
			}
		}
	}

	public List<Node> makeFilters(UiOpCodeVo opcode) {

		this.opcode = opcode;

		filterBox.getChildren().clear();
		for (Node node : getSearchFilter()) {
			filterBox.getChildren().add(node);
		}

		return new ArrayList<Node>(filterBox.getChildren());

	}

	public void addButton(Node node) {
		btnBox.getChildren().add(node);
	}

	public void addEditor(Node node) {
		filterBox.getChildren().add(node);
	}

	/**
	 * 
	 * @return 운용자가 입력한 데이터
	 */
	public Map<String, Object> getInputData() {

		Map<String, Object> p = new HashMap<String, Object>();

		String attrId;
		UiOpCodeAttrVo attr;
		for (Node node : filterBox.getChildren()) {
			if (node.getUserData() instanceof UiOpCodeAttrVo) {
				attr = (UiOpCodeAttrVo) node.getUserData();
				if (node instanceof FxEditor) {
					attrId = ((FxEditor) node).getAttrId();
					if (attrId != null && attrId.trim().length() > 0) {
						p.put(attr.getAttrName(), attrId.trim());
					}
				}
			}
		}

		return p;
	}

	public void initData(Map<String, Object> data) {

		if (data == null) {
			return;
		}

		Object value;
		UiOpCodeAttrVo attr;

		for (Node node : filterBox.getChildren()) {
			if (node.getUserData() instanceof UiOpCodeAttrVo) {
				attr = (UiOpCodeAttrVo) node.getUserData();
				if (node instanceof FxEditor) {
					value = data.get(attr.getAttrName());
					if (value != null) {
						((FxEditor) node).setAttrId(value, null);
					}
				}
			}
		}

	}

	private List<Node> getSearchFilter() {
		Node node;
		List<Node> nodeList = new ArrayList<Node>();

		if (opcode != null) {
			for (UiOpCodeAttrVo attr : opcode.getChildren()) {
				node = attr.makeEditor();
				if (node != null) {
					nodeList.add(node);
				}
			}
		}

		return nodeList;
	}

}
