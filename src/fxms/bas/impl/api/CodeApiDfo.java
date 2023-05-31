package fxms.bas.impl.api;

import java.util.List;
import java.util.Map;

import fxms.bas.api.CodeApi;
import fxms.bas.impl.dpo.co.CdCodeSelectDfo;
import fxms.bas.vo.Code;

/**
 * EventApi에 사용하는 데이터를 저장소로부터 가져온다.
 * 
 * @author subkjh
 *
 */
public class CodeApiDfo extends CodeApi {

	@Override
	protected List<Code> selectCodes(Map<String, Object> para) throws Exception {
		return new CdCodeSelectDfo().selectCodes(para);
	}

}
