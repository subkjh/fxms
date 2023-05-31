package subkjh.bas.fao;

import java.util.ArrayList;
import java.util.List;

public abstract class FaoParser extends BasFileParser<List<String>> {

	@Override
	protected List<String> parse(String line) throws Exception {
		StringBuffer sb = new StringBuffer();
		List<String> list = new ArrayList<String>();
		char chs[] = line.toCharArray();
		for (int i = 0; i < chs.length; i++) {
			if (chs[i] == '\\') {
				i++;
				sb.append(chs[i]);
			} else if (chs[i] == '|') {
				list.add(sb.toString());
				sb = new StringBuffer();
			} else {
				sb.append(chs[i]);
			}
		}

		list.add(sb.toString());

		return list;
	}
}
