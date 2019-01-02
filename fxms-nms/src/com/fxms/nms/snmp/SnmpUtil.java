package com.fxms.nms.snmp;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fxms.nms.mo.property.MoSnmppable;
import com.fxms.nms.snmp.beans.OidValue;
import com.fxms.nms.snmp.exception.SnmpErrorException;
import com.fxms.nms.snmp.exception.SnmpNotFoundOidException;
import com.fxms.nms.snmp.exception.SnmpTimeoutException;

import fxms.bas.pso.counter.ValueCounter;
import fxms.bas.pso.counter.ValueCounter32;
import fxms.bas.pso.counter.ValueCounter64;
import fxms.bas.pso.counter.exception.FirstCounterNotFoundException;
import fxms.bas.pso.counter.exception.ResetCounterException;
import subkjh.bas.log.Logger;

/**
 * 사용법<br>
 * SnmpUtil snmputil = SnmpUtil.createSnmpUtil()<br>
 * 
 * 사용함.. <br>
 * 
 * snmputil.close()<br>
 * 
 * @author subkjh
 * 
 */
public abstract class SnmpUtil {

	public enum EXIST_RET {
		error, exist, notfound, timeout
	}

	public static Charset CHARSET = Charset.forName("UTF-8");

	public static final byte TYPE_COUNTER = 65;

	public static final byte TYPE_COUNTER64 = 70;

	public static boolean debug = false;

	private static String javaClassSnmpUtil = SnmpUtilAdventNetImpl.class.getName();

	private static Map<String, SnmpUtil> snmputilMap = new HashMap<String, SnmpUtil>();

	private final static char[] hexArray = "0123456789abcdef".toCharArray();

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public static SnmpUtil createSnmpUtil() throws Exception {
		return createSnmpUtil("");
	}

	/**
	 * 이 메소드를 이용하여 SnmpUtil를 생성은 사용 후 반드시 close()를 호출해야 합니다.
	 * 
	 * @param name
	 *            별칭
	 * @return 사용할 놈
	 * @throws Exception
	 */
	public static SnmpUtil createSnmpUtil(String name) throws Exception {
		SnmpUtil snmpUtil = null;
		try {
			snmpUtil = (SnmpUtil) Class.forName(javaClassSnmpUtil).newInstance();
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		}

		Logger.logger.info(name);

		snmpUtil.open(name, Logger.logger);
		return snmpUtil;
	}

	/**
	 * SnmpUtil를 가져옵니다.<br>
	 * 없으면 생성해서 줍니다.
	 * 
	 * @param name
	 *            사용할 이름
	 * @return
	 * @throws Exception
	 */
	public synchronized static SnmpUtil getSnmpUtil(String name) {
		return getSnmpUtil(name, 5, 1);
	}

	/**
	 * SnmpUtil를 가져옵니다.<br>
	 * 없으면 생성해서 줍니다.
	 * 
	 * @param name
	 *            SnmpUtil명칭
	 * @param timeout
	 *            타임아웃(초)
	 * @param retry
	 *            재시도회수
	 * @return 사용할 SNMP UTIL
	 * @throws Exception
	 */
	public synchronized static SnmpUtil getSnmpUtil(String name, int timeout, int retry) {

		SnmpUtil snmpUtil = snmputilMap.get(name);

		if (snmpUtil == null) {
			try {
				snmpUtil = createSnmpUtil(name);
			} catch (Exception e) {
				Logger.logger.error(e);
				e.printStackTrace();
				return null;
			}

			snmputilMap.put(name, snmpUtil);

			snmpUtil.setTimeoutAndRetries(timeout * 1000, retry);

			snmpUtil.setLogger(Logger.logger);

		}

		return snmpUtil;
	}

	/**
	 * SnmpUtil를 이름으로 검색하여 제공합니다.
	 * 
	 * @param name
	 *            이름
	 * @return 찾은 놈
	 */
	public static SnmpUtil getSnmpUtil4Name(String name) {
		return snmputilMap.get(name);
	}

	/**
	 * 사용할 문자셋을 설정합니다.
	 * 
	 * @param charset
	 *            문자셋
	 */
	public static void setCharset(String charset) {
		CHARSET = Charset.forName(charset);
	}

