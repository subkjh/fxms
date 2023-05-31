package fxms.bas.vo;

import java.io.Serializable;

/**
 * 통계 생성 요청 노티
 * 
 * @author subkjh(Kim,JongHoon)
 *
 */
public class PsStatReqVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4665542125446131301L;

	private final String psTbl;

	private final String psKindName;

	private final long psDtm;

	public PsStatReqVo(String psTbl, String psKindName, long psDtm) {

		this.psTbl = psTbl;
		this.psKindName = psKindName;
		this.psDtm = psDtm;
	}

	public String getKey() {
		return psTbl + "." + psKindName + "." + psDtm;
	}

	public String getPsTbl() {
		return psTbl;
	}

	public String getPsKindName() {
		return psKindName;
	}

	public long getPsDtm() {
		return psDtm;
	}

}
