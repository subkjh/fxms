package com.fxms.ui.dx.item;

import java.util.Map;

import com.fxms.ui.bas.property.DxNode;
import com.fxms.ui.bas.vo.ui.UiBasicVo;
import com.fxms.ui.css.image.ImagePointer;

import javafx.scene.image.ImageView;

public class DxItemImage extends ImageView implements DxNode {

	private String imageName;

	public DxItemImage() {

	}

	@Override
	public void onAddedInParent() {

	}

	@Override
	public void onRemovedFromParent() {

	}

	@Override
	public boolean initDxNode(UiBasicVo vo) {
		Object value;

		Map<String, Object> properties = vo.getProperties();

		value = properties.get("image");
		if (value != null) {
			imageName = value.toString();
		} else {
			imageName = "fxms.png";
		}

		try {
			setImage(ImagePointer.getImage(imageName));
		} catch (Exception e) {
			e.printStackTrace();
		}

		value = properties.get("opacity");
		if (value != null) {
			try {
				setOpacity(Double.valueOf(value.toString()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (vo.getPropertyBoolean("fit")) {
			this.setFitWidth(vo.getUiWidth());
			this.setFitHeight(vo.getUiHeight());
		}

		return true;
	}	
}