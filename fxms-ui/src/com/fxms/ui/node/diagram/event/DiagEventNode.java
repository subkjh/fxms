package com.fxms.ui.node.diagram.event;

import com.fxms.ui.css.image.ImagePointer;
import com.fxms.ui.node.diagram.FxDiagram;
import com.fxms.ui.node.diagram.FxDiagramEdit;
import com.fxms.ui.node.diagram.node.DiagNode;
import com.fxms.ui.node.diagram.node.DiagNodeBox;
import com.fxms.ui.node.diagram.node.DiagNodeDiagram;
import com.fxms.ui.node.diagram.node.DiagNodeImage;
import com.fxms.ui.node.diagram.node.DiagNodeMo;
import com.fxms.ui.node.diagram.node.DiagNodeStatus;
import com.fxms.ui.node.diagram.node.DiagNodeText;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class DiagEventNode implements EventHandler<MouseEvent> {

	private FxDiagram diagram;
	private FxDiagramEdit editor;

	private Node selectedDiaplayNode;

	public DiagEventNode(FxDiagramEdit editor) {
		this.diagram = editor.getDiagramPane();
		this.editor = editor;
	}

	public void addEvent(Node node) {
		node.addEventHandler(MouseEvent.MOUSE_PRESSED, this);
		node.addEventHandler(MouseEvent.MOUSE_RELEASED, this);
		node.addEventHandler(MouseEvent.MOUSE_DRAGGED, this);
		node.addEventHandler(MouseEvent.MOUSE_MOVED, this);
		node.addEventFilter(MouseEvent.MOUSE_CLICKED, this);

		node.addEventHandler(MouseEvent.MOUSE_ENTERED, this);
		node.addEventHandler(MouseEvent.MOUSE_EXITED, this);
	}

	@Override
	public void handle(MouseEvent e) {

		DiagNode diagNode = (DiagNode) e.getSource();
		Node node = (Node) e.getSource();

		if (node.getScene().getCursor() == ImagePointer.getLineCursor()) {
			if (e.getEventType() == MouseEvent.MOUSE_CLICKED && e.getButton() == MouseButton.PRIMARY) {
				if (editor.setLinkNode(diagNode)) {
					node.getScene().setCursor(Cursor.DEFAULT);
				}
			}
			return;
		}

		if (e.getEventType() == MouseEvent.MOUSE_ENTERED) {
			selectedDiaplayNode = DiagNode.makeSelection(diagNode.getBounds());
			diagram.getChildren().add(selectedDiaplayNode);
			selectedDiaplayNode.toFront();

		} else if (e.getEventType() == MouseEvent.MOUSE_EXITED || e.getEventType() == MouseEvent.MOUSE_DRAGGED) {
			diagram.getChildren().remove(selectedDiaplayNode);
		}

		if (diagNode instanceof DiagNodeBox) {
			handleBox((DiagNodeBox) diagNode, e);
		} else if (diagNode instanceof DiagNodeImage) {
			handleImage((DiagNodeImage) diagNode, e);
		} else if (diagNode instanceof DiagNodeText) {
			handleText((DiagNodeText) diagNode, e);
		} else if (diagNode instanceof DiagNodeStatus) {
			handleStatus((DiagNodeStatus) diagNode, e);
		} else if (diagNode instanceof DiagNodeMo) {
			handleNode((DiagNodeMo) diagNode, e);
		} else if (diagNode instanceof DiagNodeDiagram) {
			handleDiagram((DiagNodeDiagram) diagNode, e);
		}
	}

	private void handleImage(DiagNodeImage node, MouseEvent e) {

		DiagNodeBounds diagData = node.getDiagNodeBounds();

		if (e.getEventType() == MouseEvent.MOUSE_PRESSED && e.getButton() == MouseButton.PRIMARY) {
			diagData.setPressedPointer(e, node.getBounds());
		} else if (e.getEventType() == MouseEvent.MOUSE_RELEASED) {
			node.getScene().setCursor(Cursor.DEFAULT);
			diagData.mouseEvent = MouseEvent.MOUSE_RELEASED;
		} else if (e.getEventType() == MouseEvent.MOUSE_MOVED) {
			diagData.checkResize(node, node.getFitWidth(), node.getFitHeight(), e);
		} else if (e.getEventType() == MouseEvent.MOUSE_EXITED) {
			if (diagData.mouseEvent != MouseEvent.MOUSE_PRESSED) {
				node.getScene().setCursor(Cursor.DEFAULT);
			}
		} else if (e.getEventType() == MouseEvent.MOUSE_DRAGGED && e.getButton() == MouseButton.PRIMARY) {
			if (node.getScene().getCursor() == Cursor.HAND) {
				node.setDiagNodeLocation(diagData.move(node, e));
			} else {
				node.setDiagNodeLocation(diagData.resize(node, e));
			}
			diagram.redrawLines(node);
		}
	}

	private void handleBox(DiagNodeBox node, MouseEvent e) {

		DiagNodeBounds diagData = node.getDiagNodeBounds();

		if (e.getEventType() == MouseEvent.MOUSE_PRESSED && e.getButton() == MouseButton.PRIMARY) {
			diagData.setPressedPointer(e, node.getBounds());
		} else if (e.getEventType() == MouseEvent.MOUSE_RELEASED) {
			node.getScene().setCursor(Cursor.DEFAULT);
			diagData.mouseEvent = MouseEvent.MOUSE_RELEASED;
		} else if (e.getEventType() == MouseEvent.MOUSE_MOVED) {
			diagData.checkResize(node, node.getWidth(), node.getHeight(), e);
		} else if (e.getEventType() == MouseEvent.MOUSE_EXITED) {
			if (diagData.mouseEvent != MouseEvent.MOUSE_PRESSED) {
				node.getScene().setCursor(Cursor.DEFAULT);
			}
		} else if (e.getEventType() == MouseEvent.MOUSE_DRAGGED && e.getButton() == MouseButton.PRIMARY) {
			if (node.getScene().getCursor() == Cursor.HAND) {
				node.setDiagNodeLocation(diagData.move(node, e));
			} else {
				node.setDiagNodeLocation(diagData.resize(node, e));
			}
			diagram.redrawLines(node);
		}
	}

	private void handleText(DiagNodeText node, MouseEvent e) {
		DiagNodeBounds diagData = node.getDiagNodeBounds();

		if (e.getEventType() == MouseEvent.MOUSE_PRESSED && e.getButton() == MouseButton.PRIMARY) {
			diagData.setPressedPointer(e, node.getBounds());
		} else if (e.getEventType() == MouseEvent.MOUSE_RELEASED) {
			node.getScene().setCursor(Cursor.DEFAULT);
			diagData.mouseEvent = MouseEvent.MOUSE_RELEASED;
		} else if (e.getEventType() == MouseEvent.MOUSE_MOVED) {
			node.getScene().setCursor(Cursor.HAND);
		} else if (e.getEventType() == MouseEvent.MOUSE_EXITED) {
			if (diagData.mouseEvent != MouseEvent.MOUSE_PRESSED) {
				node.getScene().setCursor(Cursor.DEFAULT);
			}
		} else if (e.getEventType() == MouseEvent.MOUSE_DRAGGED && e.getButton() == MouseButton.PRIMARY) {
			if (node.getScene().getCursor() == Cursor.HAND) {
				node.setDiagNodeLocation(diagData.move(node, e));
			}
		}
	}

	private void handleStatus(DiagNodeStatus node, MouseEvent e) {

		DiagNodeBounds diagData = node.getDiagNodeBounds();

		if (e.getEventType() == MouseEvent.MOUSE_PRESSED && e.getButton() == MouseButton.PRIMARY) {
			diagData.setPressedPointer(e, node.getBounds());
		} else if (e.getEventType() == MouseEvent.MOUSE_RELEASED) {
			node.getScene().setCursor(Cursor.DEFAULT);
			diagData.mouseEvent = MouseEvent.MOUSE_RELEASED;
		} else if (e.getEventType() == MouseEvent.MOUSE_MOVED) {
			diagData.checkResize(node, node.getWidth(), node.getHeight(), e);
		} else if (e.getEventType() == MouseEvent.MOUSE_EXITED) {
			if (diagData.mouseEvent != MouseEvent.MOUSE_PRESSED) {
				node.getScene().setCursor(Cursor.DEFAULT);
			}
		} else if (e.getEventType() == MouseEvent.MOUSE_DRAGGED && e.getButton() == MouseButton.PRIMARY) {
			if (node.getScene().getCursor() == Cursor.HAND) {
				node.setDiagNodeLocation(diagData.move(node, e));
			} else {
				node.setDiagNodeLocation(diagData.resize(node, e));
			}
			diagram.redrawLines(node);
		}
	}

	private void handleNode(DiagNodeMo node, MouseEvent e) {

		DiagNodeBounds diagData = node.getDiagNodeBounds();

		if (e.getEventType() == MouseEvent.MOUSE_PRESSED && e.getButton() == MouseButton.PRIMARY) {
			diagData.setPressedPointer(e, node.getBounds());
		} else if (e.getEventType() == MouseEvent.MOUSE_RELEASED) {
			node.getScene().setCursor(Cursor.DEFAULT);
			diagData.mouseEvent = MouseEvent.MOUSE_RELEASED;
		} else if (e.getEventType() == MouseEvent.MOUSE_MOVED) {
			diagData.checkResize(node, node.getWidth(), node.getHeight(), e);
		} else if (e.getEventType() == MouseEvent.MOUSE_EXITED) {
			if (diagData.mouseEvent != MouseEvent.MOUSE_PRESSED) {
				node.getScene().setCursor(Cursor.DEFAULT);
			}
		} else if (e.getEventType() == MouseEvent.MOUSE_DRAGGED && e.getButton() == MouseButton.PRIMARY) {
			if (node.getScene().getCursor() == Cursor.HAND) {
				node.setDiagNodeLocation(diagData.move(node, e));
			} else {
				node.setDiagNodeLocation(diagData.resize(node, e));
			}
			diagram.redrawLines(node);
		}
	}

	private void handleDiagram(DiagNodeDiagram node, MouseEvent e) {

		DiagNodeBounds diagData = node.getDiagNodeBounds();

		if (e.getEventType() == MouseEvent.MOUSE_PRESSED && e.getButton() == MouseButton.PRIMARY) {
			diagData.setPressedPointer(e, node.getBounds());
		} else if (e.getEventType() == MouseEvent.MOUSE_RELEASED) {
			node.getScene().setCursor(Cursor.DEFAULT);
			diagData.mouseEvent = MouseEvent.MOUSE_RELEASED;
		} else if (e.getEventType() == MouseEvent.MOUSE_MOVED) {
			diagData.checkResize(node, node.getWidth(), node.getHeight(), e);
		} else if (e.getEventType() == MouseEvent.MOUSE_EXITED) {
			if (diagData.mouseEvent != MouseEvent.MOUSE_PRESSED) {
				node.getScene().setCursor(Cursor.DEFAULT);
			}
		} else if (e.getEventType() == MouseEvent.MOUSE_DRAGGED && e.getButton() == MouseButton.PRIMARY) {
			if (node.getScene().getCursor() == Cursor.HAND) {
				node.setDiagNodeLocation(diagData.move(node, e));
			} else {
				node.setDiagNodeLocation(diagData.resize(node, e));
			}
			diagram.redrawLines(node);
		}
	}

}