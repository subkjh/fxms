package fxms.client;

public class ALARM_CODE {
		/** 수집요청 응답 없음 (11001)  */
	public static final int NOT_RESPONSE_ON_PS_COLLECT = 11001;
	/** 수집요청 오류 응답 (11002)  */
	public static final int ERR_RESPONSE_ON_PS_COLLECT = 11002;
	/** 수집요청 자료 없음 (11003)  */
	public static final int NO_DATA_ON_PS_COLLECT = 11003;
	/** 관리대상 추가됨 (10101)  */
	public static final int MO_ADDED = 10101;
	/** 관리대상 수정됨 (10102)  */
	public static final int MO_UPDATED = 10102;
	/** 관리대상 삭제됨 (10103)  */
	public static final int MO_DELETED = 10103;
	/** 서비스 큐 자료가 너무 많음 (10201)  */
	public static final int SERVICE_QUEUE_TOO_MANY = 10201;
	/** ICMP PING 응답없음 (21001)  */
	public static final int PING_OFF = 21001;
	/** SNMP 응답없음 (21002)  */
	public static final int SNMP_OFF = 21002;
	/** 온도 범위 초과 (22101)  */
	public static final int TEMP_OVER = 22101;
	/** 온도 범위 미달 (22102)  */
	public static final int TEMP_UNDER = 22102;
	/** pH 범위 초과 (22103)  */
	public static final int PH_OVER = 22103;
	/** pH 범위 미달 (22104)  */
	public static final int PH_UNDER = 22104;
	/** 출입문 열림 (22105)  */
	public static final int DOOR_OPEN = 22105;
	/** 조도 범위 초과 (22106)  */
	public static final int ILLUMI_OVER = 22106;
	/** 조도 범위 미달 (22107)  */
	public static final int ILLUMI_UNDER = 22107;
	/** 습도 범위 초과 (22108)  */
	public static final int HUMI_OVER = 22108;
	/** 습도 범위 미달 (22109)  */
	public static final int HUMI_UNDER = 22109;
	/** 이산화탄소량 범위 초과 (22110)  */
	public static final int CO2_OVER = 22110;
	/** 이산화탄소량 범위 미달 (22111)  */
	public static final int CO2_UNDEF = 22111;

}
