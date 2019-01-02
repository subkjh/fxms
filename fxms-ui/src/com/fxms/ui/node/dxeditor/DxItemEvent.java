package com.fxms.ui.node.dxeditor;

import com.fxms.ui.node.diagram.event.DiagNodeBounds;
import com.fxms.ui.node.diagram.node.DiagNode;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class DxItemEvent implements EventHandler<MouseEvent> {

	private DxEditor editor;
	private Node selectedDiaplayNode;

	public DxItemEvent(DxEditor editor) {
		this.editor = editor;
	}

	public void addEvent(Node node) {
		node.addEventHandler(MouseEvent.MOUSE_PRESSED, this);
		node.addEventHandler(MouseEvent.MOUSE_RELEASED, this);
		node.addEventHandler(MouseEvent.MOUSE_DRAGGED, this);
		node.addEventHandler(MouseEvent.MOUSE_MOVED, this);
		node.addEventHandler(MouseEvent.MOUSE_CLICKED, this);

		node.addEventHandler(MouseEvent.MOUSE_ENTERED, this);
		node.addEventHandler(MouseEvent.MOUSE_EXITED, this);
	}

	@Override
	public void handle(MouseEvent e) {

		DxEditItem node = (DxEditItem) e.getSource();

		if (e.getEventType() == MouseEvent.MOUSE_ENTERED) {
			selectedDiaplayNode = DiagNode.makeSelection(node.getBounds());
			editor.addNode(selectedDiaplayNode);
			selectedDiaplayNode.toBack();

		} else if (e.getEventType() == MouseEvent.MOUSE_EXITED || e.getEventType() == MouseEvent.MOUSE_DRAGGED) {
			editor.removeNode(selectedDiaplayNode);
		}

		DiagNodeBounds diagData = node.getDiagNodeBounds();

		if (e.getEventType() == MouseEvent.MOUSE_PRESSED && e.getButton() == MouseButton.PRIMARY) {
			diagData.setPressedPointer(e, node.getBounds());
		} else if (e.getEventType() == MouseEvent.MOUSE_RELEASED) {
			node.getScene().setCursor(Cursor.DEFAULT);
			diagData.mouseEvent = MouseEvent.MOUSE_RELEASED;
		} else if (e.getEventType() == MouseEvent.MOUSE_MOVED) {
			diagData.checkResize(node, node.getPrefWidth(), node.getPrefHeight(),
					e);
		} else if (e.getEventType() == MouseEvent.MOUSE_EXITED) {
			if (diagData.mouseEvent != MouseEvent.MOUSE_PRESSED) {
				node.getScene().setCursor(Cursor.DEFAULT);
			}
		} else if (e.getEventType() == MouseEvent.MOUSE_DRAGGED && e.getButton() == MouseButton.PRIMARY) {
			if (node.getScene().getCursor() == Cursor.HAND) {
				node.resize(diagData.move(node, e));
			} else {
				node.resize(diagData.resize(node, e));
			}
		}
	}

}
