package fxms.bas.fxo.service.ext;

import java.net.InetSocketAddress;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.net.httpserver.HttpServer;

import fxms.bas.fxo.FxActorParser;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.module.restapi.FxHttpHandler;
import fxms.module.restapi.vo.HandlerVo;
import subkjh.bas.co.log.Logger;
import sun.net.httpserver.HttpServerImpl;

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

	@Override
	protected void onStarted() throws Exception {

		super.onStarted();

		List<HandlerVo> restaList = FxActorParser.getParser().getActorList(HandlerVo.class);
		if (restaList != null) {

			HttpServer server;
			StringBuffer sb;
			FxHttpHandler handler;
			Map<Integer, HandlerVo> restaMap = new HashMap<Integer, HandlerVo>();
			HandlerVo dupResta;
			for (HandlerVo resta : restaList) {
				if (resta.getPort() > 0) {
					dupResta = restaMap.get(resta.getPort());
					if (dupResta != null) {
						dupResta.getHandler().putAll(resta.getHandler());
					} else {
						restaMap.put(resta.getPort(), resta);
					}
				} else {
					logger.fail(Logger.makeString("RESTA " + resta.getName(), "PORT IS NOT DEFINED"));
				}
			}

			for (HandlerVo resta : restaMap.values()) {

				sb = new StringBuffer();

				try {
					server = HttpServerImpl.create(new InetSocketAddress(resta.getPort()), 0);
					for (String context : resta.getHandler().keySet()) {
						handler = resta.getHandler().get(context);
						server.createContext(context, handler);
						sb.append("\n  ");
						sb.append(Logger.fill(context, 40, '-'));
						sb.append(handler.getClass().getName());
					}
					server.setExecutor(null);
					server.start();

					logger.info(Logger.makeString("RESTA [" + resta.getName() + "] PORT=" + resta.getPort(),
							"started", sb.toString()));

					addFxActor(resta);

				} catch (Exception e) {
					logger.fail(Logger.makeString("RESTA [" + resta.getName() + "] PORT=" + resta.getPort(),
							e.getClass().getSimpleName()));
					Logger.logger.error(e);
					throw e;
				}
			}
		}
	}

}
