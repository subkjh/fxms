package fxms.bas.ws.ps;

import fxms.bas.vo.PsVo;

public class ReqPsVo {

	private String action;

	private String moInstance;

	private long moNo;

	private String psId;

	private Number value;

	public ReqPsVo() {

	}

	public ReqPsVo(PsVo vo) {
		moNo = vo.getMo().getMoNo();
		moInstance = vo.getMoInstance();
		psId = vo.getPsItem().getPsId();
		value = vo.getValue();
	}

	public String getKey() {
		StringBuffer sb = new StringBuffer();
		sb.append(moNo).append(psId);
		if (moInstance != null && moInstance.length() > 0) {
			sb.append("/").append(moInstance);
		}
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

	public void setMoInstance(String moInstance) {
		this.moInstance = moInstance;
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
