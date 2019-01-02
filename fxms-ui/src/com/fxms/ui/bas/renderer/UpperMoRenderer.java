package com.fxms.ui.bas.renderer;

import javafx.event.ActionEvent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class UpperMoRenderer extends Label {
	
	public UpperMoRenderer(Object data)
	{
		if ( data == null) {
			setText("-");
			return;
		}
		
		final ContextMenu cm = new ContextMenu();
		
		MenuItem showProperties = new MenuItem("Show Properties");
		
		showProperties.setOnAction((ActionEvent e) -> {
		});

		cm.getItems().add(showProperties);
	
		addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
		    if (e.getButton() == MouseButton.SECONDARY)
		       cm.show(this, e.getScreenX(), e.getScreenY());
		});
		
		getStyleClass().add("has-context-menu");
		setText("MO:" + data.toString());
	}
	
	

}
