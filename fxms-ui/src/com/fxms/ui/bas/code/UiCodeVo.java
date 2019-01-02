package com.fxms.ui.bas.code;

import java.util.Map;

public class UiCodeVo {

	private String cdType;

	private String cdCode;

	private String cdName;

	private String cdDescr;

	private boolean cdEditYn;

	private String val1;

	private String val2;

	private String val3;

	private String val4;

	private String val5;

	private String val6;

	private int seqBy;

	private boolean useYn;

	private String chkQry;

	private String fillQry;

	public boolean match(Map<String, Object> para) {
		if (para == null || para.size() == 0) {
			return true;
		}
		
		for ( String key : para.keySet() ) {
			if ( key.equals("val1") && equals(para.get(key)+"", val1)) return true;
			if ( key.equals("val2") && equals(para.get(key)+"", val2)) return true;
			if ( key.equals("val3") && equals(para.get(key)+"", val3)) return true;
			if ( key.equals("val4") && equals(para.get(key)+"", val4)) return true;
			if ( key.equals("val5") && equals(para.get(key)+"", val5)) return true;
			if ( key.equals("val6") && equals(para.get(key)+"", val6)) return true;
		
		}
		
		return false;
	}
	
	private boolean equals(String v1, String v2)
	{
		if ( v1 == null && v2 == null) {
			return true;
		} else if ( v1 != null && v2 != null) {
			return v1.equals(v2);
		} else {
			return false;
		}
	}

	public UiCodeVo() {
	}

	/**
	 * 코드
	 * 
	 * @return 코드
	 */
	public String getCdCode() {
		return cdCode;
	}

	/**
	 * 설명
	 * 
	 * @return 설명
	 */
	public String getCdDescr() {
		return cdDescr;
	}

	/**
	 * 코드명
	 * 
	 * @return 코드명
	 */
	public String getCdName() {
		return cdName;
	}

	/**
	 * 코드분류
	 * 
	 * @return 코드분류
	 */
	public String getCdType() {
		return cdType;
	}

	/**
	 * 사용중여부 확인 쿼리
	 * 
	 * @return 사용중여부 확인 쿼리
	 */
	public String getChkQry() {
		return chkQry;
	}

	public String getFillQry() {
		return fillQry;
	}

	/**
	 * 정렬순서
	 * 
	 * @return 정렬순서
	 */
	public int getSeqBy() {
		return seqBy;
	}

	/**
	 * 값1
	 * 
	 * @return 값1
	 */
	public String getVal1() {
		return val1;
	}

	/**
	 * 값2
	 * 
	 * @return 값2
	 */
	public String getVal2() {
		return val2;
	}

	/**
	 * 값3
	 * 
	 * @return 값3
	 */
	public String getVal3() {
		return val3;
	}

	/**
	 * 값4
	 * 
	 * @return 값4
	 */
	public String getVal4() {
		return val4;
	}

	/**
	 * 값5
	 * 
	 * @return 값5
	 */
	public String getVal5() {
		return val5;
	}

	/**
	 * 값6
	 * 
	 * @return 값6
	 */
	public String getVal6() {
		return val6;
	}

	/**
	 * 사용여부
	 * 
	 * @return 사용여부
	 */
	public boolean isUseYn() {
		return useYn;
	}

	/**
	 * 코드
	 * 
	 * @param cdCode
	 *            코드
	 */
	public void setCdCode(String cdCode) {
		this.cdCode = cdCode;
	}

	/**
	 * 설명
	 * 
	 * @param cdDescr
	 *            설명
	 */
	public void setCdDescr(String cdDescr) {
		this.cdDescr = cdDescr;
	}

	public boolean isCdEditYn() {
		return cdEditYn;
	}

	public void setCdEditYn(boolean cdEditYn) {
		this.cdEditYn = cdEditYn;
	}

	/**
	 * 코드명
	 * 
	 * @param cdName
	 *            코드명
	 */
	public void setCdName(String cdName) {
		this.cdName = cdName;
	}

	/**
	 * 코드분류
	 * 
	 * @param cdType
	 *            코드분류
	 */
	public void setCdType(String cdType) {
		this.cdType = cdType;
	}

	/**
	 * 사용중여부 확인 쿼리
	 * 
	 * @param chkQry
	 *            사용중여부 확인 쿼리
	 */
	public void setChkQry(String chkQry) {
		this.chkQry = chkQry;
	}

	public void setFillQry(String fillQry) {
		this.fillQry = fillQry;
	}

	/**
	 * 정렬순서
	 * 
	 * @param seqBy
	 *            정렬순서
	 */
	public void setSeqBy(int seqBy) {
		this.seqBy = seqBy;
	}

	/**
	 * 사용여부
	 * 
	 * @param useYn
	 *            사용여부
	 */
	public void setUseYn(boolean useYn) {
		this.useYn = useYn;
	}

	/**
	 * 값1
	 * 
	 * @param val1
	 *            값1
	 */
	public void setVal1(String val1) {
		this.val1 = val1;
	}

	/**
	 * 값2
	 * 
	 * @param val2
	 *            값2
	 */
	public void setVal2(String val2) {
		this.val2 = val2;
	}

	/**
	 * 값3
	 * 
	 * @param val3
	 *            값3
	 */
	public void setVal3(String val3) {
		this.val3 = val3;
	}

	/**
	 * 값4
	 * 
	 * @param val4
	 *            값4
	 */
	public void setVal4(String val4) {
		this.val4 = val4;
	}

	/**
	 * 값5
	 * 
	 * @param val5
	 *            값5
	 */
	public void setVal5(String val5) {
		this.val5 = val5;
	}

	/**
	 * 값6
	 * 
	 * @param val6
	 *            값6
	 */
	public void setVal6(String val6) {
		this.val6 = val6;
	}

	@Override
	public String toString() {
		return cdName;
	}

}
