package com.fxms.ui.node.diagram.node;

import java.util.Map;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.node.diagram.vo.DiagNodeVo;

public class DiagNodeMo extends DiagNodeBase {

	private long moNo;

	@Override
	public DiagNodeVo getDiagNodeVo() {

		DiagNodeVo vo = super.getDiagNodeVo();

		vo.getProperties().put("moNo", moNo);
		return vo;
	}

	public long getMoNo() {
		return moNo;
	}

	@Override
	public OP_NAME getOpName() {
		return OP_NAME.DiagramMoEdit;
	}

	public void setMoNo(long moNo) {
		this.moNo = moNo;
	}

	@Override
	public void setProperties(Map<String, Object> properties) {

		super.setProperties(properties);

		Object value;

		value = properties.get("moNo");
		if (value != null) {
			try {
				moNo = Double.valueOf(value.toString()).longValue();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		value = properties.get("moName");
		if (value != null) {
			try {
				setText(value.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public String toString() {
		return "mo:" + moNo;
	}

}
