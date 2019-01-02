package com.fxms.ui.bas.code;

import com.fxms.ui.PS_TYPE;
import com.fxms.ui.dx.item.chart.DxItemStackedBarChart;
import com.fxms.ui.dx.item.chart.FxChart;

import fxms.client.FxmsClient;

public class UiChartVo implements Cloneable {

	private String chartId;

	private String chartName;

	private String chartJavaClass;

	private String moClass;

	private String moType;

	private String psCodes;

	private String psType;

	private String dataRange;

	private int seqBy;

	public UiChartVo() {
	}

	public Object clone() {
		try {
			return super.clone();
		} catch (Exception e) {
			return null;
		}
	}

	public String getChartId() {
		return chartId;
	}

	public FxChart makeChart() {
		FxChart node;
		try {
			node = (FxChart) Class.forName(getChartJavaClass()).newInstance();
		} catch (Exception e2) {
			node = new DxItemStackedBarChart();
		}

		return node;
	}

	public String getChartJavaClass() {
		return chartJavaClass;
	}

	/**
	 * 챠트명
	 * 
	 * @return 챠트명
	 */
	public String getChartName() {
		return chartName;
	}

	/**
	 * 데이터범위
	 * 
	 * @return 데이터범위
	 */
	public String getDataRange() {
		return dataRange;
	}

	public long getEndDate() {
		long mstime = System.currentTimeMillis();
		PS_TYPE ps_type = PS_TYPE.getPsType(psType);
		mstime = (mstime / (ps_type.getSecGap() * 1000L)) * (ps_type.getSecGap() * 1000L);
		return PS_TYPE.getHstimeByMstime(ps_type.getMstimeStart(mstime));
	}

	/**
	 * MO분류
	 * 
	 * @return MO분류
	 */
	public String getMoClass() {
		return moClass;
	}

	/**
	 * MO종류
	 * 
	 * @return MO종류
	 */
	public String getMoType() {
		return moType;
	}

	/**
	 * 상태값번호
	 * 
	 * @return 상태값번호
	 */
	public String getPsCodes() {
		return psCodes;
	}

	/**
	 * 자료종류
	 * 
	 * @return 자료종류
	 */
	public String getPsType() {
		return psType;
	}

	public int getSeqBy() {
		return seqBy;
	}

	public long getStartDate() {

		StringBuffer sb = new StringBuffer();
		for (char ch : dataRange.toCharArray()) {
			if (ch >= '0' && ch <= '9') {
				sb.append(ch);
			}
		}

		int cnt = Integer.valueOf(sb.toString());
		long mstime = System.currentTimeMillis();

		if (dataRange.toLowerCase().indexOf("day") >= 0) {
			mstime = (mstime / 86400000L) * 86400000L;
			return FxmsClient.getDate(mstime - (86400000L * cnt));
		} else if (dataRange.toLowerCase().indexOf("hour") >= 0) {
			mstime = (mstime / 3600000L) * 3600000L;
			return FxmsClient.getDate(mstime - (3600000L * cnt));
		} else {
			PS_TYPE ps_type = PS_TYPE.getPsType(psType);
			mstime = (mstime / (ps_type.getSecGap() * 1000L)) * (ps_type.getSecGap() * 1000L);
			return ps_type.getMstimeStart(mstime) - ps_type.getSecGap() * 1000L * cnt;
		}

	}

	public void setChartId(String chartId) {
		this.chartId = chartId;
	}

	public void setChartJavaClass(String chartJavaClass) {
		this.chartJavaClass = chartJavaClass;
	}

	/**
	 * 챠트명
	 * 
	 * @param chartName
	 *            챠트명
	 */
	public void setChartName(String chartName) {
		this.chartName = chartName;
	}

	/**
	 * 데이터범위
	 * 
	 * @param dataRange
	 *            데이터범위
	 */
	public void setDataRange(String dataRange) {
		this.dataRange = dataRange;
	}

	/**
	 * MO분류
	 * 
	 * @param moClass
	 *            MO분류
	 */
	public void setMoClass(String moClass) {
		this.moClass = moClass;
	}

	/**
	 * MO종류
	 * 
	 * @param moType
	 *            MO종류
	 */
	public void setMoType(String moType) {
		this.moType = moType;
	}

	/**
	 * 상태값번호
	 * 
	 * @param psCodes
	 *            상태값번호
	 */
	public void setPsCodes(String psCodes) {
		this.psCodes = psCodes;
	}

	/**
	 * 자료종류
	 * 
	 * @param psType
	 *            자료종류
	 */
	public void setPsType(String psType) {
		this.psType = psType;
	}

	public void setSeqBy(int seqBy) {
		this.seqBy = seqBy;
	}

}
