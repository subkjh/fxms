package fxms.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Token {

	public static String removeNumber(String s) {

		if (s == null) {
			return "";
		}

		char chs[] = s.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (char ch : chs) {
			if (ch >= '0' && ch <= '9') {
			} else {
				sb.append(ch);
			}
		}

		return sb.toString();
	}

	public static String toAlphaNumeric(String s) {

		if (s == null) {
			return "";
		}

		char chs[] = s.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (char ch : chs) {
			if (ch >= '0' && ch <= '9') {
				sb.append(ch);
			} else if (ch >= 'a' && ch <= 'z') {
				sb.append(ch);
			} else if (ch >= 'A' && ch <= 'Z') {
				sb.append(ch);
			}
		}

		return sb.toString();
	}

	public static String toNumber(String s) {

		if (s == null) {
			return "";
		}

		char chs[] = s.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (char ch : chs) {
			if (ch == '-' || ch == '+') {
				sb.append(ch);
			}
			if (ch >= '0' && ch <= '9') {
				sb.append(ch);
			}
		}

		return sb.toString();
	}

	private String id;
	private String text;
	private String type;

	/**
	 * 
	 * @param id
	 * @param text
	 * @param type
	 */
	public Token(String id, String text, String type) {
		this.id = id;
		this.text = text;
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public String getText() {
		return text;
	}

	public String getType() {
		return type;
	}

	public boolean match(String s) {
		int i = 0;

		if (match(s, "")) {
			return true;
		}

		List<String> postpositionList = TextPool.textPool.getPostpositionList();

		while (i < postpositionList.size()) {
			if (match(s, postpositionList.get(i))) {
				return true;
			}
			i++;
		}

		return false;
	}

	public boolean match(String s, String postPosition) {

		String word = text + postPosition;

		if (s.equals(word)) {
			return true;
		}

		return word.replaceAll(",", "").equals(s);
	}

	public boolean matchString(String defineString, String inputString) {

		if (defineString.equals(inputString)) {
			return true;
		}

		List<String> postpositionList = TextPool.textPool.getPostpositionList();

		for (String postposition : postpositionList) {
			if (inputString.equals(defineString + postposition)) {
				return true;
			}
		}

		return false;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String toString() {
		return getClass().getSimpleName() + ":" + id + "/" + type + "/" + text;
	}

	

	private void make(String ss[], char tmp[], List<String> wordList) {

		char chs[] = new char[ss.length];
		for (int i = 0; i < ss.length - tmp.length; i++) {
			chs[i] = '0';
		}
		System.arraycopy(tmp, 0, chs, chs.length - tmp.length, tmp.length);

		String word = makeWord(chs, ss);
		if (word != null && wordList.contains(word) == false) {
			wordList.add(word);
		}

		// synonym 처리
		
		List<String> synonymList;
		for (int i = 0; i < ss.length; i++) {
			synonymList = TextPool.textPool.getSynonym(ss[i]);
			if (synonymList != null) {

				String newSs[] = Arrays.copyOf(ss, ss.length);

				for (String synonym : synonymList) {
					newSs[i] = synonym;
					word = makeWord(chs, newSs);
					if (word != null && wordList.contains(word) == false) {
						wordList.add(word);
					}
				}
			}
		}

	}
	private List<String> wordList = null;

	private String makeWord(char chs[], String ss[]) {
		StringBuffer sb = new StringBuffer();
		sb.append(ss[0]);
		for (int i = 1; i < ss.length; i++) {
			if (chs[i] == '0') {
				sb.append(",");
			}
			sb.append(ss[i]);
		}
		return sb.toString();
	}

	protected List<String> getWordList() {

		if (wordList == null) {

			wordList = new ArrayList<String>();
			String ss[] = getText().split(",");
			int i = (int) Math.pow(2, ss.length) - 1;
			while (i >= 0) {
				char chs[] = Integer.toBinaryString(i).toCharArray();
				make(ss, chs, wordList);
				i--;
			}

		}

		return wordList;

	}

}
