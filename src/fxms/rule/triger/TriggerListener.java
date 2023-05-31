package fxms.rule.triger;

/**
 * 룰 실행 종료를 감시하는 리슨너
 * 
 * @author subkjh
 * @since 2023.02
 */
public interface TriggerListener {

	/**
	 * 트리거가 종료되면 호출한다.
	 */
	public void onFinish();

}
