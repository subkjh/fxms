package fxms.bas.impl.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.AppApi;
import fxms.bas.impl.dpo.DateSelectSameDfo;
import fxms.bas.impl.dpo.ps.PsTableCreateDfo;
import fxms.bas.impl.dpo.ps.PsRemoveExpiredDataDfo;
import fxms.bas.impl.dpo.ps.PsStatMakeDfo;
import fxms.bas.impl.dpo.ps.PsStatReqInsertDfo;
import fxms.bas.vo.PsStatReqVo;
import subkjh.bas.co.log.Logger;

/**
 * AppApi를 구현한 API
 * 
 * @author subkjh
 *
 */
public class AppApiDfo extends AppApi {

	private final Map<String, PsStatReqVo> cacheReqStatMap;

	public AppApiDfo() {
		this.cacheReqStatMap = new HashMap<>();
	}

	@Override
	public String checkStorage(String memo) throws Exception {

		StringBuffer sb = new StringBuffer();

		String result = new PsTableCreateDfo().makeTables();
		sb.append("make=").append(result);

		result = new PsRemoveExpiredDataDfo().removeExpiredDatas();
		sb.append(",remove=").append(result);

		return sb.toString();
	}

	@Override
	public boolean requestMakeStat(List<PsStatReqVo> reqList) throws Exception {
		
		// 없는 경우만 추출한다.
		List<PsStatReqVo> list = new ArrayList<PsStatReqVo>();
		
		synchronized (this.cacheReqStatMap) {
			for (PsStatReqVo vo : reqList) {
				if (this.cacheReqStatMap.get(vo.getKey()) == null) {
					this.cacheReqStatMap.put(vo.getKey(), vo);
					list.add(vo);
				}
			}
		}

		Logger.logger.info("req.size={}, ok.size={}", reqList.size(), list.size());

		if (list.size() > 0) {
			return new PsStatReqInsertDfo().addStatReq(list) > 0;
		} else {
			return true;
		}

	}

	@Override
	public boolean responseMakeStat(PsStatReqVo req) throws Exception {
		synchronized (this.cacheReqStatMap) {
			PsStatReqVo vo = this.cacheReqStatMap.remove(req.getKey());
			Logger.logger.info("removed req : {}={}, size={}", req.getKey(), vo != null, this.cacheReqStatMap.size());
		}
		return true;
	}

	@Override
	public int generateStatistics(String psTbl, String psKindName, long psDtm) throws Exception {
		int size = new PsStatMakeDfo().generateStatistics(psTbl, psKindName, psDtm);
		return size;
	}

	@Override
	public List<String> getSameDays(String date, int count) throws Exception {
		return new DateSelectSameDfo().selectSameDates(date, count);
	}
}
