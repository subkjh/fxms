package fxms.bas.mo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import subkjh.bas.co.utils.ObjectUtil;
import fxms.bas.co.noti.FxEventImpl;
import fxms.bas.fxo.FxObject;
import fxms.bas.mo.property.Moable;

/**
 * Managed Object
 * 
 * @author subkjh
 *
 */

public class Mo extends FxEventImpl implements FxObject, Cloneable, Serializable, Moable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6972183176049221646L;

	public static final String MO_CLASS = "MO";

	public static final int STATUS_ON = 1;
	public static final int STATUS_OFF = 0;
	public static final int STATUS_UNKNOWN = -1;

	public static void copy(Mo src, Mo dst) {
		dst.setEventType(src.getEventType());
		dst.setMoClass(src.getMoClass());
		dst.setMoName(src.getMoName());
		dst.setMoNo(src.getMoNo());
		dst.setStatus(src.getStatus());
		dst.setUpperMoNo(src.getUpperMoNo());
	}

	public static Object getValue(Mo mo, String fieldName) {
		Object value = mo.getProperties().get(fieldName);
		if (value == null) {
			value = ObjectUtil.get(mo, fieldName);
		}
		return value;
	}

	public static String makeMoKey(long upperMoNo, String moClass, String moName) {
		return upperMoNo + "|" + moClass + "|" + moName;
	}

	public static String makeMoKey(Mo mo) {
		return mo.upperMoNo + "|" + mo.moClass + "|" + mo.moName;
	}

	/** MO번호 */
	private long moNo;

	/** MO명 */
	private String moName;

	/** MO분류 */
	private String moClass;

	/** 관리여부 */
	private boolean mngYn = true;

	/** 상위MO번호 */
	private long upperMoNo = 0;

	private String moKey;

	/** 기타 속성 */
	private Map<String, Object> etcPros;

	public Mo() {
		setEventType("mo");
	}

	public boolean equalMo(Mo mo) {
		return getMoKey().equals(mo.getMoKey());
	}

	/**
	 * MO분류
	 * 
	 * @return MO분류
	 */
	public String getMoClass() {
		return moClass;
	}

	public String getMoKey() {
		if (moKey == null) {
			moKey = Mo.makeMoKey(this);
		}
		return moKey;
	}

	/**
	 * MO명
	 * 
	 * @return MO명
	 */
	public String getMoName() {
		return moName;
	}

	public long getMoNo() {
		return moNo;
	}

	public Map<String, Object> getProperties() {
		if (etcPros == null) {
			etcPros = new HashMap<String, Object>();
		}

		return etcPros;
	}

	public long getUpperMoNo() {
		return upperMoNo;
	}

	/**
	 * 관리여부
	 * 
	 * @return 관리여부
	 */
	public boolean isMngYn() {
		return mngYn;
	}

	/**
	 * 관리여부
	 * 
	 * @param mngYn
	 *            관리여부
	 */
	public void setMngYn(boolean mngYn) {
		this.mngYn = mngYn;
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
	 * MO명
	 * 
	 * @param moName
	 *            MO명
	 */
	public void setMoName(String moName) {
		this.moName = moName;
	}

	/**
	 * MO번호
	 * 
	 * @param moNo
	 *            MO번호
	 */
	public void setMoNo(long moNo) {
		this.moNo = moNo;
	}

	/**
	 * 상위MO번호
	 * 
	 * @param upperMoNo
	 *            상위MO번호
	 */
	public void setUpperMoNo(long upperMoNo) {
		this.upperMoNo = upperMoNo;
	}

	public void setUserChgAttr(Mo moOld) {

		setUpperMoNo(moOld.getUpperMoNo());
		setMoNo(moOld.getMoNo());
		setMngYn(moOld.isMngYn());

	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("MO(NO(" + moNo + ")NAME(" + moName + ")CLASS(" + getMoClass() + ")");

		if (upperMoNo > 0)
			sb.append("NO-NODE(" + upperMoNo + ")");
		sb.append(")MNG(" + mngYn + ")");

		return sb.toString();
	}
}