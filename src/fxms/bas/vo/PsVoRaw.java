package fxms.bas.vo;

import java.io.Serializable;

import fxms.bas.mo.Mo;

/**
 * 수집된 원천 데이터
 * 
 * @author subkjh
 *
 */
public class PsVoRaw implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1945522615966979370L;

	/** 필터링을 함 */
	public static final byte FILTERING = 0;
	/** 필터링 안함 */
	public static final byte FILTERING_NO = 1;

	/** 관리대상 */
	private final long moNo;
	/** 성능번호 */
	private final String psId;
	/** 성능값 */
	private final Number value;

	public PsVoRaw(long moNo, String psId, Number value) {
		this.moNo = moNo;
		this.psId = psId;
		this.value = value;
	}

	public PsVoRaw(Mo mo, Enum<?> psId, Number value) {
		this.moNo = mo.getMoNo();
		this.psId = psId.name();
		this.value = value;
	}

	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	public long getMoNo() {
		return moNo;
	}

	public String getPsId() {
		return psId;
	}

	public Number getValue() {
		return value;
	}

	@Override
	public String toString() {

		StringBuffer sb = new StringBuffer();
		sb.append("{moNo=").append(getMoNo());
		sb.append(", psId='").append(psId).append("'");
		sb.append(", value=").append(value).append("}");

		return sb.toString();
	}

}
