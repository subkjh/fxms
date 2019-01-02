package com.daims.dfc.filter.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import subkjh.exception.NotFoundException;
import subkjh.exception.TimeoutException;
import subkjh.log.Ret;

import com.daims.dfc.common.beans.filter.FilterImpl;
import com.daims.dfc.common.mo.Mo;

/**
 * 구성정보 수집 필터<br>
 * 
 * @author subkjh
 * 
 */
public abstract class ConfigFilter extends FilterImpl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8992481702532648519L;

	public static void find(File _file, List<String> nameList) {
		if (_file.isDirectory()) {
			for (File file : _file.listFiles()) {
				find(file, nameList);
			}
		} else {

			String ss[];
			Class<?> cls;
			String oidCheck;
			String name = _file.getPath();
			name = name.replaceFirst("src.", "");
			name = name.replaceFirst(".java", "");
			name = name.replaceAll("\\\\", ".");

			try {
				cls = Class.forName(name);
				if (cls.isInterface()) return;
				if (ConfigFilter.class.isAssignableFrom(cls) == false) return;

				Object obj = cls.newInstance();
				if (obj instanceof ConfigFilter) {
					ss = name.split("\\.");

					oidCheck = null;
					if (obj instanceof ConfigFilterSnmpNode) {
						ConfigFilterSnmpNode filter = (ConfigFilterSnmpNode) obj;
						String oid = filter.getOidToCheck();
						if (oid != null) {
							if (oid.startsWith(".1.3.6.1.4.1")) {
								oidCheck = oid.substring(13).split("\\.")[0];
							}
						}
					}

					nameList.add(ss[ss.length - 1] + ":" + name + (oidCheck == null ? "" : ":" + oidCheck));

				}

			} catch (Exception e) {
				// System.out.println(e.getMessage() + " : " + name);
			}

		}
	}

	public static void main2(String [ ] args) {
		File folder = new File("src");
		List<String> nameList = new ArrayList<String>();
		ConfigFilter.find(folder, nameList);
		String ss[];

		Collections.sort(nameList);

		for (String name : nameList) {
			ss = name.split(":");
			System.out.println("<filter name=\"" + ss[0] + "\" javaClass=\"" + ss[1] + "\">");
			if (ss.length > 2) {
				System.out.println("<attr name=\"sysObjectId\" startsWith=\".1.3.6.1.4.1." + ss[2] + ".\" />");
			}
			System.out.println("</filter>");
			System.out.println();
		}
	}

	public ConfigFilter ( ) {

	}

	/**
	 * 구성 정보를 수집합니다.
	 * 
	 * @param configMo
	 *            구성정보를 가져올 MO
	 * @param moClassArr
	 *            탐색할 MO 종류<br>
	 *            크기가 0이면 모두 탐색합니다.
	 * @param moName
	 *            특정 MO만 가져올 경우
	 * @return 처리 결과
	 * @throws TimeoutException
	 * @throws NotFoundException
	 * @throws Exception
	 */
	public abstract Ret filter(ConfigMo configMo, String moClassArr[], String moName) throws TimeoutException, NotFoundException,
			Exception;

	/**
	 * 가져올 수 있는 MO 종류<br>
	 * null이거나 크기가 0이면 새롭게 가져오지는 않고 가져온 내용을 근거로 다른 행위를 한다는 의미가 됩니다.
	 * 
	 * @return 가져올 수 있는 MO 종류
	 */
	public abstract String [ ] getMoClassContains();

	/**
	 * 
	 * @return 구성 정보 동기화에 사용되는 필터인지 여부
	 */
	public boolean isSyncFilter() {
		return true;
	}

	/**
	 * 해당 장비가 유효한지 여부를 판단합니다.
	 * 
	 * @param mo
	 *            유효여부를 판단할 MO
	 * @return 유효여부
	 */
	public boolean isValid(Mo mo) throws TimeoutException {
		return true;
	}

	/**
	 * 이 필터에서 가져오는 MO_CLASS와 입력된 MO_CLASS간 하나라도 충족되면 true임<br>
	 * 입력된 MO_CLASS목록이 없었도 true임
	 * 
	 * @param moClassArr
	 * @return 유효한 필터인지 여부
	 * @since 3.0
	 */
	public boolean isValidMoClass(String moClassArr[]) {

		if (moClassArr == null || moClassArr.length == 0) return true;

		String moClassMe[] = getMoClassContains();
		if (moClassMe == null || moClassMe.length == 0) return false;

		for (String moClass : moClassArr) {
			for (String e : moClassMe) {
				if (e.startsWith(moClass)) return true;
			}
		}

		return false;
	}

	/**
	 * 요청한 내용에 내가 제공하는 것과 맞는지 여부
	 * 
	 * @param moClassArr
	 *            MO분류배열
	 * @param moClass
	 *            MO분류
	 * @return 포함여부
	 */
	protected boolean containsMoClass(String moClassArr[], String moClass) {

		if (moClassArr == null || moClassArr.length == 0) return true;

		for (String target : moClassArr) {
			if (target.startsWith(moClass)) return true;
		}

		return false;
	}

}
