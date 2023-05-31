package fxms.bas.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.signal.ReloadSignal.ReloadType;
import fxms.bas.vo.FxVarVo;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;

/**
 * 환경변수 데이터 처리 API<br>
 * 환경 변수는 캐슁하지 않는다.
 * 
 * @author subkjh
 *
 */
public abstract class VarApi extends FxApi {

	public static VarApi api;

	public static final String UPDATED_TIME_VAR = "fxms.data.updated.time.";

	/**
	 * 사용할 DBM를 제공합니다.
	 * 
	 * @return DBM
	 */
	public synchronized static VarApi getApi() {
		if (api != null)
			return api;

		api = makeApi(VarApi.class);

		return api;
	}

	/** 변수가 적용된 최종 시간 */
	private final Map<String, Long> appliedTimeMap;

	public VarApi() {
		this.appliedTimeMap = new HashMap<String, Long>();
	}

	private void backupVar() throws Exception {

		List<FxVarVo> varList = getVars(null);

		StringBuffer sb = new StringBuffer();
		for (FxVarVo var : varList) {
			sb.append(Logger.makeSubString(var.getName(), var.getValue()));
		}

		Logger.logger.info(Logger.makeString("variables", null, sb.toString()));

		save2file("var.list", varList);
	}

	/**
	 * 
	 * @param varName
	 * @return
	 * @throws Exception
	 */
	private String getVarValue(String varName) throws Exception {
		List<FxVarVo> list = getVars(makePara("varName", varName));
		return list.size() == 1 ? list.get(0).getValue() : null;
	}

	/**
	 * 서비스가 해당 데이터를 적용한 시간을 기록한다.
	 * 
	 * @param type
	 * @param hstime
	 */
	public void appliedData(ReloadType type, long hstime) {
		synchronized (this.appliedTimeMap) {
			this.appliedTimeMap.put(type.name(), hstime);
		}
	}

	@Override
	public String getState(LOG_LEVEL level) {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getClass().getSimpleName());
		synchronized (this.appliedTimeMap) {
			for (String key : this.appliedTimeMap.keySet())
				sb.append(Logger.makeSubString("updated " + key, this.appliedTimeMap.get(key)));
		}
		return sb.toString();
	}

	/**
	 * 
	 * @param varGrpName
	 * @return
	 * @throws Exception
	 */
	public List<FxVarVo> getVarList(String varGrpName) throws Exception {
		return getVars(makePara("varGrpName", varGrpName));
	}

	/**
	 * 환경변수를 저장소에서 조회합니다.
	 * 
	 * @return 환경변수 목록
	 * @throws Exception
	 */
	public abstract List<FxVarVo> getVars(Map<String, Object> para) throws Exception;

	/**
	 * 
	 * @param varName
	 * @param valueDefault
	 * @return
	 */
	public int getVarValue(String varName, int valueDefault) {

		String value = null;

		try {
			value = getVarValue(varName);
			if (value == null) {
				return valueDefault;
			}
			return Integer.parseInt(value);

		} catch (Exception e) {
			Logger.logger.fail("var={}, value={}", varName, value);
			return valueDefault;
		}
	}

	/**
	 *
	 * @param varName
	 * @param valueDefault
	 * @return
	 */
	public long getVarValue(String varName, long valueDefault) {

		try {
			String value;

			value = getVarValue(varName);
			if (value == null)
				return valueDefault;

			return Long.parseLong(value);
		} catch (Exception e) {
			return valueDefault;
		}
	}

	/**
	 * 
	 * @param varName
	 * @param valueDefault
	 * @return
	 */
	public String getVarValue(String varName, String valueDefault) {
		try {
			String value = getVarValue(varName);
			if (value == null)
				return valueDefault;
			return value;
		} catch (Exception e) {
			Logger.logger.error(e);
			return valueDefault;
		}
	}

	/**
	 * 데이터가 외부에서 업데이트 되었는지 확인한다.
	 * 
	 * @param type
	 * @return
	 */
	private boolean isDataUpdated(ReloadType type, long updateTime) {
		Long time = null;
		synchronized (this.appliedTimeMap) {
			time = this.appliedTimeMap.get(type.name());
		}
		if (time == null)
			return true;

		return updateTime >= time.longValue();
	}

	/**
	 * 데이터가 변경된 시간을 확인하여 업데이트 된 내역을 조회한다.
	 * 
	 * @return
	 */
	public List<ReloadType> getUpdatedData() {

		List<ReloadType> ret = new ArrayList<>();

		try {
			List<FxVarVo> list = this.getVars(makePara("varName like", UPDATED_TIME_VAR + "%"));
			Map<String, Long> timeMap = new HashMap<>();
			for (FxVarVo vo : list) {
				try {
					timeMap.put(vo.getName(), Long.parseLong(vo.getValue()));
				} catch (Exception e) {
					Logger.logger.error(e);
				}
			}

			Long time;
			for (ReloadType data : ReloadType.values()) {
				time = timeMap.get(UPDATED_TIME_VAR + data.name());
				if (time != null && isDataUpdated(data, time)) {
					ret.add(data);
				}
			}

		} catch (Exception e) {
			Logger.logger.error(e);
		}

		return ret;

	}

	@Override
	public void onCreated() throws Exception {

		try {
			backupVar();
		} catch (Exception e) {
			Logger.logger.error(e);
		}
	}

	@Override
	public void reload(Enum<?> type) throws Exception {
		// 아무것도 하지 않는다.
	}

	/**
	 * 데이터가 업데이트 된 시간을 설정한다.
	 * 
	 * @param type
	 * @param hstime
	 * @throws Exception
	 */
	public abstract boolean setTimeUpdated(Enum<?> type, long hstime) throws Exception;

	/**
	 * FXMS 환경변수 값을 설정한다.
	 * 
	 * @param varName
	 * @param varValue
	 * @param broadcast
	 * @throws Exception
	 */
	public abstract boolean setVarValue(String varName, Object varValue, boolean broadcast) throws Exception;
	
	/**
	 * 
	 * @param varName
	 * @param enable
	 * @return
	 * @throws Exception
	 */
	public abstract boolean enable(String varName, boolean enable) throws Exception;
	
	/**
	 * 변수의 속성을 수정한다.
	 * 
	 * @param varName
	 * @param para
	 * @throws Exception
	 */
	public abstract void updateVarInfo(String varName, Map<String, Object> para) throws Exception;
}
