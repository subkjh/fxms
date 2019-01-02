package com.fxms.ui.dx.item;

import com.fxms.ui.bas.property.DxNodeMulti;
import com.fxms.ui.bas.vo.ui.UiBasicVo;
import com.fxms.ui.node.diagram.node.DiagNode;

import javafx.scene.control.Label;

public class DxItemText extends Label implements DxNodeMulti {

	public DxItemText() {
	}

	@Override
	public String getNodeTag() {
		return getText();
	}

	@Override
	public boolean initDxNode(UiBasicVo vo) {

		String text = vo.getProperty("text");
		if (text == null) {
			text = "Empty";
		}

		setText(vo.getProperty("text"));

		DiagNode.setAttributes2Node(vo.getProperties(), this);

		return true;
	}

	@Override
	public void onAddedInParent() {

	}

	@Override
	public void onRemovedFromParent() {

	}

}
