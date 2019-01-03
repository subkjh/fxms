package com.fxms.ws.fxactor;

import com.fxms.ws.biz.ps.WsPsServer;

import fxms.bas.api.ServiceApi;
import fxms.bas.fxo.FxActorImpl;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.fxo.service.property.FxServiceMember;
import subkjh.bas.log.LOG_LEVEL;
import subkjh.bas.log.Logger;

public class WsPsSender extends FxActorImpl implements FxServiceMember {

	private final String HOST = "host";
	private final String PORT = "port";
	private final String VAR = "var-name";

	private String host = null;
	private int port;
	private WsPsServer server;
	private String varName;

	public WsPsSender() {

	}

	public void start() {
		try {
			server = new WsPsServer(host, port);
			Thread th = new Thread(server);
			th.setName(getName() + "-" + port);
			th.start();

			StringBuffer sb = new StringBuffer();
			sb.append(Logger.makeSubString(HOST, host));
			sb.append(Logger.makeSubString(PORT, port));

			Logger.logger.info(Logger.makeString(th.getName(), "started", sb.toString()));

			if (varName != null) {
				ServiceApi.getApi().setVarValue(varName, host != null ? host : FxCfg.getCfg().getIpAddress(), true);
				ServiceApi.getApi().setVarValue(varName + "-port", port, true);
			}

			FxServiceImpl.fxService.addFxActor(this);

		} catch (Exception e) {
			Logger.logger.error(e);
			server = null;
		}
	}

	@Override
	public void setPara(String name, Object value) {

		super.setPara(name, value);

		if (name.equals(PORT)) {
			port = Integer.parseInt(value.toString());
		} else if (name.equals(HOST)) {
			host = String.valueOf(value);
		} else if (name.equals(VAR)) {
			varName = String.valueOf(value);
		}
	}

	@Override
	public String getState(LOG_LEVEL level) {
		StringBuffer sb = new StringBuffer();
		sb.append("port=");
		sb.append(port);
		sb.append(server.getState());

		return sb.toString();
	}

}
