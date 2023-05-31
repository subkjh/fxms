package fxms.bas.impl.dpo;

/**
 * FxMS Data Function Object<br>
 * 
 * @author subkjh
 *
 * @param <IN>  입력
 * @param <OUT> 출력
 */
public interface FxDfo<IN, OUT> {

	/**
	 * 
	 * @param fact    데이터
	 * @param dataVar 데이터명
	 * @return 처리 결과가 담긴 데이터
	 * @throws Exception
	 */

	public OUT call(FxFact fact, IN data) throws Exception;

}
