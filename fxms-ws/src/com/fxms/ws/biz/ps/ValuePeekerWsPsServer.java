package com.fxms.ws.biz.ps;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import fxms.bas.pso.PsVo;
import fxms.bas.pso.filter.ValuePeeker;

public class ValuePeekerWsPsServer extends UnicastRemoteObject implements Remote, ValuePeeker {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1996092537261755515L;

	private WsPsServer server;

	public ValuePeekerWsPsServer() throws RemoteException, Exception {
		super();
	}

	public WsPsServer getServer() {
		return server;
	}

	@Override
	public void onValue(long mstime, PsVo vo) throws RemoteException, Exception {

		if (server != null) {
			server.onValue(mstime, vo);
		}
	}

	public void setServer(WsPsServer server) {
		this.server = server;
	}

}
