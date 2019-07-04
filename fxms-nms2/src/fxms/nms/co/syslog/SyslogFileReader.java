package fxms.nms.co.syslog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

import subkjh.bas.co.log.Logger;
import fxms.bas.fxo.service.property.FxServiceMember;
import fxms.bas.fxo.thread.FxThread;
import fxms.nms.co.syslog.vo.SyslogVo;

/**
 * 화일로 부터 SYSLOG을 추출하는 클래스<br>
 * pipe를 통해서 SYSLOG를 읽습니다.
 * 
 * @author subkjh
 * 
 */
public class SyslogFileReader extends FxThread implements FxServiceMember {

	private String filename;
	private SyslogParser parser;
	private LinkedBlockingQueue<SyslogVo> queue;

	public SyslogFileReader() {
		queue = new LinkedBlockingQueue<SyslogVo>();
	}

	@Override
	protected void doInit() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doWork() {

		File logFile = new File(filename);
		FileReader fr = null;
		BufferedReader br = null;
		String logMsg = null;
		SyslogVo vo;

		try {
			fr = new FileReader(logFile);
			// SyslogFileReader로 이름을 받은 파일을 읽는다.
			br = new BufferedReader(fr);

			while (isContinue()) {

				// 한 라인 읽어 오기
				// 내용이 들어올때마다 한 라인씩 읽는다. 만약 logMsg가 null 이 아니라면 logMsg를
				// 찍는다.없을 경우 1초 대기 후 다시 읽는다
				logMsg = br.readLine();

				if (logMsg != null) {
					try {
						vo = parser.parse(logMsg);
					} catch (Exception e) {
						Logger.logger.fail("Parsing Faile [" + logMsg + "]");
						continue;
					}
					try {
						queue.put(vo);
					} catch (InterruptedException e) {
					}
				}
				// 없을 경우 1초 대기 후 다시 읽어 봄
				//
				else {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (FileNotFoundException ex) {
			Logger.logger.error("FileNotFoundException [" + this.filename + "]");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} catch (IOException ioe) {
			Logger.logger.error(ioe);
		} finally {
			try {
				if (fr != null) {
					fr.close();
				}
				if (br != null) {
					br.close();
				}

			} catch (IOException ex1) {
			}
		}

	}

	@Override
	public void startMember() throws Exception {

		int thrSize = getFxPara().getInt("thread-size", 3);

		SyslogThread th;
		for (int i = 0; i < thrSize; i++) {
			th = new SyslogThread(queue);
			th.setName(getName() + "-Thr#" + (i + 1));
			th.start();
		}

		start();
	}

}
