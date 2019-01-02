package com.fxms.ui.node.network;

import java.util.HashMap;
import java.util.Map;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.bas.code.UiOpCodeVo;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * 네트워크 모니터링용 노드
 * 
 * @author SUBKJH-DEV
 *
 */
public class NetworkNodeM extends NetworkNode {

	public NetworkNodeM(long moNo) {

		super(moNo);

		setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				UiOpCodeVo opcode = CodeMap.getMap().getOpCode(OP_NAME.MoTreeShow);
				if (opcode != null) {
					Map<String, Object> data = new HashMap<String, Object>();
					data.put("moNo", moNo);
					opcode.showScreen(NetworkNodeM.this, data, null, null);
				}
			}
		});
	}
}
