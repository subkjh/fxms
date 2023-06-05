package fxms.bas.impl.ext.influxdb;

import java.util.List;
import java.util.Map;

import fxms.bas.api.MoApi;
import fxms.bas.api.PsApi;
import fxms.bas.impl.api.ValueApiDfo;
import fxms.bas.mo.Mo;
import fxms.bas.vo.PsItem;
import fxms.bas.vo.PsKind;
import fxms.bas.vo.PsValueSeries;
import fxms.bas.vo.PsValues;
import fxms.bas.vo.PsVoList;
import subkjh.bas.co.log.Logger;

/**
 * 수집데이터를 InfluxDB를 이용하여 작업한다.
 * 
 * @author subkjh
 *
 */
public class ValueApiInfluxDb extends ValueApiDfo {

	@Override
	public Map<Long, Number> getStatValue(String psId, PsKind psKind, long startDtm, long endDtm, StatFunction statFunc)
			throws Exception {

		PsItem item = PsApi.getApi().getPsItem(psId);

		return new SelectStatValuesInfluxDfo().selectStatValue(item, startDtm, endDtm, statFunc);
	}

	@Override
	public List<PsValues> getValues(long moNo, String psKindName, long startDtm, long endDtm) throws Exception {
		PsKind psKind = PsApi.getApi().getPsKind(psKindName);
		Mo mo = MoApi.getApi().getMo(moNo);
		return new SelectValuesInfluxDfo().selectValues(mo, psKind, startDtm, endDtm);
	}

	@Override
	public List<PsValueSeries> getValues(long moNo, String psId, String psKindName, long startDtm, long endDtm)
			throws Exception {

		// 성능항목 확인
		PsItem item = PsApi.getApi().getPsItem(psId);
		Mo mo = MoApi.getApi().getMo(moNo);
		PsKind psKind = PsApi.getApi().getPsKind(psKindName);

		if (psKind.isRaw()) {
			return new SelectValuesInfluxDfo().selectSeriesValues(mo, item, psKind, null, startDtm, endDtm);
		} else {
			return new SelectValuesInfluxDfo().selectSeriesValues(mo, item, psKind, item.getKindCols(), startDtm,
					endDtm);
		}
	}

	@Override
	public List<PsValues> getValues(long moNo, String psId, String psKindName, String psKindCol, long startDtm,
			long endDtm) throws Exception {

		// 성능항목 확인
		PsItem item = PsApi.getApi().getPsItem(psId);

		Mo mo = MoApi.getApi().getMo(moNo);

		PsKind psKind = PsApi.getApi().getPsKind(psKindName);

		return new SelectValuesInfluxDfo().selectValues(mo, item, psKind, psKindCol, startDtm, endDtm);
	}

	@Override
	public List<PsValues> getValues(String psId, String psKindName, String psKindCol, long startDtm, long endDtm)
			throws Exception {

		PsItem item = PsApi.getApi().getPsItem(psId);

		PsKind psKind = PsApi.getApi().getPsKind(psKindName);

		return new SelectValuesInfluxDfo().selectValues(item, psKind, psKindCol, startDtm, endDtm);
	}

	@Override
	protected int addValues(PsVoList datas) throws Exception {

		int ret = super.addValues(datas); // RDB에 1차 기록한다.

		try {
			new ValueAddInfluxDfo().add(datas); // influxDB에 또 기록한다.
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		}

		return ret;
	}
}
