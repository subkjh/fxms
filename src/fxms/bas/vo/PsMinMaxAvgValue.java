package fxms.bas.vo;

public class PsMinMaxAvgValue {

	private Long psDate;
	private Float minValue;
	private Float maxValue;
	private Float avgValue;

	@Override
	public String toString() {

		StringBuffer sb = new StringBuffer();
		sb.append("date=").append(psDate);
		sb.append(",min=").append(minValue).append(",max=").append(maxValue).append(",avg=").append(avgValue);
		return sb.toString();
	}

	public Float getAvgValue() {
		return avgValue;
	}

	public Float getMaxValue() {
		return maxValue;
	}

	public Float getMinValue() {
		return minValue;
	}

	public void setAvgValue(Float avgValue) {
		this.avgValue = avgValue;
	}

	public void setMaxValue(Float maxValue) {
		this.maxValue = maxValue;
	}

	public void setMinValue(Float minValue) {
		this.minValue = minValue;
	}

	public Long getPsDate() {
		return psDate;
	}

	public void setPsDate(Long psDate) {
		this.psDate = psDate;
	}
}
