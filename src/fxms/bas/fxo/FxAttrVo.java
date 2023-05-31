package fxms.bas.fxo;

import java.lang.reflect.Field;

public class FxAttrVo {

	public final FxAttr attr;
	public final Field field;

	public FxAttrVo(Field field, FxAttr attr) {
		this.field = field;
		this.attr = attr;
	}

	public String getName() {
		if (attr.name() != null && attr.name().trim().length() > 0) {
			return attr.name();
		}
		return field.getName();
	}
}
