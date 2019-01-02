package com.fxms.nms.service;

import java.rmi.RemoteException;

import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.fxo.service.mo.MoServiceImpl;

public class TrafficServiceImpl extends MoServiceImpl implements TrafficService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1583939060543717633L;

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		FxServiceImpl.start(TrafficService.class.getSimpleName(), TrafficServiceImpl.class, args);
	}

	public TrafficServiceImpl(String name, int port) throws RemoteException, Exception {
		super(name, port);
	}
}