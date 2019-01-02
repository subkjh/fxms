package com.fxms.ui.bas.mo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fxms.ui.bas.utils.ObjectUtil;

import fxms.client.log.Logger;

public class Mo {

	public static final String MO_CLASS = "MO";

	public static final int STATUS_ON = 1;

	public static final int STATUS_OFF = 0;

	public static final int STATUS_UNKNOWN = -1;

	public static Class<? extends Mo> getMoClass(String moClass) {

		if (ContainerMo.MO_CLASS.equals(moClass)) {
			return ContainerMo.class;
		} else if (UiDeviceMo.MO_CLASS.equals(moClass)) {
			return UiDeviceMo.class;
		} else if (PbrMo.MO_CLASS.equals(moClass)) {
			return PbrMo.class;
		} else if (GwMo.MO_CLASS.equals(moClass)) {
			return GwMo.class;
		} else if (NeMo.MO_CLASS.equals(moClass)) {
			return NeMo.class;
		} else if (NeIfMo.MO_CLASS.equals(moClass)) {
			return NeIfMo.class;
		}

		return Mo.class;
	}

	private static final String[] LEAF_MO_CLASS = new String[] { UiDeviceMo.MO_CLASS, NeIfMo.MO_CLASS, PbrMo.MO_CLASS };

	/**
	 * 
	 * @param mo
	 * @return 말단 MO 여부
	 */
	public static boolean isLeaf(Mo mo) {

		for (String moClass : LEAF_MO_CLASS) {
			if (moClass.equals(mo.getMoClass())) {
				return true;
			}
		}

		return false;
	}

	public static Mo makeMo(Map<String, Object> map) {

		Mo mo;
		try {
			mo = getMoClass((String) map.get("moClass")).newInstance();
			ObjectUtil.toObject(map, mo);
			return mo;
		} catch (Exception e) {
			return null;
		}
	}

	public static List<Mo> makeMoList(List<Map<String, Object>> list) {

		List<Mo> ret = new ArrayList<Mo>();
		Mo mo;
		try {
			for (Map<String, Object> map : list) {
				mo = makeMo(map);
				if (mo != null) {
					ret.add(mo);
				}
			}
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Mo>();
		}
	}

	private long moNo;

	private String moName;

	private String moAname;

	private String moClass;

	private String moMemo;

	private String moType;

	private boolean mngYn = true;

	private long upperMoNo = 0;

	private int alarmCfgNo = -1;

	private int regUserNo = 0;

	private long regDate;

	private int chgUserNo = 0;

	private long chgDate;

	private int syncUserNo = 0;

	private long syncDate;

	public Mo() {

	}

	public Mo(String moAname) {
		this.moAname = moAname;
	}

	/**
	 * 경보조건번호
	 * 
	 * @return 경보조건번호
	 */
	public int getAlarmCfgNo() {
		return alarmCfgNo;
	}

	/**
	 * 수정일시
	 * 
	 * @return 수정일시
	 */
	public long getChgDate() {
		return chgDate;
	}

	/**
	 * 수정운영자번호
	 * 
	 * @return 수정운영자번호
	 */
	public int getChgUserNo() {
		return chgUserNo;
	}

	/**
	 * MO표시명
	 * 
	 * @return MO표시명
	 */
	public String getMoAname() {
		return moAname;
	}

	/**
	 * MO분류
	 * 
	 * @return MO분류
	 */
	public String getMoClass() {
		return moClass;
	}

	public String getMoMemo() {
		return moMemo;
	}

	/**
	 * MO명
	 * 
	 * @return MO명
	 */
	public String getMoName() {
		return moName;
	}

	/**
	 * MO번호
	 * 
	 * @return MO번호
	 */
	public long getMoNo() {
		return moNo;
	}

	public String getMoType() {
		return moType;
	}

	/**
	 * 등록일시
	 * 
	 * @return 등록일시
	 */
	public long getRegDate() {
		return regDate;
	}

	/**
	 * 등록운영자번호
	 * 
	 * @return 등록운영자번호
	 */
	public int getRegUserNo() {
		return regUserNo;
	}

	public String getRemarks() {
		return moMemo;
	}

	/**
	 * 동기화일시
	 * 
	 * @return 동기화일시
	 */
	public long getSyncDate() {
		return syncDate;
	}

	/**
	 * 수정운영자번호
	 * 
	 * @return 수정운영자번호
	 */
	public int getSyncUserNo() {
		return syncUserNo;
	}

	/**
	 * 상위MO번호
	 * 
	 * @return 상위MO번호
	 */
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
	 * 경보조건번호
	 * 
	 * @param alarmCfgNo
	 *            경보조건번호
	 */
	public void setAlarmCfgNo(int alarmCfgNo) {
		this.alarmCfgNo = alarmCfgNo;
	}

	/**
	 * 수정일시
	 * 
	 * @param chgDate
	 *            수정일시
	 */
	public void setChgDate(long chgDate) {
		this.chgDate = chgDate;
	}

	/**
	 * 수정운영자번호
	 * 
	 * @param chgUserNo
	 *            수정운영자번호
	 */
	public void setChgUserNo(int chgUserNo) {
		this.chgUserNo = chgUserNo;
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
	 * MO표시명
	 * 
	 * @param moAname
	 *            MO표시명
	 */
	public void setMoAname(String moAname) {
		this.moAname = moAname;
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

	public void setMoMemo(String moMemo) {
		this.moMemo = moMemo;
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

	public void setMoType(String moType) {
		this.moType = moType;
	}

	/**
	 * 등록일시
	 * 
	 * @param regDate
	 *            등록일시
	 */
	public void setRegDate(long regDate) {
		this.regDate = regDate;
	}

	/**
	 * 등록운영자번호
	 * 
	 * @param regUserNo
	 *            등록운영자번호
	 */
	public void setRegUserNo(int regUserNo) {
		this.regUserNo = regUserNo;
	}

	/**
	 * 동기화일시
	 * 
	 * @param syncDate
	 *            동기화일시
	 */
	public void setSyncDate(long syncDate) {
		this.syncDate = syncDate;
	}

	/**
	 * 수정운영자번호
	 * 
	 * @param syncUserNo
	 *            수정운영자번호
	 */
	public void setSyncUserNo(int syncUserNo) {
		this.syncUserNo = syncUserNo;
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

		// 기존 별칭을 유지합니다.
		if (moOld.getMoAname() != null && moOld.getMoAname().trim().length() > 0) {
			setMoAname(moOld.getMoAname());
		}
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(moNo);
		if (getMoName() != null) {
			sb.append(". ");
			sb.append(getMoName());
		}

		return sb.toString();
	}
}