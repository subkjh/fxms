package subkjh.bas;

public class BasVar {

	public static void main(String[] args) {
		BasVar var = new BasVar();
		System.out.println(var.getColumnName("aaa_aaa"));
		System.out.println(var.getColumnName("aaa---aaa-"));
		System.out.println(var.getColumnName("AaaName"));
		System.out.println(var.getColumnName("aaaName"));
		System.out.println(var.getColumnName("-aaa-Name-"));
		System.out.println(var.getColumnName("aaa_Name_"));
		System.out.println(var.getColumnName("AAA_NAME_"));
		System.out.println(var.getJavaFieldName("AAA_NAME_"));
		System.out.println(var.getJavaFieldName("aaa_aaa"));
		System.out.println(var.getJavaFieldName("is-name-k"));
		System.out.println(var.getJavaFieldName("is_name_k_"));
		System.out.println(var.getJavaFieldName("is_n_a_me_k_"));
	}

	public String getColumnName(String name) {

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
			return ret.toString();
		} else {

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
	}

	public String getJavaFieldName(String name) {
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
