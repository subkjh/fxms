package fxms.nms.co.snmp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import subkjh.bas.co.log.Logger;
import fxms.nms.co.snmp.exception.SnmpTimeoutException;
import fxms.nms.co.snmp.vo.OidValue;
import fxms.nms.mo.property.MoSnmppable;

/**
 * 관리대상별 필터의 처리 여부를 관리합니다.
 * 
 * @author subkjh
 * 
 */
public class FilterValidChecker {

	public static final byte EXIST = 1;
	public static final byte NOT_DEFINED = 2;
	public static final byte UNKNOWN = 3;

	private static FilterValidChecker checker;
	private static Object lock = new Object();

	public static FilterValidChecker getInstance() {
		if (checker == null) {
			synchronized (lock) {
				if (checker == null) {
					checker = new FilterValidChecker();
				}
			}
		}

		return checker;
	}

	private Map<Object, List<String>> notSupportMap;

	public static void main(String[] args) {
		System.out.println(FilterValidChecker.getInstance().valid("a", "oid"));
		FilterValidChecker.getInstance().add("a", "oid", false);
		System.out.println(FilterValidChecker.getInstance().valid("a", "oid"));
		FilterValidChecker.getInstance().add("a", "oid", true);
		System.out.println(FilterValidChecker.getInstance().valid("a", "oid"));
	}

	public String getState() {
		return "NotSupport=" + notSupportMap.size();
	}

	private FilterValidChecker() {
		notSupportMap = new HashMap<Object, List<String>>();
	}

	public synchronized void add(Object target, String filter, boolean support) {
		List<String> notList = notSupportMap.get(target);
		if (notList == null) {
			if (support)
				return;

			notList = new ArrayList<String>();
			notSupportMap.put(target, notList);
		}

		if (support) {
			notList.remove(filter);
		} else {
			Logger.logger.info("InValid", target, filter);
			notList.add(filter);
		}
	}

	/**
	 * 
	 * @param target
	 *            구분할 명칭
	 * @param filter
	 *            필터명
	 * @return 유효여부
	 */
	public boolean valid(Object target, String filter) {
		List<String> validList = notSupportMap.get(target);
		if (validList == null)
			return true;
		return validList.contains(filter) == false;
	}

	public boolean valid(Object target, String oid, SnmpUtil snmputil, MoSnmppable node) throws SnmpTimeoutException {

		List<String> notList = notSupportMap.get(target);
		if (notList != null && notList.contains(oid))
			return false;

		String ss[] = oid.split("\\.");
		if (ss[ss.length - 1].equals("0")) {
			try {
				snmputil.snmpget(node, oid);
				add(target, oid, true);
				return true;
			} catch (SnmpTimeoutException e) {
				throw e;
			} catch (Exception e) {
				Logger.logger.trace("Checking OID failed" + e.getMessage());
				add(target, oid, false);
				return false;
			}
		} else {
			try {
				List<OidValue> valueList = snmputil.snmpgetnext(node, oid);
				if (valueList.get(0).getOid().startsWith(oid)) {
					add(target, oid, true);
					return true;
				} else {
					add(target, oid, false);
				}
			} catch (SnmpTimeoutException e) {
				throw e;
			} catch (Exception e) {
				Logger.logger.trace("Checking OID failed " + e.getMessage());
				add(target, oid, false);
				return false;
			}

			return false;
		}
	}

}
