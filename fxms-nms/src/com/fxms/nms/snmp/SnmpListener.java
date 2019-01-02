package com.fxms.nms.snmp;

import java.util.List;

import com.fxms.nms.snmp.beans.OidValue;
import com.fxms.nms.snmp.exception.SnmpTimeoutException;

public interface SnmpListener {

	/**
	 * 요청에 대한 응답을 제공합니다.
	 * 
	 * @param requestId
	 * @param exception
	 */
	public void add(int requestId, Exception exception);

	/**
	 * 요청에 대한 결과를 등록합니다.
	 * 
	 * @param requestId
	 * @param valueList
	 */
	public void add(int requestId, List<OidValue> valueList);

	/**
	 * 
	 * @param requestId
	 *            요청 ID
	 * @param timeout
	 *            타임아웃<br>
	 *            millisecond 단위
	 * @return 결과
	 * @throws SnmpTimeoutException
	 * @throws Exception
	 */
	public List<OidValue> getValue(int requestId, long timeout) throws SnmpTimeoutException, Exception;

}
