package fxms.bas.co.noti;

import java.rmi.NotBoundException;
import java.util.List;

import fxms.bas.api.ServiceApi;
import fxms.bas.co.exp.NotFoundException;
import fxms.bas.co.signal.AliveSignal;
import fxms.bas.co.signal.ReloadSignal;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.fxo.service.noti.NotiService;
import fxms.bas.fxo.thread.CycleFxThread;
import fxms.bas.fxo.thread.QueueFxThread;
import subkjh.bas.co.log.Logger;

public class NotiSender extends QueueFxThread<FxEvent> {

	public static final String NAME = "NotiThread";

	private NotiService notiService;
	private int count;
	private List<FxEvent> notiList;
	private boolean isFirst = true;
	private String senderId;

	public NotiSender(String senderId) throws Exception {

		super(FxCfg.ALIVE_SIGNAL_CYCLE);
		
		setName(NAME);

		this.senderId = senderId;

		CycleFxThread thread = new CycleFxThread("AliveSender", "period " + FxCfg.ALIVE_SIGNAL_CYCLE) {

			@Override
			protected void doCycle(long mstime) {
				put(new AliveSignal(FxCfg.getCfg().getLong(FxCfg.START_TIME, 0), FxCfg.getCfg().getIpAddress(),
						FxCfg.getFxServiceName()));
			}

		};

		thread.start();
	}

	@Override
	protected void doWork(FxEvent noti) throws Exception {

		// reset count
		count = 0;

		for (int i = 0; i < 3; i++) {
			try {

				NotiService notiService = getNotiService();

				if (notiService != null) {

					notiList = notiService.onBroadcast(senderId, noti);
					
					// Noti는 보낸 서비스에게 브로드케스팅을 하지 않으므로 여기서 자신에게 보낸다.
					FxServiceImpl.fxService.onNotify(noti);

					Logger.logger.debug("send={}", noti);

					// NotiService가 보내지 못한 Noti를 여기서 처리한다.
					if (notiList != null) {
						for (FxEvent e : notiList) {
							FxServiceImpl.fxService.onNotify(e);
						}
					}

					return;
				} else {
					throw new NotFoundException("FxService", NotiService.class.getSimpleName());
				}

			} catch (Exception e) {
				Logger.logger.error(e);
				Logger.logger.fail("noti={}, msg={}", noti, e.getMessage());
				notiService = null;
				Thread.sleep(10000);
			}

			Thread.sleep(3000);
		}

		Logger.logger.fail("cant-send={}", noti);

	}

	@Override
	protected void doInit() {

	}

	private NotiService getNotiService() {
		if (notiService == null) {
			try {
				notiService = ServiceApi.getApi().getService(NotiService.class);
				if (notiService != null) {

					notiService.register(senderId, FxServiceImpl.fxService, FxServiceImpl.fxService.getNotiFilter());

					Logger.logger.info(Logger.makeString(notiService.getFxServiceId(), "CONNECTED"));

					if (isFirst == false) {
						// 처음 연결이 아니면 그 동안 노티가 존재할 수 있으므로 모든 자료를 다시 읽도록 통보한다.
						try {
							FxServiceImpl.fxService.onNotify(new ReloadSignal());
						} catch (Exception e) {
							Logger.logger.error(e);
						}
					} else {
						isFirst = false;
					}
				}
			} catch (NotBoundException e) {
				Logger.logger.fail("{} : {}", e.getClass().getName(), e.getMessage());

			} catch (Exception e) {
				Logger.logger.error(e);
			}
		}

		return notiService;

	}

	@Override
	protected void onNoDatas(long index) {

		if (++count >= 2) {
			count = 0;
			notiService = null;

			Logger.logger.info(Logger.makeString(NotiService.class.getName(), "DISCONNECTED"));

		}

	}
}