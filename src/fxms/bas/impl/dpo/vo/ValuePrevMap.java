package fxms.bas.impl.dpo.vo;

import java.util.HashMap;
import java.util.Map;

import fxms.bas.vo.PsVo;

/**
 * 이전값 보관용으로 이전 값과 현재 값의 변화 추이를 확인하기 위함입니다.<br>
 * 시스템 시작하고 이후 데이터만 보관한다.<br>
 * 저장소에 이전 데이터를 읽어오지 않는다.<br>
 */
public class ValuePrevMap {

	private static Map<String, ValuePrevMap> maps = new HashMap<>();

	public static ValuePrevMap getInstance(String name) {

		ValuePrevMap map = maps.get(name);
		if (map == null) {
			map = new ValuePrevMap();
			maps.put(name, map);
		}

		return map;
	}

	private final Map<String, Number> valPrevMap;

	private ValuePrevMap() {
		this.valPrevMap = new HashMap<>();
	}

	private String getKey(long moNo, String instance, String psId) {
		if (instance == null || instance.trim().length() == 0)
			return moNo + "|" + psId;
		return moNo + "|" + instance + "|" + psId;
	}

	public Number getValue(PsVo value) {
		String key = getKey(value.getMo().getMoNo(), value.getMoInstance(), value.getPsItem().getPsId());
		return valPrevMap.get(key);
	}

	public void setValue(PsVo value) {
		String key = getKey(value.getMo().getMoNo(), value.getMoInstance(), value.getPsItem().getPsId());
		valPrevMap.put(key, value.getValue());
	}
}
