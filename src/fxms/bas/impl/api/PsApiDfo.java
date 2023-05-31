package fxms.bas.impl.api;

import java.util.List;
import java.util.Map;

import fxms.bas.api.PsApi;
import fxms.bas.impl.dpo.ps.PsItemSelectDfo;
import fxms.bas.impl.dpo.ps.PsKindSelectDfo;
import fxms.bas.vo.PsItem;
import fxms.bas.vo.PsKind;

/**
 * 데이터베이스를 이용하여 PsApi를 구현한 클래스
 * 
 * @author subkjh
 *
 */
public class PsApiDfo extends PsApi {

	@Override
	protected List<PsItem> selectPsItem(Map<String, Object> para) throws Exception {
		return new PsItemSelectDfo().selectPsItems(para);
	}

	@Override
	protected List<PsKind> selectPsKind() throws Exception {
		return new PsKindSelectDfo().selectPsKinds();
	}

}
