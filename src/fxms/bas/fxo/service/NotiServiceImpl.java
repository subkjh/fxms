package fxms.bas.fxo.service;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import fxms.bas.cron.Cron;
import fxms.bas.event.FxEvent;
import fxms.bas.event.NotiFilter;
import fxms.bas.fxo.FX_PARA;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.thread.CycleFxThread;
import fxms.bas.signal.AliveSignal;
import fxms.bas.thread.NotiPeerThread;
import subkjh.bas.co.log.Logger;

public class NotiServiceImpl extends FxServiceImpl implements NotiService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 806347805494889063L;

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// TROS.start(NTRos.TROS, TRos.class, args);
		FxServiceImpl.start(NotiService.class.getSimpleName(), NotiServiceImpl.class, args);
		// TROS.start(null, TRos.class, args);
	}

	private final Map<String, NotiPeerThread> peers = new HashMap<>();

	public NotiServiceImpl(String name, int port) throws RemoteException, Exception {
		super(name, port);
	}

	@Override
	public NotiFilter getNotiFilter() throws RemoteException, Exception {
		NotiFilter notiFilter = new NotiFilter();
		return notiFilter;
	}

	private long eventSeqno = 0;

	private synchronized long getEventSeqno() {
		return ++eventSeqno;
	}

	@Override
	public void broadcast(String peerName, FxEvent noti) throws RemoteException, Exception {

		noti.setNo(getEventSeqno());

		logger.info("{} >> {}", peerName, noti);

		try {

			for (NotiPeerThread peer : peers.values()) {
				if (peer.getFxServiceId().equals(peerName)) {
					// 보낸 서비스에게 방송하지 않는다.
					logger.trace("{} pass", peer.getFxServiceId());
				} else {
					logger.trace("{} put", peer.getFxServiceId());
					peer.put(noti);
				}
			}

			//
			Thread th = new Thread() {
				public void run() {
					try {
						onNotify(noti);
					} catch (Exception e) {
						logger.error(e);
					}
				}
			};
			th.setName(Thread.currentThread().getName() + "-sub");
			th.start();

		} catch (Exception e) {
			logger.error(e);
			throw e;
		}

	}

	@Override
	public void register(String name, FxService service, NotiFilter filter) throws RemoteException, Exception {

		try {
			NotiPeerThread peer = new NotiPeerThread(name, service, filter);
			NotiPeerThread oldPeer;

			oldPeer = peers.get(peer.getName());
			if (oldPeer != null) {
				oldPeer.stop("NEW ONE");
				oldPeer.join();
			}

			peer.start();

			peers.put(peer.getName(), peer);

			logger.info(Logger.makeString(name + " noti-filter", filter.toString()));

		} catch (Exception e) {
			Logger.logger.error(e);
			logger.info(Logger.makeString(name, "ERROR"));
			throw e;
		}

	}

	@Override
	public void start() throws RemoteException, Exception {

		super.start();

		CycleFxThread trot = new CycleFxThread("KEEP-ALIVE-PEER", Cron.CYCLE_30_SECONDS) {

			@Override
			protected void doCycle(long mstime) {
				try {
					AliveSignal signal = new AliveSignal(FxCfg.get(FX_PARA.fxmsStartTime, 0L), FxCfg.getIpAddress(),
							FxServiceImpl.serviceName, FxCfg.getCfg().getRmiPort(), FxCfg.getCfg().getFxServicePort());
					broadcast(FxServiceImpl.serviceName, signal);
				} catch (Exception e) {
					logger.error(e);
				}
			}

		};
		trot.start();

	}

	@Override
	public void stop(String reason) throws RemoteException, Exception {
		super.stop(reason);
	}

	@Override
	protected void start09SignSedner(StringBuffer sb) throws Exception {

	}

}
