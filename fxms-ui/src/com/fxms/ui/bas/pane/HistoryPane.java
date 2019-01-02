package com.fxms.ui.bas.pane;

import com.fxms.ui.bas.FxWindow;
import com.fxms.ui.bas.code.UiOpCodeVo;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.transform.Scale;

public class HistoryPane extends ScrollPane {

	private HBox box = new HBox(8);
	private int size = 100;

	public HistoryPane() {
		box.setPadding(new Insets(8, 8, 8, 8));
		setContent(box);

		this.setPrefSize(1000, size + 24);
	}

	public void add(Region node, UiOpCodeVo opCode) {
		SnapshotParameters snapshotParameters = new SnapshotParameters();
		snapshotParameters.setTransform(new Scale(size / node.getWidth(), size / node.getHeight()));
		WritableImage image = new WritableImage(size, size);
		image = node.snapshot(snapshotParameters, image);
		Button btn = new Button("", new ImageView(image));
		btn.setUserData(node);
		Tooltip.install(node, new Tooltip(opCode.getOpTitle()));

		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				FxWindow.showStage(HistoryPane.this, node, opCode.getOpTitle());
			}
		});
		box.getChildren().add(btn);
	}

//	 // TODO: probably use a file chooser here
//    File file = new File("chart.png");
//
//    try {
//        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
//    } catch (IOException e) {
//        // TODO: handle exception here
//    }
}
