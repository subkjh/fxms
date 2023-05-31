package fxms.bas.vo;

import java.io.Serializable;

import fxms.bas.mo.Moable;

/**
 * 유효한 PsVoRaw
 * 
 * @author subkjh
 *
 */
public class PsVo implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8635138098923536014L;

	/** 관리대상 */
	private final Moable mo;
	/** 인스턴스 */
	private String moInstance;
	/** 성능번호 */
	private final PsItem psItem;
	/** 성능값 */
	private final Number value;

	public PsVo(Number value, Moable mo, PsItem psItem, String moInstance) {
		this.mo = mo;
		this.psItem = psItem;
		this.value = value;
		this.moInstance = moInstance;
	}

	public String getMoInstance() {
		return moInstance;
	}

	public PsItem getPsItem() {
		return psItem;
	}

	public Number getValue() {
		return value;
	}

	public Moable getMo() {
		return mo;
	}

}
