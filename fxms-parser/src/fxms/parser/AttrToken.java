package fxms.parser;

import java.util.HashMap;
import java.util.Map;

public class AttrToken extends Token {

	private String value;
	private String postAttr;
	private String screenTag;

	public String getScreenTag() {
		return screenTag;
	}

	public void setScreenTag(String screenTag) {
		this.screenTag = screenTag;
	}

	private Map<String, Object> appendPara;

	public Map<String, Object> getAppendPara() {
		if (appendPara == null) {
			appendPara = new HashMap<String, Object>();
		}
		return appendPara;
	}

	public AttrToken(String id, String text, String type) {
		super(id, text, type);
	}

	public String getPostAttr() {
		return postAttr;
	}

	public String getValue() {

		if ("number".equals(getType())) {
			return toNumber(value);
		} else if ("alpha-numeric".equals(getType())) {
			return toAlphaNumeric(value);
		} else if ("date".equals(getType())) {
			// TODO
		}

		return value;
	}

	public void setPostAttr(String postAttr) {
		this.postAttr = postAttr;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String toString() {
		return super.toString() + "=" + getValue();
	}
}
