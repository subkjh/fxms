package fxms.bas.vo;

import java.io.Serializable;

import fxms.bas.co.CoCode.ALARM_LEVEL;
import fxms.bas.co.CoCode.CMPR_CD;
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
	private int alcdNo;
	private Double alCriCmprVal;
	private Double alMajCmprVal;
	private Double alMinCmprVal;
	private Double alWarCmprVal;
	private int repeatTimes = 1;
	private String fpactCd;

	public AlarmCfgMem() {

	}

	public AlarmCfgMem(int alcdNo, Double alCriCmprVal,Double alMajCmprVal,Double alMinCmprVal,Double alWarCmprVal, int repeatTimes, String fpactCd) {
		this.alcdNo = alcdNo;
		this.alCriCmprVal= alCriCmprVal;
		this.alMajCmprVal= alMajCmprVal;
		this.alMinCmprVal= alMinCmprVal;
		this.alWarCmprVal= alWarCmprVal;
		this.repeatTimes = repeatTimes;
		this.fpactCd = fpactCd;
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
	private boolean match(double compVal, Number valuePre, Number valueCur) {

		CMPR_CD compareCode = getCompareCode();

		if (compareCode == CMPR_CD.IC) {
			if (valuePre == null || valuePre.doubleValue() == 0)
				return false;
			double rate = valueCur.doubleValue() / valuePre.doubleValue();
			rate *= 100;
			rate -= 100;
			return rate >= compVal;
		}

		if (compareCode == CMPR_CD.DC) {
			if (valuePre == null || valuePre.doubleValue() == 0)
				return false;
			double rate = 1 - (valueCur.doubleValue() / valuePre.doubleValue());
			rate *= 100;
			return rate >= compVal;
		}

		return ((compareCode == CMPR_CD.GT) && valueCur.doubleValue() > compVal) ||

				((compareCode == CMPR_CD.GE) && valueCur.doubleValue() >= compVal) ||

				((compareCode == CMPR_CD.LT) && valueCur.doubleValue() < compVal) ||

				((compareCode == CMPR_CD.LE) && valueCur.doubleValue() <= compVal) ||

				((compareCode == CMPR_CD.EQ) && valueCur.doubleValue() == compVal) ||

				((compareCode == CMPR_CD.NE) && valueCur.doubleValue() != compVal) ||

				(compareCode == CMPR_CD.OK);
	}

	public AlarmCode getAlarmCode() {
		return alarmCode;
	}

	public int getAlcdNo() {
		return alcdNo;
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

			return new AlarmCfgMemMatched(this, ALARM_LEVEL.Critical.getAlarmLevel(), alCriCmprVal, compute(valuePre, valueCur));

		} else if (alMajCmprVal != null && match(alMajCmprVal, valuePre, valueCur)) {

			return new AlarmCfgMemMatched(this, ALARM_LEVEL.Major.getAlarmLevel(), alMajCmprVal, compute(valuePre, valueCur));

		} else if (alMinCmprVal != null && match(alMinCmprVal, valuePre, valueCur)) {

			return new AlarmCfgMemMatched(this, ALARM_LEVEL.Minor.getAlarmLevel(), alMinCmprVal, compute(valuePre, valueCur));

		} else if (alWarCmprVal != null && match(alWarCmprVal, valuePre, valueCur)) {

			return new AlarmCfgMemMatched(this, ALARM_LEVEL.Warning.getAlarmLevel(), alWarCmprVal, compute(valuePre, valueCur));

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

}
