package fxms.bas.alarm;

public class AoCode {

	public enum ClearReason {

		Auto(11)

		, AlarmCfgNotFound(12)

		, AlarmCfgMemNotFound(13)

		, AlarmCfgMemSize0(14)

		, AlarmCodeNotFoundForPsCode(15)

		, RemoveMo(1)

		, TimeOver(2)

		, Unmanaged(3)

		, ByUser(4);

		private int no;

		private ClearReason(int no) {
			this.no = no;
		}

		public int getNo() {
			return no;
		}

	}

	public enum AlarmLevel {

		Critical(1)

		, Major(2)

		, Minor(3)

		, Warning(4)

		, Event(5)

		, Clear(0);

		private int no;

		private AlarmLevel(int no) {
			this.no = no;
		}

		public int getNo() {
			return no;
		}

		public static AlarmLevel getLevel(String name) {
			for (AlarmLevel e : AlarmLevel.values()) {
				if (e.name().equalsIgnoreCase(name))
					return e;
			}
			return null;
		}

		public static AlarmLevel getLevel(int no) {
			for (AlarmLevel e : AlarmLevel.values()) {
				if (e.no == no)
					return e;
			}
			return null;
		}
	}

	public enum COMPARE_CODE {

		/** difference */
		DF

		/** rate */
		,RT

		,

		/** 감소 */
		DC,
		/** 같으면 */
		EQ,
		/* 보다 크거나 같으면 */
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
		OK;

		private COMPARE_CODE() {
		}

		public static COMPARE_CODE getCompare(String name) {
			for (COMPARE_CODE c : COMPARE_CODE.values()) {
				if (c.name().equalsIgnoreCase(name))
					return c;
			}

			return EQ;
		}

	}
}
