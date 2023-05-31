package fxms.bas.co;

import java.util.Calendar;

/**
 * 공통 코드
 * 
 * @author subkjh
 *
 */
public class CoCode {

	/**
	 * ACCS_ST_CD 접속상태코드
	 * 
	 * @author subkjh
	 *
	 */

	public enum ACCS_ST_CD {
		/** 접속중 */
		LOGIN("0")

		/** 정상접속종료 */
		, LOGOUT_OK("1")

		/** 세션종료 */
		, LOGOUT_SESSION_CLOSE("3")

		/** 타임아웃접속종료 */
		, LOGOUT_TIMEOUT("2");

		private String code;

		private ACCS_ST_CD(String code) {
			this.code = code;
		}

		public String getCode() {
			return code;
		}
	}

	/**
	 * 알람 등급
	 * 
	 * @author subkjh
	 *
	 */
	public enum ALARM_LEVEL {

		Clear(9)

		, Critical(1)

		, Event(5)

		, Major(2)

		, Minor(3)

		, Warning(4);

		public static ALARM_LEVEL getLevel(int no) {
			for (ALARM_LEVEL e : ALARM_LEVEL.values()) {
				if (e.no == no)
					return e;
			}
			return null;
		}

		public static ALARM_LEVEL getLevel(String name) {
			for (ALARM_LEVEL e : ALARM_LEVEL.values()) {
				if (e.name().equalsIgnoreCase(name))
					return e;
			}
			return null;
		}

		private int no;

		private ALARM_LEVEL(int no) {
			this.no = no;
		}

		public int getAlarmLevel() {
			return no;
		}
	}

	/**
	 * 알람해제사유코드
	 * 
	 * @author subkjh
	 * @since 2022.03.31
	 *
	 */
	public enum ALARM_RLSE_RSN_CD {

		/** 운영자에 의한 수동해제 */
		ByUser("PU")

		/**
		 * 알람조건없음
		 */
		, NotFoundAlarmCfg("AC")

		/**
		 * 객체에서 자동 해제
		 */
		, Release("AR")

		/** 관리대상 없어짐 */
		, RemoveMo("AX")

		/** 시간경과자동해제 */
		, TimeOver("AT")

		/** 관리대상 비관리로 전환됨 */
		, Unmanaged("AU")
		
		/** 알람등급변경 */
		, ChangeAlarmLevel("AL")
		;

		private String cd;

		private ALARM_RLSE_RSN_CD(String cd) {
			this.cd = cd;
		}

		public String getCd() {
			return cd;
		}
		
	
	}

	/**
	 * 게시판구분코드
	 * 
	 * @author subkjh
	 *
	 */
	public enum BOARD_CL_CD {

		/** 일반 게시 */
		Normal("N0");

		public static BOARD_CL_CD get(String code) {
			for (BOARD_CL_CD e : BOARD_CL_CD.values()) {
				if (e.code.equals(code)) {
					return e;
				}
			}

			return null;
		}

		private String code;

		private BOARD_CL_CD(String code) {
			this.code = code;
		}

		public String getCode() {
			return code;
		}
	}

	/**
	 * 
	 * @author subkjh<br>
	 *         비교코드
	 *
	 */
	public enum CMPR_CD {

		/** 감소 */
		DC,
		/** difference */
		DF,
		/** 같으면 */
		EQ,
		/** 보다 크거나 같으면 */
		GE,
		/** 보다 크면 */
		GT,
		/** 증가 */
		IC,
		/** 보다 작거나 같으면 */
		LE,
		/** 보다 작으면 */
		LT,
		/** 같지 않으면 */
		NE,
		/** 비교하지 않음 */
		NO,
		/** 무조건 */
		OK,
		/** rate */
		RT;

		public static CMPR_CD getCompare(String name) {
			for (CMPR_CD c : CMPR_CD.values()) {
				if (c.name().equalsIgnoreCase(name))
					return c;
			}

			return EQ;
		}

		private CMPR_CD() {
		}

	}

	/**
	 * 생성상태코드
	 * 
	 * @author subkjh
	 *
	 */
	public enum CRE_ST_CD {

//		/** 대기중 */
//		Waiting("W"),	

		/** 완료 */
		Completed("C"),
		/** 생성중 */
		Processing("P"),
		/** 준비중 */
		Ready("R");

		public static CRE_ST_CD get(String code) {
			for (CRE_ST_CD e : CRE_ST_CD.values()) {
				if (e.code.equals(code)) {
					return e;
				}
			}

			return null;
		}

		private String code;

		private CRE_ST_CD(String code) {
			this.code = code;
		}

		public String getCode() {
			return code;
		}
	}

