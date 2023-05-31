package fxms.bas.mo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import fxms.bas.event.FxEventImpl;
import fxms.bas.fxo.FxObject;
import fxms.bas.fxo.FxmsUtil;

/**
 * Managed Object
 *
 * @author subkjh
 *
 */
public class FxMo extends FxEventImpl implements FxObject, Cloneable, Serializable, Mo {

	/**
	 *
	 */
	private static final long serialVersionUID = 6972183176049221646L;

	public static final String MO_CLASS = "MO";
	public static final int STATUS_ON = 1;
	public static final int STATUS_OFF = 0;
	public static final int STATUS_UNKNOWN = -1;

	/** MO번호 */
	private long moNo;

	/** MO명 */
	private String moName;

	private String moDispName;

	private String moClass = MO_CLASS;

	private String moType;

	private boolean mngYn = true;

	private long upperMoNo = -1L;

	private int alarmCfgNo = 0;

	private int modelNo = 0;

	private int inloNo = 0;

	private String moMemo;

	private String moTid;

	private String moAddJson;

	private String moKey;

	private transient final Map<String, Object> attrMap;

	public FxMo() {
		attrMap = new HashMap<String, Object>();
	}

	public FxMo(long moNo) {
		this();
		this.moNo = moNo;
	}

	@Override
	public Object get(String name) {
		return Mo.get(this, name);
	}

	@Override
	public int getAlarmCfgNo() {
		return alarmCfgNo;
	}

	@Override
	public Map<String, Object> getAttrMap() {

		if (moAddJson != null && moAddJson.trim().length() > 2 && this.attrMap.size() == 0) {
			try {
				Map<String, Object> map = FxmsUtil.toMapFromJson(moAddJson);
				if (map != null) {
					this.attrMap.clear();
					this.attrMap.putAll(map);
				}
			} catch (Exception e) {
			}
		}

		return attrMap;
	}

	@Override
	public int getInloNo() {
		return inloNo;
	}

	/**
	 * MO추가JSON
	 * 
	 * @return MO추가JSON
	 */
	public String getMoAddJson() {
		return moAddJson;
	}

	/**
	 * MO분류
	 *
	 * @return MO분류
	 */
	@Override
	public String getMoClass() {
		return moClass;
	}

	@Override
	public int getModelNo() {
		return modelNo;
	}

	public String getMoDispName() {
		return moDispName;
	}

	public String getMoKey() {
		if (moKey == null) {
			moKey = Mo.getMoKey(this);
		}
		return moKey;
	}

	public String getMoMemo() {
		return moMemo;
	}

	/**
	 * MO명
	 *
	 * @return MO명
	 */
	@Override
	public String getMoName() {
		return moName;
	}

	@Override
	public long getMoNo() {
		return moNo;
	}

	/**
	 * MO대상ID
	 * 
	 * @return MO대상ID
	 */
	public String getMoTid() {
		return moTid;
	}

	@Override
	public String getMoType() {
		return moType;
	}

	@Override
	public long getUpperMoNo() {
		return upperMoNo;
	}

	/**
	 * 관리여부
	 *
	 * @return 관리여부
	 */
	@Override
	public boolean isMngYn() {
		return mngYn;
	}

	@Override
	public void setAlarmCfgNo(int alarmCfgNo) {
		this.alarmCfgNo = alarmCfgNo;
	}

	@Override
	public void setInloNo(int inloNo) {
		this.inloNo = inloNo;
	}

	/**
	 * 관리여부
	 *
	 * @param mngYn 관리여부
	 */
	@Override
	public void setMngYn(boolean mngYn) {
		this.mngYn = mngYn;
	}

	/**
	 * MO추가JSON
	 * 
	 * @param moAddJson MO추가JSON
	 */
	public void setMoAddJson(String moAddJson) {
		this.moAddJson = moAddJson;
	}

	/**
	 * MO분류
	 *
	 * @param moClass MO분류
	 */
	public void setMoClass(String moClass) {
		this.moClass = moClass;
	}

	@Override
	public void setModelNo(int modelNo) {
		this.modelNo = modelNo;
	}

	public void setMoDispName(String moDispName) {
		this.moDispName = moDispName;
	}

	public void setMoMemo(String moMemo) {
		this.moMemo = moMemo;
	}

	/**
	 * MO명
	 *
	 * @param moName MO명
	 */
	@Override
	public void setMoName(String moName) {
		this.moName = moName;
	}

	/**
	 * MO번호
	 *
	 * @param moNo MO번호
	 */
	@Override
	public void setMoNo(long moNo) {
		this.moNo = moNo;
	}

	/**
	 * MO대상ID
	 * 
	 * @param moTid MO대상ID
	 */
	public void setMoTid(String moTid) {
		this.moTid = moTid;
	}

	@Override
	public void setMoType(String moType) {
		this.moType = moType;
	}

	/**
	 * 상위MO번호
	 *
	 * @param upperMoNo 상위MO번호
	 */
	@Override
	public void setUpperMoNo(long upperMoNo) {
		this.upperMoNo = upperMoNo;
	}

	public void setUserChgAttr(FxMo moOld) {

		setUpperMoNo(moOld.getUpperMoNo());
		setMoNo(moOld.getMoNo());
		setMngYn(moOld.isMngYn());

	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(getClass().getSimpleName());
		sb.append(",").append(upperMoNo);
		sb.append(".").append(moNo).append(")").append(moName);
		sb.append(",").append(getMoClass());
		sb.append(",").append(mngYn);
		return sb.toString();
	}

}