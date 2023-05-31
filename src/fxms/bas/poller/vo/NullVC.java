package fxms.bas.poller.vo;

/**
 * 값 변환자(VC)를 사용하지 않음을 의미하는 VC
 * 
 * @author subkjh
 * 
 */
public class NullVC extends VC {

	@Override
	public Number convert(Number value) {
		return value;
	}

	@Override
	public String convertUnit(Number max, Number min) {
		return "";
	}

}
