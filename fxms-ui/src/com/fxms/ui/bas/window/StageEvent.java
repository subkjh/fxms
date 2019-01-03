package com.fxms.ui.bas.window;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Window;

public class StageEvent implements EventHandler<MouseEvent> {

	private double x;
	private double y;
	private double windowX;
	private double windowY;

	public StageEvent() {
	}

	public void addEvent(Node node) {
		node.addEventHandler(MouseEvent.MOUSE_DRAGGED, this);
		node.addEventHandler(MouseEvent.MOUSE_PRESSED, this);
	}

	@Override
	public void handle(MouseEvent e) {

		Node node = (Node) e.getSource();
		Window window = node.getScene().getWindow();

		if (e.getEventType() == MouseEvent.MOUSE_PRESSED && e.getButton() == MouseButton.PRIMARY) {
			x = e.getScreenX();
			y = e.getScreenY();
			windowX = window.getX();
			windowY = window.getY();
		} else if (e.getEventType() == MouseEvent.MOUSE_DRAGGED && e.getButton() == MouseButton.PRIMARY) {
			window.setX(windowX + (e.getScreenX() - x));
			window.setY(windowY + (e.getScreenY() - y));
		}
	}
}
