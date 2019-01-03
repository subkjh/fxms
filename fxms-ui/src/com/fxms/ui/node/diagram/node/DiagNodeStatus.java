package com.fxms.ui.node.diagram.node;

import java.util.Map;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.bas.code.UiPsItemVo;
import com.fxms.ui.node.diagram.vo.DiagNodeVo;

public class DiagNodeStatus extends DiagNodeBase implements DiagNode {

	private long moNo;
	private UiPsItemVo psItem;

	@Override
	public DiagNodeVo getDiagNodeVo() {
		DiagNodeVo vo = super.getDiagNodeVo();

		vo.getProperties().put("moNo", moNo);
		if (psItem != null) {
			vo.getProperties().put("psCode", psItem.getPsCode());
		}

		DiagNode.setNode2Attributes(this, vo.getProperties());

		return vo;
	}

	public long getMoNo() {
		return moNo;
	}

	@Override
	public OP_NAME getOpName() {
		return OP_NAME.DiagramStatusEdit;
	}

	public UiPsItemVo getPsItem() {
		return psItem;
	}

	@Override
	public void setProperties(Map<String, Object> properties) {

		Object value;

		super.setProperties(properties);

		value = properties.get("moNo");
		if (value != null) {
			try {
				moNo = Double.valueOf(value.toString()).longValue();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		value = properties.get("psCode");
		if (value != null) {
			try {
				psItem = CodeMap.getMap().getPsItem(value.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		DiagNode.setAttributes2Node(properties, this);

	}

	public void setStatus(Number status) {
		setText(status == null ? "-" : (status.toString() + (psItem != null ? " " + psItem.getPsUnit() : "")));
	}

	@Override
	public String toString() {
		return "status:" + moNo + "," + (psItem != null ? psItem.getPsName() : "");
	}

}
