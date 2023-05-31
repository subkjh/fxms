package fxms.bas.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fxms.bas.fxo.FxmsUtil;

public class Inlo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1422788650778102260L;

	public static final int TOP_INLO_NO = 1;
	/** 미지정 : 0 */
	public static final int NONE_INLO_NO = 0;
	/** FxMS : 1 */
	public static final int FxMS_INLO_NO = TOP_INLO_NO;

	public static Inlo getTopInlo() {
		Inlo ret = new Inlo(TOP_INLO_NO, "Fxm", "TOP", "TOP", null, 0, null, null);
		return ret;
	}

	/** 설치위치번호 */
	private final int inloNo;

	/** 상위설치위치번호 */
	private final int upperInloNo;

	/** 설치위치명 */
	private final String inloName;

	/** 설치위치전체명 */
	private final String inloAllName;

	/** 설치위치종류(코드집) */
	private final String inloClCd;

	private final String inloTypeCd;

	private final String inloLevelCd;

	private final String inloTid;

	private List<Integer> children;

	public Inlo(Map<String, Object> datas) throws Exception {
		this.inloNo = FxmsUtil.getInt(datas, "inloNo", -1);
		this.inloName = FxmsUtil.getString(datas, "inloName", null);
		this.inloClCd = FxmsUtil.getString(datas, "inloClCd", null);
		this.inloTypeCd = FxmsUtil.getString(datas, "inloTypeCd", null);
		this.inloLevelCd = FxmsUtil.getString(datas, "inloLevelCd", null);
		this.upperInloNo = FxmsUtil.getInt(datas, "upperInloNo", -1);
		this.inloAllName = FxmsUtil.getString(datas, "inloAllName", null);
		this.inloTid = FxmsUtil.getString(datas, "inloTid", null);
	}

	public Inlo(int inloNo, String inloName, String inloClCd, String inloTypeCd, String inloLevelCd, int upperInloNo,
			String inloAllName, String inloTid) {
		this.inloNo = inloNo;
		this.inloName = inloName;
		this.inloClCd = inloClCd;
		this.inloTypeCd = inloTypeCd;
		this.inloLevelCd = inloLevelCd;
		this.upperInloNo = upperInloNo;
		this.inloAllName = inloAllName;
		this.inloTid = inloTid;
	}

	public String getInloClCd() {
		return inloClCd;
	}

	public String getInloLevelCd() {
		return inloLevelCd;
	}

	public String getInloName() {
		return inloName;
	}

	public int getInloNo() {
		return inloNo;
	}

	public String getInloNoStr() {
		return String.valueOf(inloNo);
	}

	public String getInloTypeCd() {
		return inloTypeCd;
	}

	public int getUpperInloNo() {
		return upperInloNo;
	}

	public String toString() {
		return "INLO(" + inloNo + ":" + inloAllName + ")";
	}

	public String getInloAllName() {
		return inloAllName;
	}

	public String getInloTid() {
		return inloTid;
	}

	public void addChild(int inloNo) {

		// 자신이면 자식에 넣지 않는다.
		if (this.inloNo == inloNo)
			return;

		if (this.children == null) {
			this.children = new ArrayList<>();
		}

		this.children.add(inloNo);
	}

	public boolean isContains(int inloNo) {

		if (this.inloNo == inloNo) {
			return true;
		}

		if (this.children == null) {
			return false;
		}

		return this.children.contains(inloNo);
	}
}