	/**
	 * 사용할 SnmpUtil 자바 클래스명 설정
	 * 
	 * @param javaClass
	 * @throws Exception
	 */
	public static void setJavaClassSnmpUtil(String javaClass) throws Exception {
		if (javaClass != null) {
			javaClassSnmpUtil = javaClass;
		}
	}

	public static String toStr(byte bytes[]) {
		if (bytes == null || bytes.length == 0) {
			return "<EMPTY>";
		} else {
			StringBuffer sb = new StringBuffer(bytes.length + "\n");
			StringBuffer sb2 = new StringBuffer();
			int index = 0;
			char ch;
			for (int i = 0; i < bytes.length; i++) {
				int v = bytes[i] & 0xFF;
				ch = (char) v;
				sb.append(hexArray[v >>> 4]);
				sb.append(hexArray[v & 0x0F]);
				sb.append(" ");
				sb2.append(ch >= ' ' && ch <= '~' ? ch : '.');
				index++;
				if (index == 8) {
					sb.append("  ");
					sb2.append(" ");
				}
				if (index == 16) {
					sb.append(" | ");
					sb.append(sb2);
					sb.append("\n");
					index = 0;
					sb2 = new StringBuffer();
				}
			}

			if (sb2.length() > 0) {
				for (int i = index; i < 16; i++) {
					sb.append("   ");
				}
				if (index < 8)
					sb.append("  ");
				sb.append(" | ");
				sb.append(sb2);
			}

			return sb.toString();
		}
	}

	protected Logger logger = Logger.logger;

	private final long WAIT_RETRY_TIME = 10000L;

	/** key : MO_NO_UPPER, key2 : OID */
	private Map<Long, Map<String, ValueCounter<?>>> countMap;

	public SnmpUtil() {
		countMap = Collections.synchronizedMap(new HashMap<Long, Map<String, ValueCounter<?>>>());
	}

	/**
	 * 닫기
	 */
	public abstract void close();

	/**
	 * 장비가 입력된 OID를 지원하는지 판단합니다.
	 * 
	 * @param node
	 *            장비
	 * @param oid
	 *            판단할 OID
	 * @return 확인값
	 */
	public EXIST_RET exist(MoSnmppable node, String oid) {

		if (oid == null || oid.length() < 5)
			return EXIST_RET.notfound;

		List<OidValue> valueList;
		try {
			char chArr[] = oid.toCharArray();
			if (chArr[chArr.length - 2] == '.' && chArr[chArr.length - 1] == '0') {
				valueList = snmpget(node, oid);
			} else {
				valueList = snmpgetnext(node, oid);
			}

			if (valueList != null && valueList.get(0).isNull() == false) {
				if (valueList.get(0).getOid().startsWith(oid))
					return EXIST_RET.exist;
			}
			return EXIST_RET.notfound;
		} catch (SnmpTimeoutException e) {
			return EXIST_RET.timeout;
		} catch (SnmpNotFoundOidException e) {
			return EXIST_RET.notfound;
		} catch (SnmpErrorException e) {
			return EXIST_RET.timeout;
		}
	}

	/**
	 * 
	 * @param varList
	 * @return key=OID 형식의 맵
	 */
	public Map<String, OidValue> makeMap(List<OidValue> varList) {
		Map<String, OidValue> map = new HashMap<String, OidValue>();
		for (OidValue var : varList) {
			map.put(var.getOid(), var);
		}
		return map;
	}

	/**
	 * 이 클래스 사용을 시작합니다.
	 * 
	 * @param name
	 *            이름
	 * @param logger
	 *            로거
	 * @throws Exception
	 */
	public abstract void open(String name, Logger logger) throws Exception;

	/**
	 * 로거를 설정합니다.
	 * 
	 * @param logger
	 *            사용할 로거
	 */
	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	/**
	 * 타임아웃과 재시도 회수를 설정합니다.
	 * 
	 * @param timeoutMiliseconds
	 *            ms 단위
	 * @param retries
	 *            재시도 회수
	 */
	public abstract void setTimeoutAndRetries(int timeoutMiliseconds, int retries);

