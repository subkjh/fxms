package subkjh.dao.util;

import java.util.List;

/**
 * Subkjh Object : Database Access Object
 * 
 * @author subkjh
 * @since 2013.10.07
 */
public class DaoUtil {

	public static String CHARSET = "UTF-8";

	public static final String LINE = "############################################################################################";

	public static String getDaoName(String name) {

		char chs[];
		StringBuffer ret = new StringBuffer();

		if (name.indexOf('-') > 0 || name.indexOf('_') > 0) {
			chs = name.toUpperCase().toCharArray();
			for (char ch : chs) {
				if (ch == '-' || ch == '_') {
					ret.append("_");
				} else {
					ret.append(ch);
				}
			}
			return ret.toString();
		}

		chs = name.toCharArray();
		ret.append(chs[0]);
		for (int i = 1; i < chs.length; i++) {
			if (chs[i] >= 'A' && chs[i] <= 'Z') {
				ret.append("_");
			}
			ret.append(chs[i]);
		}
		return ret.toString().toUpperCase();
	}

	/**
	 * 메소드명을 이용하여 필드명을 제공합니다.
	 * 
	 * @param method
	 * @return 필드명
	 */
	public static String getField(String method) {
		int index = -1;
		String field = "";

		if (method.startsWith("get"))
			index = 3;
		else if (method.startsWith("set"))
			index = 3;
		else if (method.startsWith("is")) {
			field = "IS_";
			index = 2;
		} else {
			index = 0;
		}

		field += method.charAt(index) + "";
		index++;

		for (int size = method.length(); index < size; index++) {
			if (method.charAt(index) >= 'A' && method.charAt(index) <= 'Z') {
				field += "_" + method.charAt(index) + "";
			} else {
				field += method.charAt(index) + "";
			}
		}
		return field.toUpperCase();
	}

	/**
	 * 필드명을 이용하여 Getter를 제공합니다.
	 * 
	 * @param columnName
	 * @return Getter 문자열
	 */
	public static String getGetter(String columnName) {

		String method = columnName.toUpperCase().startsWith("IS_") || columnName.toUpperCase().endsWith("_YN") ? "is"
				: "get";

		int index = columnName.toUpperCase().startsWith("IS_") ? 3 : 0;
		method += (columnName.charAt(index) + "").toUpperCase();
		index++;

		for (int size = columnName.length(); index < size; index++) {
			if (columnName.charAt(index) == '_') {
				index++;
				method += (columnName.charAt(index) + "").toUpperCase();
			} else {
				method += (columnName.charAt(index) + "").toLowerCase();

			}
		}

		return method;

	}

	/**
	 * 
	 * @param javaClassName
	 * @return
	 */
	public static String getJavaClassSimpleName(String javaClassName) {
		if (javaClassName == null)
			return "";
		String ss[] = javaClassName.split("\\.");
		return ss[ss.length - 1];
	}

	public static String getJavaFieldName(String columnName) {
		return getJavaFieldName(columnName, false);
	}

	/**
	 * 컬럼명을 자바클래스에서 사용하는 필드명으로 변환함.
	 * 
	 * @param columnName       컬럼명
	 * @param isFirstUpperCase
	 * @return
	 */
	public static String getJavaFieldName(String columnName, boolean isFirstUpperCase) {

		int index = columnName.toUpperCase().startsWith("IS_") ? 3 : 0;
		String field = (columnName.charAt(index) + "").toLowerCase();

		if (isFirstUpperCase)
			field = field.toUpperCase();

		index++;

		for (int size = columnName.length(); index < size; index++) {
			if (columnName.charAt(index) == '_') {
				index++;
				field += (columnName.charAt(index) + "").toUpperCase();
			} else {
				field += (columnName.charAt(index) + "").toLowerCase();

			}
		}

		return field;
	}

	/**
	 * 
	 * @param javaClassName
	 * @return
	 */
	public static String getJavaPackageName(String javaClassName) {
		String ss[] = javaClassName.split("\\.");

		String ret = "";
		for (int i = 0; i < ss.length - 1; i++) {
			ret += "." + ss[i];
		}
		return ret.length() > 0 ? ret.substring(1) : ret;
	}

