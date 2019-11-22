package fxms.nms.co.snmp.trap.vo;

import fxms.bas.co.noti.FxEventImpl;
import fxms.nms.co.snmp.trap.TrapNode;
import fxms.nms.mo.property.Modelable;

/**
 * 비교 조건
 * 
 * @author subkjh
 * 
 */
public abstract class TrapPattern extends FxEventImpl implements Modelable {

	public static final int ALARM_MSG_TYPE_CONTENT = 3;
	public static final int ALARM_MSG_TYPE_OID = 1;
	public static final int ALARM_MSG_TYPE_TEXT = 2;

	/**
	 * 
	 */
	private static final long serialVersionUID = 9023919420114813878L;

	/** 경보 코드 */
	private int alarmCode;
	/** 경보코드(실제 경보를 발생할 때 사용할 코드 ) */
	private int alarmCodeUse;
	/** 경보그룹 */
	private String alarmGroup;
	/** 경보 등급 */
	private int alarmLevel;
	/** 경보 메시지 */
	private String alarmMsg;
	/** 경보 메시지 유형 */
	private int alarmMsgType;
	/** 경보명 */
	private String alarmName;
	/** 사용여부 */
	private boolean enable;
	/** 발생위치검색필드 */
	protected String fieldMo;
	/** 장비검색필드 */
	private String fieldNode;
	/** 최종 수정 일시 */
	private long hstimeChg;
	/** 동일경보판단OID */
	private String oidKey;

	/** 발생위치검색OID */
	protected String oidMo;
	/** 장비검색OID */
	private String oidNode;
	/** 자동해제시간(초) - 0이거나 작으면 경우 자동 해제하지 않습니다. */
	private int secRelease;
	/** 트랩코드(발생) */
	private int specificCode;
	/** 트랩코드(해제) */
	private int specificCodeClear;
	/** 남기지않고 버릴지 여부 */
	private boolean throwOut;
	/** TRAP OID */
	private String trapOid;
	/** TRAP OID (해제) */
	private String trapOidClear;
	/** 경보조치번호. 이 조건으로 이벤트가 발생되면 처리할 내용 */
	private int treatName;
	/** 비교 OID */
	private String varOid;
	/** 비교 OID (해제) */
	private String varOidClear;
	/** 비교 값 */
	private String varVal;
	/** 비교 값 (해제) */
	private String varValClear;

	public TrapPattern() {
		enable = true;
	}

	/**
	 * PK
	 * 
	 * @param alarmCode 경보코드
	 */
	public TrapPattern(int alarmCode) {
		this.alarmCode = alarmCode;
	}

	public TrapPattern(int alarmCode, String trapOid, String varOid, String varVal, String alarmName, int alarmLevel,
			String alarmMsg, String trapOidClear, String varOidClear, String varValClear, int secRelease) {

		setAlarmCode(alarmCode);
		setAlarmLevel(alarmLevel);
		setAlarmMsg(alarmMsg);
		setTrapOid(trapOid);
		setTrapOidClear(trapOidClear);
		setVarOid(varOid);
		setVarOidClear(varOidClear);
		setVarVal(varVal);
		setVarValClear(varValClear);
		setAlarmGroup("snmptrap");
		setSecRelease(secRelease);
		setAlarmName(alarmName);
		setThrowOut(false);
	}

	/**
	 * UK
	 * 
	 * @param alarmName 경보명
	 */
	public TrapPattern(String alarmName) {
		this.alarmName = alarmName;
	}

	public int getAlarmCode() {
		return alarmCode;
	}

	public int getAlarmCodeUse() {
		return alarmCodeUse;
	}

	public String getAlarmGroup() {
		return alarmGroup;
	}

	public int getAlarmLevel() {
		return alarmLevel;
	}

	public String getAlarmMsg() {
		return alarmMsg;
	}

	public int getAlarmMsgType() {
		return alarmMsgType;
	}

	public String getAlarmName() {
		return alarmName;
	}

	public String getFieldMo() {
		return fieldMo;
	}

	public String getFieldNode() {
		return fieldNode;
	}

	public long getHstimeChg() {
		return hstimeChg;
	}

	public String getOidKey() {
		return oidKey;
	}

	public String getOidMo() {
		return oidMo;
	}

	public String getOidNode() {
		return oidNode;
	}

	public int getSecRelease() {
		return secRelease;
	}

	public int getSpecificCode() {
		return specificCode;
	}

