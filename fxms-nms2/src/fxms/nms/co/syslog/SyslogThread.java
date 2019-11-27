package fxms.nms.co.syslog;

import java.util.concurrent.LinkedBlockingQueue;

import fxms.bas.fxo.thread.FXTHREAD_STATUS;
import fxms.bas.fxo.thread.FxThread;
import fxms.nms.api.SyslogApi;
import fxms.nms.co.syslog.adapter.SyslogAdapter;
import fxms.nms.co.syslog.mo.SyslogNode;
import fxms.nms.co.syslog.vo.SyslogVo;
import subkjh.bas.co.log.Logger;

/**
 * 큐에서 SYSLOG를 읽어 이벤트 처리하는 쓰레드
 * 
 * @author subkjh(김종훈)
 *
 */
public class SyslogThread extends FxThread {

	private LinkedBlockingQueue<SyslogVo> queue;

	/**
	 * 
	 * @param queue 사용할 큐
	 */
	public SyslogThread(LinkedBlockingQueue<SyslogVo> queue) {
		this.queue = queue;
	}

	@Override
	protected void doInit() {
	}

	@Override
	public void doWork() {

		SyslogNode node;
		long ptime;
		SyslogVo vo;

		while (isContinue()) {

			getCounter().setStatus(FXTHREAD_STATUS.Waiting);

			try {
				vo = queue.take();
			} catch (InterruptedException e) {
				continue;
			}

			if (vo != null) {

				SyslogApi.getApi().writeSyslog2File(vo);

				Logger.logger.debug("{} {}", vo.getIpAddress(), vo.getMsg());

				try {
					getCounter().setStatus(FXTHREAD_STATUS.Running);

					ptime = System.currentTimeMillis();

					node = SyslogApi.getApi().getNode(vo);

					if (node == null) {

						// SyslogApi.getApi().processUnknownLog(vo);

					} else {

						Logger.logger.debug((node == null ? vo.getIpAddress() : node.toString()));

						for (SyslogAdapter actor : SyslogApi.getApi().getAdapterList()) {

							Logger.logger.trace("{}", actor.getClass().getSimpleName());

							try {
								vo = actor.parse(node, vo);
								if (vo == null)
									break;
							} catch (Exception e) {
								Logger.logger.error(e);
							}

						}

						getCounter().addOk(System.currentTimeMillis() - ptime);

					}

				} catch (Exception e) {
					Logger.logger.error(e);
				}

				vo = null;
			}
		}
	}

}
