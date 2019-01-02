package fxms.bas.pso;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.fxo.thread.QueueFxThread;
import subkjh.bas.log.LOG_LEVEL;
import subkjh.bas.log.Logger;

public abstract class VoSub extends QueueFxThread<VoList> {

	class VAL {
		long mstime;
		PsVo val;
	}

	protected long count = 0;
	// private final int MAX_QUEUE = 10000;
	/** 이전값 보관용으로 이전 값과 현재 값의 변화 추이를 확인하기 위함입니다. */
	private Map<String, Number> valPrevMap;

	public VoSub(String name) {
		super(name, 10);
		valPrevMap = Collections.synchronizedMap(new HashMap<String, Number>());
	}

	@Override
	public String getState(LOG_LEVEL level) {

		StringBuffer sb = new StringBuffer();
		sb.append("proc-count=");
		sb.append(count);
		sb.append(",prev-value-size=");
		sb.append(valPrevMap.size());

		return sb.toString();

	}

	/**
	 * 이전 값을 채워 줍니다.
	 * 
	 * @param valueList
	 */
	public void initValue(List<PsVo> valueList) {
		if (valueList != null) {
			valPrevMap.clear();
			for (PsVo value : valueList) {
				setPrevValue(value);
			}
			Logger.logger.debug("PREV-VALUE-COUNT(" + valPrevMap.size() + ")");
		}
	}

	private String getKey(long moNo, String instance, String psCode) {
		if (instance == null || instance.trim().length() == 0)
			return moNo + "|" + psCode;
		return moNo + "|" + instance + "|" + psCode;
	}

	protected Number getPrevValue(long moNo, String instance, String psCode) {
		String key = getKey(moNo, instance, psCode);
		return valPrevMap.get(key);
	}

	protected void setPrevValue(PsVo value) {
		String key = getKey(value.getMoNo(), value.getMoInstance(), value.getPsCode());
		valPrevMap.put(key, value.getValue());
	}

}
