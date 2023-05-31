package fxms.bas.fxo.service;

import java.rmi.RemoteException;

import fxms.bas.event.NotiFilter;
import fxms.bas.mo.Mo;

public class WebServiceImpl extends FxServiceImpl implements WebService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8544106731078116690L;

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		FxServiceImpl.start(WebService.class.getSimpleName(), WebServiceImpl.class, args);
	}

	public WebServiceImpl(String name, int port) throws RemoteException, Exception {
		super(name, port);
	}

	@Override
	public NotiFilter getNotiFilter() throws RemoteException, Exception {
		NotiFilter notiFilter = new NotiFilter();
		notiFilter.add(Mo.class);
		return notiFilter;
	}
}
