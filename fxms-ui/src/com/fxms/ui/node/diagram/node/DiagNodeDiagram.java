package com.fxms.ui.node.diagram.node;

import java.util.Map;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.node.diagram.vo.DiagNodeVo;

public class DiagNodeDiagram extends DiagNodeBase {

	private int diagNo;

	public int getDiagNo() {
		return diagNo;
	}

	@Override
	public DiagNodeVo getDiagNodeVo() {

		DiagNodeVo vo = super.getDiagNodeVo();

		vo.getProperties().put("diagNo", diagNo);

		return vo;

	}

	@Override
	public OP_NAME getOpName() {
		return OP_NAME.DiagramSubEdit;
	}

	public void setDiagNo(int diagNo) {
		this.diagNo = diagNo;
	}

	@Override
	public void setProperties(Map<String, Object> properties) {

		super.setProperties(properties);

		Object value;

		value = properties.get("diagNo");
		if (value != null) {
			try {
				diagNo = Double.valueOf(value.toString()).intValue();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public String toString() {
		return "diagram:" + diagNo;
	}
}
