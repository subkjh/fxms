package fxms.bas.impl.dbo.all;

import subkjh.dao.def.Column.COLUMN_OP;
import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
 * @since 2023.04.13 13:50
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_MO", comment = "관리대상테이블")
@FxIndex(name = "FX_MO__PK", type = INDEX_TYPE.PK, columns = { "MO_NO" })
@FxIndex(name = "FX_MO__KEY1", type = INDEX_TYPE.KEY, columns = { "UPPER_MO_NO" })
@FxIndex(name = "FX_MO__KEY2", type = INDEX_TYPE.KEY, columns = { "MNG_YN" })
@FxIndex(name = "FX_MO__KEY3", type = INDEX_TYPE.KEY, columns = { "DEL_YN" })
@FxIndex(name = "FX_MO__KEY4", type = INDEX_TYPE.KEY, columns = { "MO_CLASS" })
@FxIndex(name = "FX_MO__KEY5", type = INDEX_TYPE.KEY, columns = { "INLO_NO" })
@FxIndex(name = "FX_MO__KEY6", type = INDEX_TYPE.KEY, columns = { "MODEL_NO" })
@FxIndex(name = "FX_MO__FK1", type = INDEX_TYPE.FK, columns = {
		"INLO_NO" }, fkTable = "FX_CF_INLO", fkColumn = "INLO_NO")
@FxIndex(name = "FX_MO__FK2", type = INDEX_TYPE.FK, columns = {
		"MODEL_NO" }, fkTable = "FX_CF_MODEL", fkColumn = "MODEL_NO")
@FxIndex(name = "FX_MO__FK3", type = INDEX_TYPE.FK, columns = {
		"ALARM_CFG_NO" }, fkTable = "FX_AL_CFG", fkColumn = "ALARM_CFG_NO")
public class FX_MO {

	public FX_MO() {
	}

	public static final String FX_SEQ_MONO = "FX_SEQ_MONO";
	@FxColumn(name = "MO_NO", size = 19, comment = "MO번호", sequence = "FX_SEQ_MONO")
	private long moNo;

	@FxColumn(name = "MO_NAME", size = 200, comment = "MO명")
	private String moName;

	@FxColumn(name = "MO_DISP_NAME", size = 200, nullable = true, comment = "MO표시명")
	private String moDispName;

	@FxColumn(name = "MO_CLASS", size = 20, comment = "MO클래스 ")
	private String moClass;

	@FxColumn(name = "MO_TYPE", size = 30, nullable = true, comment = "MO유형")
	private String moType;

	@FxColumn(name = "MNG_YN", size = 1, comment = "관리여부", defValue = "Y")
	private boolean mngYn = true;

	@FxColumn(name = "UPPER_MO_NO", size = 19, comment = "상위MO번호", defValue = "-1")
	private long upperMoNo = -1L;

	@FxColumn(name = "ALARM_CFG_NO", size = 9, comment = "알람조건번호", defValue = "1")
	private int alarmCfgNo = 1;

	@FxColumn(name = "MODEL_NO", size = 9, comment = "모델번호", defValue = "0")
	private int modelNo = 0;

	@FxColumn(name = "INLO_NO", size = 9, comment = "설치위치번호", defValue = "0")
	private int inloNo = 0;

	@FxColumn(name = "MO_MEMO", size = 200, nullable = true, comment = "MO메모")
	private String moMemo;

	@FxColumn(name = "MO_TID", size = 100, nullable = true, comment = "MO대상ID")
	private String moTid;

	@FxColumn(name = "MO_ADD_JSON", size = 2000, nullable = true, comment = "MO추가JSON")
	private String moAddJson;

	@FxColumn(name = "POLL_CYCLE", size = 7, nullable = true, comment = "폴링주기", defValue = "0")
	private int pollCycle = 0;

	@FxColumn(name = "MO_ONLINE_ST_VAL", size = 3, nullable = true, comment = "관리대상온라인상태값", defValue = "0")
	private int moOnlineStVal = 0;

	@FxColumn(name = "MO_ONLINE_ST_CHG_DTM", size = 14, nullable = true, comment = "관리대상온라인상태변경일시")
	private long moOnlineStChgDtm;

	@FxColumn(name = "DEL_YN", size = 1, comment = "삭제여부", defValue = "N")
	private String delYn = "N";

