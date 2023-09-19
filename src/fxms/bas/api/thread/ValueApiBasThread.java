package fxms.bas.api.thread;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.event.FxEvent;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.fxo.thread.QueueFxThread;
import fxms.bas.vo.PsVo;
import fxms.bas.vo.PsVoList;
import fxms.bas.vo.PsVoRaw;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;

public abstract class ValueApiBasThread extends QueueFxThread<PsVoList> {

	class VAL {
		long mstime;
		PsVoRaw val;
	}

	protected long count = 0;
	// private final int MAX_QUEUE = 10000;

	/** 이전값 보관용으로 이전 값과 현재 값의 변화 추이를 확인하기 위함입니다. */
	private Map<String, Number> valPrevMap;

	public ValueApiBasThread(String name) {
		super(10);
		setName(name);
		valPrevMap = Collections.synchronizedMap(new HashMap<String, Number>());
	}

	private String getKey(long moNo, String psId) {
		return moNo + "|" + psId;
	}

	protected void broadcast(FxEvent event) {
		try {
			FxServiceImpl.fxService.sendEvent(event, true, true);
		} catch (Exception e) {
			Logger.logger.error(e);
		}
	}

	protected Number getPrevValue(long moNo, String psId) {
		String key = getKey(moNo, psId);
		return valPrevMap.get(key);
	}

	protected void setPrevValue(PsVo value) {
		String key = getKey(value.getMo().getMoNo(), value.getPsItem().getPsId());
		valPrevMap.put(key, value.getValue());
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
}
