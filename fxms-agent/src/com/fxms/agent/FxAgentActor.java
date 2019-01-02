package com.fxms.agent;

import fxms.bas.fxo.FxActorImpl;
import fxms.bas.fxo.service.property.FxServiceMember;
import subkjh.bas.log.LOG_LEVEL;
import subkjh.bas.log.Logger;

/**
 * FxAgentManager 매핑 액터
 * 
 * @author SUBKJH-DEV
 *
 */
public class FxAgentActor extends FxActorImpl implements FxServiceMember {

	private FxAgentManager manager;

	public FxAgentManager getManager() throws Exception {

		if (manager == null) {
			manager = makeManager();
		}

		return manager;
	}

	@Override
	public String getState(LOG_LEVEL level) {
		if (manager != null) {
			return "port=" + manager.getLocalPort();
		}
		return null;
	}

	@Override
	public void start() {
		try {
			getManager().start();
		} catch (Exception e) {
			Logger.logger.error(e);
		}
	}

	private FxAgentManager makeManager() throws Exception {

		int localPort = getParaInt("local-port", 0);
		if (localPort == 0)
			throw new Exception("local-port not defined");

		String javaClass = getParaStr("pdu-processor");
		if (javaClass == null)
			throw new Exception("pdu-processor not defined");

		manager = new FxAgentManager();
		manager.setName(getName());
		manager.setLocalPort(localPort);

		try {
			Object obj = Class.forName(javaClass).newInstance();
			if (obj instanceof FxAgentPduProcessor) {
				manager.setPduProcessor((FxAgentPduProcessor) obj);
			} else {
				throw new Exception(javaClass + " is not HoleListener");
			}
		} catch (Exception e) {
			throw e;
		}

		return manager;

	}

}