	@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, nullable = true, comment = "등록사용자번호", defValue = "0")
	private int regUserNo = 0;

	@FxColumn(name = "REG_DTM", size = 14, operator = COLUMN_OP.insert, nullable = true, comment = "등록일시")
	private long regDtm;

	@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
	private int chgUserNo = 0;

	@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시")
	private long chgDtm;

	/**
	 * MO번호
	 * 
	 * @return MO번호
	 */
	public long getMoNo() {
		return moNo;
	}

	/**
	 * MO번호
	 * 
	 * @param moNo MO번호
	 */
	public void setMoNo(long moNo) {
		this.moNo = moNo;
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
	 * MO명
	 * 
	 * @param moName MO명
	 */
	public void setMoName(String moName) {
		this.moName = moName;
	}

	/**
	 * MO표시명
	 * 
	 * @return MO표시명
	 */
	public String getMoDispName() {
		return moDispName;
	}

	/**
	 * MO표시명
	 * 
	 * @param moDispName MO표시명
	 */
	public void setMoDispName(String moDispName) {
		this.moDispName = moDispName;
	}

	/**
	 * MO클래스
	 * 
	 * @return MO클래스
	 */
	public String getMoClass() {
		return moClass;
	}

	/**
	 * MO클래스
	 * 
	 * @param moClass MO클래스
	 */
	public void setMoClass(String moClass) {
		this.moClass = moClass;
	}

	/**
	 * MO유형
	 * 
	 * @return MO유형
	 */
	public String getMoType() {
		return moType;
	}

	/**
	 * MO유형
	 * 
	 * @param moType MO유형
	 */
	public void setMoType(String moType) {
		this.moType = moType;
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
	 * @param mngYn 관리여부
	 */
	public void setMngYn(boolean mngYn) {
		this.mngYn = mngYn;
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
	 * 상위MO번호
	 * 
	 * @param upperMoNo 상위MO번호
	 */
	public void setUpperMoNo(long upperMoNo) {
		this.upperMoNo = upperMoNo;
	}

	/**
	 * 알람조건번호
	 * 
	 * @return 알람조건번호
	 */
	public int getAlarmCfgNo() {
		return alarmCfgNo;
	}

	/**
	 * 알람조건번호
	 * 
	 * @param alarmCfgNo 알람조건번호
	 */
	public void setAlarmCfgNo(int alarmCfgNo) {
		this.alarmCfgNo = alarmCfgNo;
	}

	/**
	 * 모델번호
	 * 
	 * @return 모델번호
	 */
	public int getModelNo() {
		return modelNo;
	}

	/**
	 * 모델번호
	 * 
	 * @param modelNo 모델번호
	 */
	public void setModelNo(int modelNo) {
		this.modelNo = modelNo;
	}

	/**
	 * 설치위치번호
	 * 
	 * @return 설치위치번호
	 */
	public int getInloNo() {
		return inloNo;
	}

	/**
	 * 설치위치번호
	 * 
	 * @param inloNo 설치위치번호
	 */
	public void setInloNo(int inloNo) {
		this.inloNo = inloNo;
	}

	/**
	 * MO메모
	 * 
	 * @return MO메모
	 */
	public String getMoMemo() {
		return moMemo;
	}

	/**
	 * MO메모
	 * 
	 * @param moMemo MO메모
	 */
	public void setMoMemo(String moMemo) {
		this.moMemo = moMemo;
	}

	/**
	 * MO대상ID
	 * 
	 * @return MO대상ID
	 */
	public String getMoTid() {
		return moTid;
	}

	/**
	 * MO대상ID
	 * 
	 * @param moTid MO대상ID
	 */
	public void setMoTid(String moTid) {
		this.moTid = moTid;
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
	 * MO추가JSON
	 * 
	 * @param moAddJson MO추가JSON
	 */
	public void setMoAddJson(String moAddJson) {
		this.moAddJson = moAddJson;
	}

	/**
	 * 폴링주기
	 * 
	 * @return 폴링주기
	 */
	public int getPollCycle() {
		return pollCycle;
	}

	/**
	 * 폴링주기
	 * 
	 * @param pollCycle 폴링주기
	 */
	public void setPollCycle(int pollCycle) {
		this.pollCycle = pollCycle;
	}

	/**
	 * 관리대상온라인상태값
	 * 
	 * @return 관리대상온라인상태값
	 */
	public int getMoOnlineStVal() {
		return moOnlineStVal;
	}

	/**
	 * 관리대상온라인상태값
	 * 
	 * @param moOnlineStVal 관리대상온라인상태값
	 */
	public void setMoOnlineStVal(int moOnlineStVal) {
		this.moOnlineStVal = moOnlineStVal;
	}

	/**
	 * 관리대상온라인상태변경일시
	 * 
	 * @return 관리대상온라인상태변경일시
	 */
	public long getMoOnlineStChgDtm() {
		return moOnlineStChgDtm;
	}

	/**
	 * 관리대상온라인상태변경일시
	 * 
	 * @param moOnlineStChgDtm 관리대상온라인상태변경일시
	 */
	public void setMoOnlineStChgDtm(long moOnlineStChgDtm) {
		this.moOnlineStChgDtm = moOnlineStChgDtm;
	}

	/**
	 * 삭제여부
	 * 
	 * @return 삭제여부
	 */
	public String isDelYn() {
		return delYn;
	}

	/**
	 * 삭제여부
	 * 
	 * @param delYn 삭제여부
	 */
	public void setDelYn(String delYn) {
		this.delYn = delYn;
	}

	/**
	 * 등록사용자번호
	 * 
	 * @return 등록사용자번호
	 */
	public int getRegUserNo() {
		return regUserNo;
	}

	/**
	 * 등록사용자번호
	 * 
	 * @param regUserNo 등록사용자번호
	 */
	public void setRegUserNo(int regUserNo) {
		this.regUserNo = regUserNo;
	}

	/**
	 * 등록일시
	 * 
	 * @return 등록일시
	 */
	public long getRegDtm() {
		return regDtm;
	}

	/**
	 * 등록일시
	 * 
	 * @param regDtm 등록일시
	 */
	public void setRegDtm(long regDtm) {
		this.regDtm = regDtm;
	}

	/**
	 * 수정사용자번호
	 * 
	 * @return 수정사용자번호
	 */
	public int getChgUserNo() {
		return chgUserNo;
	}

	/**
	 * 수정사용자번호
	 * 
	 * @param chgUserNo 수정사용자번호
	 */
	public void setChgUserNo(int chgUserNo) {
		this.chgUserNo = chgUserNo;
	}

	/**
	 * 수정일시
	 * 
	 * @return 수정일시
	 */
	public long getChgDtm() {
		return chgDtm;
	}

	/**
	 * 수정일시
	 * 
	 * @param chgDtm 수정일시
	 */
	public void setChgDtm(long chgDtm) {
		this.chgDtm = chgDtm;
	}
}
