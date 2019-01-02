package fxms.bas.mo.child;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

import fxms.bas.api.MoApi;
import fxms.bas.exception.NotDefineException;
import fxms.bas.fxo.FxActorImpl;
import fxms.bas.mo.Mo;
import subkjh.bas.log.Logger;
import subkjh.bas.utils.ObjectUtil;

public class AutoAddChild extends FxActorImpl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4049398511840770998L;

	public static void main(String[] args) {
		AutoAddChild c = new AutoAddChild();
		System.out.println(c.getValue(null, null));
	}


	public Mo getMo(Mo parent) throws Exception {
		String moClass = this.getParaStr("moClass");
		if (moClass == null) {
			throw new NotDefineException("moClass");
		}

		Object value;
		Mo mo = MoApi.getApi().makeNewMo(moClass);
		List<Field> fieldList = ObjectUtil.getFieldList(mo.getClass());
		for (Field field : fieldList) {
			value = getValue(field, parent);
			if (value != null) {
				try {
					ObjectUtil.setField(mo, field, value);
				} catch (Exception e) {
					Logger.logger.fail("mo={}, field={} value={}", mo, field.getName(), value);
					Logger.logger.error(e);
				}				
			}
		}
		return mo;
	}

	private Object getValue(Field field, Mo parent) {
		Object value = getPara(field.getName());
		if (value == null) {
			return null;
		}

		char chs[] = value.toString().toCharArray();
		int i = 0;

		StringBuffer var = null;
		StringBuffer ret = new StringBuffer();

		while (i < chs.length) {
			if (chs[i] == '%' && i + 1 < chs.length && chs[i + 1] == '%') {
				if (var != null) {
					ret.append(var);
				}
				ret.append(chs[i]);
				i += 2;
				continue;
			}
			if (chs[i] == '%') {
				if (var == null) {
					var = new StringBuffer();
				} else {
					ret.append(ObjectUtil.get(parent, var.toString()));
					var = null;
				}
			} else {
				if (var != null) {
					var.append(chs[i]);
				} else {
					ret.append(chs[i]);
				}
			}
			i++;
		}

		return ret.toString();
	}
}