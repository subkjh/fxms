package fxms.bas.alarm;

import fxms.bas.dbo.ao.FX_AL_CFG_MEM;
import fxms.bas.mo.Mo;
import subkjh.bas.log.Logger;

public class AlarmCfgMem extends FX_AL_CFG_MEM {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3483431110350497014L;
	private AlarmCode alarmCode;

	public Number compute(Number preValue, Number value) {
		AoCode.COMPARE_CODE compareCode = getCompareCode();

		if (compareCode == AoCode.COMPARE_CODE.IC || compareCode == AoCode.COMPARE_CODE.DC) {
			if (preValue == null || preValue.doubleValue() == 0)
				return value;

			double rate = value.doubleValue() / preValue.doubleValue();
			rate *= 100;

			return (rate - 100);
		} else {
			return value;
		}
	}

	public AlarmCode getAlarmCode() {
		return alarmCode;
	}

	public AoCode.COMPARE_CODE getCompareCode() {
		return AoCode.COMPARE_CODE.getCompare(alarmCode.getCompareCode());
	}

	public boolean isMatch(int alcdNo, String psCode) {
		return alcdNo == getAlcdNo() && psCode.equals(alarmCode.getPsCode());
	}

	/**
	 * 
	 * @param valuePre
	 * @param valueCur
	 * @param numValue
	 *            비교조건 값
	 * @return
	 */
	public boolean match(Number valuePre, Number valueCur) {

		AoCode.COMPARE_CODE compareCode = getCompareCode();
		double numValue = getCompareVal();

		if (compareCode == AoCode.COMPARE_CODE.IC) {
			if (valuePre == null || valuePre.doubleValue() == 0)
				return false;
			double rate = valueCur.doubleValue() / valuePre.doubleValue();
			rate *= 100;
			rate -= 100;
			return rate >= numValue;
		}

		if (compareCode == AoCode.COMPARE_CODE.DC) {
			if (valuePre == null || valuePre.doubleValue() == 0)
				return false;
			double rate = 1 - (valueCur.doubleValue() / valuePre.doubleValue());
			rate *= 100;
			return rate >= numValue;
		}

		return ((compareCode == AoCode.COMPARE_CODE.GT) && valueCur.doubleValue() > numValue) ||

				((compareCode == AoCode.COMPARE_CODE.GE) && valueCur.doubleValue() >= numValue) ||

				((compareCode == AoCode.COMPARE_CODE.LT) && valueCur.doubleValue() < numValue) ||

				((compareCode == AoCode.COMPARE_CODE.LE) && valueCur.doubleValue() <= numValue) ||

				((compareCode == AoCode.COMPARE_CODE.EQ) && valueCur.doubleValue() == numValue) ||

				((compareCode == AoCode.COMPARE_CODE.NE) && valueCur.doubleValue() != numValue) ||

				(compareCode == AoCode.COMPARE_CODE.OK);
	}

	public void setAlarmCode(AlarmCode alarmCode) {
		this.alarmCode = alarmCode;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("ALCD-NO(" + getAlcdNo() + ")");
		sb.append("COMPARE(CODE(" + getAlarmCode().getCompareCode() + ")");
		sb.append("VAL(" + getCompareVal() + "))");
		sb.append("ALARM-LEVEL(" + getAlarmLevel() + ")");

		return sb.toString();
	}

	public boolean isValid(Mo mo) {

		if (getVerifierJavaClass() == null || getVerifierJavaClass().trim().length() == 0 ) {
			return true;
		}

		if ("all".equalsIgnoreCase(getVerifierValue())) {
			return true;
		}

		try {
			AlarmCfgVerifier verifier = (AlarmCfgVerifier) Class.forName(getVerifierJavaClass()).newInstance();
			return verifier.isValid(mo, getVerifierValue());
		} catch (Exception e) {
			Logger.logger.error(e);
			return false;
		}

	}

}
