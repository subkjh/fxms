package com.fxms.ui.node.tree;

import com.fxms.ui.bas.FxStage;
import com.fxms.ui.bas.mo.Mo;
import com.fxms.ui.dx.FxCallback;
import com.fxms.ui.node.tree.vo.TreeItemVo;

import javafx.scene.Node;

public class FxTreeAllPanelStage {

	private static FxTreeAllPane selector;
	private static FxCallback<Mo> callback;
	private static FxStage stage;

	public static void show(Node parent, String moClass, FxCallback<Mo> callback) {

		if (selector == null) {
			selector = new FxTreeAllPane(moClass) {

				@Override
				protected void onSelected(TreeItemVo treeItemVo) {
					if (FxTreeAllPanelStage.callback != null) {
						if (treeItemVo != null && treeItemVo.getSource() instanceof Mo) {
							Mo mo = (Mo) treeItemVo.getSource();
							callback.onCallback(mo);
							stage.hide();
						}
					}
				}

			};
		}

		FxTreeAllPanelStage.callback = callback;

		stage = FxStage.showStage(parent, selector, "관리대상 선택");

	}

}
