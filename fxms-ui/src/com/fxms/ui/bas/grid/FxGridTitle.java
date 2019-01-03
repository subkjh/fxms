package com.fxms.ui.bas.grid;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class FxGridTitle extends BorderPane {

	private Label title;
	private Button closeBtn;

	public FxGridTitle(FxGridPane fxGridPane, String title) {

		getStyleClass().add("fx-grid-title");

		this.title = new Label(title);
		this.closeBtn = new Button("X");
		closeBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				FxGridPane parentPane = (FxGridPane)fxGridPane.getProperties().get(FxGridPane.class);
				if ( parentPane != null	 ) {
					parentPane.remove(fxGridPane.getId());
				}
			}
		});
		
		
		setCenter(this.title);
		setRight(closeBtn);
	}

}
