package com.fxms.ui.biz;

public class BizCode {

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

}
