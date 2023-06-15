package fxms.bas.co;

/**
 * 
 * 
 * @author subkjh
 *
 */
public enum ALARM_CODE {
	/** 시험용 경보 */
	test_alarm("test.alarm", 10000),

	/** 관리대상 추가됨 */
	mo_added("mo.added", 10101),

	/** 관리대상 수정됨 */
	mo_updated("mo.updated", 10102),

	/** 관리대상 삭제됨 */
	mo_deleted("mo.deleted", 10103),

	/** 미등록된 관리대상 */
	mo_notfound("mo.notfound", 10104),

	/** 관리대상 오프라인 */
	mo_offline("mo.offline", 10105),

	/** 서비스 시작됨 */
	service_start("service.start", 10200),

	/** 서비스 큐 자료가 너무 많음 */
	service_queue_too_many("service.queue.too.many", 10201),

	/** 시스템내부오류 */
	fxms_error("fxms.error", 10900),

	/** 통계생성오류 */
	fxms_error_ps_stat("fxms.error.ps.stat", 10901),

	/** 통계아탑터실행오류 */
	fxms_error_ps_stat_adapter("fxms.error.ps.stat.adapter", 10902),

	/** CRON 작업 수행 중 오류 발생됨 */
	fxms_error_cron("fxms.error.cron", 10903),

	/** 성능항목 등록 안됨 */
	fxms_not_found_psitem("fxms.not.found.psitem", 10904),

	/** 미등록된 경보 코드 */
	fxms_not_found_alcdno("fxms.not.found.alcdno", 10905),

	/** 관제시스템 생성 알람 */
	fxms_extra_alarm_made("fxms.extra.alarm.made", 10911),

	/** 수집요청 응답 없음 */
	value_not_response("value.not.response", 11001),

	/** 수집요청 오류 응답 */
	value_error_response("value.error.response", 11002),

	/** 데이터 수집 안됨 */
	value_not_collected("value.not.collected", 11003),

	/** 수집데이터 확인 필요 */
	value_not_acceptable("value.not.acceptable", 11004),

	/** IQR 최대값 초과 */
	value_iqr_over("value.iqr.over", 11005),

	/** IQR 최소값 미달 */
	value_iqr_under("value.iqr.undef", 11006);

	private final int alcdNo;
	private final String alcdName;

	ALARM_CODE(String alcdName, int alcdNo) {
		this.alcdName = alcdName;
		this.alcdNo = alcdNo;
	}

	public String getAlcdName() {
		return alcdName;
	}

	public int getAlcdNo() {
		return alcdNo;
	}

	public static void main(String[] args) {

		String datas = "ALCD_NO	ALCD_NAME	ALCD_DISP_NAME\r\n" + "10000	test.alarm	시험용 경보\r\n"
				+ "10101	mo.added	관리대상 추가됨\r\n" + "10102	mo.updated	관리대상 수정됨\r\n"
				+ "10103	mo.deleted	관리대상 삭제됨\r\n" + "10104	mo.notfound	미등록된 관리대상\r\n"
				+ "10105	mo.offline	관리대상 오프라인\r\n" + "10200	service.start	서비스 시작됨\r\n"
				+ "10201	service.queue.too.many	서비스 큐 자료가 너무 많음\r\n" + "10900	fxms.error	시스템내부오류\r\n"
				+ "10901	fxms.error.ps.stat	통계생성오류\r\n" + "10902	fxms.error.ps.stat.adapter	통계아탑터실행오류\r\n"
				+ "10903	fxms.error.cron	CRON 작업 수행 중 오류 발생됨\r\n" + "10904	fxms.not.found.psitem	성능항목 등록 안됨\r\n"
				+ "10905	fxms.not.found.alcdno	미등록된 경보 코드\r\n" + "10911	fxms.extra.alarm.made	관제시스템 생성 알람\r\n"
				+ "11001	value.not.response	수집요청 응답 없음\r\n" + "11002	value.error.response	수집요청 오류 응답\r\n"
				+ "11003	value.not.collected	데이터 수집 안됨\r\n" + "11004	value.not.acceptable	수집데이터 확인 필요\r\n"
				+ "11005	value.iqr.over	IQR 최대값 초과\r\n" + "11006	value.iqr.under	IQR 최소값 미달\r\n";

		String list[] = datas.split("\n");

//			int index = 0;
		String ss[];
		for (String line : list) {
//				if (index++ == 0) {
//					continue;
//				}
//				System.out.println(line);

			ss = line.trim().split("\t");

			System.out.println("/** " + ss[2] + "*/");
			System.out.println(ss[1].replaceAll("\\.", "_") + "( \"" + ss[1] + "\", " + ss[0] + "),");
			System.out.println();

		}

	}

}
