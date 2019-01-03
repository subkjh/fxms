package subkjh.bas.utils;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Clob;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.service.FxServiceImpl;
import subkjh.bas.log.Logger;

public class ObjectUtil {

	protected static Object lockObj = new Object();

	private static Map<String, Class<?>> classMap = null;

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

	public static Class<?> getClass4Use(Class<?> classOf) {

		if (classMap == null) {
			loadClassDef();
		}

		Class<?> cls = classMap.get(classOf.getName());
		if (cls == null)
			return classOf;

		return cls;

	}

	public static List<Field> getFieldList(Class<?> classOfO) {

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

	public static boolean isNumber(Class<?> classOfT) throws Exception {
		return classOfT.equals(Integer.TYPE) //
				|| classOfT.equals(Long.TYPE) //
				|| classOfT.equals(Short.TYPE) //
				|| classOfT.equals(Double.TYPE) //
				|| classOfT.equals(Byte.TYPE);
	}

	/**
	 * deploy/conf/classes.xml에 정의된 클래스를 로딩한다.
	 */
	@SuppressWarnings("unchecked")
	public static synchronized void loadClassDef() {

		if (classMap == null) {
			classMap = new HashMap<String, Class<?>>();
		} else {
			return;
		}

		String filterName = FxCfg.getHomeDeployConf("classes.xml");

		File f = new File(filterName);

		if (f.exists() == false) {
			FxServiceImpl.logger.fail("File not found [" + f.getPath() + "]");
			return;
		}

		SAXBuilder builder = new SAXBuilder();

		Document document = null;
		try {
			document = builder.build(new FileInputStream(f));
		} catch (Exception e) {
			FxServiceImpl.logger.error(e);
		}

		StringBuffer sb = new StringBuffer();

		Element root = document.getRootElement();
		List<Element> children = root.getChildren();
		String org, use, service;
		Class<?> orgClass;
		Object useObj;

		for (Element child : children) {

			if ("class".equals(child.getName())) {
				org = child.getAttributeValue("org");
				use = child.getAttributeValue("use");
				service = child.getAttributeValue("service");

				// 특정서비스가 지정되어 있다면 그 서비스만 사용할 수 있습니다.
				if (FxCfg.getFxServiceName() != null) {
					if (service != null && FxCfg.getFxServiceName().equals(service) == false)
						continue;
				}

				try {
					orgClass = Class.forName(org);
					useObj = Class.forName(use).newInstance();
					if (orgClass.isInstance(useObj)) {
						classMap.put(org, useObj.getClass());
						sb.append(Logger.makeSubString(org, use));
					} else {
						sb.append(Logger.makeSubString(org, "error"));
					}
				} catch (Exception e) {
					Logger.logger.error(e);
					sb.append(Logger.makeSubString(org, "error"));
				}
			}
		}

		FxServiceImpl.logger.info(Logger.makeString("class-define-file", filterName, sb.toString()));
	}

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

	public static Object makeObject(String className) throws Exception {

		if (classMap == null) {
			loadClassDef();
		}

		Class<?> cls = classMap.get(className);
		if (cls == null)
			throw new Exception("Not Found Class : " + className);

		return cls.newInstance();

	}

	/**
	 * 클래스를 생성합니다.
	 * 
	 * @param classOfT
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static <T> T makeObject4Use(Class<T> classOfT) throws Exception {

		if (classMap == null) {
			loadClassDef();
		}

		if (classMap.size() == 0)
			return (T) classOfT.newInstance();

		Class<?> cls = classMap.get(classOfT.getName());
		if (cls == null)
			cls = classOfT;

		try {
			return (T) cls.newInstance();
		} catch (Exception e) {
			Logger.logger.fail("CLASS ({})", classOfT.getName());
			Logger.logger.error(e);
			throw e;
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

	private static boolean isInteger(Class<?> classOf) {
		return classOf == Integer.class || classOf == int.class;
	}

	private static boolean isLong(Class<?> classOf) {
		return classOf == Long.class || classOf == long.class;
	}

	private static boolean isDouble(Class<?> classOf) {
		return classOf == Double.class || classOf == double.class;
	}

	private static boolean isFloat(Class<?> classOf) {
		return classOf == Float.class || classOf == float.class;
	}

	private static boolean isShort(Class<?> classOf) {
		return classOf == Short.class || classOf == short.class;
	}

	private static boolean isByte(Class<?> classOf) {
		return classOf == Byte.class || classOf == byte.class;
	}

	public static void setField(Object target, String fieldName, Object value) throws NoSuchFieldException, Exception {

		Field field = target.getClass().getDeclaredField(fieldName);
		if (field == null) {
			throw new NoSuchFieldException(fieldName);
		}

		setField(target, fieldName, value);
	}

	public static void setField(Object target, Field field, Object value) throws Exception {

		if (Modifier.isFinal(field.getModifiers()))
			return;

		field.setAccessible(true);

		String str = (value == null) ? "" : value.toString();

		if (isInteger(field.getType())) {
			setInt(target, field, value);
		} else if (isLong(field.getType())) {
			setLong(target, field, value);
		} else if (isShort(field.getType())) {
			setShort(target, field, value);
		} else if (isDouble(field.getType())) {
			setDouble(target, field, value);
		} else if (isFloat(field.getType())) {
			setFloat(target, field, value);
		} else if (isByte(field.getType())) {
			setByte(target, field, value);
		} else if (field.getType() == Number.class) {
			try {
				field.set(target, Long.parseLong(str));
			} catch (Exception ex) {
				field.set(target, (long) Double.parseDouble(str));
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

	@SuppressWarnings("unchecked")
	public static Map<String, Object> toMap(Object obj) {

		try {

			Map<String, Object> resultMap = new HashMap<String, Object>();

			Class<?> classOf = obj.getClass();

			if (Map.class.isInstance(obj)) {
				return (Map<String, Object>) obj;
			} else if (classOf.getSuperclass() == Object.class) {
				setMap(obj, classOf, resultMap);
			} else {
				List<Class<?>> listClass = new ArrayList<Class<?>>();

				while (classOf != Object.class) {
					listClass.add(classOf);
					classOf = classOf.getSuperclass();
				}

				for (int i = listClass.size() - 1; i >= 0; i--) {
					setMap(obj, listClass.get(i), resultMap);
				}
			}

			return resultMap;

		} catch (IllegalArgumentException e) {
			e.printStackTrace();

		}
		return null;
	}

	public static void toObject(Map<String, Object> para, Object o) {

		List<Field> fieldList = getFieldList(o.getClass());

		for (String fieldName : para.keySet()) {
			for (Field field : fieldList) {
				if (field.getName().equals(fieldName)) {
					try {
						field.setAccessible(true);
						setField(o, field, para.get(fieldName));
						// field.set(o, para.get(fieldName));
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				}
			}
		}
	}

	private static Object get(Object obj, Class<?> classOf, String fieldName) throws NoSuchFieldException {

		try {
			Field field = classOf.getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(obj);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			throw e;
		} catch (SecurityException e) {
			e.printStackTrace();
		}

		return null;

	}

	private static boolean isTrue(String str) {
		return str != null && (str.equalsIgnoreCase("y") || str.equalsIgnoreCase("true"));
	}

	private static void setMap(Object obj, Class<?> classOf, Map<String, Object> map) {

		try {

			Field[] fields;

			fields = classOf.getDeclaredFields();

			for (int i = 0; i <= fields.length - 1; i++) {

				if ((fields[i].getModifiers() & Modifier.FINAL) != Modifier.FINAL //
						&& (fields[i].getModifiers() & Modifier.STATIC) != Modifier.STATIC) {
					fields[i].setAccessible(true);
					map.put(fields[i].getName(), fields[i].get(obj));
				}
			}

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

	}

}
