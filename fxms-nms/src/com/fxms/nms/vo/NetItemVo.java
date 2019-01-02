package com.fxms.nms.vo;

import com.fxms.nms.dbo.FN_NET_ITEM;

public class NetItemVo extends FN_NET_ITEM {

	private String startMoNos = null;

	public String getStartMoNos() {
		if (startMoNos == null) {

			StringBuffer sb = new StringBuffer();
			sb.append(getStartNeMoNo());
			if (getStartIfMoNo() > 0) {
				sb.append(".");
				sb.append(getStartIfMoNo());
			}

			startMoNos = sb.toString();
		}

		return startMoNos;
	}

	private String endMoNos = null;

	public String getEndMoNos() {
		if (endMoNos == null) {

			StringBuffer sb = new StringBuffer();
			sb.append(getStartNeMoNo());
			if (getStartIfMoNo() > 0) {
				sb.append(".");
				sb.append(getStartIfMoNo());
			}

			endMoNos = sb.toString();
		}

		return endMoNos;
	}
}
