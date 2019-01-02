package com.fxms.ui.node.diagram.event;

import com.fxms.ui.node.diagram.FxDiagramEdit;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class DiagEvent implements EventHandler<MouseEvent> {

	public void addEvent(FxDiagramEdit node) {
//		node.addEventFilter(MouseEvent.MOUSE_CLICKED, this);
//		node.addEventHandler(MouseEvent.MOUSE_CLICKED, this);
	}

	public void handle(MouseEvent e) {

		FxDiagramEdit node = (FxDiagramEdit) e.getSource();

		if (e.getEventType() == MouseEvent.MOUSE_CLICKED) {
//			node.setMode(FxDiagramEdit.Mode.normal);
		}

	}

}
