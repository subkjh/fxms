package fxms.bas.po;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;
import fxms.bas.FxmsCodes;
import fxms.bas.api.EventApi;
import fxms.bas.api.FxApi;
import fxms.bas.api.MoApi;
import fxms.bas.api.PsApi;
import fxms.bas.co.def.PS_TYPE;
import fxms.bas.fxo.FxCfg;
import fxms.bas.po.exp.PsItemNotFoundException;
import fxms.bas.po.item.PsItem;
import fxms.bas.po.vo.FxServicePsVo;
import fxms.bas.po.vo.UpdateDataVo;
import fxms.bas.po.vo.ValMo;

public abstract class ValueApi extends FxApi {

	/** use DBM */
	public static ValueApi api;

	/**
	 * 사용할 DBM를 제공합니다.
	 * 
	 * @return DBM
	 */
	public synchronized static ValueApi getApi() {
		if (api != null)
			return api;

		api = makeApi(ValueApi.class);

		return api;
	}

	private VoSubAlarm checker;

	/** 수집하고 있는 성능항목 */
	private final Map<String, FxServicePsVo> collectedPsCodeMap;

	private VoSubUpdater updater;

	/**
	 * 현재성능을 가지고 있는 맵<br>
	 * KEY : PS_CODE
	 */
	private Map<String, List<ValMo>> valCurMap;

	private VoSubInsert writer;

	/**
	 * 
	 */
	public ValueApi() {
		valCurMap = new HashMap<String, List<ValMo>>();
		collectedPsCodeMap = Collections.synchronizedMap(new HashMap<String, FxServicePsVo>());
	}

	/**
	 * 수집한 데이터를 기록합니다.
	 * 
	 * @param mstime
	 *            수집일시
	 * @param voList
	 *            기록할 값
	 * @param checkAlarm
	 *            경보확인 여부
	 * @param noti
	 *            기록 후 보낼 노티
	 */
	public synchronized void addValue(VoList voList, boolean checkAlarm) {

		if (voList == null || voList.size() == 0)
			return;

		if (voList.getMstime() == 0) {
			Logger.logger.fail(String.valueOf(voList));
			return;
		}

		Logger.logger.debug(String.valueOf(voList));
		FxServicePsVo ps;

		// 메모리에 적재 먼저 합니다.

		PsVo value;
		for (int i = voList.size() - 1; i >= 0; i--) {
			value = voList.get(i);

			if (value == null) {
				voList.remove(i);
				continue;
			}

			if (PsApi.getApi().getItem(value.getPsCode()) == null) {

				Logger.logger.fail(Logger.makeString("  CHECK >> ps-code='" + value.getPsCode()
						+ "' IS NOT DEFINED. mo-no=" + value.getMoNo() + ", value=" + value.getValue()));

				EventApi.getApi().check(MoApi.getApi().getSystemMo(), value.getPsCode(),
						FxmsCodes.AlarmCode.PsItemNotDefined, value.getPsCode(), null);

				voList.remove(i);

				continue;
			}
			ps = collectedPsCodeMap.get(voList.getOwner() + "-" + value.getPsCode());
			if (ps == null) {
//				FxServicePs e = new FxServicePs();
//				e.setFxactorJavaClass(voList.getOwner());
//				e.setMsIpaddr(FxCfg.getCfg().getIpAddress());
//				e.setPsCode(value.getPsCode());
//				e.setServiceName(FxCfg.getFxServiceName());
//				e.setLastDate(FxApi.getDate());
//				collectedPsCodeMap.put(e.getFxactorJavaClass() + "-" + e.getPsCode(), e);
			} else {
				ps.setLastDate(FxApi.getDate());
			}

			setCurValue(voList.getMstime(), value);
		}

		// 업데이트를 위해 큐에 넣음.
		getUpdater().put(voList.clone());

		// 기록
		getInserter().put(voList.clone());

		// 통보
		VoSubNotifier.getVoNotifier().put(voList.clone());

		// 경보를 확인
		if (checkAlarm) {
			getChecker().put(voList);
		}

	}

