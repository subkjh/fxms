package fxms.bas.impl.dpo;

import java.util.Map;

import fxms.bas.exp.FxOkException;
import subkjh.bas.co.utils.DateUtil;

/**
 * FxMS Data Process Object<br>
 * 
 * @author subkjh
 *
 */
public interface FxDpo<IN, OUT> {

	/**
	 * 
	 * @param fact  데이터객체
	 * @param datas 입력데이터
	 * @return
	 * @throws FxOkException 예외발생이거 정상적임
	 * @throws Exception     오류 예외
	 */
	public OUT run(FxFact fact, IN datas) throws FxOkException, Exception;

	public static void initRegChg(int userNo, Map<String, Object> datas) {

		datas.put("chgDtm", DateUtil.getDtm());
		datas.put("chgUserNo", userNo);

	}
}