	/**
	 * 요일 코드
	 * 
	 * @author subkjh
	 *
	 */
	public enum DOW_CD {
		FRI(Calendar.FRIDAY), MON(Calendar.MONDAY), SAT(Calendar.SATURDAY), SUN(Calendar.SUNDAY),
		THUR(Calendar.THURSDAY), TUE(Calendar.TUESDAY), WED(Calendar.WEDNESDAY);

		public static DOW_CD getDOW_CD(int no) {
			for (DOW_CD e : DOW_CD.values()) {
				if (e.no == no) {
					return e;
				}
			}
			return null;
		}

		int no;

		private DOW_CD(int no) {
			this.no = no;
		}

		public int getNo() {
			return no;
		}
	}

	/**
	 * FX서비스상태코드
	 * 
	 * @author subkjh
	 *
	 */
	public enum FXSVC_ST_CD {
		ADDED, INIT, ON;
	}

	/**
	 * 인덱스유형코드
	 * 
	 * @author subkjh
	 *
	 */

	public enum IDX_TYPE_CD {
		FK, KEY, PK, UK;
	}

	/**
	 * 관리대상의 ONLINE 상태
	 * 
	 * @author subkjh
	 *
	 */
	public enum MO_STATUS {

		Offline(0), Online(1);

		public static MO_STATUS get(int no) {
			for (MO_STATUS e : MO_STATUS.values()) {
				if (e.no == no) {
					return e;
				}
			}
			return null;
		}

		private int no;

		private MO_STATUS(int no) {
			this.no = no;
		}

		public int getNo() {
			return no;
		}
	}

	/**
	 * MO작업유형코드
	 * 
	 * @author subkjh
	 *
	 */
	public enum MO_WORK_TYPE_CD {

		Add("A"), Delete("D"), Sync("S"), Update("C");

		public static MO_WORK_TYPE_CD get(String code) {
			for (MO_WORK_TYPE_CD e : MO_WORK_TYPE_CD.values()) {
				if (e.code.equals(code)) {
					return e;
				}
			}

			return null;
		}

		private String code;

		private MO_WORK_TYPE_CD(String code) {
			this.code = code;
		}

		public String getCode() {
			return code;
		}
	}

	public enum NEW_USER_REG_ST_CD {

		Approval("A"), Init("I"), Processing("P"), Reject("R");

		public static NEW_USER_REG_ST_CD get(String code) {
			for (NEW_USER_REG_ST_CD e : NEW_USER_REG_ST_CD.values()) {
				if (e.code.equals(code)) {
					return e;
				}
			}

			return null;
		}

		private String code;

		private NEW_USER_REG_ST_CD(String code) {
			this.code = code;
		}

		public String getCode() {
			return code;
		}
	}

	/**
	 * OP_TYPE_CD 기능유형코드
	 */
	public enum OP_TYPE_CD {
		/** Dashboard */
		DASHBOARD,
		/** 삭제 */
		DELETE,
		/** 추가 */
		INSERT,
		/** 대표 */
		MAIN,
		/** 메뉴 */
		MENU,
		/** 통보 */
		NOTIFY,
		/** 조회 */
		SELECT,
		/** 수정 */
		UPDATE
	}

	/**
	 * 질문구분코드
	 * 
	 * @author subkjh
	 *
	 */
	public enum QUEST_CL_CD {

		/** 자주하는질문 */
		Frequency("FQ"),

		/** 일반질문 */
		Normal("NQ");

		public static QUEST_CL_CD get(String code) {
			for (QUEST_CL_CD e : QUEST_CL_CD.values()) {
				if (e.code.equals(code)) {
					return e;
				}
			}

			return null;
		}

		private String code;

		private QUEST_CL_CD(String code) {
			this.code = code;
		}

		public String getCode() {
			return code;
		}
	}

	public enum THREAD_STATUS {

		/** 초기화중 */
		Init(false),

		/** queue 처리중 */
		Queueing(false),

		/** 준비 중 */
		Ready(false),

		/** 처리 중 */
		Running(false),

		/** Slept */
		Slept(false),

		/** 종료됨 */
		Stopped(true),

		/** 종료 중 */
		Stopping(true),

		/** 대기 중 */
		Waiting(false);

		private boolean finished;

		THREAD_STATUS(boolean finished) {
			this.finished = finished;
		}

		public boolean isFinished() {
			return finished;
		}

	}

	public enum VAR_TYPE_CD {

		Element("E"), Number("N"), String("S")

		;

		public static VAR_TYPE_CD get(String code) {
			for (VAR_TYPE_CD e : VAR_TYPE_CD.values()) {
				if (e.code.equals(code)) {
					return e;
				}
			}

			return null;
		}

		private String code;

		private VAR_TYPE_CD(String code) {
			this.code = code;
		}

		public String getCode() {
			return code;
		}
	}

}
