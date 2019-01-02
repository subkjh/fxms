package fxms.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import fxms.parser.dao.ParserDao;

public abstract class TextParser {

	public static void main(String[] args) {
		String ss;

		ss = "경기파주HFC 관할 CMTS 장비 목록을 보여줘";
		ss = "분배센터별 품질통계 화면 보여줘";
		ss = "장비명이 AAS2D이고 포트번호 5인 것에 대한 최근 일주일 동안의 장애이력을 보여줘";
		ss = "교환망 장비조회 화면 띄워줘";
		ss = "경기파주HFC 관할 CMTS 장비 목록을 보여줘";
		ss = "경기파주HFC 관할 장비 AAA 상세정보 보여줘";
		ss = "장비명 AAS2D에 대한 지난 일주일 동안의 경보내역을 띄워줘";
		ss = "동작분배센터에서 장애를 가지고 있는 장비목록을 보여줘";
		ss = "인천 연수구 내 CMTS 장비목록 보여줘";
		ss = "장비ID 123123에 대한 장비 실장도 화면 보여줘";
		ss = "경기파주HFC 관할 장비명이 AAA인 장비상세정보 보여줘";
		ss = "장비ID 123123의 최근 10분 발생 현재장애 보여줘";
		ss = "장비명이 AAS2D이고 포트번호 5인 것에 대한 지난달 경보내역을 띄워줘";
		ss = "장애코드 1111에 대한 이번달 경보내역을 보여줘";
		ss = "장비명 AAAA이고 장애코드 1111에 대한 지난달 경보내역을 보여줘";
		ss = "장애코드 1111에 대한 오늘 경보내역을 보여줘";
		ss = "장애코드 1111에 대한 그제 경보내역을 보여줘";
		ss = "장애코드 1111에 대한 4일 전 경보내역을 보여줘";
		ss = "장비 개발서버 장비정보 보여줘~";
		ss = "교환기 KKK의 상세정보 보여줘";
		ss = "장애코드 1111에 대한 어제 경보내역을 보여줘";
		ss = "최근 10건 현재장애 보여줘";
		ss = "인천 연수구 내 교환기 장비목록 보여줘";
		ss = "장애코드 1111에 대한 4일 전 경보내역을 보여줘";
		ss = "장애코드 1111에 대한 4일 전 장애내역을 보여줘";
		ss = "장애 있는 장비목록 보여줘";
		ss = "장비명이 AAS2D이고 포트번호 5인 것에 대한 최근 일주일 동안의 경보내역을 띄워줘";
		ss = "장비 KKK의 상세 정보 보여줘";

		TextParser parser = new TextParser(ss) {

			@Override
			protected List<Map<String, Object>> selectQid(String qid) throws Exception {
				// TODO Auto-generated method stub
				return new ParserDao().selectList(qid);
			}

		};

		parser.show();
	}

	private List<String> remainWordList;
	private List<String> inputWordList;
	private List<ScreenToken> screenTokenList;
	private CommandToken commandToken;
	private TextPool textPool;

	private List<Token> tokenList = new ArrayList<Token>();

	protected abstract List<Map<String, Object>> selectQid(String qid) throws Exception;

	public TextParser(String sentence) {

		inputWordList = new ArrayList(Arrays.asList(sentence.split(" |\t|\\+|\\*|/")));
		remainWordList = new ArrayList<>(inputWordList);

		textPool = new TextPool() {
			@Override
			protected List<Map<String, Object>> selectQid(String qid) throws Exception {
				return TextParser.this.selectQid(qid);
			}
		};
		screenTokenList = getScreenToken();
		commandToken = getCommandToken();

		Token token = null;

		for (String s : remainWordList) {
			token = getToken(token, s);
			if (token != null) {
				tokenList.add(token);
			}
		}

		List<String> etcList = new ArrayList<String>();
		for (int i = tokenList.size() - 1; i >= 0; i--) {
			token = tokenList.get(i);
			if (token instanceof EtcWord) {
				etcList.add(((EtcWord) token).getText());
			}
		}

		if (etcList.size() > 1) {
			makeAttrFromEtc(etcList, tokenList);
		}
		if (etcList.size() > 0) {
			makeCountFromEtc(etcList, tokenList);
		}

	}

	public CommandToken getCommandToken() {
		CommandToken token = null;
		for (int i = 0; i < remainWordList.size(); i++) {
			token = (CommandToken) getReserveToken(textPool.getCommandList(), remainWordList.get(i));
			if (token != null) {
				remainWordList.remove(i);
				break;
			}
		}

		return token;
	}

