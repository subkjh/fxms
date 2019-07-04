package fxms.bas.fxo.service.noti;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import fxms.bas.co.cron.Cron;
import fxms.bas.co.noti.FxEvent;
import fxms.bas.co.noti.NotiFilter;
import fxms.bas.co.signal.AliveSignal;
import fxms.bas.fxo.FxActor;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.service.FxService;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.fxo.service.noti.thread.NotiPeerFxThread;
import fxms.bas.fxo.thread.CycleFxThread;
import subkjh.bas.co.log.Logger;

public class NotiServiceImpl extends FxServiceImpl implements NotiService {

	public NotiServiceImpl(String name, int port) throws RemoteException, Exception {
		super(name, port);
	}

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

	@Override
	public void start(Map<String, Object> para) throws RemoteException, Exception {

		super.start(para);

		CycleFxThread trot = new CycleFxThread("KEEP-ALIVE-PEER", Cron.CYCLE_30_SECONDS) {

			@Override
			protected void doCycle(long mstime) {
				try {
					AliveSignal signal = new AliveSignal(FxCfg.getCfg().getLong(FxCfg.START_TIME, 0),
							FxCfg.getCfg().getIpAddress(), FxCfg.getFxServiceName());
					onBroadcast(FxCfg.getFxServiceName(), signal);
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
	public List<FxEvent> onBroadcast(String peerName, FxEvent noti) throws RemoteException, Exception {

		logger.info("peer-name={}, noti={}", peerName, noti);

		try {

			int count = 0;

			List<NotiPeerFxThread> peerList = getFxActorList(NotiPeerFxThread.class);
			List<FxEvent> ret = null;

			for (NotiPeerFxThread peer : peerList) {
				if (peer.getFxServiceId().equals(peerName)) {
					// 보낸 서비스에게 방송하지 않는다.
					ret = peer.getNotiList();
					logger.trace("{} pass", peer.getFxServiceId());
				} else {
					logger.trace("{} put", peer.getFxServiceId());
					peer.put(noti);
					count++;
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

			logger.info("peer-size={}", count);

			return ret;

		} catch (Exception e) {
			logger.error(e);
			throw e;
		}

	}

	@Override
	protected void start09SignSedner(StringBuffer sb) throws Exception {

	}

	@Override
	public void register(String name, FxService service, NotiFilter filter) throws RemoteException, Exception {

		try {
			NotiPeerFxThread peer = new NotiPeerFxThread(name, service, filter);
			FxActor oldPeer;

			oldPeer = getFxActor(peer.getName());

			if (oldPeer instanceof NotiPeerFxThread) {
				((NotiPeerFxThread) oldPeer).stop("NEW ONE");
				((NotiPeerFxThread) oldPeer).join();
			}

			peer.start();

			logger.info(Logger.makeString(name, "REGISTERED"));

		} catch (Exception e) {
			Logger.logger.error(e);
			logger.info(Logger.makeString(name, "ERROR"));
			throw e;
		}

	}
}
