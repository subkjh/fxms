package com.fxms.ui.bas.vo.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UiGroupVo {

	private int uiGroupNo;

	private String uiGroupName;

	private boolean visibleYn = true;

	private int seqBy;

	private List<UiBasicVo> children;

	public Map<String, Object> getSaveMap() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		List<UiPropertyVo> properties = new ArrayList<UiPropertyVo>();

		map.put("group", this);
		map.put("basic-list", getChildren());

		for (UiBasicVo vo : getChildren()) {
			properties.addAll(vo.getChildren());
		}

		map.put("property-list", properties);

		return map;
	}

	public List<UiBasicVo> getChildren() {
		if (children == null) {
			children = new ArrayList<UiBasicVo>();
		}
		return children;
	}

	public int getUiGroupNo() {
		return uiGroupNo;
	}

	public void setUiGroupNo(int uiGroupNo) {
		this.uiGroupNo = uiGroupNo;
	}

	public String getUiGroupName() {
		return uiGroupName;
	}

	public void setUiGroupName(String uiGroupName) {
		this.uiGroupName = uiGroupName;
	}

	public boolean isVisibleYn() {
		return visibleYn;
	}

	public void setVisibleYn(boolean visibleYn) {
		this.visibleYn = visibleYn;
	}

	public int getSeqBy() {
		return seqBy;
	}

	public void setSeqBy(int seqBy) {
		this.seqBy = seqBy;
	}

}
