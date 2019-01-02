package com.fxms.ui.bas.editor;

import java.util.Map;

import com.fxms.ui.bas.FxStage;
import com.fxms.ui.bas.vo.LocationVo;
import com.fxms.ui.biz.pane.LocationTreePane;
import com.fxms.ui.dx.FxCallback;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class LocationTreeEditor extends HBox implements FxEditor {

	private TextField text;
	private LocationTreePane locationTree;
	private LocationVo location = null;
	private FxStage stage;

	public LocationTreeEditor() {
		super(8);
	}

	@Override
	public void clearEditor() {
		text.setText("");
		location = null;
	}

	@Override
	public String getAttrId() {
		return location != null ? location.getInloNo() + "" : "";
	}

	@Override
	public void init(String attrValueList, String promptText, int width) {

		text = new TextField("");
		text.setDisable(true);
		text.setMinWidth(200);
		text.setPromptText(promptText);

		setPrefWidth(width);

		locationTree = new LocationTreePane(new FxCallback<Object>() {
			@Override
			public void onCallback(Object data) {
				if (data instanceof LocationVo) {
					setAttrId(((LocationVo) data).getInloNo(), null);
				}
			}
		});

		Button btn = new Button("...");
		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (stage == null) {
					stage = new FxStage(locationTree, "Choice Install Location...");
					stage.showStage(getScene().getWindow());
				} else {
					stage.show();
				}
			}
		});

		getChildren().add(text);
		getChildren().add(btn);
	}

	@Override
	public void setAttrId(Object id, Map<String, Object> objectData) {
		int locationNo = (id instanceof Number ? ((Number) id).intValue() : -1);

		location = locationTree.getLocation(locationNo);
		if (location != null) {
			text.setText(location.getInloFname());
		} else {
			text.setText(id == null ? "" : id.toString());
		}

	}

}
