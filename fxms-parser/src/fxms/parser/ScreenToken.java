package fxms.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScreenToken extends Token {


	public ScreenToken(String id, String text) {
		super(id, text, "screen");
	}

	public boolean isTag(String screenTag) {

		if (screenTag == null) {
			return true;
		}
		String dstTags[] = screenTag.split(",");

		List<String> list = getWordList();
		String ss[];
		for (String s : list) {
			ss = s.split(",");
			for (String tag : ss) {
				for (String tag2 : dstTags) {
					if (tag.equals(tag2)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private List<Integer> matchIndexList = new ArrayList<Integer>();
	
	public List<Integer> getMatchIndexList() {
		return matchIndexList;
	}

	public boolean match(List<String> inputWordList) {

		List<String> list = getWordList();
		String ss[];
		String inputWord;
		int matchCount;

		for (String s : list) {

			ss = s.split(",");
			matchCount = 0;
			matchIndexList.clear();
			
			for (String defineWord : ss) {
				for (int index = 0; index < inputWordList.size(); index++) {
					inputWord = inputWordList.get(index);
					if (matchString(defineWord, inputWord)) {
						matchIndexList.add(index);
						matchCount++;
						break;
					}
				}
			}

			if (matchCount == ss.length) {
				
				System.out.println(inputWordList + ", ");
				System.out.println(matchIndexList);
				
				return true;
			}

		}

		return false;
	}

	public boolean startWith(List<String> inputWordList) {

		List<String> list = getWordList();
		String ss[];
		int matchCount;

		for (String s : list) {

			ss = s.split(",");
			matchCount = 0;

			for (String defineWord : ss) {
				for (String inputWord : inputWordList) {
					if (inputWord.startsWith(defineWord)) {
						matchCount++;
						break;
					}
				}
			}

			if (matchCount == ss.length) {
				return true;
			}

		}

		return false;
	}

}
