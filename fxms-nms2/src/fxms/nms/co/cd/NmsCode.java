package fxms.nms.co.cd;

public class NmsCode {

	public class AlarmCode {
		public static final String NODE_AUTH_FAIL = "node-auth-fail";
		public static final String NODE_COLD_START = "node-cold-start";
		public static final String NODE_WARM_START = "node-warm-start";
		public static final String NODE_EGP_NEIGH_LOSS = "node-egp-neigh-loss";

		public static final String UNKNOWN_SYSLOG = "unknown-syslog";
		public static final String NOT_SET_SYSLOG = "not-set-syslog";
		public static final String UNKNOWN_TRAP = "unknown-trap";
		public static final String NOT_SET_TRAP = "not-set-trap";
		
		public static final String SYSLOG_QUEUE_JAM = "syslog-queue-jam";

	}

	public class PsCode {
		public static final String IF_STATUS_LINK = "ifStatusLink";
	}

	public class AlarmGroup {
		public static final String SNMP_TRAP = "snmp-trap";
		public static final String SYSLOG = "syslog";
	}

	public class EventType {
		public static final String SYSLOG_THR = "syslog-thr";
		public static final String TRAP_THR = "trap-thr";
	}

	public class Var {
		/** 플로우 트래픽 조회시 데이터 건수 목록 */
		public static final String FLOW_DATA_COUNT_UI = "FLOW_DATA_COUNT_UI";
		/** 월간 FLOW 마지막 생성 일시 */
		public static final String FLOW_LAST_TIME_MONTHLY = "FLOW_LAST_TIME_MONTHLY";
		/** 주간 FLOW 마지막 생성 일시 */
		public static final String FLOW_LAST_TIME_WEEKLY = "FLOW_LAST_TIME_WEEKLY";
		/** 년간 FLOW 마지막 생성 일시 */
		public static final String FLOW_LAST_TIME_YEARLY = "FLOW_LAST_TIME_YEARLY";
		/** 플로우 그래프에 표시될 데이터의 건수 */
		public static final String FLOW_SIZE_GRAPH_DATA = "FLOW_SIZE_GRAPH_DATA";
		/** 5분 FLOW 보관 주기(일) */
		public static final String FLOW_TERM_DAILY = "FLOW_TERM_DAILY";
		/** 2시간 FLOW 보관 주기(월) */
		public static final String FLOW_TERM_MONTHLY = "FLOW_TERM_MONTHLY";
		/** 30분 FLOW 보관 주기(주) */
		public static final String FLOW_TERM_WEEKLY = "FLOW_TERM_WEEKLY";
		/** 1일 FLOW 보관 주기(년) */
		public static final String FLOW_TERM_YEARLY = "FLOW_TERM_YEARLY";
		/** 구성정보백업폴더(AP서버내) */
		public static final String FOLDER_DB_BACKUP = "FOLDER_DB_BACKUP";
		/** 보관기간-경보이력(일) */
		public static final String KEEP_DAYS_ALARM = "KEEP_DAYS_ALARM";
		/** 보관기간-이력(일) */
		public static final String KEEP_DAYS_LOG = "KEEP_DAYS_LOG";
		/** 보관기간-명령어실행결과(일) */
		public static final String KEEP_DAYS_SCRIPT_LOG = "KEEP_DAYS_SCRIPT_LOG";
		/** 보관기간-SYSLOG(일) */
		public static final String KEEP_DAYS_LOG_SYSLOG = "KEEP_DAYS_LOG_SYSLOG";
		/** 시스로그 발생 후 얼마 후 ZIP으로 묶을 것인지 */
		public static final String SYSLOG_MAKE_ZIP_AFTER_DAYS = "SYSLOG_MAKE_ZIP_AFTER_DAYS";
		/** 보관기간-TRAP(일) */
		public static final String KEEP_DAYS_LOG_TRAP = "KEEP_DAYS_LOG_TRAP";
		/** 보관기간-경보통계(년) */
		public static final String KEEP_YEARS_STAT_ALARM = "KEEP_YEARS_STAT_ALARM";
		/** 보관기간-운용통계(년) */
		public static final String KEEP_YEARS_STAT_USR = "KEEP_YEARS_STAT_USR";
		/** 통계일시(경보통계, 마지막) */
		public static final String LAST_HSTIME_MAKE_ALARM_STAT = "LAST_HSTIME_MAKE_ALARM_STAT";
		/** 통계일자(운용통계, 마지막) */
		public static final String LAST_YYYYMMDD_MAKE_USER_OP_STAT = "LAST_YYYYMMDD_MAKE_USER_OP_STAT";
		/** 그래프에 표시될 데이터의 건수 */
		public static final String MRTG_DATA_COUNT = "MRTG_DATA_COUNT";
		/** 합산(1일) 성능 마지막 생성 일시 */
		public static final String PERF_AGG_LAST_TIME_D1 = "PERF_AGG_LAST_TIME_D1";
		/** 합산(2시간) 성능 마지막 생성 일시 */
		public static final String PERF_AGG_LAST_TIME_H2 = "PERF_AGG_LAST_TIME_H2";
		/** 합산(30분) 성능 마지막 생성 일시 */
		public static final String PERF_AGG_LAST_TIME_M30 = "PERF_AGG_LAST_TIME_M30";
		/** 합산(5분) 성능 마지막 생성 일시 */
		public static final String PERF_AGG_LAST_TIME_M5 = "PERF_AGG_LAST_TIME_M5";
		/** 월간 성능 마지막 생성 일시 */
		public static final String PERF_LAST_TIME_MONTHLY = "PERF_LAST_TIME_MONTHLY";
		/** 주간 성능 마지막 생성 일시 */
		public static final String PERF_LAST_TIME_WEEKLY = "PERF_LAST_TIME_WEEKLY";
		/** 년간 성능 마지막 생성 일시 */
		public static final String PERF_LAST_TIME_YEARLY = "PERF_LAST_TIME_YEARLY";
		/** 5분 성능 보관 주기(일) */
		public static final String PERF_TERM_DAILY = "PERF_TERM_DAILY";

		/** 2시간 성능 보관 주기(월) */
		public static final String PERF_TERM_MONTHLY = "PERF_TERM_MONTHLY";
		/** 30분 성능 보관 주기(주) */
		public static final String PERF_TERM_WEEKLY = "PERF_TERM_WEEKLY";
		/** 1일 성능 보관 주기(년) */
		public static final String PERF_TERM_YEARLY = "PERF_TERM_YEARLY";
		/** SMS 재발송 주기(분) */
		public static final String SMS_RESEND_CYCLE = "SMS_RESEND_CYCLE";
		/** SMS 발송 ID */
		public static final String SMS_SENDER_ID = "SMS_SENDER_ID";

		/** SNMP 커뮤니티(읽기) 목록 */
		public static final String SNMP_COMM_READ = "SNMP_COMM_READ";
		/** SNMP 포트 목록 */
		public static final String SNMP_PORT = "SNMP_PORT";
		/** SNMP 버전 */
		public static final String SNMP_VERSION = "SNMP_VERSION";
	}
}
