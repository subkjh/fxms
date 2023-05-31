package fxms.bas.fxo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import fxms.bas.exp.AttrNotFoundException;
import fxms.bas.impl.vo.AlarmCfgVo;
import subkjh.bas.co.utils.ObjectUtil;

/**
 * FxMS 공동 함수
 * 
 * @author subkjh
 *
 */
public class FxmsUtil {

//	private static void initFxParaValue(Class<?> classOf, Object obj, Map<String, Object> map, StringBuffer err)
//			throws Exception {
//
//		StringBuffer msg = new StringBuffer();
//
//		// 자동 필드 설정
//		for (Field f : classOf.getDeclaredFields()) {
//
//			FxParaValue value = f.getAnnotation(FxParaValue.class);
//			if (value != null) {
//				if (value.required() && map.get(value.value()) == null) {
//					if (err.length() > 0)
//						err.append(",");
//					err.append(value.value());
//					continue;
//				}
//
//				// 파라메터 값을 필드에 설정한다.
//				try {
//					Object v = map.get(value.value());
//					ObjectUtil.setField(obj, f, v);
//					if (msg.length() > 0)
//						msg.append(",");
//					msg.append(f.getName()).append("=").append(f.get(obj));
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//
//			}
//		}
//
//		if (msg.length() > 0) {
//			Logger.logger.trace("{} : {}", obj.getClass().getName(), msg.toString());
//		}
//
//	}
//
//	/**
//	 * FxParaValue로 선언된 필드를 맵의 값으로 설정한다.
//	 * 
//	 * @param obj
//	 * @param map
//	 * @throws Exception
//	 */
//	public static void initFxParaValue(Object obj, Map<String, Object> map) throws Exception {
//
//		StringBuffer err = new StringBuffer();
//		Class<?> classOf = obj.getClass();
//		while (true) {
//			initFxParaValue(classOf, obj, map, err);
//			classOf = classOf.getSuperclass();
//			if (classOf == null)
//				break;
//		}
//
//		if (err.length() > 0) {
//			throw new Exception("not defined : " + err);
//		}
//	}

	public static void main(String[] args) {
		AlarmCfgVo vo = new AlarmCfgVo();
		String str = FxmsUtil.toJson(vo);
		System.out.println(str);
		System.out.println(FxmsUtil.toObject(str, AlarmCfgVo.class));
	}

	/**
	 * Map에 파라메터를 추가한다. 이미 존재하면 List로 처리한다.
	 * 
	 * @param para
	 * @param name
	 * @param value
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void addPara(Map<String, Object> para, String name, String value) {

		Object data = para.get(name);
		if (data != null) {
			if (data instanceof String) {
				List<String> list = new ArrayList<String>();
				para.put(name, list);
				list.add(data.toString());
				list.add(value);
			} else if (data instanceof List) {
				((List) data).add(value);
			}
		} else {
			para.put(name, value);
		}
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> toMapFromJson(String json) {
		if (json == null || json.length() == 0) {
			return new HashMap<String, Object>();
		}
		return getGson().fromJson(json, HashMap.class);
	}

	public static <T> T toObject(String json, Class<T> classOfT) {
		if (json == null || json.length() == 0) {
			return null;
		}
		return getGson().fromJson(json, classOfT);
	}

	public static Gson getGson() {
//		Gson gson = new GsonBuilder().registerTypeAdapter(Integer.class, new JsonSerializer<Integer>() {
//			@Override
//			public JsonElement serialize(Integer src, Type typeOfSrc, JsonSerializationContext context) {
////				return new JsonPrimitive(src.toString());
//				return new JsonPrimitive(src.intValue());
//			}
//		}).create();
//		
//		return gson;

		return new Gson();
	}

	public static String toJson(Object obj) {
		return getGson().toJson(obj);
	}

	public static Map<String, Object> toMap(Object obj) {
		return toMapFromJson(toJson(obj));
	}

	public static long getLong(Map<String, Object> datas, String name, long defVal) {
		if (datas != null) {
			Object val = datas.get(name);
			if (val instanceof Number) {
				return ((Number) val).longValue();
			} else {
				try {
					return Long.parseLong(val.toString());
				} catch (Exception e) {
				}
			}
		}
		return defVal;
	}

	public static long getLong(Map<String, Object> datas, String name) throws AttrNotFoundException, Exception {
		if (datas != null) {
			Object val = datas.get(name);
			if (val instanceof Number) {
				return ((Number) val).longValue();
			} else {
				try {
					return Long.parseLong(val.toString());
				} catch (Exception e) {
				}
			}
		}

		throw new AttrNotFoundException(name);
	}

	public static int getInt(Map<String, Object> datas, String name, int defVal) {
		if (datas != null) {
			Object val = datas.get(name);
			if (val instanceof Number) {
				return ((Number) val).intValue();
			} else {
				try {
					return Integer.parseInt(val.toString());
				} catch (Exception e) {
				}
			}
		}
		return defVal;
	}

	public static String getString(Map<String, Object> datas, String name, String defVal) {
		if (datas != null) {
			Object val = datas.get(name);
			if (val != null)
				return val.toString();
		}
		return defVal;
	}

	public static void print(Object obj) {
		Map<String, Object> datas = ObjectUtil.toMap(obj);
		for (String key : datas.keySet()) {
			System.out.println(key + " = " + datas.get(key));
		}
	}

	public static void print(List<?> list) {
		for (Object o : list) {
			System.out.println(FxmsUtil.toJson(o));
		}
	}

}
