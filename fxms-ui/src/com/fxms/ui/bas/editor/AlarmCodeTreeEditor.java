package com.fxms.ui.bas.editor;

import java.util.Map;

import com.fxms.ui.bas.FxStage;
import com.fxms.ui.bas.code.UiAlarmCodeVo;
import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.biz.pane.AlarmCodeTreePane;
import com.fxms.ui.dx.FxCallback;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class AlarmCodeTreeEditor extends HBox implements FxEditor, FxCallback<UiAlarmCodeVo> {

	private TextField text;
	private FxStage stage;
	private AlarmCodeTreePane alarmCodeTree;
	private UiAlarmCodeVo selectedVo;

	public AlarmCodeTreeEditor() {
		super(5);

		text = new TextField("");
		text.setDisable(true);
		text.setPrefWidth(200);

		// moTree = new MoTreeAllPane(moClass, this);
		alarmCodeTree = new AlarmCodeTreePane(this);
		stage = new FxStage(alarmCodeTree, "Choice Alarm-Code");

		Button btn = new Button("...");

		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				stage.showStage(getScene().getWindow());
			}
		});

		getChildren().add(text);
		getChildren().add(btn);
	}

	@Override
	public void clearEditor() {
		text.setText("");
		selectedVo = null;
	}

	@Override
	public String getAttrId() {
		return selectedVo != null ? selectedVo.getAlcdNo() + "" : "";
	}

	@Override
	public void init(String attrValueList, String promptText, int width) {

		text.setPromptText(promptText);
		setPrefWidth(width);

	}

	@Override
	public void onCallback(UiAlarmCodeVo data) {
		selectedVo = data;
		text.setText(data.getAlcdDescr());
	}

	@Override
	public void setAttrId(Object id, Map<String, Object> objectData) {

		if (id instanceof UiAlarmCodeVo) {
			selectedVo = (UiAlarmCodeVo) id;
		} else {
			int alcdNo = (id instanceof Number ? ((Number) id).intValue() : -1);
			selectedVo = CodeMap.getMap().getAlarmCode(alcdNo);
		}

		text.setText(selectedVo == null ? "" : selectedVo.getAlcdDescr());

	}

}
