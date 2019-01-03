package com.fxms.ui.node.dxeditor;

import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.bas.property.DxNode;
import com.fxms.ui.bas.property.DxNodeMulti;
import com.fxms.ui.bas.property.FxNode;
import com.fxms.ui.bas.vo.ui.UiBasicVo;
import com.fxms.ui.bas.vo.ui.UiGroupVo;
import com.fxms.ui.node.diagram.event.FxBounds;

import fxms.client.log.Logger;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

public class DxPane extends ScrollPane implements FxNode {

	class DxNodeEvent implements EventHandler<MouseEvent> {

		private Pane parent;

		public DxNodeEvent(Pane parent) {
			this.parent = parent;
		}

		public void addEvent(Node node) {
			node.addEventHandler(MouseEvent.MOUSE_ENTERED, this);
			node.addEventHandler(MouseEvent.MOUSE_EXITED, this);
		}

		@Override
		public void handle(MouseEvent e) {
			if (e.getSource() instanceof Region && e.getSource() instanceof DxNode) {

				DxNode dxNode = (DxNode) e.getSource();
				Region region = (Region) e.getSource();
				UiBasicVo ui = (UiBasicVo) region.getUserData();

				if (e.getEventType() == MouseEvent.MOUSE_ENTERED) {

					String text = ui.getUiTitle();

					if (dxNode instanceof DxNodeMulti) {
						text += " (" + ((DxNodeMulti) dxNode).getNodeTag() + ")";
					}

					// if (selectedDiaplayNode != null) {
					// parent.getChildren().remove(selectedDiaplayNode);
					// }

					selectedDiaplayNode = new Label(text);
					selectedDiaplayNode.setLayoutX(region.getLayoutX() + 2);
					selectedDiaplayNode.setLayoutY(region.getLayoutY() + region.getPrefHeight() - 18);
					selectedDiaplayNode.setPrefSize(region.getPrefWidth() - 4, 16);
					selectedDiaplayNode.getStyleClass().add("dx-node-selected");
					selectedDiaplayNode.setMouseTransparent(true);
					// selectedDiaplayNode.setContextMenu(nodeMenu);
					// nodeMenu.setUiBasicVo(ui);

					parent.getChildren().add(selectedDiaplayNode);
					selectedDiaplayNode.toFront();

				} else if (e.getEventType() == MouseEvent.MOUSE_EXITED) {
					parent.getChildren().remove(selectedDiaplayNode);
				}

			}
		}
	}

	class DxPaneEvent implements EventHandler<MouseEvent> {

		public DxPaneEvent(DxPane parent) {
			parent.addEventHandler(MouseEvent.MOUSE_MOVED, this);
		}

		@Override
		public void handle(MouseEvent e) {
			if (e.getSource().equals(DxPane.this)) {
				if (selectedDiaplayNode != null) {
					pane.getChildren().remove(selectedDiaplayNode);
				}
			}
		}
	}

	public static int FIRST_GROUP = 0;
	private final AnchorPane pane;
	private DxNodeEvent nodeEvent;
	private DxViewContextMenu contextMenu;
	private UiGroupVo uiGroup;
	private Label selectedDiaplayNode;
	private final FxBounds fxBound;
	private final Label tag = new Label();

	public DxPane() {

		pane = new AnchorPane();
		pane.setStyle("-fx-background-color: transparent;");

		nodeEvent = new DxNodeEvent(pane);
		contextMenu = new DxViewContextMenu(this);
		fxBound = new FxBounds();
		// new DxPaneEvent(this);

		// zoom 처리를 위해 Group에 넣음
		Group group = new Group();
		group.getChildren().add(pane);

		setId("dx-main-pane");
		setContent(group);
		setFitToHeight(true);
		setFitToWidth(true);
		setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		setContextMenu(contextMenu);
		setBorder(Border.EMPTY);
		setStyle("-fx-background-color: transparent;");

	}

	public AnchorPane getMainPane() {
		return pane;
	}

	public DxPane(int uiGroupNo) {
		this();
		setUiGroupNo(uiGroupNo);
	}

	public DxPane(UiGroupVo vo) {
		this();
		setUiGroupVo(vo);
	}

	public void addDxNode(Node node) {

		pane.getChildren().add(node);

		if (node instanceof DxNode) {
			((DxNode) node).onAddedInParent();
		}

		if (node instanceof Region) {
			nodeEvent.addEvent(node);
		}

	}

	public UiGroupVo getUiGroup() {
		return uiGroup;
	}

	public void removeDxNode(Node node) {
		pane.getChildren().remove(node);
		if (node instanceof FxNode) {
			((FxNode) node).onRemovedFromParent();
		}
	}

	public void setUiGroupNo(int uiGroupNo) {

		if (uiGroupNo == FIRST_GROUP) {
			setUiGroupVo(CodeMap.getMap().getUiList().get(0));
			return;
		}

		for (UiGroupVo vo : CodeMap.getMap().getUiList()) {
			if (vo.getUiGroupNo() == uiGroupNo) {
				setUiGroupVo(vo);
				break;
			}
		}
	}

	public void setUiGroupVo(UiGroupVo vo) {

		onRemovedFromParent();

		pane.getChildren().clear();

		uiGroup = vo;

		if (contextMenu != null) {
			contextMenu.setSelected(vo.getUiGroupNo());
		}

		for (UiBasicVo ui : vo.getChildren()) {
			try {
				Node node = ui.makeNode();
				addDxNode(node);
			} catch (Exception e) {
				Logger.logger.error(e);
			}

			fxBound.makeBound(ui);
		}

		// 공백을 맞추기 위해서
		tag.setLayoutX(fxBound.getWidth() + fxBound.getX());
		tag.setLayoutY(fxBound.getHeight() + fxBound.getY());
		tag.setPrefSize(1, 1);
		pane.getChildren().add(tag);
	}

	@Override
	public void onAddedInParent() {

	}

	@Override
	public void onRemovedFromParent() {
		for (Node node : pane.getChildren()) {
			if (node instanceof FxNode) {
				((FxNode) node).onRemovedFromParent();
			}
		}
	}
}
