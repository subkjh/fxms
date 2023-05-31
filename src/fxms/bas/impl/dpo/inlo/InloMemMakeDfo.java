package fxms.bas.impl.dpo.inlo;

import java.util.ArrayList;
import java.util.List;

import fxms.bas.fxo.FxmsUtil;
import fxms.bas.impl.dbo.all.FX_CF_INLO_MEM;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import subkjh.dao.ClassDaoEx;
import subkjh.dao.util.FxTableMaker;

/**
 * 설치위치 관계 설정 DFO
 * 
 * @author subkjh
 *
 */
public class InloMemMakeDfo implements FxDfo<Void, List<FX_CF_INLO_MEM>> {

	public static void main(String[] args) {
		InloMemMakeDfo dfo = new InloMemMakeDfo();
		try {
			FxFact fact = new FxFact("upperInloNo", 11109, "inloNo", 99999);
			FxmsUtil.print(dfo.call(fact, null));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public List<FX_CF_INLO_MEM> call(FxFact fact, Void data) throws Exception {

		int userNo = fact.getUserNo();
		int upperInloNo = fact.getInt("upperInloNo");
		int inloNo = fact.getInt("inloNo");

		return makeMembers(userNo, upperInloNo, inloNo);
	}

	public List<FX_CF_INLO_MEM> makeMembers(int userNo, int upperInloNo, int inloNo) throws Exception {

		List<FX_CF_INLO_MEM> memList = new ArrayList<FX_CF_INLO_MEM>();

		// 1. 자신과 자신 매핑
		FX_CF_INLO_MEM mine = new FX_CF_INLO_MEM();
		mine.setInloNo(inloNo);
		mine.setLowerInloNo(inloNo);
		mine.setLowerDepth(0);
		FxTableMaker.initRegChg(userNo, mine);

		memList.add(mine);

		// 2. 상위와 자신 매핑
		if (upperInloNo > 0) {
			List<FX_CF_INLO_MEM> list = ClassDaoEx.SelectDatas(FX_CF_INLO_MEM.class,
					ClassDaoEx.makePara("lowerInloNo", upperInloNo));
			for (FX_CF_INLO_MEM mem : list) {
				mem.setLowerInloNo(inloNo);
				mem.setLowerDepth(mem.getLowerDepth() + 1);
				FxTableMaker.initRegChg(userNo, mem);

			}
			memList.addAll(list);
		}

		return memList;

	}
}