	/**
	 * 
	 * @param snmpNode
	 * @param nonRepeaters
	 * @param maxRepetitions
	 * @param oidArray
	 * @return
	 * @throws SnmpTimeoutException
	 * @throws SnmpNotFoundOidException
	 * @throws SnmpErrorException
	 */
	public abstract List<OidValue> snmpbulk(MoSnmppable snmpNode, int nonRepeaters, int maxRepetitions,
			String... oidArray) throws SnmpTimeoutException, SnmpNotFoundOidException, SnmpErrorException;

	/**
	 * 
	 * @param snmpNode
	 * @param snmpListener
	 * @param nonRepeaters
	 * @param maxRepetitions
	 * @param oidArray
	 * @return
	 * @throws SnmpErrorException
	 */
	public abstract int snmpbulk(MoSnmppable snmpNode, SnmpListener snmpListener, int nonRepeaters, int maxRepetitions,
			String... oidArray) throws SnmpErrorException;

	/**
	 * Counter 값을 시간과 비교하여 처리합니다.
	 * 
	 * @param node
	 * @param ovList
	 * @param tag
	 * @return
	 * @throws SnmpTimeoutException
	 * @throws Exception
	 */
	public List<OidValue> snmpCheckCounter(MoSnmppable node, List<OidValue> ovList, String tag)
			throws SnmpTimeoutException, Exception {

		List<OidValue> ovListAll = new ArrayList<OidValue>();
		List<String> oidRetry = new ArrayList<String>();

		long mstimeGet = System.currentTimeMillis();

		for (OidValue oid : ovList) {

			if (oid.getValue() == null)
				throw new SnmpNotFoundOidException(node.getMoNo() + ":" + oid.getOid() + "'s value is null");

			try {
				if (oid.getType() == SnmpUtil.TYPE_COUNTER64) {
					oid.setNumber(getValueCounter(node.getMoNo(), oid.getOid(), mstimeGet, oid.getBigInteger(), tag));
				} else if (oid.getType() == SnmpUtil.TYPE_COUNTER) {
					oid.setNumber(getValueCounter(node.getMoNo(), oid.getOid(), mstimeGet, oid.getLong(), tag));
				}

				ovListAll.add(oid);

			} catch (FirstCounterNotFoundException e) {
				if (debug) {
					e.printStackTrace();
				}
				oidRetry.add(e.getTarget());
			} catch (ResetCounterException e) {
				if (debug) {
					e.printStackTrace();
				}
				oidRetry.add(e.getTarget());
			} catch (Exception e) {
				throw e;
			}
		}

		if (oidRetry.size() > 0) {

			Thread.yield();

			try {
				Thread.sleep(WAIT_RETRY_TIME);
			} catch (InterruptedException e1) {
			}

			mstimeGet = System.currentTimeMillis();

			try {
				ovList = snmpget(node, oidRetry.toArray(new String[oidRetry.size()]));
			} catch (Exception e) {
				throw e;
			}

			for (OidValue oid : ovList) {

				if (oid.getValue() == null)
					throw new SnmpNotFoundOidException(oid.getOid() + "'s value is null");

				try {
					if (oid.getType() == SnmpUtil.TYPE_COUNTER64) {
						oid.setNumber(
								getValueCounter(node.getMoNo(), oid.getOid(), mstimeGet, oid.getBigInteger(), tag));
					} else if (oid.getType() == SnmpUtil.TYPE_COUNTER) {
						oid.setNumber(getValueCounter(node.getMoNo(), oid.getOid(), mstimeGet, oid.getLong(), tag));
					}

					ovListAll.add(oid);

				} catch (FirstCounterNotFoundException e) {
					if (debug) {
						e.printStackTrace();
					}
				} catch (ResetCounterException e) {
					if (debug) {
						e.printStackTrace();
					}
				} catch (Exception e) {
					throw e;
				}
			}
		}

		if (ovListAll == null || ovListAll.size() == 0) {
			logger.fail(node.getMoNo() + " snmpget result is empty");
			return null;
		}

		return ovListAll;

	}

