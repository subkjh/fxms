package fxms.bas.mo;

import java.lang.reflect.Field;
import java.util.Map;

import subkjh.bas.co.utils.ObjectUtil;

/**
 * 관리대상(Managed Object) 기본
 *
 * @author subkjh
 *
 */
public interface Mo extends Moable {

	public static final long NULL_MO_NO = -1L;

	public static void copy(Mo src, Object dst) {
		ObjectUtil.toMap(src);
		ObjectUtil.toObject(ObjectUtil.toMap(src), dst);
	}

	public static void copy(Object src, Mo dst) {
		ObjectUtil.toMap(src);
		ObjectUtil.toObject(ObjectUtil.toMap(src), dst);
	}

	public static Object getValue(Mo mo, String fieldName) {
		Object value = null; // mo.getProperties().get(fieldName);
		if (value == null) {
			value = ObjectUtil.get(mo, fieldName);
		}
		return value;
	}

	public static String getMoKey(long upperMoNo, String moClass, String moName) {
		return upperMoNo + "|" + moClass + "|" + moName;
	}

	public static String getMoKey(Mo mo) {
		return mo.getUpperMoNo() + "|" + mo.getMoClass() + "|" + mo.getMoName();
	}

	/**
	 * 모델번호
	 *
	 * @return 모델번호
	 */
	public int getModelNo();

	/**
	 * 관리여부
	 *
	 * @return 관리여부
	 */
	public boolean isMngYn();

	/**
	 * 알람조건번호
	 *
	 * @param alarmCfgNo 알람조건번호
	 */
	public void setAlarmCfgNo(int alarmCfgNo);

	/**
	 * 설치위치번호
	 *
	 * @param inloNo 설치위치번호
	 */
	public void setInloNo(int inloNo);

	/**
	 * 관리여부
	 *
	 * @param mngYn 관리여부
	 */
	public void setMngYn(boolean mngYn);

	/**
	 * 모델번호
	 *
	 * @param modelNo 모델번호
	 */
	public void setModelNo(int modelNo);

	/**
	 * MO명
	 *
	 * @param moName MO명
	 */
	public void setMoName(String moName);

	/**
	 * MO번호
	 *
	 * @param moNo MO번호
	 */
	public void setMoNo(long moNo);

	/**
	 * MO유형
	 *
	 * @param moType MO유형
	 */
	public void setMoType(String moType);

	/**
	 * 상위MO번호
	 *
	 * @param upperMoNo 상위MO번호
	 */
	public void setUpperMoNo(long upperMoNo);

	/**
	 *
	 * @param moClass
	 */
	public void setMoClass(String moClass);

	public String getMoTid();

	public void setMoTid(String moTid);

	/**
	 *
	 * @return
	 */
	public Map<String, Object> getAttrMap();

	/**
	 * 속성의 값을 찾는다.
	 *
	 * @param name
	 * @return
	 */
	public Object get(String name);

	public String getMoAddJson();

	public void setMoAddJson(String json);

	/**
	 *
	 * @param mo
	 * @param name
	 * @return
	 */
	public static Object get(Mo mo, String name) {
		try {
			Field field = mo.getClass().getField(name);
			if (field != null) {
				return field.get(mo);
			}
		} catch (Exception e) {
		}

		return mo.getAttrMap() != null ? mo.getAttrMap().get(name) : null;
	}

	/**
	 * 상위 관리대상 존재 여부 확인
	 *
	 * @param mo
	 * @return
	 */
	public static boolean hasUpper(Moable mo) {
		if (mo == null) {
			return false;
		}
		return mo.getUpperMoNo() > 0 && mo.getMoNo() != mo.getUpperMoNo();
	}
}