	public int getSpecificCodeClear() {
		return specificCodeClear;
	}

	public String getTrapOid() {
		return trapOid;
	}

	public String getTrapOidClear() {
		return trapOidClear;
	}

	public int getTreatName() {
		return treatName;
	}

	public String getVarOid() {
		return varOid;
	}

	public String getVarOidClear() {
		return varOidClear;
	}

	public String getVarVal() {
		return varVal;
	}

	public String getVarValClear() {
		return varValClear;
	}

	public boolean isEnable() {
		return enable;
	}

	public boolean isSetNode() {
		return oidNode != null && oidNode.length() > 0 //
				&& fieldNode != null && fieldNode.length() > 0;
	}

	public boolean isThrowOut() {
		return throwOut;
	}

	/**
	 * MO에 해당되는 트랩정보조건인지 확인합니다.
	 * 
	 * @param mo MO
	 * @return 부합여부
	 */
	public abstract boolean match(TrapNode node);

	/**
	 * 이 임계가 입력된 TRAP_OID에 비교 대상인지 판단합니다.<br>
	 * 이 임계에 TRAP_OID가 정의되어 있지 않다면 true입니다.
	 * 
	 * @param trapOid
	 * @return 매칭결과
	 */
	public boolean matchTrapOid(String trapOid) {
		return this.trapOid == null //
				|| this.trapOid.trim().length() == 0//
				|| this.trapOid.equals(trapOid);
	}

	/**
	 * 이 임계가 입력된 TRAP_OID에 비교 대상인지 판단합니다.<br>
	 * 이 임계에 TRAP_OID가 정의되어 있지 않다면 true입니다.
	 * 
	 * @param trapOid
	 * @return 매칭결과
	 */
	public boolean matchTrapOidClear(String trapOid) {
		return this.trapOidClear == null //
				|| this.trapOidClear.trim().length() == 0//
				|| this.trapOidClear.equals(trapOid);
	}

	public void setAlarmCode(int alarmCode) {
		this.alarmCode = alarmCode;
	}

	public void setAlarmCodeUse(int alarmCodeUse) {
		this.alarmCodeUse = alarmCodeUse;
	}

	public void setAlarmGroup(String alarmGroup) {
		this.alarmGroup = alarmGroup;
	}

	public void setAlarmLevel(int alarmLevel) {
		this.alarmLevel = alarmLevel;
	}

	public void setAlarmMsg(String alarmMsg) {
		this.alarmMsg = alarmMsg;
	}

	public void setAlarmMsgType(int alarmMsgType) {
		this.alarmMsgType = alarmMsgType;
	}

	public void setAlarmName(String alarmName) {
		this.alarmName = alarmName;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public void setFieldMo(String fieldMo) {
		this.fieldMo = fieldMo;
	}

	public void setFieldNode(String fieldNode) {
		this.fieldNode = fieldNode;
	}

	public void setHstimeChg(long hstimeChg) {
		this.hstimeChg = hstimeChg;
	}

	public void setOidKey(String oidKey) {
		this.oidKey = oidKey;
	}

	public void setOidMo(String oidMo) {
		this.oidMo = oidMo;
	}

	public void setOidNode(String oidNode) {
		this.oidNode = oidNode;
	}

	public void setSecRelease(int secRelease) {
		this.secRelease = secRelease;
	}

	public void setSpecificCode(int specificCode) {
		this.specificCode = specificCode;
	}

	public void setSpecificCodeClear(int specificCodeClear) {
		this.specificCodeClear = specificCodeClear;
	}

	public void setThrowOut(boolean throwOut) {
		this.throwOut = throwOut;
	}

	public void setTrapOid(String trapOid) {
		this.trapOid = trapOid;
	}

	public void setTrapOidClear(String trapOidClear) {
		this.trapOidClear = trapOidClear;
	}

	public void setTreatName(int treatName) {
		this.treatName = treatName;
	}

	public void setVarOid(String varOid) {
		this.varOid = varOid;
	}

	public void setVarOidClear(String varOidClear) {
		this.varOidClear = varOidClear;
	}

	public void setVarVal(String varVal) {
		this.varVal = varVal;
	}

	public void setVarValClear(String varValClear) {
		this.varValClear = varValClear;
	}

	@Override
	public String toString() {
		return "TRAP-PATTERN(code=" + alarmCode + ",name=" + alarmName + ")";
	}

}
