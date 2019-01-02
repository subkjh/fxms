package com.fxms.ui.bas.editor;

import java.util.Map;

import com.fxms.ui.bas.FxDialog;
import com.fxms.ui.css.image.ImagePointer;
import com.fxms.ui.dx.FxCallback;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

public class ImageEditor extends HBox implements FxEditor {

	private final ToggleGroup group = new ToggleGroup();
	private final FlowPane imagePane;
	private final ImageView imageView;

	public static void main(String[] args) {

	}

	public ImageEditor() {

		super(5);

		imagePane = getImagePane();
		imageView = new ImageView();
		imageView.setFitHeight(48);
		imageView.setFitWidth(48);

		Button btn = new Button("...");

		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				new FxDialog<Object>(btn, imagePane, "이미지 선택", "선택", "취소", new FxCallback<Object>() {

					@Override
					public void onCallback(Object data) {
						setAttrId(data, null);
					}
				}) {
					@Override
					public Object getDoneValue() {
						return getAttrId();
					}
				};
			}
		});

		getChildren().add(imageView);
		getChildren().add(btn);
	}

	@Override
	public void clearEditor() {
	}

	private FlowPane getImagePane() {

		FlowPane pane = new FlowPane();
		pane.setVgap(8);
		pane.setHgap(4);
		pane.setPrefWrapLength(300); // preferred width = 300

		ToggleButton btn;
		ImageView imageView;

		for (String imageName : ImagePointer.getImageList()) {
			imageView = new ImageView(ImagePointer.getImage(imageName));
			imageView.setFitHeight(48);
			imageView.setFitWidth(48);
			btn = new ToggleButton();
			btn.setToggleGroup(group);
			btn.setUserData(imageName);
			btn.setGraphic(imageView);

			pane.getChildren().add(btn);
		}

		return pane;
	}

	@Override
	public String getAttrId() {
		ToggleButton btn = (ToggleButton) group.getSelectedToggle();
		return btn != null ? btn.getUserData().toString() : null;
	}

	@Override
	public void setAttrId(Object id, Map<String, Object> objectData) {

		if (id == null) {
			return;
		}

		imageView.setImage(ImagePointer.getImage(id.toString()));

		ToggleButton btn;

		for (Node node : getChildren()) {
			if (node instanceof ToggleButton) {
				btn = (ToggleButton) node;
				if (btn.getUserData().toString().equals(id.toString())) {
					btn.setSelected(true);
					return;
				}
			}
		}
	}

	@Override
	public void init(String attrValueList, String promptText, int width) {
		// TODO Auto-generated method stub

	}

}
