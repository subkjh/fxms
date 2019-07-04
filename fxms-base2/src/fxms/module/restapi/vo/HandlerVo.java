package fxms.module.restapi.vo;

import java.util.HashMap;
import java.util.Map;

import fxms.bas.fxo.FxActor;
import fxms.bas.fxo.FxActorImpl;
import fxms.module.restapi.FxHttpHandler;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Loggable;
import subkjh.bas.co.log.Logger;

/**
 * REST-API를 위한 context 정의 내용
 * 
 * @author SUBKJH-DEV
 *
 */
public class HandlerVo extends FxActorImpl implements Loggable, FxActor {

	private Map<String, FxHttpHandler> mapHandler;
	private final String CONTEXT = "context";
	private final String PORT = "port";
	private int port;

	public HandlerVo() {
		mapHandler = new HashMap<String, FxHttpHandler>();
	}

	public Map<String, FxHttpHandler> getHandler() {
		return mapHandler;
	}

	public int getPort() {
		return port;
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

	@Override
	public void setPara(String name, String value) {

		getFxPara().set(name, value);

		if (name.equals(PORT)) {
			port = Integer.parseInt(value.toString());
			return;
		}

		if (name.equals(CONTEXT) == false) {
			return;
		}

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
			getFxPara().set(name, value);
		}
	}

}