	public Map<String, FxServicePsVo> getCollectedPsCodeMap() {
		return collectedPsCodeMap;
	}

	/**
	 * 수집된 성능에 대한 경보를 확인합니다.<br>
	 * 해당 스레드가 경보를 확인하지 않고 경보 확인을 요청하기만 합니다.<br>
	 * 그러면 "API-VALUE#Check" 스레드에 의해 경보가 확인됩니다.
	 * 
	 * @param mstime
	 *            수집일시
	 * @param valueList
	 *            수집된 성능목록
	 */
	public void checkThreshold(VoList valueList) {

		if (valueList == null || valueList.size() == 0)
			return;

		getChecker().put(valueList);

	}

	public abstract Map<String, Integer> doInsertValue(VoList voList) throws Exception;

	/**
	 * 장비 상태를 업데이트할때 비교할 현재값을 조회합니다. <br>
	 * 이 메소드는 브로거용으로 다른 클래스에서 사용하지 않습니다.<br>
	 * 
	 * @return 현재값
	 * @throws Exception
	 */
	public abstract List<PsVo> doSelectValueCur() throws Exception;

	/**
	 * 컬럼정보를 업데이트 합니다.<br>
	 * 이 메소드는 브로거용으로 다른 클래스에서 사용하지 않습니다.<br>
	 * 
	 * @param updateList
	 */
	public abstract void doUpdateColumn(List<UpdateDataVo> updateList) throws Exception;

	public abstract void doUpdateServicePsCode() throws Exception;

	public VoSubInsert getInserter() {
		if (writer == null)
			startSub();
		return writer;
	}

	/**
	 * 관리대상의 현재값을 조회합니다.<br>
	 * 
	 * @param moNo
	 *            관리대상번호
	 * @param perfNo
	 *            성능항목<br>
	 *            성능항목이 0이하이면 모든 성능을 조회해 줍니다.
	 * @return 성능값<br>
	 *         key : 성능항목
	 */
	public Map<String, List<ValMo>> getPsValue(long moNo) {

		Map<String, List<ValMo>> map = new HashMap<String, List<ValMo>>();

		synchronized (valCurMap) {
			List<ValMo> entry;
			for (String key : valCurMap.keySet()) {
				entry = findValue(valCurMap.get(key), moNo);
				if (entry != null)
					map.put(key, entry);
			}
		}

		return map;
	}

	public List<ValMo> getPsValue(String psCode, long moNo) {

		synchronized (valCurMap) {
			List<ValMo> allList = valCurMap.get(psCode);
			return findValue(allList, moNo);
		}

	}

	@Override
	public String getState(LOG_LEVEL level) {
		StringBuffer sb = new StringBuffer();
		if (LOG_LEVEL.trace.contains(level)) {

			synchronized (valCurMap) {
				sb.append("cur-value[");
				for (String psCode : valCurMap.keySet()) {
					sb.append(psCode);
					sb.append("(");
					sb.append(valCurMap.get(psCode).size());
					sb.append(")");
				}
				sb.append("]");
			}

		} else {
			sb.append("cur-value=" + valCurMap.size());
		}

		sb.append("\n::colledted-ps-code");
		for (String key : collectedPsCodeMap.keySet()) {
			sb.append("\n\t");
			sb.append(collectedPsCodeMap.get(key));
		}
		return sb.toString();
	}

	/**
	 * 현재 성능을 조회합니다.
	 * 
	 * @param moNo
	 *            MO번호
	 * @param instance
	 *            인스턴스
	 * @param perfNo
	 *            성능항목
	 * @return 현재값
	 */
	public ValMo getValMo(String psCode, long moNo, String moInstance) {

		synchronized (valCurMap) {

			List<ValMo> allList = valCurMap.get(psCode);
			List<ValMo> valList = findValue(allList, moNo);
			if (valList != null) {

				if (moInstance == null) {
					for (ValMo val : valList) {
						if (val.getMoInstance() == null) {
							return val;
						}
					}
				} else {
					for (ValMo val : valList) {
						if (moInstance.equals(val.getMoInstance())) {
							return val;
						}
					}
				}
			}

			return null;
		}

	}