	/**
	 * 
	 * @param node
	 * @param oid
	 * @param tag
	 * @return
	 * @throws SnmpTimeoutException
	 * @throws Exception
	 */
	public OidValue snmpCheckCounter(MoSnmppable node, OidValue oid, String tag)
			throws SnmpTimeoutException, Exception {

		long mstimeGet = System.currentTimeMillis();

		if (oid.getValue() == null)
			throw new SnmpNotFoundOidException(node.getMoNo() + ":" + oid.getOid() + "'s value is null");

		try {
			if (oid.getType() == SnmpUtil.TYPE_COUNTER64) {
				oid.setNumber(getValueCounter(node.getMoNo(), oid.getOid(), mstimeGet, oid.getBigInteger(), tag));
			} else if (oid.getType() == SnmpUtil.TYPE_COUNTER) {
				oid.setNumber(getValueCounter(node.getMoNo(), oid.getOid(), mstimeGet, oid.getLong(), tag));
			}

			return oid;

		} catch (FirstCounterNotFoundException e) {
			if (debug) {
				e.printStackTrace();
			}
		} catch (ResetCounterException e) {
			if (debug) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			throw e;
		}

		Thread.yield();

		try {
			Thread.sleep(WAIT_RETRY_TIME);
		} catch (InterruptedException e1) {
		}

		mstimeGet = System.currentTimeMillis();

		try {
			List<OidValue> ovList = snmpget(node, oid.getOid());
			oid = ovList.get(0);
		} catch (Exception e) {
			throw e;
		}

		if (oid.getValue() == null)
			throw new SnmpNotFoundOidException(oid.getOid() + "'s value is null");

		try {
			if (oid.getType() == SnmpUtil.TYPE_COUNTER64) {
				oid.setNumber(getValueCounter(node.getMoNo(), oid.getOid(), mstimeGet, oid.getBigInteger(), tag));
			} else if (oid.getType() == SnmpUtil.TYPE_COUNTER) {
				oid.setNumber(getValueCounter(node.getMoNo(), oid.getOid(), mstimeGet, oid.getLong(), tag));
			}

			return oid;

		} catch (FirstCounterNotFoundException e) {
			if (debug) {
				e.printStackTrace();
			}
		} catch (ResetCounterException e) {
			if (debug) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			throw e;
		}

		return null;
	}

	/**
	 * 
	 * @param snmpNode
	 * @param snmpListener
	 * @param oidArr
	 * @return
	 * @throws SnmpErrorException
	 */
	public abstract int snmpget(MoSnmppable snmpNode, SnmpListener snmpListener, String... oidArr)
			throws SnmpErrorException;

	/**
	 * 
	 * @param snmpNode
	 * @param oidArray
	 * @return
	 * @throws SnmpTimeoutException
	 * @throws SnmpNotFoundOidException
	 * @throws SnmpErrorException
	 */
	public List<OidValue> snmpget(MoSnmppable snmpNode, String... _oidArray)
			throws SnmpTimeoutException, SnmpNotFoundOidException, SnmpErrorException {

		List<OidValue> oidValueList;

		if (_oidArray.length > 50) {
			int srcPos = 0;
			String oidArray[];
			List<OidValue> oidValueEntry;
			oidValueList = new ArrayList<OidValue>();

			while (srcPos < _oidArray.length) {
				oidArray = new String[_oidArray.length > srcPos + 50 ? 50 : _oidArray.length - srcPos];
				System.arraycopy(_oidArray, srcPos, oidArray, 0, oidArray.length);
				oidValueEntry = doSnmpget(snmpNode, oidArray);
				srcPos += 50;
				if (oidValueEntry != null) {
					oidValueList.addAll(oidValueEntry);
				}
			}

		} else {
			oidValueList = doSnmpget(snmpNode, _oidArray);
		}

		if (debug) {
			if (oidValueList != null) {
				System.out.println(snmpNode.getIpAddress());
				for (OidValue v : oidValueList) {
					System.out.println(v.getOid() + "=" + v.getValue());
				}
			} else {
				System.out.println(snmpNode.getIpAddress() + " : " + Arrays.toString(_oidArray) + " is null");
			}
		}

		return oidValueList;

	}

	/**
	 * 
	 * @param snmpNode
	 * @param oidArray
	 * @return
	 * @throws SnmpTimeoutException
	 * @throws SnmpNotFoundOidException
	 * @throws SnmpErrorException
	 */
	public abstract List<OidValue> snmpgetnext(MoSnmppable snmpNode, String... oidArray)
			throws SnmpTimeoutException, SnmpNotFoundOidException, SnmpErrorException;

