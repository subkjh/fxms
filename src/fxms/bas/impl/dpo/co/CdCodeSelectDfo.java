package fxms.bas.impl.dpo.co;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.impl.dbo.all.FX_CO_CD;
import fxms.bas.impl.dbo.all.FX_CO_CD_CL;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.vo.Code;
import subkjh.dao.ClassDaoEx;

/**
 * 코드 목록 조회
 * 
 * @author subkjh
 *
 */
public class CdCodeSelectDfo implements FxDfo<Map<String, Object>, List<Code>> {

	@Override
	public List<Code> call(FxFact fact, Map<String, Object> data) throws Exception {
		return selectCodes(data);
	}

	/**
	 * 
	 * @param para
	 * @return
	 * @throws Exception
	 */
	public List<Code> selectCodes(Map<String, Object> para) throws Exception {

		if (para == null) {
			para = new HashMap<String, Object>();
		}
		para.put("useYn", "Y");

		List<FX_CO_CD> list = ClassDaoEx.SelectDatas(FX_CO_CD.class, para);
		List<Code> ret = new ArrayList<Code>();
		for (FX_CO_CD data : list) {
			ret.add(new Code(data.getCdClass(), data.getCdCode(), data.getCdName()));
		}
		return ret;
	}

	/**
	 * 
	 * @param cdClass
	 * @return
	 * @throws Exception
	 */
	public Map<String, Code> selectCodeMap(String cdClass) throws Exception {

		Map<String, Object> para = new HashMap<>();
		para.put("useYn", "Y");
		para.put("cdClass", cdClass);
		Map<String, Code> ret = new HashMap<>();

		List<FX_CO_CD> list = ClassDaoEx.SelectDatas(FX_CO_CD.class, para);

		for (FX_CO_CD data : list) {
			ret.put(data.getCdCode(), new Code(data.getCdClass(), data.getCdCode(), data.getCdName()));
		}
		return ret;
	}

	/**
	 * 
	 * @param cdClass
	 * @return
	 * @throws Exception
	 */
	public FX_CO_CD_CL selectCl(String cdClass) throws Exception {
		return ClassDaoEx.SelectData(FX_CO_CD_CL.class, FxApi.makePara("cdClass", cdClass));
	}

}