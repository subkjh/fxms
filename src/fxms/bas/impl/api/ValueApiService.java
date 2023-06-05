package fxms.bas.impl.api;

import java.util.List;
import java.util.Map;

import fxms.bas.api.FxApiServiceTag;
import fxms.bas.api.ServiceApi;
import fxms.bas.api.ValueApi;
import fxms.bas.exp.FxServiceNotFoundException;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.fxo.service.ValueService;
import fxms.bas.fxo.thread.QueueFxThread;
import fxms.bas.vo.PsKind;
import fxms.bas.vo.PsValueComp;
import fxms.bas.vo.PsValueSeries;
import fxms.bas.vo.PsValues;
import fxms.bas.vo.PsVoRawList;
import subkjh.bas.co.log.Logger;

/**
 * ValueService를 이용하여 수집된 값을 기록하는 ValueApi
 * 
 * @author subkjh
 *
 */
public class ValueApiService extends ValueApi implements FxApiServiceTag {

	class Data {
		PsVoRawList voList;
		boolean checkAlarm;
	}

	private ValueService valueService = null;
	private QueueFxThread<Data> thread;

	public ValueApiService() {
		try {
			setName(FxServiceImpl.serviceName + ":ValueApi");
		} catch (Exception e) {
			setName("ValueApiService");
		}
	}

	private ValueService getValueService() throws FxServiceNotFoundException, Exception {

		if (this.valueService == null) {
			this.valueService = ServiceApi.getApi().getService(ValueService.class);
		}

		// ping을 해봐서 끊겼는지 확인한다.
		try {
			this.valueService.ping(getName());
		} catch (Exception e) {
			this.valueService = ServiceApi.getApi().getService(ValueService.class);
		}

		return this.valueService;

	}

	@Override
	public int addValue(PsVoRawList voList, boolean checkAlarm) {

		Data data = new Data();
		data.checkAlarm = checkAlarm;
		data.voList = voList;

		thread.put(data);

		return 0;
	}

	@Override
	public PsValueComp getCurValue(long moNo, String moInstance, String psId) throws Exception {
		ValueService svc = getValueService();
		return svc.getCurValue(moNo, moInstance, psId);
	}

	@Override
	public List<PsValueSeries> getValues(long moNo, String psId, String psKindName, long startDtm, long endDtm)
			throws Exception {
		return getValueService().getSeriesValues(moNo, psId, psKindName, null, startDtm, endDtm);
	}

	@Override
	public Map<Long, Number> getStatValue(String psId, PsKind psKind, long startDtm, long endDtm, StatFunction stat)
			throws Exception {
		ValueService svc = getValueService();
		return svc.getStatValue(psId, psKind.getPsKindName(), startDtm, endDtm, stat);
	}

	@Override
	public List<PsValues> getValues(long moNo, String psKindName, long startDtm, long endDtm) throws Exception {
		ValueService svc = getValueService();
		return svc.getValues(moNo, psKindName, startDtm, endDtm);
	}

	@Override
	public List<PsValues> getValues(long moNo, String psId, String psKindName, String psKindCol, long startDtm,
			long endDtm) throws Exception {
		ValueService svc = getValueService();
		return svc.getValues(moNo, psId, psKindName, psKindCol, startDtm, endDtm);

	}

	@Override
	public List<PsValues> getValues(String psId, String psKindName, String psKindCol, long startDtm, long endDtm)
			throws Exception {
		ValueService svc = getValueService();
		return svc.getValues(psId, psKindName, psKindCol, startDtm, endDtm);
	}

	@Override
	public void onCreated() throws Exception {

		super.onCreated();

		if (thread != null) {
			return;
		}

		thread = new QueueFxThread<Data>() {

			@Override
			protected void doInit() throws Exception {

			}

			@Override
			protected void doWork(Data data) throws Exception {

				try {
					ValueService svc = getValueService();
					svc.addValue(data.voList, data.checkAlarm);
					Logger.logger.debug("send values : {}", data.voList);

				} catch (Exception e) {

					Logger.logger.error(e);

					// ValueService 다시 연결하기 위해
					valueService = null;

					// 못 보낸 데이터 다시 보내기 위해
					thread.put(data);

					// 부하를 방지하기 위해
					Thread.sleep(1000);
				}

			}

			@Override
			protected void onNoDatas(long index) {

			}

		};

		thread.setName(getClass().getSimpleName() + "-thread");
		thread.start();
	}

}
