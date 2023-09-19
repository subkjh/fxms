package fxms.bas.impl.dpo.ps;

import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.exp.FxDupException;
import fxms.bas.impl.dpo.FxDpo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.vo.PsItem;

/**
 * 성능항목 등록 프로세스
 * 
 * @author subkjh
 *
 */
public class PsItemAddDpo implements FxDpo<Void, Boolean> {

	@Override
	public Boolean run(FxFact fact, Void data) throws Exception {
		int userNo = fact.getUserNo();
		String psId = fact.getString("psId");
		String psName = fact.getString("psName");
		Map<String, Object> datas = fact.getMap("datas");
		return this.addPsItem(userNo, psId, psName, datas);
	}

	/**
	 * 
	 * @param userNo 등록사용자번호
	 * @param psId   성능ID
	 * @param psName 성능명
	 * @param para   기타정보
	 * @return
	 * @throws Exception
	 */
	public boolean addPsItem(int userNo, String psId, String psName, Map<String, Object> para) throws Exception {

		PsItemSelectDfo selectDfo = new PsItemSelectDfo();

		PsItem psItem = selectDfo.selectPsItem(psId); // 조회한다.

		// 2. 없으면 추가
		if (psItem == null) {

			new PsItemAddDfo().addPsItem(userNo, psId, psName, para);

		} else {

			if (psItem.isUse()) {
				throw new FxDupException("PsItem", psId);
			}

			new PsItemUpdateDfo().updatePsItem(0, psId, FxApi.makePara("useYn", "Y"));

		}

		psItem = selectDfo.selectPsItem(psId); // 등록된 내용 조회

		new PsTableCreateDfo().createTable(psItem); // 관련 테이블 생성

		return true;
	}

}
