package fxms.nms.co.syslog;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import subkjh.bas.BasCfg;
import subkjh.bas.co.log.Logger;
import fxms.nms.co.syslog.vo.SyslogVo;

public class SyslogSender {

	private String m_ip;
	private String m_id = "";
	private int facility = SyslogVo.LOG_LOCAL4;
	private int priority = SyslogVo.PRIORITY_DEBUG;
	private Logger logger = Logger.logger;

	public SyslogSender(String id, String ip) {
		m_id = id;
		if (ip == null)
			m_ip = "localhost";
		else
			m_ip = ip;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public void setFacility(int f) {
		facility = f;
	}

	public void setPriority(int p) {
		priority = p;
	}

	/**
	 * Sends message to the specified syslogd server
	 * <p>
	 * Not directky called by the application developer
	 * </p>
	 * 
	 * @param id
	 *            This is prepended to every message, and is typically set to
	 *            the program name. The option argument specifies If it is null,
	 *            use default id
	 * @param facility
	 * @param priority
	 * @param msg
	 *            실제 기록할 메세지를 넣는다.
	 * @return void
	 */
	public void sendLog(String logMsg) throws IOException {
		sendLog(m_id, facility, priority, logMsg);
	}

	public void sendLog(String id, int facility, int priority, String logMsg) throws IOException {
		if (id == null)
			id = m_id;

		DatagramSocket soc = null;
		try {
			soc = new DatagramSocket();
			int nTag = facility + priority;
			String tag = "<" + String.valueOf(nTag) + ">";
			String msgA = tag + id + ": " + logMsg;
			byte msg[] = msgA.getBytes();

			if (logger != null)
				logger.debug(m_ip + ":" + " ( " + msgA + " )" + " length is " + msg.length);

			DatagramPacket p = new DatagramPacket(msg, msg.length, InetAddress.getByName(m_ip), 514);
			soc.send(p);
		} catch (Exception e) {
			if (logger != null)
				logger.error(e);
			else
				e.printStackTrace();
		} finally {
			if (soc != null) {
				soc.close();
			}
		}

	}

	public static void main(String[] args) {

		Map<String, Object> props = BasCfg.parseArgs(args);

		System.out.println(props);

		String ip = (String) props.get("ip");
		int count;
		long gap = 200;
		String text = "";

		try {
			count = Integer.parseInt(props.get("count") + "");
		} catch (Exception e) {
			count = 10;
		}

		try {
			gap = Integer.parseInt(props.get("gap") + "");
		} catch (Exception e) {
			gap = 200;
		}

		text = "TEST SYSLOG TEXT .. ";

		if (ip == null) {
			System.out.println("Usage : ip=<ip> count=<count>  gap=<gap>");
			System.exit(0);
		}

		SyslogSender syslogSender = new SyslogSender("test", ip);
		syslogSender.setLogger(Logger.logger);

		try {
			syslogSender.setFacility(SyslogVo.LOG_LOCAL4);
			syslogSender.setPriority(SyslogVo.PRIORITY_INFO);

			if (count <= 0) {
				while (true) {
					syslogSender.sendLog(text + getYYYYMMDD_HHMMSS_sss(System.currentTimeMillis()));
					try {
						Thread.sleep(gap);
					} catch (Exception ex) {
					}
				}
			} else {
				for (int i = 0; i < count; i++) {
					syslogSender.sendLog(text + getYYYYMMDD_HHMMSS_sss(System.currentTimeMillis()));
					try {
						Thread.sleep(gap);
					} catch (Exception ex) {
					}
				}
			}

		} catch (IOException e) {
			System.out.println("Exception is " + e.getMessage());
		}
	}

	private static final SimpleDateFormat YYYYMMDD_HHMMSS = new SimpleDateFormat("yyyyMMdd_HHmmss");

	private static synchronized String getYYYYMMDD_HHMMSS_sss(long ttime) {
		String ret = YYYYMMDD_HHMMSS.format(new Date(ttime));
		ret += "_" + String.format("%03d", (ttime - ((ttime / 1000L) * 1000L)));
		return ret;
	}

}
