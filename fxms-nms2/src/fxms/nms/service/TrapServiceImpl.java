package fxms.nms.service;

import java.rmi.RemoteException;

import fxms.bas.api.FxApi;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.nms.api.TrapApi;
import fxms.nms.co.snmp.trap.TrapReceiver;

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

	private long nextZipMstime = System.currentTimeMillis() + 1800000L;

	@Override
	public void onCycle(long mstime) {

		super.onCycle(mstime);

		if (FxApi.getTime().startsWith("02") && System.currentTimeMillis() > nextZipMstime) {
			// 02:10 이후에 zip 작업함.

			Thread th = new Thread() {
				public void run() {
					try {
						TrapApi.getApi().zipTrapFiles();
					} catch (Exception e) {
						logger.error(e);
					}

				}
			};
			th.setName("Traplog2Zip");
			th.start();

			nextZipMstime += 1800000L;
		}

	}

	@Override
	protected void onStarted() throws Exception {

		super.onStarted();

		FxCfg cfg = FxCfg.getCfg();

		TrapReceiver receiver = new TrapReceiver();
		receiver.setPara(TrapReceiver.PORT, cfg.getString(TrapReceiver.PORT, null));
		receiver.setPara(TrapReceiver.PARSER_COUNT, cfg.getString(TrapReceiver.PARSER_COUNT, null));
		receiver.setPara(TrapReceiver.BUFFER_SIZE, cfg.getString(TrapReceiver.BUFFER_SIZE, null));
		receiver.setPara(TrapReceiver.COMMUNITY, cfg.getString(TrapReceiver.COMMUNITY, null));
		receiver.startMember();

	}
}
