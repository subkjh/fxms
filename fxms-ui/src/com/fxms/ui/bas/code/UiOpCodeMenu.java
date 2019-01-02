package com.fxms.ui.bas.code;

import java.util.Map;

public class UiOpCodeMenu {

	private int opNo;

	private int menuOpNo;

	private String menuEnAttr;

	private int menuIndex;

	private String menuDescr;

	public boolean isEnable(Map<String, Object> data) {
		if (menuEnAttr == null || menuEnAttr.length() == 0 || data == null) {
			return true;
		}

		String key;
		String comValue;
		Object value;
		String ss[] = menuEnAttr.split(",|;");
		for (String s : ss) {
			String nv[] = s.split("=");
			if (nv.length == 2) {
				key = nv[0].trim();
				comValue = nv[1].trim();
				value = data.get(key);
				if (match(comValue, value) == false) {
					return false;
				}
			}
		}

		return true;
	}

	private boolean match(String s1, Object s2) {
		if (s1 == null || s2 == null) {
			return false;
		}

		if (s1.equals(s2.toString())) {
			return true;
		}
		
		if ( s2 instanceof Number) {
			try {
				return Double.valueOf(s1).doubleValue() == ((Number)s2).doubleValue();
			} catch (Exception e) {
			}
		}
		
		return false;
	}

	public String getMenuEnAttr() {
		return menuEnAttr;
	}

	public void setMenuEnAttr(String menuEnAttr) {
		this.menuEnAttr = menuEnAttr;
	}

	public String getMenuDescr() {
		return menuDescr;
	}

	public int getMenuIndex() {
		return menuIndex;
	}

	public int getMenuOpNo() {
		return menuOpNo;
	}

	public int getOpNo() {
		return opNo;
	}

	public void setMenuDescr(String menuDescr) {
		this.menuDescr = menuDescr;
	}

	public void setMenuIndex(int menuIndex) {
		this.menuIndex = menuIndex;
	}

	public void setMenuOpNo(int menuOpNo) {
		this.menuOpNo = menuOpNo;
	}

	public void setOpNo(int opNo) {
		this.opNo = opNo;
	}

}
