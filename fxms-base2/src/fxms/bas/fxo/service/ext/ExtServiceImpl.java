package fxms.bas.fxo.service.ext;

import java.rmi.RemoteException;

import fxms.bas.fxo.service.FxServiceImpl;

public class ExtServiceImpl extends FxServiceImpl implements ExtService {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2550317865836236575L;

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		FxServiceImpl.start(ExtService.class.getSimpleName(), ExtServiceImpl.class, args);
	}

	public ExtServiceImpl(String name, int port) throws RemoteException, Exception {
		super(name, port);
	}

}
