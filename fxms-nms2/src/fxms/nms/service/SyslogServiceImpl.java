package fxms.nms.service;

import java.rmi.RemoteException;

import fxms.bas.fxo.service.FxServiceImpl;

public class SyslogServiceImpl extends FxServiceImpl implements SyslogService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7660076009483224835L;

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		FxServiceImpl.start(SyslogService.class.getSimpleName(), SyslogServiceImpl.class, args);
	}

	public SyslogServiceImpl(String name, int port) throws RemoteException, Exception {
		super(name, port);
	}

}
