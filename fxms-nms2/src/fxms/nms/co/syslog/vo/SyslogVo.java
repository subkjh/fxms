package fxms.nms.co.syslog.vo;

import java.io.Serializable;

public class SyslogVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 639810694309972145L;

	/*
	 * system is unusable
	 */
	public static final int PRIORITY_EMERG = 0;

	/*
	 * action must be taken immediately
	 */
	public static final int PRIORITY_ALERT = 1;

	/*
	 * critical conditions
	 */
	public static final int PRIORITY_CRIT = 2;

	/*
	 * error conditions
	 */
	public static final int PRIORITY_ERR = 3;

	/*
	 * warning conditions
	 */
	public static final int PRIORITY_WARNING = 4;

	/*
	 * normal but significant condition
	 */
	public static final int PRIORITY_NOTICE = 5;

	/*
	 * informational
	 */
	public static final int PRIORITY_INFO = 6;

	/*
	 * debug-level messages
	 */
	public static final int PRIORITY_DEBUG = 7;

	// facility

	/*
	 * kernel messages
	 */
	public static final int LOG_KERN = 0 << 3;
	/*
	 * random user-level messages
	 */
	public static final int LOG_USER = 1 << 3;

	/*
	 * mail system
	 */
	public static final int LOG_MAIL = 2 << 3;

	/*
	 * system daemons
	 */
	public static final int LOG_DAEMON = 3 << 3;
	/*
	 * security/authorization messages
	 */
	public static final int LOG_AUTH = 4 << 3;
	/*
	 * messages generated internally by syslogd
	 */
	public static final int LOG_SYSLOG = 5 << 3;
	/*
	 * line printer subsystem
	 */
	public static final int LOG_LPR = 6 << 3;
	/*
	 * network news subsystem
	 */
	public static final int LOG_NEWS = 7 << 3;
	/*
	 * UUCP subsystem
	 */
	public static final int LOG_UUCP = 8 << 3;
	/*
	 * clock daemon
	 */
	public static final int LOG_CRON = 9 << 3;
	/*
	 * security/authorization messages (private)
	 */
	public static final int LOG_AUTHPRIV = 10 << 3;

	/*
	 * ftp daemon
	 */
	public static final int LOG_FTP = 11 << 3;

	/* other codes through 15 reserved for system use */
	public static final int LOG_LOCAL0 = 16 << 3; /* reserved for local use */
	public static final int LOG_LOCAL1 = 17 << 3; /* reserved for local use */
	public static final int LOG_LOCAL2 = 18 << 3; /* reserved for local use */
	public static final int LOG_LOCAL3 = 19 << 3; /* reserved for local use */
	public static final int LOG_LOCAL4 = 20 << 3; /* reserved for local use */
	public static final int LOG_LOCAL5 = 21 << 3; /* reserved for local use */
	public static final int LOG_LOCAL6 = 22 << 3; /* reserved for local use */
	public static final int LOG_LOCAL7 = 23 << 3; /* reserved for local use */

	public static String getFacilityString(int facility) {
		switch (facility) {
		case LOG_KERN:
			return "kernel";
		case LOG_USER:
			return "user";
		case LOG_MAIL:
			return "mail";
		case LOG_DAEMON:
			return "daemon";
		case LOG_AUTH:
			return "auth";
		case LOG_SYSLOG:
			return "syslog";
		case LOG_LPR:
			return "lpr";
		case LOG_NEWS:
			return "news";
		case LOG_UUCP:
			return "uucp";
		case LOG_CRON:
			return "cron";
		case LOG_AUTHPRIV:
			return "authprv";
		case LOG_FTP:
			return "ftp";
		case LOG_LOCAL0:
		case LOG_LOCAL1:
		case LOG_LOCAL2:
		case LOG_LOCAL3:
		case LOG_LOCAL4:
		case LOG_LOCAL5:
		case LOG_LOCAL6:
		case LOG_LOCAL7:
			return "local";

		default:
			return "unknown";
		}

	}

	public static String getPriorityString(int priority) {
		switch (priority) {
		case PRIORITY_EMERG:
			return "emegency";
		case PRIORITY_ALERT:
			return "alert";
		case PRIORITY_CRIT:
			return "critical";
		case PRIORITY_ERR:
			return "error";
		case PRIORITY_WARNING:
			return "warning";
		case PRIORITY_NOTICE:
			return "notice";
		case PRIORITY_INFO:
			return "infomation";
		case PRIORITY_DEBUG:
			return "debug";
		default:
			return "unknown";
		}
	}

	private int facility = -1;
	private int severity = -1;
	private String ipAddress;
	private String hostname;
	private long msTime;
	private String msg;

	public SyslogVo() {
	}

	public SyslogVo(int facility, int severity, String ipAddress, long msTime, String msg) {
		this.facility = facility;
		this.severity = severity;
		this.msTime = msTime;
		this.ipAddress = ipAddress;
		this.msg = msg;
	}

	public SyslogVo(String host, byte buffer[]) {
		this(host, new String(buffer, 0, buffer.length));
	}

	public SyslogVo(String host, String msg) {
		int spos, epos;
		String buf, ss[];

		spos = msg.indexOf("<");
		epos = msg.indexOf(">");

		msTime = System.currentTimeMillis();

		/*
		 * Originally received message <00>... Relayed message <13>TIMESTAMP
		 * HOSTNAME <00>...
		 */
		if (spos == 0 && epos > 0) {
			try {
				severity = Integer.parseInt(msg.substring(spos + 1, epos));
				if (severity == 13) {
					msg = msg.substring(epos + 1);
					spos = msg.indexOf("<");
					epos = msg.indexOf(">");
					if (spos == 0 && epos > 0) {
						severity = Integer.parseInt(msg.substring(spos + 1, epos));
						buf = msg.substring(0, spos - 1);
						ss = buf.split(" ");
						if (ss.length >= 2) {
							try {
								msTime = Long.parseLong(ss[0]) * 1000L;
							} catch (Exception ex) {
							}
							host = ss[1];
						}
					}
				}
				msg = msg.substring(epos + 1);
				facility = severity & 0x3f8;
				severity = severity & 0x07;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		this.ipAddress = host;
		this.msg = msg;
	}

	public int getFacility() {
		return facility;
	}

	public String getHostname() {
		return hostname;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public String getMsg() {
		return msg;
	}

	/**
	 * @return the msTime
	 */
	public long getMsTime() {
		return msTime;
	}

	public int getSeverity() {
		return severity;
	}

	public void setFacility(int facility) {
		this.facility = facility;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * @param msTime
	 *            the msTime to set
	 */
	public void setMsTime(long msTime) {
		this.msTime = msTime;
	}

	public void setSeverity(int severity) {
		this.severity = severity;
	}

	public String toString() {
		return getClass().getSimpleName() + "(" + msg + ")";
	}

}
