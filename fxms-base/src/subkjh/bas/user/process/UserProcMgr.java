package subkjh.bas.user.process;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.fxo.service.app.proc.mo.SyncMoProc;
import subkjh.bas.log.Logger;
import subkjh.bas.user.UserProc;
import subkjh.bas.user.dao.UserDao;
import subkjh.bas.user.dbo.OpDbo;
import subkjh.bas.user.define.UsrProcess0;
import subkjh.bas.utils.ObjectUtil;

/**
 * proc.xml 관리자
 * 
 * @author subkjh
 * 
 */
public class UserProcMgr {

	private static final Class<?> cArr[][] = new Class<?>[][] { //
			{ int.class, Integer.class } //
			, { long.class, Long.class } //
			, { double.class, Double.class } //
			, { float.class, Float.class } //
			, { short.class, Short.class } //
			, { byte.class, Byte.class } //
			, { boolean.class, Boolean.class } //
	};

	private static UserProcMgr parser;

	public static Constructor<?> getConstructor(Class<?> cls, Object parameters[]) {

		Class<?> paraTypes[] = new Class<?>[parameters.length];

		for (int i = 0; i < paraTypes.length; i++) {
			paraTypes[i] = (parameters[i] == null ? null : parameters[i].getClass());
		}

		CC: for (Constructor<?> c : cls.getConstructors()) {
			if (c.getParameterTypes().length == paraTypes.length) {
				for (int i = 0; i < paraTypes.length; i++) {
					if (matchType(c.getParameterTypes()[i], paraTypes[i]) == false) {
						continue CC;
					}
				}
				return c;
			}
		}
		return null;
	}

	public static void main(String[] args) {
		System.out.println(getConstructor(SyncMoProc.class));
	}

	public static String getConstructor(Class<?> cls) {

		StringBuffer sb = new StringBuffer();
		StringBuffer para = new StringBuffer();

		sb.append(cls.getSimpleName());

		for (Constructor<?> c : cls.getConstructors()) {
			sb.append("(");
			para = new StringBuffer();
			for (Class<?> type : c.getParameterTypes()) {
				if (para.length() > 0) {
					para.append(", ");
				}
				para.append(type.getSimpleName());
			}
			sb.append(para);
			sb.append(") ");
		}
		return sb.toString();
	}

	public static Constructor<?> getConstructor(Class<?> cls, int paraSize) {

		for (Constructor<?> c : cls.getConstructors()) {
			if (c.getParameterTypes().length == paraSize) {
				return c;
			}
		}
		return null;
	}

	public static UserProcMgr getMgr() {
		if (parser == null) {
			parser = new UserProcMgr();
		}
		return parser;
	}

	public static Object[] makeParameterObject(Class<?> type[], Object parameters[])
			throws IllegalArgumentException, Exception {
		Object ret[] = new Object[type.length];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = ObjectUtil.convert(type[i], parameters[i]);
		}
		return ret;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static boolean matchType(Class c1, Class c2) {

		// System.out.println(c1 + " : " + c2);

		if (c2 == null)
			return false;

		if (c1.equals(c2))
			return true;
		if (c1 == c2)
			return true;
		if (c1.isAssignableFrom(c2))
			return true;

		if (c1.isPrimitive() || c2.isPrimitive()) {
			for (Class<?> ca[] : cArr) {
				if ((c1 == ca[0] && c2 == ca[1]) || (c2 == ca[0] && c1 == ca[0]))
					return true;
			}
		}

		return false;
	}

	private Map<String, OpDbo> classMap;
	private Map<Integer, OpDbo> opMap;

	public OpDbo getOp(int opNo) {
		return opMap.get(opNo);
	}

	public OpDbo getOp(Class<?> classOfT) {
		return classMap.get(classOfT.getName());
	}

	private UserProcMgr() {
		classMap = new HashMap<String, OpDbo>();
		opMap = new HashMap<Integer, OpDbo>();
		try {
			List<OpDbo> opList = new UserDao().selectOpList();
			for (OpDbo op : opList) {
				if (op.getOpJavalClass() != null && op.getOpJavalClass().length() > 0) {
					classMap.put(op.getOpJavalClass(), op);
				}
				opMap.put(op.getOpNo(), op);
			}
		} catch (Exception e) {
			Logger.logger.error(e);
		}
	}

	@SuppressWarnings("rawtypes")
	public UserProc getProcess(String className, Object parameters[]) throws Exception {

		Class<?> cls = Class.forName(className);

		UsrProcess0 info = cls.getAnnotation(UsrProcess0.class);

		if (info != null) {

			Constructor<?> c = getConstructor(cls, info.para(), info.paraType());
			if (c == null) {
				throw new Exception("Constructor not found in " + cls.getName() + Arrays.toString(info.paraType()));
			}
			Object paraObject[] = makeParameterObject(c.getParameterTypes(), parameters);
			return (UserProc) c.newInstance(paraObject);

		} else {
			CC: for (Constructor<?> c : cls.getConstructors()) {
				if (c.getParameterTypes().length == parameters.length) {
					for (int i = 0; i < c.getParameterTypes().length; i++) {
						if (matchType(c.getParameterTypes()[0], parameters[i].getClass()) == false)
							continue CC;
					}
					return (UserProc) c.newInstance(parameters);
				}
			}
		}

		throw new Exception("Can not found UsrProcess for " + className);

	}

	private Constructor<?> getConstructor(Class<?> cls, String name[], String types[]) throws Exception {

		if (types.length == 0) {

			for (Constructor<?> c : cls.getConstructors()) {
				if (c.getParameterTypes().length == name.length) {
					return c;
				}
			}

		} else {
			Class<?> paraTypes[] = makeParameterTypes(types);
			CC: for (Constructor<?> c : cls.getConstructors()) {
				if (c.getParameterTypes().length == paraTypes.length) {
					for (int i = 0; i < paraTypes.length; i++) {
						if (matchType(c.getParameterTypes()[i], paraTypes[i]) == false) {
							continue CC;
						}
					}
					return c;
				}
			}
		}

		return null;
	}

	private Class<?>[] makeParameterTypes(String type[]) throws ClassNotFoundException {
		Class<?> ret[] = new Class<?>[type.length];
		for (int i = 0; i < ret.length; i++) {
			if (type[i].equals("int"))
				ret[i] = int.class;
			else if (type[i].equals("long"))
				ret[i] = long.class;
			else if (type[i].equals("float"))
				ret[i] = float.class;
			else if (type[i].equals("double"))
				ret[i] = double.class;
			else if (type[i].equals("short"))
				ret[i] = short.class;
			else if (type[i].equals("boolean"))
				ret[i] = boolean.class;
			else if (type[i].equals("byte"))
				ret[i] = byte.class;
			else {
				ret[i] = Class.forName(type[i]);
			}
		}
		return ret;
	}
}