	/**
	 * SNMP SET<br>
	 * 
	 * @param snmpNode
	 * @param oidValueArray
	 * @return
	 * @throws SnmpTimeoutException
	 * @throws SnmpNotFoundOidException
	 * @throws SnmpErrorException
	 */
	public abstract List<OidValue> snmpset(MoSnmppable snmpNode, OidValue... oidValueArray)
			throws SnmpTimeoutException, SnmpNotFoundOidException, SnmpErrorException;

	/**
	 * 
	 * @param snmpNode
	 * @param oid
	 * @return
	 * @throws SnmpTimeoutException
	 * @throws SnmpNotFoundOidException
	 * @throws SnmpErrorException
	 */
	public abstract List<OidValue> snmpwalk(MoSnmppable snmpNode, String oid)
			throws SnmpTimeoutException, SnmpNotFoundOidException, SnmpErrorException;

	/**
	 * snmpgetnext를 이용하여 oid walk를 수행합니다.
	 * 
	 * @param snmpNode
	 *            장비
	 * @param oidArr
	 *            가져올 SNMP OID
	 * @return 조회된 값
	 * @throws SnmpTimeoutException
	 * @throws SnmpNotFoundOidException
	 * @throws SnmpErrorException
	 */
	public abstract List<OidValue>[] snmpwalk(MoSnmppable snmpNode, String... oidArr)
			throws SnmpTimeoutException, SnmpNotFoundOidException, SnmpErrorException;

	/**
	 * 
	 * @param snmpNode
	 * @param oidArr
	 * @return
	 * @throws SnmpTimeoutException
	 * @throws SnmpNotFoundOidException
	 * @throws SnmpErrorException
	 */
	protected abstract List<OidValue> doSnmpget(MoSnmppable snmpNode, String... oidArr)
			throws SnmpTimeoutException, SnmpNotFoundOidException, SnmpErrorException;

	/**
	 * 
	 * @param moNoNode
	 *            장비MO번호
	 * @param oid
	 *            SNMP OID
	 * @param mstime
	 *            수집일시
	 * @param value
	 *            수집된 값
	 * @param tag
	 *            태깅
	 * @return 계산된 값
	 * @throws FirstCounterNotFoundException
	 * @throws ResetSnmpCounterException
	 * @throws Exception
	 */
	private synchronized long getValueCounter(long moNoNode, String oid, long mstime, Number value, String tag)
			throws FirstCounterNotFoundException, ResetCounterException, Exception {

		Map<String, ValueCounter<?>> map = countMap.get(moNoNode);

		String oidKey = oid;
		if (tag != null && tag.length() > 0)
			oidKey = oid + tag;

		if (map == null) {
			map = new HashMap<String, ValueCounter<?>>();
			countMap.put(moNoNode, map);
		}

		long valueRet;
		int retAdd;
		ValueCounter<?> counterValue;

		if (value instanceof BigInteger) {

			counterValue = (ValueCounter64) map.get(oidKey);

			if (counterValue == null) {
				counterValue = new ValueCounter64();
				map.put(oidKey, counterValue);
			}

			retAdd = ((ValueCounter64) counterValue).add(mstime / 1000, (BigInteger) value);

		} else {
			counterValue = (ValueCounter32) map.get(oidKey);

			if (counterValue == null) {
				counterValue = new ValueCounter32();
				map.put(oidKey, counterValue);
			}

			retAdd = ((ValueCounter32) counterValue).add(mstime / 1000, value.longValue());
		}

		if (retAdd == ValueCounter32.VALUE_FIRST) {
			FirstCounterNotFoundException e = new FirstCounterNotFoundException(moNoNode + ":" + oid);
			e.setTarget(oid);
			throw e;
		} else if (retAdd == ValueCounter32.VALUE_RESET) {
			ResetCounterException e = new ResetCounterException(moNoNode + ":" + oid);
			e.setTarget(oid);
			throw e;
		}

		valueRet = (long) counterValue.getValue();

		return valueRet;
	}
}
