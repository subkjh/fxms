package fxms.bas.impl.dpo.ps;

import java.util.List;

import fxms.bas.fxo.FxmsUtil;
import fxms.bas.impl.dbo.all.FX_PS_POINT;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.vo.PsPoint;
import subkjh.dao.ClassDaoEx;

/**
 * 관제점 정보를 조회한다.
 * 
 * @author subkjh
 *
 */
public class PsPointSelectDfo implements FxDfo<Void, List<PsPoint>> {

	public static void main(String[] args) {
		PsPointSelectDfo dfo = new PsPointSelectDfo();
		try {
			List<PsPoint> list = dfo.selectPsPoints();
			for ( PsPoint ps : list) {
				System.out.println(FxmsUtil.toJson(ps));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<PsPoint> call(FxFact fact, Void data) throws Exception {
		return selectPsPoints();
	}

	public List<PsPoint> selectPsPoints() throws Exception {
		List<PsPoint> list = ClassDaoEx.SelectDatas(FX_PS_POINT.class, null, PsPoint.class);

		return list;
	}

}
