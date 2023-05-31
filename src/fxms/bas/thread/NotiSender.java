package fxms.bas.thread;

import java.rmi.NotBoundException;

import fxms.bas.api.ServiceApi;
import fxms.bas.event.FxEvent;
import fxms.bas.exp.NotFoundException;
import fxms.bas.fxo.FX_PARA;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.fxo.service.NotiService;
import fxms.bas.fxo.thread.CycleFxThread;
import fxms.bas.fxo.thread.QueueFxThread;
import fxms.bas.signal.AliveSignal;
import fxms.bas.signal.ReloadSignal;
import fxms.bas.signal.ReloadSignal.ReloadType;
import subkjh.bas.co.log.Logger;

/**
 * 큐에 적재된 NOTI를 보내는 스레드
 * 
 * @author subkjh
 *
 */
public class NotiSender extends QueueFxThread<FxEvent> {

	public static final String NAME = "NotiThread";
	private NotiService notiService;
	private int count;
	private boolean isFirst = true;
	private final String sender;

	/**
	 * 
	 * @param senderId 보내는이의 구분자
	 * @throws Exception
	 */
	public NotiSender(String sender) throws Exception {

		super(FxCfg.ALIVE_SIGNAL_CYCLE);

		setName(NAME);

		this.sender = sender;

		CycleFxThread thread = new CycleFxThread("AliveSender", "period " + FxCfg.ALIVE_SIGNAL_CYCLE) {

			@Override
			protected void doCycle(long mstime) {
				put(new AliveSignal(FxCfg.get(FX_PARA.fxmsStartTime, 0L), FxCfg.getIpAddress(),
						FxServiceImpl.serviceName, FxCfg.getCfg().getRmiPort(), FxCfg.getCfg().getFxServicePort()));
			}

		};

		thread.start();
	}

	@Override
	protected void doInit() {

	}

	@Override
	protected void doWork(FxEvent noti) throws Exception {

		// reset count
		count = 0;

		for (int i = 0; i < 3; i++) {
			try {

				NotiService notiService = getNotiService();

				if (notiService != null) {
					notiService.broadcast(sender, noti);
					Logger.logger.debug("send={}", noti);
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

		Logger.logger.fail("can not send={}", noti);

	}

	@Override
	protected boolean isAddToService() {
		
		// 이 스레드는 이벤트를 받지 않으므로 false로 리턴한다.
		return false;
		
	}

	@Override
	protected void onNoDatas(long index) {

		if (++count >= 2) {
			count = 0;
			notiService = null;

			Logger.logger.info(Logger.makeString(NotiService.class.getName(), "DISCONNECTED"));

		}
	}

	private NotiService getNotiService() {
		if (notiService == null) {
			try {
				notiService = ServiceApi.getApi().getService(NotiService.class);
				if (notiService != null) {

					// NotiService 보내이를 등록한다.
					notiService.register(sender, FxServiceImpl.fxService, FxServiceImpl.fxService.getNotiFilter());

					Logger.logger.info(Logger.makeString(notiService.getFxServiceId(), "CONNECTED"));

					if (isFirst == false) {
						// 처음 연결이 아니면 그 동안 노티가 존재할 수 있으므로 모든 자료를 다시 읽도록 통보한다.
						try {
							FxServiceImpl.fxService.onNotify(new ReloadSignal(ReloadType.All));
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
}