package com.fxms.ui.bas.vo;

import java.util.HashMap;
import java.util.List;

import com.fxms.ui.bas.code.UiPsItemVo;

public class PsValueMap extends HashMap<Object, List<PsValue>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8564388124052284408L;

	private UiPsItemVo psItem;
	private long startDate;
	private long endDate;

	public PsValueMap(UiPsItemVo psItem, long startDate, long endDate) {
		this.psItem = psItem;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public boolean exist(Object key, PsValue value) {

		List<PsValue> valueList = get(key);
		if (valueList != null && valueList.size() > 0) {
			for (PsValue e : valueList) {
				if (e.getTime() == value.getTime()) {
					return true;
				}
			}
		}

		return false;
	}

	public long getEndDate() {
		return endDate;
	}

	public PsValue getLastValue() {
		for (Object key : keySet()) {
			List<PsValue> entry = get(key);
			if (entry != null && entry.size() > 0) {
				return entry.get(entry.size() - 1);
			}
		}
		return null;
	}

	public UiPsItemVo getPsItem() {
		return psItem;
	}

	public long getStartDate() {
		return startDate;
	}
}
