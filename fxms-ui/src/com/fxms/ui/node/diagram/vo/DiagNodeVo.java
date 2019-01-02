package com.fxms.ui.node.diagram.vo;

import java.util.HashMap;
import java.util.Map;

import com.fxms.ui.bas.utils.ObjectUtil;
import com.fxms.ui.node.diagram.event.FxBounds;

public class DiagNodeVo implements Cloneable {

	private int diagNo;

	private int diagNodeNo;

	private String diagNodeType;

	private double diagNodeX;

	private double diagNodeY;

	private double diagNodeWidth = 0;

	private double diagNodeHeight = 0;

	private Map<String, Object> properties;
	
	private boolean removed = false;
	
	

	public boolean isRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public DiagNodeVo() {

	}

	public DiagNodeVo(String diagNodeType, double x, double y, double width, double height) {
		this.diagNodeType = diagNodeType;
		diagNodeX = x;
		diagNodeY = y;
		diagNodeWidth = width;
		diagNodeHeight = height;
	}

	public int getDiagNo() {
		return diagNo;
	}

	public double getDiagNodeHeight() {
		return diagNodeHeight;
	}

	public int getDiagNodeNo() {
		return diagNodeNo;
	}

	public String getDiagNodeType() {
		return diagNodeType;
	}

	public double getDiagNodeWidth() {
		return diagNodeWidth;
	}

	public double getDiagNodeX() {
		return diagNodeX;
	}

	public double getDiagNodeY() {
		return diagNodeY;
	}

	public Map<String, Object> getProperties() {
		if (properties == null) {
			properties = new HashMap<String, Object>();
		}
		return properties;
	}

	public void setBounds(FxBounds bounds) {

		if (bounds.getX() > 0) {
			setDiagNodeX(bounds.getX());
		}
		if (bounds.getY() > 0) {
			setDiagNodeY(bounds.getY());
		}

		if (bounds.getHeight() > 0) {
			setDiagNodeHeight(bounds.getHeight());
		}

		if (bounds.getWidth() > 0) {
			setDiagNodeWidth(bounds.getWidth());
		}
	}

	public void setDiagNo(int diagNo) {
		this.diagNo = diagNo;
	}

	public void setDiagNodeHeight(double diagNodeHeight) {
		this.diagNodeHeight = diagNodeHeight;
	}

	public void setDiagNodeNo(int diagNodeNo) {
		this.diagNodeNo = diagNodeNo;
	}

	public void setDiagNodeWidth(double diagNodeWidth) {
		this.diagNodeWidth = diagNodeWidth;
	}

	public void setDiagNodeX(double diagNodeX) {
		this.diagNodeX = diagNodeX;
	}

	public void setDiagNodeY(double diagNodeY) {
		this.diagNodeY = diagNodeY;
	}

	public String toString() {
		return String.valueOf(ObjectUtil.toMap(this));
	}

	public long getMoNo()
	{
		Object value = getProperties().get("moNo");
		if (value != null) {
			try {
				long moNo = Double.valueOf(value.toString()).longValue();
				return moNo;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return -1;
	}
}
