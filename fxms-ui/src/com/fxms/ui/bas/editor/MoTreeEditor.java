package com.fxms.ui.bas.editor;

import java.util.Map;

import com.fxms.ui.bas.FxStage;
import com.fxms.ui.bas.mo.Mo;
import com.fxms.ui.dx.DxAsyncSelector;
import com.fxms.ui.dx.FxCallback;
import com.fxms.ui.node.tree.FxTreeAllPane;
import com.fxms.ui.node.tree.vo.TreeItemVo;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class MoTreeEditor extends HBox implements FxEditor {

	private TextField text;
	private FxStage stage;
	private Mo mo = null;
	private FxCallback<Mo> callback;
	// private final MoTreeAllPane moTree;
	private FxTreeAllPane moTree;

	public MoTreeEditor() {
		super(5);
	}

	@Override
	public String getAttrId() {
		return mo != null ? mo.getMoNo() + "" : "";
	}

	@Override
	public void clearEditor() {
		text.setText("");
		mo = null;
	}

	@Override
	public void setAttrId(Object id, Map<String, Object> objectData) {

		if (id instanceof Mo) {
			mo = (Mo) id;
		} else {
			long moNo = (id instanceof Number ? ((Number) id).intValue() : -1);
			mo = DxAsyncSelector.getSelector().getMo(moNo);
		}

		if (mo != null) {
			text.setText(mo.getMoName());
		} else {
			text.setText(id == null ? "" : id.toString());
		}

	}

	public void setCallback(FxCallback<Mo> callback) {
		this.callback = callback;
	}

	@Override
	public void init(String attrValueList, String promptText, int width) {
		
		String moClass = attrValueList;

		text = new TextField("");
		text.setDisable(true);
		text.setMinWidth(200);

		// moTree = new MoTreeAllPane(moClass, this);
		moTree = new FxTreeAllPane(moClass) {

			@Override
			protected void onSelected(TreeItemVo treeItemVo) {
				if (treeItemVo != null && treeItemVo.getSource() instanceof Mo) {
					Mo mo = (Mo) treeItemVo.getSource();
					setAttrId(mo, null);
					if (callback != null) {
						callback.onCallback(mo);
					}
				}
			}

		};
		stage = new FxStage(moTree, "Choice MO");

		Button btn = new Button("...");

		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				stage.showStage(getScene().getWindow());
			}
		});

		getChildren().add(text);
		getChildren().add(btn);

		text.setPromptText(promptText);
		setPrefWidth(width);

	}

}
