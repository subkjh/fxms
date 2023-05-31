package fxms.bas.impl.dpo.inlo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dbo.all.FX_CF_INLO;
import fxms.bas.impl.dbo.all.FX_CF_INLO_MEM;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.vo.Inlo;
import subkjh.bas.co.log.Logger;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;

/**
 * 설치위치 조회
 * 
 * @author subkjh
 *
 */
public class SelectInloListDfo implements FxDfo<Map<String, Object>, List<Inlo>> {

	public static void main(String[] args) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("moClass", "NODE");
		para.put("inloNo", 200184);
		SelectInloListDfo dpo = new SelectInloListDfo();
		FxFact fact = new FxFact("para", para);
		try {
//			System.out.println(FxmsUtil.toJson(dpo.call(fact, "para")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Inlo> call(FxFact fact, Map<String, Object> data) throws Exception {
		return selectInlo(data);
	}

	public Inlo selectInlo(int inloNo) throws Exception {
		Map<String, Object> para = FxApi.makePara("inloNo", inloNo);
		List<Inlo> list = selectInlo(para);
		return list.size() == 1 ? list.get(0) : null;
	}

	public List<Inlo> selectInlo(Map<String, Object> para) throws Exception {
		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		if (para == null) {
			para = new HashMap<String, Object>();
		}
		para.put("delYn", "N");

		Map<Integer, Inlo> map = new HashMap<>();
		Inlo upper, inlo;

		try {
			tran.start();
			List<FX_CF_INLO> list = tran.select(FX_CF_INLO.class, para);
			List<FX_CF_INLO_MEM> memList = tran.select(FX_CF_INLO_MEM.class, para);

			for (FX_CF_INLO a : list) {
				inlo = new Inlo(a.getInloNo(), a.getInloName(), a.getInloClCd(), a.getInloTypeCd(), a.getInloLevelCd(),
						a.getUpperInloNo(), a.getInloAllName(), a.getInloTid());
				map.put(a.getInloNo(), inlo);
			}

			// 하위 위치정보를 추가한다.
			for (FX_CF_INLO_MEM a : memList) {
				upper = map.get(a.getInloNo());
				if ( upper != null) {
					upper.addChild(a.getLowerInloNo());
				}
			}

			return new ArrayList<>(map.values());

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

}
