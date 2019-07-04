package fxms.bas.po;

import java.io.Serializable;

import fxms.bas.mo.Mo;

/**
 * Value Object
 * 
 * @author subkjh
 *
 */
public class PsVo implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1945522615966979370L;

	/** 필터링을 함 */
	public static final byte FILTERING = 0;
	/** 필터링 안함 */
	public static final byte FILTERING_NO = 1;

	/** 관리대상 */
	private long moNo;
	/** 인스턴스 */
	private String moInstance;
	/** 성능번호 */
	private String psCode;
	/** 성능값 */
	private Number value;

	public PsVo() {

	}

	public PsVo(Mo mo, String instance, String psCode, Number value) {
		this(mo.getMoNo(), instance, psCode, value);
	}
	
	public PsVo(Mo mo, String instance, Enum<?> psCode, Number value) {
		this(mo.getMoNo(), instance, psCode.name(), value);
	}
	
	public PsVo(long moNo, String instance, String psCode, Number value) {
		this.moNo = moNo;
		this.moInstance = instance;
		this.psCode = psCode;
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

	public String getMoInstance() {
		return moInstance;
	}

	public long getMoNo() {
		return moNo;
	}

	public String getPsCode() {
		return psCode;
	}

	public Number getValue() {
		return value;
	}

	public void setMoInstance(String instance) {
		this.moInstance = instance;
	}

	public void setMoNo(long moNo) {
		this.moNo = moNo;
	}

	public void setPsCode(String psCode) {
		this.psCode = psCode;
	}

	public void setValue(Number value) {
		this.value = value;
	}

	@Override
	public String toString() {

		StringBuffer sb = new StringBuffer();
		sb.append(getMoNo());
		if (moInstance != null && moInstance.length() > 0)
			sb.append("." + moInstance);
		sb.append("." + psCode );
		sb.append("=" + value);

		return sb.toString();
	}
}
