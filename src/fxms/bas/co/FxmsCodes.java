package fxms.bas.co;

public class FxmsCodes {

	public enum DATA_TYPE {

		VAR("var"), USER_PROPERTIES("user-properties"), USER_UI("user_ui"), CODE("code"), OP_CODE("op-code"),
		PS_ITEM("ps-item"), USER("user"), LOCATION("location"), ALARM_CFG("alarm-cfg"), ALARM_CODE("alarm-code"),
		CHART("chart"), MO("mo");

		private String code;

		private DATA_TYPE(String code) {
			this.code = code;
		}

		public String getCode() {
			return code;
		}
	}

	public enum OP_TYPE {

		Group(0), Ui(1), List(2), Add(3), Update(4), Delete(5), Dx(6);

		private int code;

		private OP_TYPE(int code) {
			this.code = code;
		}

		public int getCode() {
			return code;
		}

		public static OP_TYPE getOpType(int code) {
			for (OP_TYPE e : OP_TYPE.values()) {
				if (code == e.getCode()) {
					return e;
				}
			}

			return Ui;
		}
	}
}