	/**
	 * 필드명의 Setter를 제공합니다.
	 * 
	 * @param field
	 * @return Setter 문자열
	 */
	public static String getSetter(String columnName) {

		String method = "set";
		int index = columnName.toUpperCase().startsWith("IS_") ? 3 : 0;
		method += (columnName.charAt(index) + "").toUpperCase();
		index++;

		for (int size = columnName.length(); index < size; index++) {
			if (columnName.charAt(index) == '_') {
				index++;
				method += (columnName.charAt(index) + "").toUpperCase();
			} else {
				method += (columnName.charAt(index) + "").toLowerCase();

			}
		}

		return method;
	}

	public static boolean isNull(String s) {
		return s == null || s.trim().length() == 0;
	}

	/**
	 * primitive 값인지 여부 판단.
	 * 
	 * @param c
	 * @return 기본 클래스인 경우 true
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isPrimitive(Class c) {
		return c == String.class || c == Long.class || c == Integer.class || c == Boolean.class || c == Float.class
				|| c == Double.class || c == Byte.class || c == Short.class || c == Object.class || c == null;
	}

	public static boolean isPrimitive(String javaType) {
		return javaType.equals("Object") || javaType.equals("String") ? false : true;
	}

	public static boolean isValidName(String name) {
		char chs[] = name.toLowerCase().toCharArray();
		for (char ch : chs) {
			if (ch == '_' || (ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9')) {

			} else {
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args) {
		System.out.println(DaoUtil.getDaoName("AaaName"));
		System.out.println(DaoUtil.getDaoName("aaaName"));
		System.out.println(DaoUtil.getDaoName("aaa-Name-"));
		System.out.println(DaoUtil.getDaoName("aaa_Name_"));
		System.out.println(DaoUtil.getDaoName("AAA_NAME_"));
		System.out.println(DaoUtil.getJavaFieldName("AAA_NAME", false));
		System.out.println(DaoUtil.getJavaFieldName("AAA_NAME"));
	}

	/**
	 * 
	 * @param name
	 * @param size
	 * @return
	 */
	public static String makeColSize(String name, int size) {
		if (size <= 0)
			return name.trim();

		String s = name;
		for (int i = s.length(); i < size; i++)
			s += " ";

		return s;
	}

	public static String makeInSql(List<?> list, String quotes) {

		StringBuffer ret = new StringBuffer();
		if (list == null)
			return ret.toString();

		ret.append(quotes);
		ret.append(list.get(0));
		ret.append(quotes);

		for (int i = 1; i < list.size(); i++) {

			ret.append(",");
			ret.append(quotes);
			ret.append(list.get(i));
			ret.append(quotes);
		}

		return ret.toString();
	}
	

	public static String getColumnName(String name) {

		char chs[] = name.toCharArray();
		StringBuffer ret = new StringBuffer();

		if (name.indexOf('-') >= 0 || name.indexOf('_') >= 0) {
			for (int i = 0; i < chs.length; i++) {
				if (chs[i] == '-' || chs[i] == '_') {
					if (ret.length() > 0 && i + 1 < chs.length ) {
						ret.append("_");
					}
				} else {
					ret.append(chs[i]);
				}
			}
		} else {

			chs = name.toCharArray();
			ret.append(chs[0]);
			for (int i = 1; i < chs.length; i++) {
				if (chs[i] >= 'A' && chs[i] <= 'Z') {
					ret.append("_");
				}
				ret.append(chs[i]);
			}
		}
		
		return ret.toString().toUpperCase();
	}

	public static String getJavaFieldName3(String name) {
		if (name.indexOf('-') > 0 || name.indexOf('_') > 0) {
			char chs[] = name.toLowerCase().toCharArray();
			StringBuffer sb = new StringBuffer();
			boolean toUpper = false;
			for (char ch : chs) {
				if (ch == '_' || ch == '-') {
					toUpper = true;
				} else {
					if (toUpper) {
						sb.append((ch + "").toUpperCase());
					} else {
						sb.append(ch);
					}
					toUpper = false;
				}
			}
			return sb.toString();
		} else {
			return name;
		}

	}
}
