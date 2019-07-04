package fxms.nms.service;

import java.rmi.RemoteException;

import fxms.bas.fxo.service.FxServiceImpl;

public class TrapServiceImpl extends FxServiceImpl implements TrapService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3180566861186377061L;

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		FxServiceImpl.start(TrapService.class.getSimpleName(), TrapServiceImpl.class, args);
	}

	public TrapServiceImpl(String name, int port) throws RemoteException, Exception {
		super(name, port);
	}

}
