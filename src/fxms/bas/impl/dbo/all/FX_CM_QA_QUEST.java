package fxms.bas.impl.dbo.all;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
 * @since 2022.05.25 14:42
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_CM_QA_QUEST", comment = "소통QA질문테이블")
@FxIndex(name = "FX_CM_QA_QUEST__PK", type = INDEX_TYPE.PK, columns = { "QUEST_NO" })
public class FX_CM_QA_QUEST {

	public FX_CM_QA_QUEST() {
	}

	public static final String FX_SEQ_QUESTNO = "FX_SEQ_QUESTNO";
	@FxColumn(name = "QUEST_NO", size = 9, comment = "질문번호", sequence = "FX_SEQ_QUESTNO")
	private int questNo;

	@FxColumn(name = "QUEST_TITLE", size = 100, comment = "질문제목")
	private String questTitle;

	@FxColumn(name = "QUEST_CL_CD", size = 2, comment = "질문구분코드")
	private String questClCd;

	@FxColumn(name = "QUEST_CNTS", size = 4000, comment = "질문내용")
	private String questCnts;

	@FxColumn(name = "CLOSE_YN", size = 1, comment = "마감여부")
	private boolean closeYn;

	@FxColumn(name = "QUEST_USER_NO", size = 9, nullable = true, comment = "질문사용자번호", defValue = "0")
	private int questUserNo = 0;

	@FxColumn(name = "QUEST_NAME", size = 50, comment = "질문자명")
	private String questName;

	@FxColumn(name = "INLO_NO", size = 9, nullable = true, comment = "설치위치번호", defValue = "0")
	private int inloNo = 0;

	@FxColumn(name = "QUEST_DATE", size = 8, comment = "질문일자")
	private int questDate;

	@FxColumn(name = "REG_USER_NO", size = 9, nullable = true, comment = "등록사용자번호", defValue = "0")
	private int regUserNo = 0;

	@FxColumn(name = "REG_DTM", size = 14, nullable = true, comment = "등록일시")
	private long regDtm;

	@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
	private int chgUserNo = 0;

	@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시")
	private long chgDtm;

	/**
	 * 질문번호
	 * 
	 * @return 질문번호
	 */
	public int getQuestNo() {
		return questNo;
	}

	/**
	 * 질문번호
	 * 
	 * @param questNo 질문번호
	 */
	public void setQuestNo(int questNo) {
		this.questNo = questNo;
	}

	/**
	 * 질문제목
	 * 
	 * @return 질문제목
	 */
	public String getQuestTitle() {
		return questTitle;
	}

	/**
	 * 질문제목
	 * 
	 * @param questTitle 질문제목
	 */
	public void setQuestTitle(String questTitle) {
		this.questTitle = questTitle;
	}

	/**
	 * 질문구분코드
	 * 
	 * @return 질문구분코드
	 */
	public String getQuestClCd() {
		return questClCd;
	}

	/**
	 * 질문구분코드
	 * 
	 * @param questClCd 질문구분코드
	 */
	public void setQuestClCd(String questClCd) {
		this.questClCd = questClCd;
	}

	/**
	 * 질문내용
	 * 
	 * @return 질문내용
	 */
	public String getQuestCnts() {
		return questCnts;
	}

	/**
	 * 질문내용
	 * 
	 * @param questCnts 질문내용
	 */
	public void setQuestCnts(String questCnts) {
		this.questCnts = questCnts;
	}

	/**
	 * 마감여부
	 * 
	 * @return 마감여부
	 */
	public boolean isCloseYn() {
		return closeYn;
	}

	/**
	 * 마감여부
	 * 
	 * @param closeYn 마감여부
	 */
	public void setCloseYn(boolean closeYn) {
		this.closeYn = closeYn;
	}

	/**
	 * 질문사용자번호
	 * 
	 * @return 질문사용자번호
	 */
	public int getQuestUserNo() {
		return questUserNo;
	}

	/**
	 * 질문사용자번호
	 * 
	 * @param questUserNo 질문사용자번호
	 */
	public void setQuestUserNo(int questUserNo) {
		this.questUserNo = questUserNo;
	}

	/**
	 * 질문자명
	 * 
	 * @return 질문자명
	 */
	public String getQuestName() {
		return questName;
	}

	/**
	 * 질문자명
	 * 
	 * @param questName 질문자명
	 */
	public void setQuestName(String questName) {
		this.questName = questName;
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
	 * 질문일자
	 * 
	 * @return 질문일자
	 */
	public int getQuestDate() {
		return questDate;
	}

	/**
	 * 질문일자
	 * 
	 * @param questDate 질문일자
	 */
	public void setQuestDate(int questDate) {
		this.questDate = questDate;
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
