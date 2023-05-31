package fxms.bas.impl.dbo;

import fxms.bas.impl.dbo.all.FX_UR_USER_WORK_HST;
import subkjh.bas.co.utils.DateUtil;

public class UserLogDbo extends FX_UR_USER_WORK_HST {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8059584176443977483L;

	public UserLogDbo() {

	}

	public UserLogDbo(int userNo, String sessionId, String opId, String opName) {
		setUserNo(userNo);
		setSessionId(sessionId);
		setOpId(opId);
		setOpName(opName);
	}

	public UserLogDbo(int userNo, String sessionId, String opId, String inPara, String outRet, int retNo, String retMsg,
			long mstimeStart, String objType, Object objNo, String objName) {

		setEndDtm(DateUtil.getDtm());
		setInPara(inPara);
		setOpId(opId);
		setOpSeqNo(0L);
		setOutRet(outRet);
		setRstCont(retMsg);
		setRstNo(retNo);
		setSessionId(sessionId);
		setStrtDtm(DateUtil.getDtm(mstimeStart));
		setUserNo(userNo);

		setOpObjType(objType);
		if (objNo != null) {
			setOpObjNo(String.valueOf(objNo));
		}
		setOpObjName(objName);

	}

	public void setOpObjNo(Object opObjNo) {
		if (opObjNo != null) {
			super.setOpObjNo(String.valueOf(opObjNo));
		}
	}
}
