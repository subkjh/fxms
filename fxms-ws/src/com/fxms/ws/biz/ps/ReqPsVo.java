package com.fxms.ws.biz.ps;

import fxms.bas.pso.PsVo;

public class ReqPsVo {

	private String action;

	private String moInstance;

	private long moNo;

	private String psCode;

	public ReqPsVo() {

	}

	public ReqPsVo(PsVo vo) {
		moNo = vo.getMoNo();
		moInstance = vo.getMoInstance();
		psCode = vo.getPsCode();
	}

	@Override
	public boolean equals(Object obj) {

		if (obj instanceof ReqPsVo) {
			ReqPsVo vo = (ReqPsVo) obj;

			if (moNo != vo.moNo)
				return false;
			if (psCode.equals(vo.psCode) == false)
				return false;
			if (moInstance == null || vo.moInstance == null)
				return true;

			return String.valueOf(moInstance).equals(String.valueOf(vo.moInstance));
		}

		return super.equals(obj);
	}

	public String getAction() {
		return action;
	}

	public String getMoInstance() {
		return moInstance;
	}

	public long getMoNo() {
		return moNo;
	}

	public String getPsCode() {
		return psCode;
	}

	@Override
	public int hashCode() {
		return 1;
	}

	public boolean isAdd() {
		return "add".equalsIgnoreCase(action);
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void setMoInstance(String moInstance) {
		this.moInstance = moInstance;
	}

	public void setMoNo(long moNo) {
		this.moNo = moNo;
	}

	public void setPsCode(String psCode) {
		this.psCode = psCode;
	}

}
