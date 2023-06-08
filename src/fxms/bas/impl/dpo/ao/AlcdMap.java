package fxms.bas.impl.dpo.ao;

import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.AlarmApi;
import fxms.bas.api.FxApi;
import fxms.bas.co.CoCode.CMPR_CD;
import fxms.bas.exp.AlcdNotFoundException;
import fxms.bas.exp.NotFoundException;
import fxms.bas.vo.AlarmCode;
import subkjh.bas.co.log.Logger;

/**
 * 이전값 보관용으로 이전 값과 현재 값의 변화 추이를 확인하기 위함입니다.<br>
 * 시스템 시작하고 이후 데이터만 보관한다.<br>
 * 저장소에 이전 데이터를 읽어오지 않는다.<br>
 */
public class AlcdMap {

	private static AlcdMap map = null;
	private static final Object lockObj = new Object();

	/**
	 * 데이터가 없는 인스턴스만 제공<br>
	 * load()함수를 호출해야 데이터가 적재됨
	 * 
	 * @return
	 */
	public static AlcdMap getInstance() {

		synchronized (lockObj) {
			if (map == null) {
				map = new AlcdMap();
			}
		}

		return map;
	}

	/**
	 * 데이터가 적재된 인스턴스 제공
	 * 
	 * @return
	 */
	public static AlcdMap getMap() {

		synchronized (lockObj) {
			if (map == null) {
				map = new AlcdMap();
				try {
					map.load();
				} catch (Exception e) {
					Logger.logger.error(e);
				}
			}
		}

		return map;
	}

	private final Map<Integer, AlarmCode> codeMap;
	/** 경보코드. key=경보명 */
	private final Map<String, AlarmCode> nameMap;
	private final Map<String, List<Integer>> psAlcdMap;

	private AlcdMap() {
		this.codeMap = new HashMap<>();
		this.nameMap = new HashMap<>();
		this.psAlcdMap = new HashMap<>();
	}

	/**
	 * 
	 * @param alcdNo
	 * @return
	 * @throws NotFoundException
	 */
	public AlarmCode getAlarmCode(int alcdNo) throws AlcdNotFoundException {
		synchronized (this.codeMap) {
			AlarmCode ret = this.codeMap.get(alcdNo);
			if (ret == null) {
				throw new AlcdNotFoundException(alcdNo);
			}
			return ret;
		}
	}

	/**
	 * 
	 * @param name
	 * @return
	 * @throws AlcdNotFoundException
	 */
	public AlarmCode getAlarmCode(String name) throws AlcdNotFoundException {

		synchronized (this.codeMap) {
			AlarmCode code = this.nameMap.get(name);
			if (code == null) {
				code = this.nameMap.get(name.toLowerCase());
			}
			if (code == null) {
				throw new AlcdNotFoundException(name);
			}
			return code;
		}
	}

	/**
	 * 
	 * @param moClass
	 * @return
	 */
	public List<AlarmCode> getAlarmCodeList(String moClass) {
		List<AlarmCode> list = new ArrayList<AlarmCode>();
		synchronized (this.codeMap) {
			for (AlarmCode ac : codeMap.values()) {
				if (moClass != null && ("all".equalsIgnoreCase(moClass) || moClass.equals(ac.getMoClass()))) {
					list.add(ac);
				}
			}
		}

		return list;
	}

	/**
	 * IQR 처리하는 성능항목을 조회한다.
	 * 
	 * @return IQR적용 성능 항목
	 */
	public List<String> getPsIdForIQR() {
	
		synchronized (this.codeMap) {
			List<String> list = new ArrayList<>();
			for (AlarmCode ac : codeMap.values()) {
				
				if ( "none".equalsIgnoreCase(ac.getPsId())) continue;
				
				if (CMPR_CD.IQR.name().equalsIgnoreCase(ac.getCompareCode())) {
					if (list.contains(ac.getPsId()) == false) {
						list.add(ac.getPsId());
					}
				}
			}
			return list;
		}
	}

	/**
	 * 성능항목에 해당되는 알람코드를 가져온다.
	 * 
	 * @param psId
	 * @return
	 */
	public List<Integer> getAlcdNos(String psId) {
		synchronized (this.psAlcdMap) {
			return this.psAlcdMap.get(psId);
		}
	}

	public void load() throws NotBoundException, Exception {

		List<AlarmCode> list = AlarmApi.getApi().getAlCds();

		Map<Integer, AlarmCode> noMap = new HashMap<Integer, AlarmCode>();
		Map<String, AlarmCode> nmMap = new HashMap<String, AlarmCode>();
		Map<String, List<Integer>> psMap = new HashMap<String, List<Integer>>();
		List<Integer> entry;

		for (AlarmCode bean : list) {
			noMap.put(bean.getAlcdNo(), bean);
			nmMap.put(bean.getAlcdName().toLowerCase(), bean);
			nmMap.put(bean.getAlcdName(), bean);

			if (bean.hasPs()) {
				entry = psMap.get(bean.getPsId());
				if (entry == null) {
					entry = new ArrayList<Integer>();
					psMap.put(bean.getPsId(), entry);
				}
				entry.add(bean.getAlcdNo());
			}
		}

		synchronized (this.codeMap) {
			this.codeMap.clear();
			this.codeMap.putAll(noMap);
		}
		synchronized (this.codeMap) {
			this.nameMap.clear();
			this.nameMap.putAll(nmMap);
		}
		synchronized (this.psAlcdMap) {
			this.psAlcdMap.clear();
			this.psAlcdMap.putAll(psMap);
		}

		FxApi.save2file("alarm.code.list", list);

		Logger.logger.info(Logger.makeString("AlarmCode loaded", codeMap.size()));
	}

	public int size() {
		synchronized (this.codeMap) {
			return this.codeMap.size();
		}
	}
}
