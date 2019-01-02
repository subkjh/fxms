package com.fxms.ui.biz.menu;

import com.fxms.ui.biz.main.DxMainPane;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class DxContextMenu extends ContextMenu {

	private DxMainPane pane;

	public DxContextMenu(DxMainPane pane) {

		this.pane = pane;

		MenuItem miAddWorks = new MenuItem("Add Works");

		miAddWorks.setOnAction((ActionEvent e) -> {
			System.out.println("Add Works");
		});

		getItems().add(miAddWorks);

		getItems().add(new SeparatorMenuItem());

		getItems().add(getViewMenu());

		getItems().add(new SeparatorMenuItem());

		MenuItem miExit = new MenuItem("Exit");

		miExit.setOnAction((ActionEvent e) -> {
			// TODO
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			System.exit(0);
		});

		getItems().add(miExit);
	}

	public void addMenu(Stage stage) {
		stage.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
			if (e.getButton() == MouseButton.SECONDARY)
				show(stage, e.getScreenX(), e.getScreenY());
		});
	}

	private Menu getViewMenu() {
		Menu menuEffect = new Menu("Show ...");

		CheckMenuItem viewGrid = new CheckMenuItem("View Grid");
		viewGrid.setSelected(pane.getGrid().isGridLinesVisible());
		viewGrid.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				pane.getGrid().setGridLinesVisible(viewGrid.isSelected());
			}
		});

		CheckMenuItem showTitle = new CheckMenuItem("View Works Title");
		showTitle.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
			}
		});

		menuEffect.getItems().addAll(viewGrid, showTitle);
		return menuEffect;
	}

}