	public List<TimeSeriesVo> getValueList(long moNo, String psCode, PS_TYPE pstype, long startDate, long endDate)
			throws Exception {

		PsItem item = PsApi.getApi().getItem(psCode);
		if (item == null) {
			throw new PsItemNotFoundException(psCode);
		}

		return doSelectPsValue(moNo, item, pstype, startDate, endDate);
	}

	private List<ValMo> findValue(List<ValMo> valueList, long moNo) {
		List<ValMo> ret = null;

		if (valueList != null) {
			for (ValMo val : valueList) {
				if (val.getMoNo() == moNo) {
					if (ret == null) {
						ret = new ArrayList<ValMo>();
					}
					ret.add(val);
				}
			}
		}
		return ret;
	}

	private VoSubAlarm getChecker() {
		if (checker == null)
			startSub();
		return checker;
	}

	private VoSubUpdater getUpdater() {
		if (updater == null)
			startSub();
		return updater;
	}

	/**
	 * 현재값 비교가 필요한 내용을 가져옵니다.
	 */
	private void initValueCur() {
		try {

			List<PsVo> voList = doSelectValueCur();

			if (checker != null)
				checker.initValue(voList);
			if (updater != null)
				updater.initValue(voList);

			long mstime = System.currentTimeMillis();
			for (PsVo vo : voList) {
				setCurValue(mstime, vo);
			}

		} catch (Exception e) {
			Logger.logger.error(e);
		}
	}

	/**
	 * 수집한 성능값을 메모리에 적재합니다.
	 * 
	 * @param mstime
	 *            수집일시
	 * @param value
	 *            값
	 */
	private void setCurValue(long mstime, PsVo value) {

		List<ValMo> valList;
		ValMo entry = null;

		synchronized (valCurMap) {

			valList = valCurMap.get(value.getPsCode());

			if (valList == null) {
				valList = new ArrayList<ValMo>();
				valCurMap.put(value.getPsCode(), valList);
				entry = new ValMo(value.getMoNo(), value.getMoInstance(), null);
				valList.add(entry);
			} else {
				for (ValMo e : valList) {
					if (e.getMoNo() == value.getMoNo()) {
						if (e.getMoInstance() != null) {
							if (e.getMoInstance().equals(value.getMoInstance())) {
								entry = e;
							}
							continue;
						} else {
							entry = e;
						}
						break;
					}
				}
				if (entry == null) {
					entry = new ValMo(value.getMoNo(), value.getMoInstance(), null);
					valList.add(entry);
				}
			}

			entry.setValue(value.getValue(), mstime);
		}
	}

	private synchronized void startSub() {
		if (checker == null) {
			List<PsVo> valList = null;
			try {
				valList = doSelectValueCur();
			} catch (Exception e) {
				Logger.logger.error(e);
			}

			checker = new VoSubAlarm();
			checker.initValue(valList);
			checker.start();

			updater = new VoSubUpdater();
			updater.initValue(valList);
			updater.start();

			writer = new VoSubInsert();
			writer.start();

			VoSubNotifier.getVoNotifier();
		}
	}

	protected abstract List<TimeSeriesVo> doSelectPsValue(long moNo, PsItem item, PS_TYPE pstype, long startDate,
			long endDate) throws Exception;

	public abstract List<FxServicePsVo> doSelectServicePs(String msIpaddr, String serviceName) throws Exception;

	@Override
	protected void initApi() throws Exception {

		initValueCur();

		try {
			List<FxServicePsVo> list = doSelectServicePs(FxCfg.getCfg().getIpAddress(), FxCfg.getFxServiceName());

			for (FxServicePsVo e : list) {
				collectedPsCodeMap.put(e.getFxactorJavaClass() + "-" + e.getPsCode(), e);
			}
		} catch (Exception e) {
			Logger.logger.error(e);
		}

	}

	@Override
	protected void reload() throws Exception {
		initValueCur();
	}
}