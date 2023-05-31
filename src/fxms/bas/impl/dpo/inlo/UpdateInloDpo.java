package fxms.bas.impl.dpo.inlo;

import java.util.Map;

import fxms.bas.fxo.FxmsUtil;
import fxms.bas.impl.dpo.BroadcastDfo;
import fxms.bas.impl.dpo.FxDpo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.signal.ReloadSignal;
import fxms.bas.signal.ReloadSignal.ReloadType;
import fxms.bas.vo.Inlo;

/**
 * 설치위치를 기록하는 DPO
 * 
 * @author subkjh
 *
 */
public class UpdateInloDpo implements FxDpo<Map<String, Object>, Boolean> {

	@Override
	public Boolean run(FxFact fact, Map<String, Object> data) throws Exception {

		int userNo = fact.getUserNo();
		int inloNo = fact.getInt("inloNo");

		return updateInlo(userNo, inloNo, data);
	}

	/**
	 * 
	 * @param userNo
	 * @param datas
	 * @return
	 * @throws Exception
	 */
	public boolean updateInlo(int userNo, int inloNo, Map<String, Object> datas) throws Exception {

		int upperInloNo = FxmsUtil.getInt(datas, "upperInloNo", Inlo.TOP_INLO_NO); // 상위위치 번호 확인

		new UpdateInloDfo().updateInlo(userNo, inloNo, datas); // 수정 내역 기록

		new SetInloMemDfo().setInloMem(userNo, upperInloNo, inloNo); // 관계 설정

		new BroadcastDfo().broadcast(new ReloadSignal(ReloadType.Inlo)); // 기록 통보

		return true;
	}

}
