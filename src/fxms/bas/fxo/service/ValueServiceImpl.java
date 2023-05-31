package fxms.bas.fxo.service;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.api.PsApi;
import fxms.bas.api.ValueApi;
import fxms.bas.api.ValueApi.StatFunction;
import fxms.bas.event.NotiFilter;
import fxms.bas.mo.Mo;
import fxms.bas.vo.PsKind;
import fxms.bas.vo.PsValueComp;
import fxms.bas.vo.PsValueSeries;
import fxms.bas.vo.PsValues;
import fxms.bas.vo.PsVoRawList;
import subkjh.bas.co.log.Logger;

/**
 * 
 * @author subkjh
 *
 */
public class ValueServiceImpl extends FxServiceImpl implements ValueService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1625599685662558741L;

	/**
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		FxServiceImpl.start(ValueService.class.getSimpleName(), ValueServiceImpl.class, args);
	}

	public ValueServiceImpl(String name, int port) throws RemoteException, Exception {
		super(name, port);
	}

	@Override
	protected void onInit(StringBuffer sb) throws Exception {
		super.onInit(sb);

		FxApi.initServiceApi(ValueApi.class);
	}

	@Override
	public void addValue(PsVoRawList voList, boolean checkAlarm) throws RemoteException, Exception {
		int count = -1;
		try {
			count = ValueApi.getApi().addValue(voList, checkAlarm);
		} catch (Exception e) {
			logger.error(e);
		} finally {
			Logger.logger.info("value={} alarm={} --> {}", voList, checkAlarm, count);
		}

	}

	@Override
	public PsValueComp getCurValue(long moNo, String moInstance, String psId) throws RemoteException, Exception {

		PsValueComp ret = null;
		try {
			ret = ValueApi.getApi().getCurValue(moNo, moInstance, psId);
			return ret;
		} catch (Exception e) {
			logger.error(e);
			return null;
		} finally {
			logger.info("moNo={}, moInstance={}, psId={} --> {}", moNo, moInstance, psId,
					ret == null ? "error" : ret.toString());
		}

	}

	@Override
	public NotiFilter getNotiFilter() throws RemoteException, Exception {
		NotiFilter notiFilter = new NotiFilter();
		notiFilter.add(Mo.class);
		return notiFilter;
	}

	@Override
	public Map<Long, Number> getStatValue(String psId, String psKindName, String psKindCol, long startDtm, long endDtm,
			StatFunction statFunc) throws RemoteException, Exception {

		Map<Long, Number> ret = null;
		try {
			PsKind psKind = PsApi.getApi().getPsKind(psKindName);
			ret = ValueApi.getApi().getStatValue(psId, psKind, psKindCol, startDtm, endDtm, statFunc);
			return ret;
		} catch (Exception e) {
			logger.error(e);
			throw e;
		} finally {
			logger.info("psId={}, startDtm={}, endDtm={}, statFunc={} --> {}", psId, startDtm, endDtm, statFunc,
					ret == null ? "error" : ret.size());
		}
	}

	@Override
	public List<PsValueSeries> getSeriesValues(long moNo, String psId, String psKindName, String statFunc[],
			long startDtm, long endDtm) throws RemoteException, Exception {

		PsKind psKind = PsApi.getApi().getPsKind(psKindName);

		psKind.checkDateRange(startDtm, endDtm);

		return ValueApi.getApi().getValues(moNo, psId, psKindName, startDtm, endDtm);

	}

	@Override
	public List<PsValues> getValues(long moNo, String psKindName, long startDtm, long endDtm)
			throws RemoteException, Exception {

		List<PsValues> list = null;
		try {
			list = ValueApi.getApi().getValues(moNo, psKindName, startDtm, endDtm);
			return list;
		} catch (Exception e) {
			logger.error(e);
			throw e;
		} finally {
			logger.info("moNo={}, psKindName={}, startDtm={}, endDtm={} --> {}", moNo, psKindName, startDtm, endDtm,
					list == null ? "error" : list.size());
		}

	}

	@Override
	public List<PsValues> getValues(long moNo, String psId, String psKindName, String psKindCol, long startDtm,
			long endDtm) throws RemoteException, Exception {

		List<PsValues> list = null;
		try {
			list = ValueApi.getApi().getValues(moNo, psId, psKindName, psKindCol, startDtm, endDtm);
			return list;
		} catch (Exception e) {
			logger.error(e);
			throw e;
		} finally {
			logger.info("moNo={}, psId={}, psKindName={}, psKindCol={}, startDtm={}, endDtm={} --> {}", moNo, psId,
					psKindName, psKindCol, startDtm, endDtm, list == null ? "error" : list.size());
		}

	}

	@Override
	public List<PsValues> getValues(String psId, String psKindName, String psKindCol, long startDtm, long endDtm)
			throws RemoteException, Exception {

		List<PsValues> list = null;
		try {
			list = ValueApi.getApi().getValues(psId, psKindName, psKindCol, startDtm, endDtm);
			return list;
		} catch (Exception e) {
			logger.error(e);
			throw e;
		} finally {
			logger.info("psId={}, psKindName={}, startDtm={}, endDtm={}, psKindCol={} --> {}", psId, psKindName,
					startDtm, endDtm, psKindCol, list == null ? "error" : list.size());
		}
	}
}