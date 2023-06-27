package fxms.bas.impl.dpo.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fxms.bas.api.AppApi;
import fxms.bas.api.PsApi;
import fxms.bas.exp.NotFoundException;
import fxms.bas.fxo.FxmsUtil;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.vo.PsKind;
import fxms.bas.vo.PsStatReqVo;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.DateUtil;

/**
 * 수집한 데이터에 대한 생성할 통계 목록을 제공한다.
 * 
 * @author subkjh
 *
 */
public class ReqMakeStatValueDfo implements FxDfo<List<PsStatReqVo>, Boolean> {

	public static void main(String[] args) throws NotFoundException {
		ReqMakeStatValueDfo dpo = new ReqMakeStatValueDfo();
		List<String> list = new ArrayList<>();
		list.add("AAA");
		list.add("AAA2");
		list.add("AAA3");
		System.out
				.println(FxmsUtil.toJson(dpo.make(System.currentTimeMillis(), list, PsApi.getApi().getPsKind(PsKind.PSKIND_5M))));
	}

	private List<PsStatReqVo> make(long mstime, Collection<String> psTables, PsKind psKindSrc) {

		long hstime = DateUtil.getDtm(mstime);

		List<PsStatReqVo> retList = new ArrayList<>();
		try {

			List<PsKind> psKindList = PsApi.getApi().getPsKind2Dst(psKindSrc);
			long psDate;
			PsStatReqVo newData;

			for (String psTable : psTables) {

				for (PsKind psKind : psKindList) {

					// 원천에 대한 통계는 생성하지 않는다.
					if (psKind.isRaw()) {
						continue;
					}

					// 통계 일시
					psDate = psKind.getHstimeStart(hstime);

					newData = new PsStatReqVo(psTable, psKind.getPsKindName(), psKind.getHstimeStart(psDate));
					retList.add(newData);
				}
			}
		} catch (Exception e) {
			Logger.logger.error(e);
		}

		return retList;
	}

	private boolean request(List<PsStatReqVo> statReqList) {
		try {
			AppApi.getApi().requestMakeStat(statReqList);
			return true;
		} catch (Exception e) {
			Logger.logger.error(e);
			return false;
		}
	}

	@Override
	public Boolean call(FxFact fact, List<PsStatReqVo> voList) throws Exception {
		return request(voList);
	}

	public void requestMakeStat(long mstime, Collection<String> psTables, PsKind psKindSrc) {
		List<PsStatReqVo> list = make(mstime, psTables, psKindSrc);
		request(list);
	}
}