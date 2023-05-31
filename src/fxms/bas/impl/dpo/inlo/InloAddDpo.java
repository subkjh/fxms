package fxms.bas.impl.dpo.inlo;

import java.util.List;
import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.fxo.FxmsUtil;
import fxms.bas.impl.dbo.all.FX_CF_INLO;
import fxms.bas.impl.dbo.all.FX_CF_INLO_MEM;
import fxms.bas.impl.dpo.BroadcastDfo;
import fxms.bas.impl.dpo.FxDpo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.signal.ReloadSignal;
import fxms.bas.signal.ReloadSignal.ReloadType;
import fxms.bas.vo.Inlo;
import subkjh.dao.ClassDaoEx;

/**
 * 설치위치를 기록하는 DPO
 * 
 * @author subkjh
 *
 */
public class InloAddDpo implements FxDpo<Map<String, Object>, Integer> {

	public static void main(String[] args) {
		InloAddDpo dpo = new InloAddDpo();
		FxFact fact = new FxFact();
		Map<String, Object> datas = FxApi.makePara("upperInloNo", 11109, "inloName", "TEST", "inloDesc", "테스트데이터");
		try {
			System.out.println(dpo.run(fact, datas));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Integer run(FxFact fact, Map<String, Object> data) throws Exception {
		int userNo = fact.getUserNo();
		return addInlo(userNo, data);
	}

	/**
	 * 
	 * @param userNo
	 * @param datas
	 * @return
	 * @throws Exception
	 */
	public int addInlo(int userNo, Map<String, Object> datas) throws Exception {

		int upperInloNo = FxmsUtil.getInt(datas, "upperInloNo", Inlo.TOP_INLO_NO); // 상위위치 번호 확인

		Inlo upper = new SelectInloListDfo().selectInlo(upperInloNo); // 상위위치 조회

		FX_CF_INLO inlo = new InloMakeDfo().make(userNo, upper, datas);

		List<FX_CF_INLO_MEM> members = new InloMemMakeDfo().makeMembers(userNo, upper.getInloNo(), inlo.getInloNo());

		ClassDaoEx.open().insertOfClass(FX_CF_INLO.class, inlo)
				.deleteOfClass(FX_CF_INLO_MEM.class, FxApi.makePara("lowerInloNo", inlo.getInloNo()))
				.insertOfClass(FX_CF_INLO_MEM.class, members).close();

		new BroadcastDfo().broadcast(new ReloadSignal(ReloadType.Inlo)); // 기록 통보

		return inlo.getInloNo();

	}

}
