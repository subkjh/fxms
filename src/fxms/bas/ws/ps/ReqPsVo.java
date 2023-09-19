package fxms.bas.ws.ps;

import fxms.bas.vo.PsVo;

public class ReqPsVo {

	private String action;

	private long moNo;

	private String psId;

	private Number value;

	public ReqPsVo() {

	}

	public ReqPsVo(PsVo vo) {
		moNo = vo.getMo().getMoNo();
		psId = vo.getPsItem().getPsId();
		value = vo.getValue();
	}

	public String getKey() {
		StringBuffer sb = new StringBuffer();
		sb.append(moNo).append(psId);
		return sb.toString();
	}

	@Override
	public boolean equals(Object obj) {

		if (obj instanceof ReqPsVo) {
			ReqPsVo vo = (ReqPsVo) obj;

			if (moNo != vo.moNo)
				return false;
			if (psId.equals(vo.psId) == false)
				return false;

			return true;
		}

		return super.equals(obj);
	}

	public String getAction() {
		return action;
	}

	public long getMoNo() {
		return moNo;
	}

	public String getPsId() {
		return psId;
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

	public void setMoNo(long moNo) {
		this.moNo = moNo;
	}

	public void setPsId(String psId) {
		this.psId = psId;
	}

	public Number getValue() {
		return value;
	}

	public void setValue(Number value) {
		this.value = value;
	}

}