	public List<ScreenToken> getScreenToken() {

		List<ScreenToken> screenList = new ArrayList<ScreenToken>();

		for (ScreenToken token : textPool.getScreenList()) {
			if (token.match(remainWordList)) {
				screenList.add(token);
			}
		}

		if (screenList.size() == 0) {
			for (ScreenToken token : textPool.getScreenList()) {
				if (token.startWith(remainWordList)) {
					screenList.add(token);
				}
			}
		}

		if (screenList.size() > 1) {
			// 많이 매칭된 화면을 앞으로 한다.
			screenList.sort(new Comparator<ScreenToken>() {
				@Override
				public int compare(ScreenToken o1, ScreenToken o2) {
					return o2.getMatchIndexList().size() - o1.getMatchIndexList().size();
				}
			});

			int maxMatch = screenList.get(0).getMatchIndexList().size();
			for (int i = 1; i < screenList.size(); i++) {
				if (screenList.get(i).getMatchIndexList().size() == maxMatch) {
					break;
				} else {
					screenList.remove(1);
				}
			}
		}

		return screenList;
	}

	private Token getAttr(String s) {

		return getReserveToken(textPool.getAttrList(), s);
	}

	private Token getReserveToken(List<? extends Token> list, String s) {

		for (Token e : list) {
			if (e.match(s)) {
				return e;
			}
		}

		return null;
	}

	private Token getTerm(String s) {
		for (TermToken token : textPool.getTermList()) {
			if (token.match(s)) {
				if (token.getValue() == null || token.getValue().length() == 0) {
					token.setValue(s);
				}
				return token;
			}
		}
		return null;
	}

	private Token getToken(Token prev, String s) {

		Token token;
		AttrToken at;

		// System.out.println(prev + ", " + s);

		if (prev instanceof AttrToken) {
			at = (AttrToken) prev;
			if (at.getValue() == null) {
				at.setValue(removePostPosition(s));
				return null;
			} else if (at.getPostAttr() != null && at.getPostAttr().length() > 0) {
				AttrToken attr = textPool.getAttr(at.getPostAttr());
				if (attr != null) {
					attr.setValue(removePostPosition(s));
					return attr;
				}
			}
		}

		token = getAttr(s);
		if (token != null) {
			return token;
		}

		token = getTerm(s);
		if (token != null) {
			return token;
		}

		token = getReserveToken(textPool.getOrderList(), s);
		if (token != null) {
			return token;
		}

		return new EtcWord(s);
	}

	private void makeAttrFromEtc(List<String> etcList, List<Token> tokenList) {

		if (etcList.size() > 1) {
			for (AttrToken token : textPool.getAttrList()) {
				if (match(etcList, token)) {
					tokenList.add(token);
				}
			}
		}
	}

	private void makeCountFromEtc(List<String> etcList, List<Token> tokenList) {

		System.out.println("makeCountFromEtc : " + etcList);

		if (etcList.size() > 0) {
			for (CountToken token : textPool.getCountList()) {
				if (match(etcList, token)) {
					tokenList.add(token);
				}
			}
		}
	}

	/**
	 * 목록 내용이 토큰과 일지하는지 여부를 판단하고 일지하면 목록에서 제거함.
	 * 
	 * @param list
	 * @param token
	 * @return
	 */
	private boolean match(List<String> list, Token token) {

		for (int i = 0; i < list.size(); i++) {
			if (token.match(list.get(i))) {
				list.remove(i);
				return true;
			}
		}

		String ss[] = token.getText().split(",");
		String word;
		boolean match;
		List<Integer> matchIndexList = new ArrayList<Integer>();

		for (String s : ss) {
			match = false;
			for (int i = 0; i < list.size(); i++) {
				word = removePostPosition(list.get(i));
				if (word.equals(s)) {
					match = true;
					matchIndexList.add(i);
					break;
				}
			}

			if (match == false) {
				return false;
			}
		}

		matchIndexList.sort(new Comparator<Integer>() {
			@Override
			public int compare(Integer arg0, Integer arg1) {
				return arg0.intValue() - arg1.intValue();
			}
		});

		for (int i = matchIndexList.size() - 1; i >= 0; i--) {
			list.remove(matchIndexList.get(i).intValue());
		}

		return true;
	}

	/**
	 * 
	 * @param s
	 * @return 조사가 제거된 단어
	 */
	private String removePostPosition(String s) {
		String newS = s;
		for (String post : textPool.getPostpositionList()) {
			if (post.length() <= newS.length()) {
				if (newS.substring(newS.length() - post.length()).equals(post)) {
					return newS.substring(0, newS.length() - post.length());
				}
			}
		}
		return newS;
	}

	public void show() {
		System.out.println("----------------------------------------------------------------------------");
		System.out.println(screenTokenList);
		System.out.println(commandToken);

		System.out.println("---");
		for (Token token : tokenList) {
			System.out.println(token);
		}

		System.out.println("---");

		System.out.println(textPool.makeParameters(screenTokenList, commandToken, tokenList));

		System.out.println("----------------------------------------------------------------------------");
	}

	public List<Map<String, Object>> getParameters() {
		return textPool.makeParameters(screenTokenList, commandToken, tokenList);
	}

	public void showPool() {
		textPool.show();
	}

}
