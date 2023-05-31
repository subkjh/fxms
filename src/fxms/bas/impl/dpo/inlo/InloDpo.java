package fxms.bas.impl.dpo.inlo;

public class InloDpo {

	public static String getAllName(String upperAllName, String name) {
		if (upperAllName == null || upperAllName.trim().length() == 0)
			return name;
		return upperAllName + " > " + name;
	}

}
