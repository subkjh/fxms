package fxms.bas.impl.dto;

import fxms.bas.fxo.FxAttr;

/**
 * @description 사용자신규요청테이블
 * @author fxms auto
 * @date 20230509
 */

public class ConfirmUserDto {

	@FxAttr(description = "신청설치위치번호", required = false, example = "0")
	private Integer applyInloNo = 0;

	@FxAttr(description = "사용시작일자", required = false, example = "20000101")
	private Integer useStrtDate = 20000101;

	@FxAttr(description = "사용종료일자", required = false, example = "39991231")
	private Integer useEndDate = 39991231;

	@FxAttr(description = "접근NETWORK", required = false)
	private String accsNetwork;

	@FxAttr(description = "접근NETMASK", required = false)
	private String accsNetmask;

	@FxAttr(description = "처리사용자번호", required = false)
	private Integer procUserNo;

	@FxAttr(description = "처리일시", required = false)
	private Long procDtm;

	@FxAttr(description = "처리사유", required = false)
	private String procRsn;

	@FxAttr(description = "신청사용자메일")
	private String applyUserMail;

	@FxAttr(description = "신규사용자등록상태코드", example = "A")
	private String newUserRegStCd = "A";

	public String getApplyUserMail() {
		return applyUserMail;
	}

	@FxAttr(description = "사용자그룹번호")
	private Integer ugrpNo;

	public void setNewUserRegStCd(String newUserRegStCd) {
		this.newUserRegStCd = newUserRegStCd;
	}

	public void setUgrpNo(Integer ugrpNo) {
		this.ugrpNo = ugrpNo;
	}

}
