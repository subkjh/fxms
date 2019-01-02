package com.fxms.nms.service;

import java.rmi.RemoteException;

import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.fxo.service.mo.MoServiceImpl;

public class SnmpPingServiceImpl extends MoServiceImpl implements SnmpPingService {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3695976757099265168L;

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		FxServiceImpl.start(SnmpPingService.class.getSimpleName(), SnmpPingServiceImpl.class, args);
	}

	public SnmpPingServiceImpl(String name, int port) throws RemoteException, Exception {
		super(name, port);
	}
}
