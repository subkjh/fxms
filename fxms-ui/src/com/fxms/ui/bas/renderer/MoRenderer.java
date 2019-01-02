package com.fxms.ui.bas.renderer;

import com.fxms.ui.bas.mo.Mo;
import com.fxms.ui.dx.DxAsyncSelector;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class MoRenderer extends HBox implements FxRenderer {

	private final TextField name;
	private final Button showDetail;

	public MoRenderer() {
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
			long moNo = FxRenderer.getLong(value, -1L);

			if (moNo > 0) {
				Mo mo = DxAsyncSelector.getSelector().getMo(moNo);
				if (mo != null) {
					name.setText(mo.getMoAname());
					showDetail.setDisable(false);
					return;
				}
			}

		} catch (Exception e) {
		}

		name.setText("-");
		getChildren().remove(showDetail);
	}

}
