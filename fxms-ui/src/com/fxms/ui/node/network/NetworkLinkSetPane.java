package com.fxms.ui.node.network;

import com.fxms.ui.bas.editor.FxEditor;
import com.fxms.ui.bas.editor.FxEditor.EDITOR_TYPE;
import com.fxms.ui.bas.editor.QidEditor;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class NetworkLinkSetPane extends BorderPane {

	private QidEditor westPort;
	private QidEditor eastPort;

	public NetworkLinkSetPane(long westNeMoNo, long eastNeMoNo) {

		westPort = (QidEditor) FxEditor.makeEditor("westPortMoNo", EDITOR_TYPE.Qid.name(),
				"SELECT_NE_IF_LIST?upperMoNo=" + westNeMoNo, "WEST 포트선택", 200, null);
		eastPort = (QidEditor) FxEditor.makeEditor("eastPortMoNo", EDITOR_TYPE.Qid.name(),
				"SELECT_NE_IF_LIST?upperMoNo=" + eastNeMoNo, "EAST 포트선택", 200, null);

		HBox hbox = new HBox(10);
		hbox.getChildren().addAll(westPort, eastPort);

		setCenter(hbox);
	}

	public long getWestIfMoNo() {
		String id = westPort.getAttrId();

		try {
			return Double.valueOf(id).longValue();
		} catch (Exception e) {
			return -1;
		}

	}

	public long getEastIfMoNo() {
		String id = eastPort.getAttrId();

		try {
			return Double.valueOf(id).longValue();
		} catch (Exception e) {
			return -1;
		}

	}
}
