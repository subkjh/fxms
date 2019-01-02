package com.fxms.ui.bas.renderer;

import javafx.scene.Node;

public interface FxRenderer {

	public static int getInt(Object value, int defVal) {
		if (value == null) {
			return defVal;
		}

		if (value instanceof Number) {
			return ((Number) value).intValue();
		}

		try {
			return Double.valueOf(value.toString()).intValue();
		} catch (Exception e) {
			return defVal;
		}
	}

	public static long getLong(Object value, long defVal) {
		if (value == null) {
			return defVal;
		}

		if (value instanceof Number) {
			return ((Number) value).longValue();
		}

		try {
			return Double.valueOf(value.toString()).longValue();
		} catch (Exception e) {
			return defVal;
		}
	}

	public static Node makeRenderer(String type) {
		
		String className = FxRenderer.class.getPackage().getName() + "." + type + "Renderer";

		try {
			Object obj = Class.forName(className).newInstance();
			if (obj instanceof FxRenderer) {
				return (Node) obj;
			}
		} catch (Exception e) {
		}

		return new TextRenderer();

	}

	/**
	 * 
	 * @param value
	 * @param type
	 *            : FX_CD_OP_ATTR.ATTR_DEFAULT_VALUE
	 */
	public void setValue(Object value, String type);
}
