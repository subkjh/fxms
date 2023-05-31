package fxms.bas.impl.dbo.all;

import java.io.Serializable;

import subkjh.dao.def.Column.COLUMN_OP;
import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
 * @since 2022.05.02 18:01
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_AL_CD", comment = "경보코드테이블")
@FxIndex(name = "FX_AL_CODE__PK", type = INDEX_TYPE.PK, columns = { "ALCD_NO" })
@FxIndex(name = "FX_AL_CODE__UK_NAME", type = INDEX_TYPE.UK, columns = { "ALCD_NAME" })
public class FX_AL_CD implements Serializable {

	public FX_AL_CD() {
	}

	@FxColumn(name = "ALCD_NO", size = 9, comment = "경보코드번호")
	private int alcdNo;

	@FxColumn(name = "ALCD_NAME", size = 200, comment = "경보코드명")
	private String alcdName;

	@FxColumn(name = "ALCD_DISP_NAME", size = 50, nullable = true, comment = "경보코드표시명")
	private String alcdDispName;

	@FxColumn(name = "ALCD_DESC", size = 200, nullable = true, comment = "경보코드설명")
	private String alcdDesc;

	@FxColumn(name = "ALCD_GRP", size = 50, nullable = true, comment = "경보코드그룹")
	private String alcdGrp;

	@FxColumn(name = "ALARM_MSG", size = 500, nullable = true, comment = "경보메시지")
	private String alarmMsg;

	@FxColumn(name = "MO_CLASS", size = 50, comment = "MO클래스")
	private String moClass;

	@FxColumn(name = "AUTO_RLSE_SEC", size = 9, nullable = true, comment = "자동해제초 ")
	private int autoRlseSec;

	@FxColumn(name = "ALARM_LEVEL", size = 1, nullable = true, comment = "경보등급")
	private int alarmLevel;

	@FxColumn(name = "CMPR_CD", size = 2, nullable = true, comment = "비교코드")
	private String cmprCd;

	@FxColumn(name = "PS_ID", size = 20, nullable = true, comment = "성능ID")
	private String psId;

	@FxColumn(name = "SVC_ALARM_YN", size = 1, nullable = true, comment = "서비스경보여부", defValue = "N")
	private boolean svcAlarmYn = false;

	@FxColumn(name = "FPACT_CD", size = 10, nullable = true, comment = "후속조치코드")
	private String fpactCd;

	@FxColumn(name = "USE_YN", size = 1, nullable = true, comment = "사용여부", defValue = "Y")
	private boolean useYn = true;

