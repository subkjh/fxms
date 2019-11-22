package fxms.nms.co.snmp.trap.vo;

import fxms.nms.co.snmp.mo.TrapMo;
import fxms.nms.co.snmp.trap.TrapNode;
import fxms.nms.mo.property.ModelNoable;
import fxms.nms.mo.property.Modelable;

public class TrapPatternFx extends TrapPattern implements ModelNoable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5013436102083475859L;
	/** 발생위치검색MO분류 */
	private String moClass;
	/** 적용 모델 */
	private int modelNo;

	public TrapPatternFx(int alarmCode) {
		super(alarmCode);
	}

	public TrapPatternFx(int alarmCode, int modelNo, String trapOid, String varOid, String varVal, String alarmName,
			int alarmLevel, String alarmMsg, String trapOidClear, String varOidClear, String varValClear,
			int secRelease) {

		super(alarmCode, trapOid, varOid, varVal, alarmName, alarmLevel, alarmMsg, trapOidClear, varOidClear,
				varValClear, secRelease);

		setModelNo(modelNo);
	}

	/**
	 * 발생위치정보가 설정되어 있는지 여부를 판단합니다.
	 * 
	 * @return 발생위치설정여부
	 */
	public boolean isSetMo() {
		return oidMo != null && oidMo.length() > 0 //
				&& moClass != null && moClass.length() > 0 //
				&& fieldMo != null && fieldMo.length() > 0;
	}

	public String getMoClass() {
		return moClass;
	}

	public int getModelNo() {
		return modelNo;
	}

	public void setMoClass(String moClass) {
		this.moClass = moClass;
	}

	public void setModelNo(int modelNo) {
		this.modelNo = modelNo;
	}

	@Override
	public boolean equalModel(Modelable o) {
		if (o instanceof ModelNoable) {
			return ((ModelNoable) o).getModelNo() == modelNo;
		}
		return false;
	}

	@Override
	public boolean match(TrapNode node) {
		if (modelNo <= 0)
			return true;

		if (node instanceof TrapMo) {
			return ((TrapMo) node).getModelNo() == modelNo;
		}

		return false;
	}
}
