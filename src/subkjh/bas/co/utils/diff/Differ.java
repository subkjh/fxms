package subkjh.bas.co.utils.diff;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import subkjh.bas.co.utils.ObjectUtil;

public class Differ {

	private Collection<String> checkKeyList;

	public Differ() {
	}

	public Differ(Collection<String> checkKeyList) {
		this.checkKeyList = checkKeyList;
	}

	public List<DiffData> diff(Object bfData, Object afData) {

		Map<String, Object> map1 = ObjectUtil.toMap(bfData);

		Map<String, Object> map2 = ObjectUtil.toMap(afData);

		return diff0(map1, map2);

	}

	private Collection<String> getCheckKeyList(Map<String, Object> map1) {
		if (checkKeyList != null) {
			return checkKeyList;
		}
		return map1.keySet();
	}

	private List<DiffData> diff0(Map<String, Object> map1, Map<String, Object> map2) {

		List<DiffData> diffList = new ArrayList<DiffData>();
		Object obj1, obj2;

		for (String key : getCheckKeyList(map2)) {

			obj1 = map1.get(key);
			obj2 = map2.get(key);

			if (obj1 == null && obj2 == null) {
				continue;
			}

			if ((obj1 != null && obj2 != null) && obj1.equals(obj2) == true) {
				continue;
			}

			diffList.add(new DiffData(key, obj1, obj2));

		}

		return diffList;
	}
}
