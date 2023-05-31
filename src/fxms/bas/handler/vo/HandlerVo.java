package fxms.bas.handler.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.fxo.FxActorImpl;
import fxms.bas.fxo.FxAttr;
import fxms.bas.handler.FxHttpHandler;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Loggable;
import subkjh.bas.co.log.Logger;

/**
 * REST-API를 위한 context 정의 내용
 * 
 * @author SUBKJH-DEV
 *
 */
public class HandlerVo extends FxActorImpl implements Loggable {

	private Map<String, FxHttpHandler> mapHandler;

	@FxAttr(name="port", required = true)
	private int port;
	@FxAttr(name="port2db", required = false)
	private int port2db;
	@FxAttr(name="host2db", required = false)
	private String host2db;
	@FxAttr(name="context", required = false)
	private Object context;

	private List<String> samePortList = new ArrayList<String>();

	public HandlerVo() {
		mapHandler = new HashMap<String, FxHttpHandler>();
	}

	public Map<String, FxHttpHandler> getHandler() {
		return mapHandler;
	}

	public String getHost2db() {
		return host2db;
	}

	public int getPort() {
		return port;
	}

	public int getPort2db() {
		return port2db <= 0 ? port : port2db;
	}

	public List<String> getSamePortList() {
		return samePortList;
	}

	@Override
	public String getState(LOG_LEVEL level) {

		StringBuffer sb = new StringBuffer();

		sb.append("\n  ");
		sb.append(Logger.fill("PORT", 40, '-'));
		sb.append(getPort());

		FxHttpHandler handler;
		for (String context : mapHandler.keySet()) {
			handler = mapHandler.get(context);
			sb.append("\n  ");
			sb.append(Logger.fill(context, 40, '-'));
			sb.append(handler.getClass().getName());
		}

		return sb.toString();
	}

	public void merge(HandlerVo hand) {
		mapHandler.putAll(hand.getHandler());
		samePortList.add(hand.getName());
	}

	@Override
	public void onCreated() throws Exception {

		super.onCreated();

		List<String> list = null;

		if (context instanceof String) {
			list = new ArrayList<String>();
			list.add((String) context);
		} else if (context instanceof List) {
			list = (List) context;
		}

		if (list != null) {
			for (String value : list) {

				String ss[] = value.toString().split(",");
				if (ss.length != 2) {
					return;
				}

				String context = ss[0].trim();
				String javaClass = ss[1].trim();

				try {
					FxHttpHandler handler = (FxHttpHandler) Class.forName(javaClass).newInstance();
					mapHandler.put(context, handler);
				} catch (Exception e) {
					Logger.logger.error(e);
				}
			}
		}

	}

}
