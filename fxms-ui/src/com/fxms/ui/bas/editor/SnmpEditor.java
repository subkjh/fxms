package com.fxms.ui.bas.editor;

import java.util.Map;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.bas.pane.EditorTabPane;
import com.fxms.ui.bas.utils.ObjectUtil;
import com.fxms.ui.bas.vo.SnmpPass;

public class SnmpEditor extends EditorTabPane implements FxEditor {

	private SnmpPass snmpPass = new SnmpPass();

	public SnmpEditor() {
		init(CodeMap.getMap().getOpCode(OP_NAME.EtcSnmpConfigEdit));
	}

	@Override
	public void clearEditor() {
	}

	@Override
	public String getAttrId() {
		ObjectUtil.toObject(getInputData(), snmpPass);
		return snmpPass.getSnmpString();
	}

	@Override
	public void setAttrId(Object id, Map<String, Object> objectData) {
		try {
			snmpPass.setSnmpString(id == null ? "" : id.toString());
			this.initData(snmpPass.getMap());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init(String attrValueList, String promptText, int width) {

	}

}
