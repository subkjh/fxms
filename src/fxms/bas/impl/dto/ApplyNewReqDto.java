package fxms.bas.impl.dto;

import java.io.Serializable;

import fxms.bas.fxo.FxAttr;

/**
 * @description 사용자신규요청테이블
 * @author fxms auto
 * @date 20230509
 */

public class ApplyNewReqDto implements Serializable {

	private static final long serialVersionUID = 1L;

	public ApplyNewReqDto() {
	}

	@FxAttr(description = "신청사용자명")
	private String applyUserName;

	@FxAttr(description = "신청전화번호", required = false)
	private String applyTelNo;

	@FxAttr(description = "신청설치위치명", required = false)
	private String applyInloName;

	@FxAttr(description = "신청설치위치번호", required = false, example = "0")
	private Integer applyInloNo = 0;

	@FxAttr(description = "신청메모", required = false)
	private String applyMemo;

	@FxAttr(description = "신청사용자암호")
	private String applyUserPwd;

	@FxAttr(description = "신청사용자메일")
	private String applyUserMail;

	@FxAttr(description = "인증번호", required = false)
	private String certfNum;

}
