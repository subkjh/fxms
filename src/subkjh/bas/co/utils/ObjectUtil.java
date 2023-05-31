package subkjh.bas.co.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Clob;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import subkjh.bas.co.log.Logger;

/**
 * 객체 관리에 필요한 기능 모음
 * 
 * @author subkjh
 * @since 1970.11
 */
public class ObjectUtil {

	protected static Object lockObj = new Object();

	private static Object get(Object obj, Class<?> classOf, String fieldName) throws NoSuchFieldException {

		try {
			Field field = classOf.getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(obj);
		} catch (NoSuchFieldException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	private static boolean isByte(Class<?> classOf) {
		return classOf == Byte.class || classOf == byte.class;
	}

	private static boolean isDouble(Class<?> classOf) {
		return classOf == Double.class || classOf == double.class;
	}

	private static boolean isFloat(Class<?> classOf) {
		return classOf == Float.class || classOf == float.class;
	}

	private static boolean isInteger(Class<?> classOf) {
		return classOf == Integer.class || classOf == int.class;
	}

	private static boolean isLong(Class<?> classOf) {
		return classOf == Long.class || classOf == long.class;
	}

	private static boolean isShort(Class<?> classOf) {
		return classOf == Short.class || classOf == short.class;
	}

	private static boolean isTrue(String str) {
		return str != null && ("y".equalsIgnoreCase(str) || "true".equalsIgnoreCase(str));
	}

	private static void setByte(Object target, Field field, Object value) throws Exception {
		if (field.getType() == byte.class && value != null) {
			if (value instanceof Number) {
				field.setByte(target, ((Number) value).byteValue());
			} else {
				field.setByte(target, Double.valueOf(value.toString()).byteValue());
			}
		} else if (field.getType() == Byte.class) {
			if (value == null) {
				field.set(target, null);
			} else if (value instanceof Number) {
				field.set(target, ((Number) value).byteValue());
			} else {
				field.set(target, Double.valueOf(value.toString()).byteValue());
			}
		}
	}

	private static void setDouble(Object target, Field field, Object value) throws Exception {
		if (field.getType() == double.class && value != null) {
			if (value instanceof Number) {
				field.setDouble(target, ((Number) value).doubleValue());
			} else {
				field.setDouble(target, Double.valueOf(value.toString()));
			}
		} else if (field.getType() == Double.class) {
			if (value == null) {
				field.set(target, null);
			} else if (value instanceof Number) {
				field.set(target, ((Number) value).doubleValue());
			} else {
				field.set(target, Double.valueOf(value.toString()));
			}
		}
	}

	private static void setFloat(Object target, Field field, Object value) throws Exception {
		if (field.getType() == float.class && value != null) {
			if (value instanceof Number) {
				field.setFloat(target, ((Number) value).floatValue());
			} else {
				field.setFloat(target, Double.valueOf(value.toString()).floatValue());
			}
		} else if (field.getType() == Float.class) {
			if (value == null) {
				field.set(target, null);
			} else if (value instanceof Number) {
				field.set(target, ((Number) value).floatValue());
			} else {
				field.set(target, Double.valueOf(value.toString()).floatValue());
			}
		}
	}

	private static void setInt(Object target, Field field, Object value) throws Exception {
		if (field.getType() == int.class && value != null) {
			if (value instanceof Number) {
				field.setInt(target, ((Number) value).intValue());
			} else {
				field.setInt(target, Double.valueOf(value.toString()).intValue());
			}
		} else if (field.getType() == Integer.class) {
			if (value == null) {
				field.set(target, null);
			} else if (value instanceof Number) {
				field.set(target, ((Number) value).intValue());
			} else {
				field.set(target, Double.valueOf(value.toString()).intValue());
			}
		}
	}

	private static void setLong(Object target, Field field, Object value) throws Exception {
		if (field.getType() == long.class && value != null) {
			if (value instanceof Number) {
				field.setLong(target, ((Number) value).longValue());
			} else {
				field.setLong(target, Double.valueOf(value.toString()).longValue());
			}
		} else if (field.getType() == Long.class) {
			if (value == null) {
				field.set(target, null);
			} else if (value instanceof Number) {
				field.set(target, ((Number) value).longValue());
			} else {
				field.set(target, Double.valueOf(value.toString()).longValue());
			}
		}
	}

	/**
	 * 객체에서 필드의 값을 가져와 맵에 넣는다.
	 * 
	 * @param obj     객체
	 * @param classOf 클래스
	 * @param map     넣은 맵
	 */
	private static void setMap(Object obj, Class<?> classOf, Map<String, Object> map) {

		try {

			Field[] fields;
			Object value;
			fields = classOf.getDeclaredFields();

			for (Field field : fields) {

				if ((field.getModifiers() & Modifier.STATIC) != Modifier.STATIC //
						&& (field.getModifiers() & Modifier.TRANSIENT) != Modifier.TRANSIENT) {

					field.setAccessible(true);

					value = field.get(obj);

					if (value != null) {
						map.put(field.getName(), value);
					}

				}
			}

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

	}

	private static void setShort(Object target, Field field, Object value) throws Exception {
		if (field.getType() == short.class && value != null) {
			if (value instanceof Number) {
				field.setShort(target, ((Number) value).shortValue());
			} else {
				field.setShort(target, Double.valueOf(value.toString()).shortValue());
			}
		} else if (field.getType() == Short.class) {
			if (value == null) {
				field.set(target, null);
			} else if (value instanceof Number) {
				field.set(target, ((Number) value).shortValue());
			} else {
				field.set(target, Double.valueOf(value.toString()).shortValue());
			}
		}
	}

	public static Object convert(Class<?> paraType, Object o) {

		if (o == null)
			return null;

		String str = o.toString();

		if (paraType == int.class || paraType.equals(Integer.class)) {
			if (o instanceof Number)
				return ((Number) o).intValue();
			return Integer.parseInt(str);
		} else if (paraType == long.class || paraType.equals(Long.class)) {
			if (o instanceof Number)
				return ((Number) o).longValue();
			return Long.parseLong(str);
		} else if (paraType == boolean.class || paraType.equals(Boolean.class)) {
			return str.toLowerCase().equals("y") || str.toLowerCase().equals("true");
		} else if (paraType == short.class || paraType.equals(Short.class)) {
			if (o instanceof Number)
				return ((Number) o).shortValue();
			return Short.parseShort(str);
		} else if (paraType == double.class || paraType.equals(Double.class)) {
			if (o instanceof Number)
				return ((Number) o).doubleValue();
			return Double.parseDouble(str);
		} else if (paraType == float.class || paraType.equals(Float.class)) {
			if (o instanceof Number)
				return ((Number) o).floatValue();
			return Float.parseFloat(str);
		} else if (paraType == byte.class || paraType.equals(Byte.class)) {
			if (o instanceof Number)
				return ((Number) o).byteValue();
			return Byte.parseByte(str);
		} else if (paraType.equals(Character.class)) {
			if (str.length() > 0) {
				return str.charAt(0);
			} else {
				return ' ';
			}
		} else if (paraType == String.class) {
			return str;
		} else if (paraType.isInstance(o)) {
			return o;
		}
		return o;
	}

	public static Object get(Object obj, String fieldName) {

		try {

			Class<?> classOf = obj.getClass();

			if (classOf.getSuperclass() == Object.class) {
				return get(obj, classOf, fieldName);
			} else {
				List<Class<?>> listClass = new ArrayList<Class<?>>();

				while (classOf != Object.class) {
					listClass.add(classOf);
					classOf = classOf.getSuperclass();
				}

				for (int i = listClass.size() - 1; i >= 0; i--) {
					try {
						return get(obj, listClass.get(i), fieldName);
					} catch (Exception e) {
					}
				}
			}

		} catch (IllegalArgumentException e) {
			e.printStackTrace();

		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * final과 static을 제외한 필드를 조회한다.
	 * 
	 * @param classOfO
	 * @return
	 */
	public static List<Field> getFields(Class<?> classOfO) {

		List<Field> fieldList = new ArrayList<Field>();

		try {

			Class<?> classOf = classOfO;

			while (classOf != Object.class) {
				for (Field field : classOf.getDeclaredFields()) {
					if ((field.getModifiers() & Modifier.FINAL) != Modifier.FINAL //
							&& (field.getModifiers() & Modifier.STATIC) != Modifier.STATIC) {
						fieldList.add(field);
					}
				}
				classOf = classOf.getSuperclass();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return fieldList;
	}

	public static Method getMethod(Class<?> source, String name, Class<?>... parameterTypes) {

		M: for (Method method : source.getMethods()) {
			if (method.getName().equals(name) == false)
				continue;
			if (method.getParameterTypes().length != parameterTypes.length)
				continue;

			for (int i = 0; i < method.getParameterTypes().length; i++) {
				if (parameterTypes[i].getClass().isInstance(method.getParameterTypes()[i]) == false)
					continue M;
			}
			return method;
		}
		return null;
	}

	/**
	 * 입력 객체를 이용하여 출력 맵을 채운다.
	 * 
	 * @param srcObj 입력객체
	 * @param dstMap 출력맵
	 * @return 출력맵
	 */
	public static Map<String, Object> initMap(Object srcObj, Map<String, Object> dstMap) {

		if (srcObj == null || dstMap == null) {
			return dstMap;
		}

		Map<String, Object> para = toMap(srcObj);
		if (para != null) {
			for (String fieldName : para.keySet()) {
				dstMap.put(fieldName, para.get(fieldName));
			}
		}
		return dstMap;
	}

	/**
	 * 입력된 객체를 이용하여 출력 객체를 채운다.
	 * 
	 * @param srcObj 입력 객체
	 * @param dstObj 출력 객체
	 * @return 출력 객체
	 * @throws Exception
	 */
	public static <T> T initObject(Object srcObj, T dstObj) {

		if (srcObj == null || dstObj == null) {
			return dstObj;
		}

		List<Field> fieldList = getFields(dstObj.getClass());
		Map<String, Object> para = toMap(srcObj);
		Object value;

		if (para != null) {
			for (Field field : fieldList) {
				value = para.get(field.getName());
				if (value != null) {
					try {
						field.setAccessible(true);
						setField(dstObj, field, value);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		return dstObj;
	}

	public static boolean isNumber(Class<?> classOfT) throws Exception {
		return classOfT.equals(Integer.TYPE) //
				|| classOfT.equals(Long.TYPE) //
				|| classOfT.equals(Short.TYPE) //
				|| classOfT.equals(Double.TYPE) //
				|| classOfT.equals(Byte.TYPE);
	}

//	public static boolean isEmpty(String s) {
//		return s == null || s.trim().length() == 0;
//	}

	public static void main(String[] args) {

		Integer i = 1;

		System.out.println(i.getClass() == Integer.class);
		System.out.println(int.class == Integer.TYPE);
		System.out.println(Integer.TYPE == Integer.class);
		System.out.println(i.getClass() == int.class);

		// FxServiceMo mo = new FxServiceMo();
		// mo.setMsIpaddr("10.0.0.1");
		// mo.setMoAname("TEST");
		//
		// System.out.println(ObjectUtil.toMap(mo));
		// System.out.println(ObjectUtil.get(mo, "moAName"));
		// System.out.println(ObjectUtil.get(mo, "ipAddress"));
		//
		// Map<String, Object> para = new HashMap<String, Object>();
		// para.put("moName", "subkjh-note");
		// para.put("moClass", "DEVICE");
		//
		// toObject(para, mo);
		//
		// System.out.println("moName = " + mo.getMoName());
	}

	public static void setField(Object target, Field field, Object value) throws Exception {

		if (Modifier.isFinal(field.getModifiers()))
			return;

		field.setAccessible(true);

		String str = (value == null) ? "" : value.toString();
		String numVal = value == null || value.toString().trim().length() == 0 ? null : value.toString();

		if (isInteger(field.getType())) {
			setInt(target, field, numVal);
		} else if (isLong(field.getType())) {
			setLong(target, field, numVal);
		} else if (isShort(field.getType())) {
			setShort(target, field, numVal);
		} else if (isDouble(field.getType())) {
			setDouble(target, field, numVal);
		} else if (isFloat(field.getType())) {
			setFloat(target, field, numVal);
		} else if (isByte(field.getType())) {
			setByte(target, field, numVal);
		} else if (field.getType() == Number.class) {
			if (numVal == null) {
				field.set(target, null);
			} else {
				try {
					field.set(target, Long.parseLong(numVal));
				} catch (Exception ex) {
					field.set(target, (long) Double.parseDouble(numVal));
				}
			}
		} else if (field.getType().equals(Boolean.TYPE)) {
			if (value instanceof String) {
				String v = (String) value;
				field.setBoolean(target, isTrue(v));
			} else {
				field.setBoolean(target, isTrue(str));
			}
		} else if (field.getType().equals(Character.TYPE)) {
			if (str.length() > 0) {
				field.setChar(target, str.charAt(0));
			} else {
				field.setChar(target, ' ');
			}
		} else if (field.getType() == String.class) {
			field.set(target, str);
		} else
			field.set(target, value);
	}

	public static void setMethod(Object target, Method method, Object value) throws Exception {

		String str = value == null ? null : value.toString();
		Class<?> type = method.getParameterTypes()[0];

		if (Logger.debug) {
			System.out.println(method.getName() + " " + (value == null ? "null" : value.getClass().getName()));
		}

		if (value == null) {
			method.invoke(target, new Object[] { value });
		} else if (type == String.class && value instanceof Clob) {
			method.invoke(target, value.toString());
		} else if (type == String.class) {
			method.invoke(target, str);
		} else if (type == Integer.TYPE || type == Integer.class) {
			method.invoke(target,
					(int) (value instanceof Number ? ((Number) value).intValue() : Integer.parseInt(str)));
		} else if (type == Long.TYPE || type == Long.class) {
			method.invoke(target,
					(long) (value instanceof Number ? ((Number) value).longValue() : Long.parseLong(str)));
		} else if (type == Double.TYPE || type == Double.class) {
			method.invoke(target,
					(double) (value instanceof Number ? ((Number) value).doubleValue() : Double.parseDouble(str)));
		} else if (type == Short.TYPE || type == Short.class) {
			method.invoke(target,
					(short) (value instanceof Number ? ((Number) value).shortValue() : Short.parseShort(str)));
		} else if (type == Float.TYPE || type == Float.class) {
			method.invoke(target,
					(float) (value instanceof Number ? ((Number) value).floatValue() : Float.parseFloat(str)));
		} else if (type == Byte.TYPE || type == Byte.class) {
			method.invoke(target,
					(byte) (value instanceof Number ? ((Number) value).byteValue() : Byte.parseByte(str)));
		} else if (type == Boolean.TYPE || type == Boolean.class) {
			method.invoke(target, str.toLowerCase().equals("y") || str.toLowerCase().equals("true"));
		} else {
			method.invoke(target, value);
		}

	}

//	public static void setField(Object target, String fieldName, Object value) throws NoSuchFieldException, Exception {
//
//		Field field = target.getClass().getDeclaredField(fieldName);
//		if (field == null) {
//			throw new NoSuchFieldException(fieldName);
//		}
//
//		setField(target, fieldName, value);
//	}

	/**
	 * 객체를 맵 클래스에 넣는다.
	 * 
	 * @param obj         객체
	 * @param includeNull 값이 NULL일때 포함 여부
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> toMap(Object obj) {

		Map<String, Object> ret = new HashMap<>();

		if (obj != null) {
			try {
				Class<?> classOf = obj.getClass();
				if (Map.class.isInstance(obj)) {
					return (Map<String, Object>) obj;
				} else {
					List<Class<?>> listClass = new ArrayList<>();
					while (classOf != Object.class) {
						listClass.add(0, classOf); // super의 필드부터 처리하기 위해서 super를 제일 앞으로 보낸다.
						classOf = classOf.getSuperclass();
					}
					for (Class<?> cls : listClass) {
						setMap(obj, cls, ret);
					}
				}
			} catch (Exception e) {
				Logger.logger.error(e);
			}
		}

		return ret;
	}

	/**
	 * 맵 데이터를 객체에 설정하고 설정되지 않은 나머지 맵의 값을 리턴한다.
	 * 
	 * @param inPara
	 * @param o
	 * @return 객체에 설정하고 남은 맵
	 */
	public static Map<String, Object> toObject(Map<String, Object> inPara, Object o) {

		if (inPara == null) {
			return null;
		}

		Map<String, Object> para = new HashMap<>(inPara); // 입력된 맵을 그대로 유지하기 위해 임시 맵 사용
		List<Field> fieldList = getFields(o.getClass()); // 객체의 설정한 필드 목록
		Object value;

		// 필드에 해당되는 값을 맵에서 찾아 객체에 적용한다.
		for (Field field : fieldList) {

			value = para.remove(field.getName());

			if (value != null) {
				try {
					// 객체에 설정
					field.setAccessible(true);
					setField(o, field, value);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return para;

	}

	/**
	 * 
	 * @param <T>
	 * @param source   원천객체
	 * @param classOfT 변환하여 넣을 클래스
	 * @return 변환된 객체
	 * @throws Exception
	 */
	public static <T> T toObject(Object source, Class<T> classOfT) throws Exception {
		T obj = classOfT.newInstance();
		ObjectUtil.toObject(ObjectUtil.toMap(source), obj);
		return obj;
	}

	public static <T> T toObject(Map<String, Object> data, Class<T> classOfT) throws Exception {
		T obj = classOfT.newInstance();
		ObjectUtil.toObject(data, obj);
		return obj;
	}

}
