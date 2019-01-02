package com.fxms.ui.bas;

import java.util.HashMap;

public class VarMap extends HashMap<String, Object> {

	private static VarMap map;

	public static VarMap getVarMap() {
		if (map != null) {
			return map;
		}

		map = new VarMap();
		map.init();
		return map;
	}

	public String getAlarmName(int alarmLevel)
	{
		Object value = map.get("alarm-level-name." + alarmLevel);
		return value == null ? String.valueOf(alarmLevel) : value.toString();
	}
	
	
	private VarMap() {

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 6322220409373942046L;

	public void init() {
		put("alarm-level-name.1", "critical");
		put("alarm-level-name.2", "major");
		put("alarm-level-name.3", "minor");
		put("alarm-level-name.4", "warning");
		put("alarm-level-name.5", "event");

	}

}