	@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, nullable = true, comment = "등록사용자번호")
	private int regUserNo;

	@FxColumn(name = "REG_DTM", size = 14, operator = COLUMN_OP.insert, nullable = true, comment = "등록일시")
	private long regDtm;

	@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
	private int chgUserNo = 0;

	@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시", defValue = "0")
	private long chgDtm = 0L;

	/**
	 * 경보코드번호
	 * 
	 * @return 경보코드번호
	 */
	public int getAlcdNo() {
		return alcdNo;
	}

	/**
	 * 경보코드번호
	 * 
	 * @param alcdNo 경보코드번호
	 */
	public void setAlcdNo(int alcdNo) {
		this.alcdNo = alcdNo;
	}

	/**
	 * 경보코드명
	 * 
	 * @return 경보코드명
	 */
	public String getAlcdName() {
		return alcdName;
	}

	/**
	 * 경보코드명
	 * 
	 * @param alcdName 경보코드명
	 */
	public void setAlcdName(String alcdName) {
		this.alcdName = alcdName;
	}

	/**
	 * 경보코드표시명
	 * 
	 * @return 경보코드표시명
	 */
	public String getAlcdDispName() {
		return alcdDispName;
	}

	/**
	 * 경보코드표시명
	 * 
	 * @param alcdDispName 경보코드표시명
	 */
	public void setAlcdDispName(String alcdDispName) {
		this.alcdDispName = alcdDispName;
	}

	/**
	 * 경보코드설명
	 * 
	 * @return 경보코드설명
	 */
	public String getAlcdDesc() {
		return alcdDesc;
	}

	/**
	 * 경보코드설명
	 * 
	 * @param alcdDesc 경보코드설명
	 */
	public void setAlcdDesc(String alcdDesc) {
		this.alcdDesc = alcdDesc;
	}

	/**
	 * 경보코드그룹
	 * 
	 * @return 경보코드그룹
	 */
	public String getAlcdGrp() {
		return alcdGrp;
	}

	/**
	 * 경보코드그룹
	 * 
	 * @param alcdGrp 경보코드그룹
	 */
	public void setAlcdGrp(String alcdGrp) {
		this.alcdGrp = alcdGrp;
	}

	/**
	 * 경보메시지
	 * 
	 * @return 경보메시지
	 */
	public String getAlarmMsg() {
		return alarmMsg;
	}

	/**
	 * 경보메시지
	 * 
	 * @param alarmMsg 경보메시지
	 */
	public void setAlarmMsg(String alarmMsg) {
		this.alarmMsg = alarmMsg;
	}

	/**
	 * 대상MO클래스
	 * 
	 * @return 대상MO클래스
	 */
	public String getMoClass() {
		return moClass;
	}

	/**
	 * 대상MO클래스
	 * 
	 * @param targtMoClass 대상MO클래스
	 */
	public void setMoClass(String moClass) {
		this.moClass = moClass;
	}

	/**
	 * 자동해제초
	 * 
	 * @return 자동해제초
	 */
	public int getAutoRlseSec() {
		return autoRlseSec;
	}

	/**
	 * 자동해제초
	 * 
	 * @param autoRlseSec 자동해제초
	 */
	public void setAutoRlseSec(int autoRlseSec) {
		this.autoRlseSec = autoRlseSec;
	}

	/**
	 * 경보등급
	 * 
	 * @return 경보등급
	 */
	public int getAlarmLevel() {
		return alarmLevel;
	}

	/**
	 * 경보등급
	 * 
	 * @param alarmLevel 경보등급
	 */
	public void setAlarmLevel(int alarmLevel) {
		this.alarmLevel = alarmLevel;
	}

	/**
	 * 비교코드
	 * 
	 * @return 비교코드
	 */
	public String getCmprCd() {
		return cmprCd;
	}

	/**
	 * 비교코드
	 * 
	 * @param cmprCd 비교코드
	 */
	public void setCmprCd(String cmprCd) {
		this.cmprCd = cmprCd;
	}

	/**
	 * 성능ID
	 * 
	 * @return 성능ID
	 */
	public String getPsId() {
		return psId;
	}

	/**
	 * 성능ID
	 * 
	 * @param psId 성능ID
	 */
	public void setPsId(String psId) {
		this.psId = psId;
	}

	/**
	 * 서비스경보여부
	 * 
	 * @return 서비스경보여부
	 */
	public boolean isSvcAlarmYn() {
		return svcAlarmYn;
	}

	/**
	 * 서비스경보여부
	 * 
	 * @param svcAlarmYn 서비스경보여부
	 */
	public void setSvcAlarmYn(boolean svcAlarmYn) {
		this.svcAlarmYn = svcAlarmYn;
	}

	/**
	 * 후속조치코드
	 * 
	 * @return 후속조치코드
	 */
	public String getFpactCd() {
		return fpactCd;
	}

	/**
	 * 후속조치코드
	 * 
	 * @param fpactCd 후속조치코드
	 */
	public void setFpactCd(String fpactCd) {
		this.fpactCd = fpactCd;
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
	 * 사용여부
	 * 
	 * @param useYn 사용여부
	 */
	public void setUseYn(boolean useYn) {
		this.useYn = useYn;
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
