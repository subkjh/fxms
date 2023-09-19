package fxms.bas.impl.dbo.all;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
 * @since 2023.02.17 14:20
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_EV_CRON_LOG", comment = "이벤트CRON로그테이블")
@FxIndex(name = "FX_EV_CRON_LOG__PK", type = INDEX_TYPE.PK, columns = { "CRON_RUN_NO" })
@FxIndex(name = "FX_EV_CRON_LOG__KEY1", type = INDEX_TYPE.KEY, columns = { "CRON_NAME", "STRT_DTM" })
public class FX_EV_CRON_LOG {

	public FX_EV_CRON_LOG() {
	}

	public static final String FX_SEQ_CRONRUNNO = "FX_SEQ_CRONRUNNO";
	@FxColumn(name = "CRON_RUN_NO", size = 14, comment = "CRON실행번호", sequence = "FX_SEQ_CRONRUNNO")
	private Long cronRunNo;

	@FxColumn(name = "CRON_NAME", size = 200, comment = "CRON명")
	private String cronName;

	@FxColumn(name = "STRT_DTM", size = 14, nullable = true, comment = "시작일시")
	private Long strtDtm;

	@FxColumn(name = "FNSH_DTM", size = 14, nullable = true, comment = "종료일시")
	private Long fnshDtm;

	@FxColumn(name = "TIME_TAKEN_MSEC", size = 9, nullable = true, comment = "소요시간(Millseconds)")
	private Integer timeTakenMsec;

	@FxColumn(name = "OK_YN", size = 1, comment = "정상여부", defValue = "R")
	private String okYn = "R";

	@FxColumn(name = "IN_PARA_JSON", size = 2000, nullable = true, comment = "입력인자JSON")
	private String inParaJson;

	@FxColumn(name = "OUT_PARA_JSON", size = 2000, nullable = true, comment = "출력인자JSON")
	private String outParaJson;

	/**
	 * CRON실행번호
	 * 
	 * @return CRON실행번호
	 */
	public Long getCronRunNo() {
		return cronRunNo;
	}

	/**
	 * CRON실행번호
	 * 
	 * @param cronRunNo CRON실행번호
	 */
	public void setCronRunNo(Long cronRunNo) {
		this.cronRunNo = cronRunNo;
	}

	/**
	 * CRON명
	 * 
	 * @return CRON명
	 */
	public String getCronName() {
		return cronName;
	}

	/**
	 * CRON명
	 * 
	 * @param cronName CRON명
	 */
	public void setCronName(String cronName) {
		this.cronName = cronName;
	}

	/**
	 * 시작일시
	 * 
	 * @return 시작일시
	 */
	public Long getStrtDtm() {
		return strtDtm;
	}

	/**
	 * 시작일시
	 * 
	 * @param strtDtm 시작일시
	 */
	public void setStrtDtm(Long strtDtm) {
		this.strtDtm = strtDtm;
	}

	/**
	 * 종료일시
	 * 
	 * @return 종료일시
	 */
	public Long getFnshDtm() {
		return fnshDtm;
	}

	/**
	 * 종료일시
	 * 
	 * @param fnshDtm 종료일시
	 */
	public void setFnshDtm(Long fnshDtm) {
		this.fnshDtm = fnshDtm;
	}

	/**
	 * 소요시간(Millseconds)
	 * 
	 * @return 소요시간(Millseconds)
	 */
	public Integer getTimeTakenMsec() {
		return timeTakenMsec;
	}

	/**
	 * 소요시간(Millseconds)
	 * 
	 * @param timeTakenMsec 소요시간(Millseconds)
	 */
	public void setTimeTakenMsec(Integer timeTakenMsec) {
		this.timeTakenMsec = timeTakenMsec;
	}

	/**
	 * 정상여부
	 * 
	 * @return 정상여부
	 */
	public String isOkYn() {
		return okYn;
	}

	/**
	 * 정상여부
	 * 
	 * @param okYn 정상여부
	 */
	public void setOkYn(String okYn) {
		this.okYn = okYn;
	}

	/**
	 * 입력인자JSON
	 * 
	 * @return 입력인자JSON
	 */
	public String getInParaJson() {
		return inParaJson;
	}

	/**
	 * 입력인자JSON
	 * 
	 * @param inParaJson 입력인자JSON
	 */
	public void setInParaJson(String inParaJson) {
		this.inParaJson = inParaJson;
	}

	/**
	 * 출력인자JSON
	 * 
	 * @return 출력인자JSON
	 */
	public String getOutParaJson() {
		return outParaJson;
	}

	/**
	 * 출력인자JSON
	 * 
	 * @param outParaJson 출력인자JSON
	 */
	public void setOutParaJson(String outParaJson) {
		this.outParaJson = outParaJson;
	}
}
