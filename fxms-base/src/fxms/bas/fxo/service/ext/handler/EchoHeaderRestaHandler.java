package fxms.bas.fxo.service.ext.handler;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map.Entry;

import fxms.module.restapi.FxHttpHandler;

import java.util.Set;

/**
 * Echo 핸들러
 * 
 * @author subkjh@naver.com(김종훈)
 *
 */
public class EchoHeaderRestaHandler extends FxHttpHandler {

	@Override
	protected byte[] onProcess(InetSocketAddress client, Set<Entry<String, List<String>>> header, String body)
			throws Exception {
		StringBuffer response = new StringBuffer();
		for (Entry<String, List<String>> entry : header) {
			response.append(entry.toString());
			response.append("\n");
		}
		return response.toString().getBytes();
	}

}
