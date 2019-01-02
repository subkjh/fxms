package com.fxms.ui.node.network;

import com.fxms.ui.bas.FxDialog;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Shape;

public class NetworkEvent {

	class EVT implements EventHandler<MouseEvent> {

		public void handle(MouseEvent e) {

			Node node = (Node) e.getSource();

		

			miSetLink.setDisable((node instanceof Shape) == false);

			if ("canvas".equals(node.getId())) {
				selectedNode = null;
				miRemove.setText("Remove");
			} else {
				selectedNode = node;
				miRemove.setText("Remove " + node.getId());
			}

			miRemove.setDisable(selectedNode == null);

			if (e.getEventType() == MouseEvent.MOUSE_ENTERED) {
				if (tooltip != null) {
					String msg = node.getId();
					if (node.getUserData() instanceof NetworkLinkVo) {
						msg = ((NetworkLinkVo) node.getUserData()).toString();
					}
					tooltip.setText(msg);
				}
			} else if (e.getEventType() == MouseEvent.MOUSE_EXITED) {
				if (tooltip != null) {
					tooltip.setText("");
				}
			}

		}

		public void set(Node node) {
			node.addEventHandler(MouseEvent.MOUSE_ENTERED, this);
			node.addEventHandler(MouseEvent.MOUSE_CLICKED, this);
			node.addEventHandler(MouseEvent.MOUSE_EXITED, this);
		}
	}

	private Node selectedNode;
	private final ContextMenu menu;
	private final EVT evt;
	private final MenuItem miRemove;
	private final MenuItem miSetLink;
	private Label tooltip;

	public NetworkEvent(NetworkDrawPane canvas, Label tooltip) {

		miRemove = new MenuItem("Remove");
		miSetLink = new MenuItem("Set Link Interface");

		miRemove.setOnAction((ActionEvent e) -> {
			if (selectedNode != null) {
				canvas.removeNode(selectedNode);
			}
		});

		miSetLink.setOnAction((ActionEvent e) -> {
			NetworkLinkVo linkVo = (NetworkLinkVo) selectedNode.getUserData();
			NetworkLinkSetPane pane = new NetworkLinkSetPane(linkVo.getWestNeMoNo(), linkVo.getEastNeMoNo());
			FxDialog dialog = FxDialog.showDialog(canvas, pane, miSetLink.getText(), "Apply");
			if (dialog.getResult() != null) {
				linkVo.setEastIfMoNo(pane.getEastIfMoNo());
				linkVo.setWestIfMoNo(pane.getWestIfMoNo());
			}
		});

		menu = new ContextMenu();
		menu.getItems().add(miSetLink);
		menu.getItems().add(new SeparatorMenuItem());
		menu.getItems().add(miRemove);

		evt = new EVT();
		this.tooltip = tooltip;
	}

	public void addEvent(Node node) {
		evt.set(node);
	}

	public ContextMenu getContextMenu() {
		return menu;
	}

	public Node getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(Node selectedNode) {
		this.selectedNode = selectedNode;
	}
}
