package com.fxms.ui.bas.menu;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.bas.FxAlert;
import com.fxms.ui.bas.FxAlert.FxAlertType;
import com.fxms.ui.bas.FxStage;
import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.bas.property.DxNode;
import com.fxms.ui.bas.vo.ui.UiBasicVo;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

public abstract class FxMenuButton extends BorderPane implements DxNode {

	class Event implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent e) {
			Node node = (Node) e.getSource();
			if (e.getEventType() == MouseEvent.MOUSE_ENTERED) {
				node.getScene().setCursor(Cursor.HAND);
			} else if (e.getEventType() == MouseEvent.MOUSE_EXITED) {
				node.getScene().setCursor(Cursor.DEFAULT);
			}
		}
	}

	private Parent pane;
	protected final Button button;
	private UiOpCodeVo opCode;

	public FxMenuButton() {

		getStyleClass().add("dx-node-data-count-menu");

		setPadding(new Insets(3, 3, 3, 3));

		button = new Button();
		button.setWrapText(true);
		button.getStyleClass().add("dx-node-data-count-menu-center-text");
		setCenter(button);

		initEvent();

	}

	protected abstract OP_NAME getContentOpName();

	public Parent getPane() {
		return pane;
	}

	@Override
	public boolean initDxNode(UiBasicVo vo) {

		opCode = CodeMap.getMap().getOpCode(vo.getOpNo());

		if (opCode != null && button.getText().length() == 0) {
			button.setText(opCode.getOpTitle());
		}

		return opCode != null;
	}

	@Override
	public void onAddedInParent() {
	}

	@Override
	public void onRemovedFromParent() {
	}

	private void initEvent() {

		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				onAction();
			}
		});

		Event t = new Event();
		button.addEventHandler(MouseEvent.MOUSE_ENTERED, t);
		button.addEventHandler(MouseEvent.MOUSE_EXITED, t);
	}

	protected Button getCenterButton() {
		return button;
	}

	protected UiOpCodeVo getOpCode() {
		return opCode;
	}

	protected Parent makeScreen(UiOpCodeVo contentOpCode) {
		return null;
	}

	public void onAction(Node parent) {

		UiOpCodeVo contentOpCode = CodeMap.getMap().getOpCode(getContentOpName());
		if (contentOpCode == null) {
			FxAlert.showAlert(FxAlertType.error, parent, "error", "화면이 정의되지 않았습니다.");
			return;
		}

		Parent pane = makeScreen(contentOpCode);
		if (pane == null) {
			contentOpCode.showScreen();
		} else {
			FxStage.showDialog(parent, pane, contentOpCode, null, null, null);
		}

	}

	protected void onAction() {
		onAction(this);
	}

}
