package fxms.nms.service;

import java.rmi.RemoteException;

import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.fxo.service.mo.MoServiceImpl;

public class PingServiceImpl extends MoServiceImpl implements PingService {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4300162206456302314L;

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		FxServiceImpl.start(PingService.class.getSimpleName(), PingServiceImpl.class, args);
	}

	public PingServiceImpl(String name, int port) throws RemoteException, Exception {
		super(name, port);
	}
}
