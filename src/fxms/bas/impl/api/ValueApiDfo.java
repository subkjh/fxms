package fxms.bas.impl.api;

import java.util.List;
import java.util.Map;

import fxms.bas.api.MoApi;
import fxms.bas.api.PsApi;
import fxms.bas.api.ValueApi;
import fxms.bas.impl.dpo.vo.CheckAlarmVoDfo;
import fxms.bas.impl.dpo.vo.SelectCurValueDfo;
import fxms.bas.impl.dpo.vo.SelectStatValuesDfo;
import fxms.bas.impl.dpo.vo.SelectValuesDfo;
import fxms.bas.impl.dpo.vo.UpdateCurValueDfo;
import fxms.bas.impl.dpo.vo.ValueAddDpo;
import fxms.bas.impl.dpo.vo.ValueCurMap;
import fxms.bas.impl.dpo.vo.ValueExtractValidDfo;
import fxms.bas.impl.dpo.vo.ValueWriteFileDfo;
import fxms.bas.mo.Mo;
import fxms.bas.vo.PsItem;
import fxms.bas.vo.PsKind;
import fxms.bas.vo.PsValueComp;
import fxms.bas.vo.PsValueSeries;
import fxms.bas.vo.PsValues;
import fxms.bas.vo.PsVoList;
import fxms.bas.vo.PsVoRawList;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.DateUtil;

/**
 * ValueApi를 구현한 API<br>
 * 이 API는 주로 ValueService에서 사용하는 API이고 다른 서비스는 ValueApiService를 주로 사용한다.
 * 
 * @author subkjh
 *
 */
public class ValueApiDfo extends ValueApi {

	private final ValueCurMap curValues = new ValueCurMap();

	public static void main(String[] args) throws Exception {

		ValueApiDfo api = new ValueApiDfo();

		List<PsValueSeries> list = api.getValues(-1, "ePowerAmt", "HOUR1",
				DateUtil.getDtm(System.currentTimeMillis() - 10 * 3600000L),
				DateUtil.getDtm(System.currentTimeMillis()));
		for (PsValueSeries vo : list) {
			System.out.println(vo.getDebug());
		}

	}
	
	public ValueApiDfo()
	{
		
	}

	@Override
	public int addValue(PsVoRawList rawList, boolean checkAlarm) {

		int ret = 0;

		// 정재된 수집 데이터를 사용하기 위해 데이터를 정재한다.
		PsVoList list = new ValueExtractValidDfo().extractValidData(rawList);
		if (list == null)
			return -1;

		try {
			ret = addValues(list);
		} catch (Exception e) {
			Logger.logger.error(e);
		}

		// 파일에 별도 보관한다.(테스트용)
		// TODO : Remove
		try {
			new ValueWriteFileDfo().write(list);
		} catch (Exception e) {
			Logger.logger.error(e);
		}

		// 4. 알람확인
		if (checkAlarm) {
			new CheckAlarmVoDfo().checkAlarm(list);
		}

		try {
			new UpdateCurValueDfo().updateCur(list);
		} catch (Exception e) {
			Logger.logger.error(e);
		}

		// 현재값을 보관한다.
		curValues.setCurValueInCache(list);

		return ret;
	}

	/**
	 * 
	 * @param datas
	 * @return
	 * @throws Exception
	 */
	protected int addValues(PsVoList datas) throws Exception {
		try {
			return new ValueAddDpo().run(null, datas);
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		}
	}

	@Override
	public PsValueComp getCurValue(long moNo, String moInstance, String psId) throws Exception {

		// 캐쉬에 존재하는지 확인
		PsValueComp ret = curValues.getCurValueInCache(moNo, moInstance, psId);

		if (ret != null) {
			return ret;
		}

		// 저장소에서 읽어오기
		List<PsValueComp> list = new SelectCurValueDfo().selectCurValues(
				makePara("moNo", moNo, "moInstance", moInstance == null ? "*" : moInstance, "psId", psId));
		ret = list.size() == 1 ? list.get(0) : null;

		// 캐쉬에 보관하기
		if (ret != null) {
			curValues.setCurValueInCache(ret);
		}

		return ret;

	}

	@Override
	public List<PsValueSeries> getValues(long moNo, String psId, String psKindName, long startDtm, long endDtm)
			throws Exception {

		// 성능항목 확인
		PsItem item = PsApi.getApi().getPsItem(psId);
		Mo mo = MoApi.getApi().getMo(moNo);
		PsKind psKind = PsApi.getApi().getPsKind(psKindName);

		if (psKind.isRaw()) {
			return new SelectValuesDfo().selectSeriesValues(mo, item, psKind, null, startDtm, endDtm);
		} else {
			return new SelectValuesDfo().selectSeriesValues(mo, item, psKind, item.getKindCols(), startDtm, endDtm);
		}
	}

	@Override
	public Map<Long, Number> getStatValue(String psId, PsKind psKind, long startDtm, long endDtm, StatFunction statFunc)
			throws Exception {

		PsItem item = PsApi.getApi().getPsItem(psId);

		return new SelectStatValuesDfo().selectStatValue(item, psKind, startDtm, endDtm, statFunc);
	}

	@Override
	public List<PsValues> getValues(long moNo, String psKindName, long startDtm, long endDtm) throws Exception {
		PsKind psKind = PsApi.getApi().getPsKind(psKindName);
		Mo mo = MoApi.getApi().getMo(moNo);
		return new SelectValuesDfo().selectValues(mo, psKind, startDtm, endDtm);
	}

	@Override
	public List<PsValues> getValues(long moNo, String moInstance, String psId, String psKindName, String psKindCol, long startDtm,
			long endDtm) throws Exception {

		// 성능항목 확인
		PsItem item = PsApi.getApi().getPsItem(psId);

		Mo mo = MoApi.getApi().getMo(moNo);

		PsKind psKind = PsApi.getApi().getPsKind(psKindName);

		return new SelectValuesDfo().selectValues(mo, moInstance, item, psKind, psKindCol, startDtm, endDtm);
	}

	@Override
	public List<PsValues> getValues(String psId, String psKindName, String psKindCol, long startDtm, long endDtm)
			throws Exception {

		PsItem item = PsApi.getApi().getPsItem(psId);

		PsKind psKind = PsApi.getApi().getPsKind(psKindName);

		return new SelectValuesDfo().selectValues(item, psKind, psKindCol, startDtm, endDtm);
	}
}
