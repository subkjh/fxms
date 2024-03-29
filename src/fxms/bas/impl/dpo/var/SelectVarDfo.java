package fxms.bas.impl.dpo.var;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.fxo.FxmsUtil;
import fxms.bas.impl.dbo.all.FX_CF_VAR;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.vo.FxVarVo;
import subkjh.dao.ClassDaoEx;

/**
 * 환경변수 값을 조회한다.
 * 
 * @author subkjh
 *
 */
public class SelectVarDfo implements FxDfo<Map<String, Object>, List<FxVarVo>> {

	public static void main(String[] args) {

		SelectVarDfo dfo = new SelectVarDfo();
		FxFact fact = new FxFact();
		try {
			System.out.println(
					FxmsUtil.toJson(dfo.call(fact, FxApi.makePara("varName like", "fxms.data.updated.time%"))));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<FxVarVo> call(FxFact fact, Map<String, Object> data) throws Exception {
		return selectVar(data);
	}

	/**
	 * 입력된 조건에 맞는 사용중인 환경변수 목록을 조회한다.
	 * 
	 * @param para 조건
	 * @return
	 * @throws Exception
	 */
	public List<FxVarVo> selectVar(Map<String, Object> para) throws Exception {

		if (para == null)
			para = new HashMap<>();
		para.put("useYn", "Y");

		List<FX_CF_VAR> list = ClassDaoEx.SelectDatas(FX_CF_VAR.class, para);
		List<FxVarVo> ret = new ArrayList<FxVarVo>();
		for (FX_CF_VAR e : list) {
			ret.add(new FxVarVo(e.getVarName(), e.getVarVal()));
		}
		return ret;
	}

	/**
	 * 입력된 조건에 맞는 사용중인 환경변수 목록을 조회한다.
	 * 
	 * @param para 조건
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> selectVarMap(Map<String, Object> para) throws Exception {
		if (para == null)
			para = new HashMap<>();
		para.put("useYn", "Y");

		List<FX_CF_VAR> list = ClassDaoEx.SelectDatas(FX_CF_VAR.class, para);

		Map<String, String> ret = new HashMap<>();
		for (FX_CF_VAR e : list) {
			ret.put(e.getVarName(), e.getVarVal());
		}
		return ret;

	}
}
