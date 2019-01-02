package fxms.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SynonymPool extends HashMap<String, List<String>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1375020284636635882L;

	public static void main(String[] args) {
		SynonymPool pool = new SynonymPool();
		pool.add("경보,장애");
		pool.add("목록,리스트,list");
		System.out.println(pool);
	}
	
	public void add(String str) {
		
		String ss[] = str.split(",");

		List<String> list;

		String s;

		for (int i = 0; i < ss.length; i++) {

			s = ss[i];

			list = get(s);
			if (list == null) {
				list = new ArrayList<String>();
				put(s, list);
			}

			for (int n = 0; n < ss.length; n++) {
				if (i != n) {
					if (list.contains(ss[n]) == false) {
						list.add(ss[n]);
					}
				}
			}

		}
	}

}
