package com.fxms.ui.bas.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.client.log.Logger;

public class FxEventDispatcher {

	private static FxEventDispatcher dispather;

	public static FxEventDispatcher getDispatcher() {
		if (dispather == null) {
			dispather = new FxEventDispatcher();
		}

		return dispather;
	}

	private final Map<FX_EVENT_TYPE, List<FxEventHandler>> typeMap;

	private FxEventDispatcher() {
		typeMap = Collections.synchronizedMap(new HashMap<FX_EVENT_TYPE, List<FxEventHandler>>());
	}

	public void dispatcher(FX_EVENT_TYPE type, Object target) {
		
		Logger.logger.debug("type={}, target={}", type, target);
		
		List<FxEventHandler> list = typeMap.get(type);
		if (list == null || list.size() == 0) {
			return;
		}

		for (FxEventHandler handler : list) {
			handler.onFxEvent(type, target);
		}
	}

	public synchronized void addHandler(FX_EVENT_TYPE type, FxEventHandler handler) {
		
		Logger.logger.debug("type={}, handler={}", type, handler);

		
		List<FxEventHandler> list = typeMap.get(type);
		if (list == null) {
			list = new ArrayList<FxEventHandler>();
			typeMap.put(type, list);
		}
		if (list.contains(handler) == false) {
			list.add(handler);
		}
	}

	public FxEventHandler removeHandler(FX_EVENT_TYPE type, FxEventHandler handler) {
		
		Logger.logger.debug("type={}, handler={}", type, handler);

		
		List<FxEventHandler> list = typeMap.get(type);

		if (list == null) {
			return null;
		}

		return list.remove(handler) ? handler : null;
	}
}
