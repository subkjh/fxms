package fxms.parser;

public class OrderToken extends Token {

	private String value;

	public OrderToken(String text, String value) {
		super("", text, "order");

		this.value = value;
	}

	public boolean desc() {
		return "-1".equals(value);
	}

}
