package fxms.bas.vo;

import fxms.bas.co.CoCode.MO_STATUS;
import fxms.bas.event.FxEventImpl;
import fxms.bas.mo.Mo;

/**
 * 관리대상 상태 이벤트
 * @author subkjh
 *
 */
public class MoStateEvt extends FxEventImpl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1022864021115743632L;

	private Mo mo;

	private MO_STATUS moStatus;

	public MoStateEvt() {

	}

	public MoStateEvt(Mo mo, MO_STATUS moStatus) {
		this.mo = mo;
		this.moStatus = moStatus;
	}

	public Mo getMo() {
		return mo;
	}

	public void setMo(Mo mo) {
		this.mo = mo;
	}

	public MO_STATUS getMoStatus() {
		return moStatus;
	}

	public void setMoStatus(MO_STATUS moStatus) {
		this.moStatus = moStatus;
	}
	
	@Override
	public String toString() {

		StringBuffer sb = new StringBuffer();
		sb.append(getNo()).append(")");
		sb.append(getClass().getSimpleName());
		sb.append("(").append(mo.getMoNo());
		sb.append(",").append(mo.getMoName()).append(")");
		sb.append(" ").append(moStatus);
		return sb.toString();
	}

}
