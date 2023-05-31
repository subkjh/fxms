package fxms.bas.impl.dbo.all;

import subkjh.dao.def.Column.COLUMN_OP;
import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
 * @since 2023.05.18 17:24
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_EV_CHECK", comment = "이벤트확인테이블")
@FxIndex(name = "FX_EV_CHECK__PK", type = INDEX_TYPE.PK, columns = { "EVENT_CLASS", "EVENT_KEY" })
public class FX_EV_CHECK {

	public FX_EV_CHECK() {
	}

	@FxColumn(name = "EVENT_CLASS", size = 50, comment = "이벤트분류")
	private String eventClass;

	@FxColumn(name = "EVENT_KEY", size = 50, comment = "이벤트키")
	private String eventKey;

	@FxColumn(name = "EVENT_TEXT", size = 400, nullable = true, comment = "이벤트내용")
	private String eventText;

	@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, nullable = true, comment = "등록사용자번호")
	private Integer regUserNo;

	@FxColumn(name = "REG_DTM", size = 14, operator = COLUMN_OP.insert, nullable = true, comment = "등록일시")
	private Long regDtm;

	@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
	private Integer chgUserNo = 0;

	@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시", defValue = "0")
	private Long chgDtm = 0L;

	/**
	 * @return 이벤트분류
	 */
	public String getEventClass() {
		return eventClass;
	}

	/**
	 * @param eventClass 이벤트분류
	 */
	public void setEventClass(String eventClass) {
		this.eventClass = eventClass;
	}

	/**
	 * @return 이벤트키
	 */
	public String getEventKey() {
		return eventKey;
	}

	/**
	 * @param eventKey 이벤트키
	 */
	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}

	/**
	 * @return 이벤트내용
	 */
	public String getEventText() {
		return eventText;
	}

	/**
	 * @param eventText 이벤트내용
	 */
	public void setEventText(String eventText) {
		this.eventText = eventText;
	}

	/**
	 * @return 등록사용자번호
	 */
	public Integer getRegUserNo() {
		return regUserNo;
	}

	/**
	 * @param regUserNo 등록사용자번호
	 */
	public void setRegUserNo(Integer regUserNo) {
		this.regUserNo = regUserNo;
	}

	/**
	 * @return 등록일시
	 */
	public Long getRegDtm() {
		return regDtm;
	}

	/**
	 * @param regDtm 등록일시
	 */
	public void setRegDtm(Long regDtm) {
		this.regDtm = regDtm;
	}

	/**
	 * @return 수정사용자번호
	 */
	public Integer getChgUserNo() {
		return chgUserNo;
	}

	/**
	 * @param chgUserNo 수정사용자번호
	 */
	public void setChgUserNo(Integer chgUserNo) {
		this.chgUserNo = chgUserNo;
	}

	/**
	 * @return 수정일시
	 */
	public Long getChgDtm() {
		return chgDtm;
	}

	/**
	 * @param chgDtm 수정일시
	 */
	public void setChgDtm(Long chgDtm) {
		this.chgDtm = chgDtm;
	}
}
