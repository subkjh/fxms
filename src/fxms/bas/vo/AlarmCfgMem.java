package fxms.bas.vo;

import java.io.Serializable;

import fxms.bas.co.CMPR_CD;
import fxms.bas.co.CoCode.ALARM_LEVEL;
import fxms.bas.fxo.filter.AlarmCfgVerifier;
import fxms.bas.mo.Moable;
import subkjh.bas.co.log.Logger;

public class AlarmCfgMem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3483431110350497014L;

	private AlarmCode alarmCode;
	private String verifierJavaClass;
	private String verifierValue = "all";
	private final int alcdNo;
	private final Number alCriCmprVal;
	private final Number alMajCmprVal;
	private final Number alMinCmprVal;
	private final Number alWarCmprVal;
	private final int repeatTimes;
	private final String fpactCd;

	public AlarmCfgMem(int alcdNo, Number alCriCmprVal, Number alMajCmprVal, Number alMinCmprVal, Number alWarCmprVal,
			int repeatTimes, String fpactCd) {
		this.alcdNo = alcdNo;
		this.alCriCmprVal = alCriCmprVal;
		this.alMajCmprVal = alMajCmprVal;
		this.alMinCmprVal = alMinCmprVal;
		this.alWarCmprVal = alWarCmprVal;
		this.repeatTimes = repeatTimes;
		this.fpactCd = fpactCd;
	}

	public AlarmCode getAlarmCode() {
		return alarmCode;
	}

	public int getAlcdNo() {
		return alcdNo;
	}

	public Number getAlCriCmprVal() {
		return alCriCmprVal;
	}

	public Number getAlMajCmprVal() {
		return alMajCmprVal;
	}

	public Number getAlMinCmprVal() {
		return alMinCmprVal;
	}

	public Number getAlWarCmprVal() {
		return alWarCmprVal;
	}

	public CMPR_CD getCompareCode() {
		return CMPR_CD.getCompare(alarmCode.getCompareCode());
	}

	public String getFpactCd() {
		return fpactCd;
	}

	public int getRepeatTimes() {
		return repeatTimes;
	}

	public String getVerifierJavaClass() {
		return verifierJavaClass;
	}

	public String getVerifierValue() {
		return verifierValue;
	}

	public boolean isMatch(int alcdNo, String psId) {
		return alcdNo == getAlcdNo() && psId.equals(alarmCode.getPsId());
	}

	public boolean isValid(Moable mo) {

		if (getVerifierJavaClass() == null || getVerifierJavaClass().trim().length() == 0) {
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

	/**
	 * 
	 * @param valuePre
	 * @param valueCur
	 * @param numValue 비교조건 값
	 * @return
	 */
	public AlarmCfgMemMatched match(Number valuePre, Number valueCur) {

		if (alCriCmprVal != null && match(alCriCmprVal, valuePre, valueCur)) {

			return new AlarmCfgMemMatched(this, ALARM_LEVEL.Critical.getAlarmLevel(), alCriCmprVal,
					compute(valuePre, valueCur));

		} else if (alMajCmprVal != null && match(alMajCmprVal, valuePre, valueCur)) {

			return new AlarmCfgMemMatched(this, ALARM_LEVEL.Major.getAlarmLevel(), alMajCmprVal,
					compute(valuePre, valueCur));

		} else if (alMinCmprVal != null && match(alMinCmprVal, valuePre, valueCur)) {

			return new AlarmCfgMemMatched(this, ALARM_LEVEL.Minor.getAlarmLevel(), alMinCmprVal,
					compute(valuePre, valueCur));

		} else if (alWarCmprVal != null && match(alWarCmprVal, valuePre, valueCur)) {

			return new AlarmCfgMemMatched(this, ALARM_LEVEL.Warning.getAlarmLevel(), alWarCmprVal,
					compute(valuePre, valueCur));

		}

		return null;
	}

	public void setAlarmCode(AlarmCode alarmCode) {
		this.alarmCode = alarmCode;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("ALCD-NO(" + getAlcdNo() + ")");
		sb.append("COMPARE(CODE(" + getAlarmCode().getCompareCode() + ")");

		return sb.toString();
	}

	private Number compute(Number preValue, Number value) {
		CMPR_CD compareCode = getCompareCode();

		if (compareCode == CMPR_CD.IC || compareCode == CMPR_CD.DC) {
			if (preValue == null || preValue.doubleValue() == 0)
				return value;

			double rate = value.doubleValue() / preValue.doubleValue();
			rate *= 100;

			return (rate - 100);
		} else {
			return value;
		}
	}

	/**
	 * 
	 * @param compVal  비교값
	 * @param valuePre 이전수집값
	 * @param valueCur 현재수집값
	 * @return
	 */
	private boolean match(Number compVal, Number valuePre, Number valueCur) {

		double curVal = valueCur.doubleValue();
		double comVal = compVal.doubleValue();
		CMPR_CD compareCode = getCompareCode();

		if (compareCode == CMPR_CD.IC) {
			if (valuePre == null || valuePre.doubleValue() == 0)
				return false;
			double rate = valueCur.doubleValue() / valuePre.doubleValue();
			rate *= 100;
			rate -= 100;
			return rate >= comVal;
		}

		if (compareCode == CMPR_CD.DC) {
			if (valuePre == null || valuePre.doubleValue() == 0)
				return false;
			double rate = 1 - (valueCur.doubleValue() / valuePre.doubleValue());
			rate *= 100;
			return rate >= comVal;
		}

		return ((compareCode == CMPR_CD.GT) && curVal > comVal) ||

				((compareCode == CMPR_CD.GE) && curVal >= comVal) ||

				((compareCode == CMPR_CD.LT) && curVal < comVal) ||

				((compareCode == CMPR_CD.LE) && curVal <= comVal) ||

				((compareCode == CMPR_CD.EQ) && curVal == comVal) ||

				((compareCode == CMPR_CD.NE) && curVal != comVal) ||

				(compareCode == CMPR_CD.OK);
	}

}
