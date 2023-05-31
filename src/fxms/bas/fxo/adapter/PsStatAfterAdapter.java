package fxms.bas.fxo.adapter;

/**
 * 통계가 생성된 후 호출되는 아답터
 * 
 * @author subkjh
 *
 */
public abstract class PsStatAfterAdapter extends FxAdapterImpl {

	/**
	 * 통계를 생성한다.
	 * 
	 * @param rawList 원천데이터
	 * @return
	 */
	public abstract void afterStat(String psTable, String psDataCd, long psDate) throws Exception;

}
