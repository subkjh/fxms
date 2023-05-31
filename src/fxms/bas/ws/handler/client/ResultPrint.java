package fxms.bas.ws.handler.client;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import subkjh.bas.co.utils.ObjectUtil;

public class ResultPrint {

	private final SimpleDateFormat YYYYMMDDHHMMSS = new SimpleDateFormat("yyyyMMddHHmmss");

	private int getLength(Object obj) {
		if (obj == null) {
			return 0;
		}

		try {
			return obj.toString().getBytes("euc-kr").length;
		} catch (UnsupportedEncodingException e) {
			return obj.toString().length();
		}
	}

	public <T> List<T> convert(Class<T> classOf, List<Map<String, Object>> list) {

		if (list == null) {
			return new ArrayList<T>();
		}

		List<T> ret = new ArrayList<T>();
		T data;
		try {
			for (Map<String, Object> map : list) {
				data = classOf.newInstance();
				ObjectUtil.toObject(map, data);
				ret.add(data);
			}
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<T>();
		}
	}

	/**
	 * 눈으로 확인할 수 있는 시간 형식으로 넘긴다.
	 * 
	 * @return yyyyMMddHHmmss의 값
	 */
	public synchronized long getDate(long mstime) {
		if (mstime <= 0) {
			return Long.parseLong(YYYYMMDDHHMMSS.format(new Date(System.currentTimeMillis())));
		} else {
			return Long.parseLong(YYYYMMDDHHMMSS.format(new Date(mstime)));
		}
	}

	public Map<String, Object> makaPara(Object... objects) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		int index = 0;
		while (index + 1 < objects.length) {
			parameters.put(objects[index] + "", objects[index + 1]);
			index += 2;
		}
		return parameters;
	}

	public void print(List<Map<String, Object>> mapList) {
		if (mapList == null || mapList.size() == 0)
			return;

		Map<String, Object> map = mapList.get(0);
		String nameArr[] = map.keySet().toArray(new String[map.size()]);

		Arrays.sort(nameArr);

		int max[] = new int[nameArr.length];

		for (int i = 0; i < max.length; i++) {
			max[i] = nameArr[i].length();
		}

		for (Map<String, Object> e : mapList) {
			for (int i = 0; i < nameArr.length; i++) {
				max[i] = Math.max(max[i], (getLength(e.get(nameArr[i]))));
			}
		}

		StringBuffer line = new StringBuffer();
		line.append("+-");
		for (int i = 0; i < max.length; i++) {
			for (int n = 0; n < max[i]; n++)
				line.append("-");
			line.append("-+");
			if (i < max.length - 1)
				line.append("-");
		}

		System.out.println(line);
		System.out.print("| ");
		for (int i = 0; i < nameArr.length; i++) {
			System.out.format("%-" + max[i] + "s", nameArr[i]);
			if (i < nameArr.length)
				System.out.print(" | ");
		}
		System.out.println("\n" + line);

		for (Map<String, Object> e : mapList) {
			System.out.print("| ");
			for (int i = 0; i < nameArr.length; i++) {
				System.out.format("%-" + max[i] + "s", e.get(nameArr[i]));
				if (i < nameArr.length)
					System.out.print(" | ");
			}
			System.out.println();
		}

		System.out.println(line);

		System.out.println("  " + mapList.size() + " rows");
		System.out.println(line);
	}

}
