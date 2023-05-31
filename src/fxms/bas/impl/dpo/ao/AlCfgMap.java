package fxms.bas.impl.dpo.ao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.AlarmApi;
import fxms.bas.api.FxApi;
import fxms.bas.exp.NotFoundException;
import fxms.bas.vo.AlarmCfg;
import fxms.bas.vo.AlarmCfgMem;
import fxms.bas.vo.AlarmCode;
import subkjh.bas.co.log.Logger;

/**
 * 이전값 보관용으로 이전 값과 현재 값의 변화 추이를 확인하기 위함입니다.<br>
 * 시스템 시작하고 이후 데이터만 보관한다.<br>
 * 저장소에 이전 데이터를 읽어오지 않는다.<br>
 */
public class AlCfgMap {

	/** 정의되지 않은 경우 : 0 */
	public static final int NONE_ALARM_CFG_NO = 0;

	/** 알람을 발생하지 않을 경우 설정한다 : 1 */
	public static final int NOALARM_ALARM_CFG_NO = 1;

	private static final Object lockObj = new Object();

	public static void main(String[] args) {
		AlCfgMap.getInstance();
	}

	private static AlCfgMap map = null;

	/**
	 * 데이터가 없는 인스턴스만 제공<br>
	 * load()함수를 호출해야 데이터가 적재됨
	 * 
	 * @return
	 */
	public static AlCfgMap getInstance() {

		synchronized (lockObj) {
			if (map == null) {
				map = new AlCfgMap();
			}
		}

		return map;
	}

	/**
	 * 데이터가 적재된 인스턴스 제공
	 * 
	 * @return
	 */
	public static AlCfgMap getMap() {

		synchronized (lockObj) {
			if (map == null) {
				map = new AlCfgMap();
				try {
					map.load();
				} catch (Exception e) {
					Logger.logger.error(e);
				}
			}
		}

		return map;
	}

	/** 경보조건템플릿맵. 키:경보조건템플릿번호 */
	private final Map<Integer, AlarmCfg> alarmCfgMap;
	private final Map<String, AlarmCfg> moClassAlarmCfgMap;

	private AlCfgMap() {
		this.alarmCfgMap = new HashMap<>();
		this.moClassAlarmCfgMap = new HashMap<>();
	}

	public void load() throws Exception {

		List<AlarmCfg> cfgList = AlarmApi.getApi().getAlCfgs(null);
		Map<Integer, AlarmCfg> map = new HashMap<Integer, AlarmCfg>();
		AlarmCfgMem mem;
		AlarmCode alarmCode;
		Map<String, AlarmCfg> moClassMap = new HashMap<String, AlarmCfg>();

		for (AlarmCfg cfg : cfgList) {
			if (cfg.getMemList() != null) {
				for (int i = cfg.getMemList().size() - 1; i >= 0; i--) {
					mem = cfg.getMemList().get(i);
					try {
						alarmCode = AlcdMap.getInstance().getAlarmCode(mem.getAlcdNo());
						mem.setAlarmCode(alarmCode);
					} catch (NotFoundException e) {
						cfg.getMemList().remove(i);
						Logger.logger.fail("alarm-code={} not found. removed alarm-cfg-no={}", mem.getAlcdNo(),
								cfg.getAlarmCfgNo());
					}
				}
				map.put(cfg.getAlarmCfgNo(), cfg);
			}

			if (cfg.isBasicCfgYn() && cfg.getMoClass() != null) {
				if (cfg.getMoType() != null && cfg.getMoType().trim().length() > 0) {
					moClassMap.put(cfg.getMoClass() + "\t" + cfg.getMoType(), cfg);
				} else {
					moClassMap.put(cfg.getMoClass(), cfg);
				}
			}
		}

		FxApi.save2file("alarm.cfg.list", cfgList);

		synchronized (this.alarmCfgMap) {
			alarmCfgMap.clear();
			alarmCfgMap.putAll(map);
		}

		synchronized (this.moClassAlarmCfgMap) {
			moClassAlarmCfgMap.clear();
			moClassAlarmCfgMap.putAll(moClassMap);
		}

		Logger.logger.info(Logger.makeString("AlarmCfg loaded", cfgList.size()));
	}

	public AlarmCfg getAlarmCfg(int alarmCfgNo) {
		synchronized (this.alarmCfgMap) {
			return this.alarmCfgMap.get(alarmCfgNo);
		}
	}

	public int size() {
		synchronized (this.alarmCfgMap) {
			return this.alarmCfgMap.size();
		}
	}

	public int getAlarmCfgNo(String moClass, String moType) {

		synchronized (moClassAlarmCfgMap) {
			AlarmCfg cfg = moClassAlarmCfgMap.get(moClass + "\t" + moType);
			if (cfg == null) {
				cfg = moClassAlarmCfgMap.get(moClass);
			}
			return cfg == null ? NONE_ALARM_CFG_NO : cfg.getAlarmCfgNo();
		}

	}
}
