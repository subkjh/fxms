package com.fxms.ui.node.diagram.node;

import java.util.Map;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.node.diagram.vo.DiagNodeVo;

public class DiagNodeInlo extends DiagNodeBase {

	private int inloNo;

	@Override
	public DiagNodeVo getDiagNodeVo() {

		DiagNodeVo vo = super.getDiagNodeVo();

		vo.getProperties().put("inloNo", inloNo);
		return vo;
	}

	public int getInloNo() {
		return inloNo;
	}

	@Override
	public OP_NAME getOpName() {
		return OP_NAME.DiagramInloEdit;
	}

	public void setInloNo(int inloNo) {
		this.inloNo = inloNo;
	}

	@Override
	public void setProperties(Map<String, Object> properties) {

		super.setProperties(properties);

		Object value;

		value = properties.get("inloNo");
		if (value != null) {
			try {
				inloNo = Double.valueOf(value.toString()).intValue();
			} catch (Exception e) {
				inloNo = -1;
			}
		}

	}
}
