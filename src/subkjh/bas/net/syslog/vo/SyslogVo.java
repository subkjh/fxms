package subkjh.bas.net.syslog.vo;

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

	private int facility = -1;
	private int priority = -1;
	private String ipAddress;
	private long msTime;
	private String msg;

	public SyslogVo() {
	}

	public SyslogVo(int facility, int priority, String ipAddress, long msTime, String msg) {
		this.facility = facility;
		this.priority = priority;
		this.msTime = msTime;
		this.ipAddress = ipAddress;
		this.msg = msg;
	}

	public int getFacility() {
		return facility;
	}

	public void setFacility(int facility) {
		this.facility = facility;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * @return the msTime
	 */
	public long getMsTime() {
		return msTime;
	}

	/**
	 * @param msTime
	 *            the msTime to set
	 */
	public void setMsTime(long msTime) {
		this.msTime = msTime;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String toString() {
		return getClass().getSimpleName() + "(" + msg + ")";
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
}
