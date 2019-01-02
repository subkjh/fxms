package com.fxms.ui.node.diagram.event;

import com.fxms.ui.node.diagram.FxDiagram;
import com.fxms.ui.node.diagram.FxDiagramEdit;
import com.fxms.ui.node.diagram.node.DiagNodeLine;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public class DiagEventLine implements EventHandler<MouseEvent> {

	private FxDiagram diagram;
	private Node selectedDiaplayNode;

	public DiagEventLine(FxDiagramEdit editor) {
		this.diagram = editor.getDiagramPane();
	}

	public void addEvent(Node node) {
		node.addEventHandler(MouseEvent.MOUSE_ENTERED, this);
		node.addEventHandler(MouseEvent.MOUSE_EXITED, this);
	}

	@Override
	public void handle(MouseEvent e) {

		DiagNodeLine diagNode = (DiagNodeLine) e.getSource();

		if (e.getEventType() == MouseEvent.MOUSE_ENTERED) {
			selectedDiaplayNode = diagNode.getSelectedDisplayNode();
			diagram.getChildren().add(selectedDiaplayNode);
			selectedDiaplayNode.toBack();
		} else if (e.getEventType() == MouseEvent.MOUSE_EXITED) {
			diagram.getChildren().remove(selectedDiaplayNode);
		}
	}

}
