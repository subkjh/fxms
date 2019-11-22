package fxms.nms.service;

import java.rmi.RemoteException;

import fxms.bas.api.FxApi;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.nms.api.SyslogApi;
import fxms.nms.co.syslog.SyslogReceiver2;
import fxms.nms.co.syslog.vo.SyslogVo;

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

	private long nextZipMstime = System.currentTimeMillis() + 1800000L;
	private SyslogReceiver2 receiver;
	public static SyslogServiceImpl syslogService;

	public SyslogServiceImpl(String name, int port) throws RemoteException, Exception {
		super(name, port);
		SyslogServiceImpl.syslogService = this;
	}

	@Override
	public void onCycle(long mstime) {

		super.onCycle(mstime);

		if (FxApi.getTime().startsWith("02") && System.currentTimeMillis() > nextZipMstime) {
			// 02:10 이후에 zip 작업함.

			Thread th = new Thread() {
				public void run() {
					try {
						SyslogApi.getApi().zipSyslogFiles();
					} catch (Exception e) {
						logger.error(e);
					}

				}
			};
			th.setName("Syslog2Zip");
			th.start();

			nextZipMstime += 1800000L;
		}

	}

	public void putSyslog(SyslogVo vo) {
		receiver.put(vo);
	}

	@Override
	protected void onStarted() throws Exception {

		super.onStarted();

		FxCfg cfg = FxCfg.getCfg();

		receiver = new SyslogReceiver2();
		receiver.setPara(SyslogReceiver2.PORT, cfg.getString(SyslogReceiver2.PORT, null));
		receiver.setPara(SyslogReceiver2.PARSER_COUNT, cfg.getString(SyslogReceiver2.PARSER_COUNT, null));
		receiver.setPara(SyslogReceiver2.BUFFER_SIZE, cfg.getString(SyslogReceiver2.BUFFER_SIZE, null));
		receiver.startMember();

	}

}
