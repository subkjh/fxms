package com.fxms.ui.bas.renderer;

import com.fxms.ui.bas.code.UiAlarmCfgVo;
import com.fxms.ui.bas.code.CodeMap;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class AlarmCfgRenderer extends HBox implements FxRenderer {

	private final TextField name;
	private final Button showDetail;

	public AlarmCfgRenderer() {
		super(8);

		name = new TextField();
		name.setEditable(false);
		name.getStyleClass().add("text-renderer");

		showDetail = new Button("...");

		getChildren().add(name);
		getChildren().add(showDetail);
	}

	@Override
	public void setValue(Object value, String type) {


		try {
			int alarmCfgNo = FxRenderer.getInt(value, -1);
			UiAlarmCfgVo cfg = CodeMap.getMap().getAlarmCfg(alarmCfgNo);
			if (cfg != null) {
				name.setText(cfg.getAlarmCfgName());
				showDetail.setDisable(false);
				return;
			}
		} catch (Exception e) {
		}

		name.setText("");
		getChildren().remove(showDetail);
	}

}
