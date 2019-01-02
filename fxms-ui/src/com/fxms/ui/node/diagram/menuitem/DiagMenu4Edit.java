package com.fxms.ui.node.diagram.menuitem;

import java.util.Map;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.biz.action.FxMenuItem;
import com.fxms.ui.css.image.ImagePointer;
import com.fxms.ui.dx.DxCallback;
import com.fxms.ui.node.diagram.FxDiagram;
import com.fxms.ui.node.diagram.FxDiagramEdit;
import com.fxms.ui.node.diagram.event.DiagNodeBounds;
import com.fxms.ui.node.diagram.node.DiagNode;
import com.fxms.ui.node.diagram.node.DiagNode.NODE_TYPE;
import com.fxms.ui.node.diagram.vo.DiagNodeVo;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.stage.WindowEvent;

public class DiagMenu4Edit extends ContextMenu {

	class CallBack implements DxCallback {

		private DiagNodeVo vo;

		public CallBack(DiagNodeVo vo) {
			this.vo = vo;
		}

		public void onCallback(Map<String, Object> data) {
			vo.getProperties().putAll(data);
			diagram.addDiagNode(vo);
		}
	}
	private FxDiagram diagram;
	private final DiagMenuItem diagNodeMenu;

	private Menu itemMenu;

	public DiagMenu4Edit(FxDiagramEdit editor) {

		this.diagram = editor.getDiagramPane();

		diagNodeMenu = new DiagMenuItem(diagram);

		Menu menu = new Menu("Diagram");
		getItems().add(menu);

		setNodeMenu(menu);

		menu.getItems().add(new SeparatorMenuItem());
		menu.getItems().add(getClearMenuItem());
		menu.getItems().add(new SeparatorMenuItem());
		menu.getItems().add(getNewMenuItem());

		this.setOnHiding(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent e) {
				removeMenu(itemMenu);
				setNode(null);
			}
		});

		setOnShowing(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent e) {
				addMenu(itemMenu);
			}
		});
	}

	public void setNode(Object node) {

		if (node == null) {
			itemMenu = null;
		} else if (node instanceof DiagNode) {
			diagNodeMenu.setNode((DiagNode) node);
			itemMenu = diagNodeMenu;
		}
	}

	private void addMenu(Menu menu) {

		if (menu == null) {
			return;
		}

		if (getItems().contains(menu) == false) {
			getItems().add(0, new SeparatorMenuItem());
			getItems().add(0, menu);
		}
	}

	private MenuItem getClearMenuItem() {
		MenuItem menu = new MenuItem("Clear");
		menu.setOnAction((ActionEvent e) -> {
			diagram.getChildren().clear();
		});

		return menu;
	}

	private MenuItem getNewMenuItem() {
		MenuItem menu = new MenuItem("New");
		menu.setOnAction((ActionEvent e) -> {
			diagram.setDiagram(null);
		});

		return menu;
	}

	private double getNodeX() {
		return DiagNodeBounds.makeGridUnit(getAnchorX() - diagram.getScene().getWindow().getX());
	}

	private double getNodeY() {
		return DiagNodeBounds.makeGridUnit(getAnchorY() - diagram.getScene().getWindow().getY());
	}

	private void removeMenu(Menu menu) {

		if (menu == null) {
			return;
		}

		if (getItems().contains(menu)) {
			getItems().remove(0);
			getItems().remove(0);
		}
	}

	private void setNodeMenu(Menu parent) {

		for (NODE_TYPE t : NODE_TYPE.values()) {
			FxMenuItem item = new FxMenuItem(t.getOpName()) {
				@Override
				protected void onAction() {
					getOpCode().showDialog(diagram, null, null,
							new CallBack(new DiagNodeVo(t.name(), getNodeX(), getNodeY(), 240, 48)));
				}
			};
			parent.getItems().add(item);
		}

		FxMenuItem item = new FxMenuItem(OP_NAME.DiagramLineEdit) {
			@Override
			protected void onAction() {
				diagram.getScene().setCursor(ImagePointer.getLineCursor());
			}
		};
		parent.getItems().add(item);

	}
}
