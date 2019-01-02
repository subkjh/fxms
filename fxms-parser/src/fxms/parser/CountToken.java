package fxms.parser;

public class CountToken extends AttrToken {

	public CountToken(String id, String text) {
		super(id, text, "number");
	}

	@Override
	public boolean match(String s) {
		
		String newS = removeNumber(s);
		
		if ( super.match(newS) ) {
			setValue(s);
			return true;
		}
		
		return false;
	}

}
