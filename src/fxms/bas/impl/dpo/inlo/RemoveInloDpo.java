package fxms.bas.impl.dpo.inlo;

import fxms.bas.api.FxApi;
import fxms.bas.impl.dpo.BroadcastDfo;
import fxms.bas.impl.dpo.FxDpo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.impl.handler.dto.inlo.InloDeleteDto;
import fxms.bas.signal.ReloadSignal;
import fxms.bas.signal.ReloadSignal.ReloadType;
import subkjh.bas.co.utils.ObjectUtil;

/**
 * 설치위치를 기록하는 DPO
 * 
 * @author subkjh
 *
 */
public class RemoveInloDpo implements FxDpo<InloDeleteDto, Boolean> {

	public static void main(String[] args) {
		RemoveInloDpo dpo = new RemoveInloDpo();
		try {
			dpo.run(new FxFact(),
					ObjectUtil.toObject(FxApi.makePara("inloNo", 200332, "inloName", "TEST"), InloDeleteDto.class));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Boolean run(FxFact fact, InloDeleteDto data) throws Exception {
		return removeInlo(data.getInloNo(), data.getInloName());
	}

	/**
	 * 
	 * @param userNo
	 * @param datas
	 * @return
	 * @throws Exception
	 */
	public boolean removeInlo(int inloNo, String inloName) throws Exception {

		boolean ret = new SetDeleteFlagDfo().setDeleteFlag(inloNo, inloName); // 삭제 플래그 설정
		if (ret == false)
			return false;

		new RemoveDeletedInloDfo().removeInlo(); // 실제 삭제함.

		new BroadcastDfo().broadcast(new ReloadSignal(ReloadType.Inlo)); // 기록 통보

		return true;
	}

}
