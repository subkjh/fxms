package fxms.bas.fxo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.exp.AttrNotFoundException;
import fxms.bas.impl.handler.dto.GetValuesPara;
import fxms.bas.impl.handler.dto.SelectPsValueMinMaxPara;
import subkjh.bas.co.utils.ObjectUtil;

/**
 * FxMS 공동 함수
 * 
 * @author subkjh
 *
 */
public class FxAttrApi {

	public static void main(String[] args) {
		try {
			System.out.println(FxAttrApi.toHelp(GetValuesPara.class));
			System.out.println(FxAttrApi.toHelp(SelectPsValueMinMaxPara.class));

			Map<String, Object> datas = FxApi.makePara("moNo", 11234, "psKindName", "aaa", "psId", "MoStatus",
					"startDate", 20220202111111L);

			GetValuesPara dto = new GetValuesPara();
			System.out.println(FxmsUtil.toJson(FxAttrApi.toObject(datas, dto)));
			System.out.println(FxmsUtil.toJson(dto));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * FxAttr 선언 목록을 조회한다.
	 * 
	 * @param classOf
	 * @return
	 */
	public static List<FxAttr> getAnnotations(Class<?> classOf) {

		List<FxAttr> list = new ArrayList<>();

		// 자동 필드 설정
		for (Field f : classOf.getDeclaredFields()) {
			FxAttr value = f.getAnnotation(FxAttr.class);
			if (value != null) {
				list.add(value);
			}
		}

		return list;

	}

	public static <T> T toObject(Map<String, Object> datas, Class<T> classOfT) throws AttrNotFoundException, Exception {
		T obj = classOfT.newInstance();
		toObject(datas, obj);
		return obj;
	}

	/**
	 * 맵 데이터를 객체에 넣는다.
	 * 
	 * @param datas 설정한 원천 데이터 맵
	 * @param obj   설정할 대상 객체
	 * @return 설정된 데이터 맵
	 * @throws AttrNotFoundException
	 * @throws Exception
	 */
	public static Map<String, Object> toObject(Map<String, Object> datas, Object obj)
			throws AttrNotFoundException, Exception {

		Map<String, Object> retMap = new HashMap<>();
		StringBuffer notAttr = new StringBuffer();
		String name;
		Object value;

		for (FxAttrVo vo : getFxAttrField(obj.getClass())) {

			name = vo.getName();
			value = datas == null ? null : datas.get(name);
			if (vo.attr.required() && value == null) {
				if (notAttr.length() > 0)
					notAttr.append(",");
				notAttr.append(name);
				continue;
			}

			// 없으면 설정하지 않는다.
			if (value != null) {
				try {

					// 필드 값 설정
					ObjectUtil.setField(obj, vo.field, value);

					// 설정값 맵에 넣어 나중에 전달
					retMap.put(vo.field.getName(), value);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

		if (notAttr.length() > 0) {
			throw new AttrNotFoundException(notAttr.toString());
		}

		return retMap;

	}

	/**
	 * 입력데이터 정보를 제공한다.
	 * 
	 * @param classOf
	 * @return
	 */
	public static String toHelp(Class<?> classOf) {

		String name;
		StringBuffer sb = new StringBuffer();
		List<Class<?>> list = getClasses(classOf);

		for (Class<?> c : list) {

			// 자동 필드 설정
			for (Field f : c.getDeclaredFields()) {

				FxAttr attr = f.getAnnotation(FxAttr.class);

				if (attr != null) {
					name = attr.name().length() == 0 ? f.getName() : attr.name();

					sb.append(name).append(" (").append(f.getType().getSimpleName()).append(")");
					sb.append(attr.required() ? " (mandantory)" : "");
					sb.append(" : ").append(attr.description());

					if (attr.example().length() > 0)
						sb.append(",  ex) ").append(attr.example());

					sb.append(" <br>\n");

				}
			}
		}

		return sb.toString();

	}

	/**
	 * 
	 * @param classOfT
	 * @return
	 */
	private static List<Class<?>> getClasses(Class<?> classOfT) {

		List<Class<?>> ret = new ArrayList<>();

		Class<?> classOf = classOfT;
		while (true) {
			ret.add(classOf);
			classOf = classOf.getSuperclass();
			if (classOf == null)
				break;
		}

		return ret;
	}

	/**
	 * FxAttr Annotation을 갖는 필드 목록을 조회한다.
	 * 
	 * @param classOfT
	 * @return
	 */
	public static List<FxAttrVo> getFxAttrField(Class<?> classOfT) {
		List<Class<?>> classList = getClasses(classOfT);
		List<FxAttrVo> fields = new ArrayList<FxAttrVo>();
		for (Class<?> cls : classList) {
			for (Field f : cls.getDeclaredFields()) {
				FxAttr attr = f.getAnnotation(FxAttr.class);
				if (attr != null) {
					fields.add(new FxAttrVo(f, attr));
				}
			}
		}
		return fields;
	}

	public static void printParameters(Class<?> classOfT) {

		List<FxAttrVo> attrs = getFxAttrField(classOfT);
		for (FxAttrVo attr : attrs) {
			StringBuffer sb = new StringBuffer();
			sb.append(attr.getName()).append("\t");
			sb.append(attr.field.getType().getSimpleName()).append("\t");
			if (attr.attr.required()) {
				sb.append("필수\t");
			} else {
				sb.append("선택\t");
			}
			sb.append(attr.attr.description());
			System.out.println(sb.toString());
		}
		System.out.println();
	}
}
