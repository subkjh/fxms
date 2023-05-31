package fxms.bas.co;

/**
 * 
 * 
 * @author subkjh
 *
 */
public enum ALARM_CODE {

	/** 시험용 경보 */
	TestAlarm(10000),

	/** 미정의된 성능 항목 */
	PsItemNotDefined(10001),

	/** 미등록된 경보 코드 */
	AlarmCodeNotDefined(10011),

	/** 관리대상 추가됨 */
	MO_ADDED(10101),

	/** 관리대상 수정됨 */
	MO_UPDATED(10102),

	/** 관리대상 삭제됨 */
	MO_DELETED(10103),

	/** 미등록된 관리대상 */
	MO_NOT_FOUND(10104),

	/**
	 * 시스템내부오류<br>
	 * XMS 서비스 내부에서 오류가 발생됨
	 */
	FXMSERR(10900),

	/**
	 * 통계생성오류<br>
	 * FXMS 서비스에서 통계 데이터를 생성중 오류가 발생됨
	 */
	FXMSERR_PS_STAT(10901),

	/**
	 * 통계아탑터실행오류<br>
	 * FXMS 서비스에서 통계 데이터 생성 후 통계아탑터 처리중 오류가 발생됨
	 */
	FXMSERR_PS_STAT_AFTER_ADAPTER(10902),

	/**
	 * CRON 작업 수행 중 오류 발생됨
	 */
	FXMSERR_CRON(10903),

	/**
	 * 성능항목 등록 안됨
	 */
	FXMSERR_PS_ITEM_NOT_FOUND(10904),

	/** 서비스 시작됨 */
	SERVICE_ON_NOTI(10200),

	/** 서비스 큐 자료가 너무 많음 */
	SERVICE_QUEUE_TOO_MANY(10201),

	/** 수집요청 응답 없음 */
	NOT_RESPONSE_ON_PS_COLLECT(11001),

	/** 수집요청 오류 응답 */
	ERR_RESPONSE_ON_PS_COLLECT(11002),

	/** 수집요청 자료 없음 */
	No_data_collected(11003),
	
	/** 수집데이터 확인 필요 */
	PS_VALUE_NOT_ACCEPTABLE(11004),

	/** ICMP PING 응답없음 */
	PING_OFF(21001),

	/** SNMP 응답없음 */
	SNMP_OFF(21002),

	/** 온도 범위 초과 */
	TEMP_OVER(22101),

	/** 온도 범위 미달 */
	TEMP_UNDER(22102),

	/** pH 범위 초과 */
	PH_OVER(22103),

	/** pH 범위 미달 */
	PH_UNDER(22104),

	/** 출입문 열림 */
	DOOR_OPEN(22105),

	/** 조도 범위 초과 */
	ILLUMI_OVER(22106),

	/** 조도 범위 미달 */
	ILLUMI_UNDER(22107),

	/** 습도 범위 초과 */
	HUMI_OVER(22108),

	/** 습도 범위 미달 */
	HUMI_UNDER(22109),

	/** 이산화탄소량 범위 초과 */
	CO2_OVER(22110),

	/** 이산화탄소량 범위 미달 */
	CO2_UNDEF(22111),

	NODE_AUTH_FAIL(23201), NODE_COLD_START(23202), NODE_WARM_START(23203), NODE_EGP_NEIGH_LOSS(23204);

	//
	//
	// tool.BaseDataMake 클래스 사용해서 base-data-alarmCode.txt 내용으로 만들어짐.
	//
	//

	private int alcdNo;

	ALARM_CODE(int alcdNo) {
		this.alcdNo = alcdNo;
	}

	public int getAlcdNo() {
		return alcdNo;
	}

}
