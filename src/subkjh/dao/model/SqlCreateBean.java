package subkjh.dao.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 테이블, 인덱스 생서용 SQL
 * 
 * @author subkjh
 * 
 */
public class SqlCreateBean extends SqlBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4590813581452536311L;

	private static final String PATTERN = "( |\t|\n|\\()+";

	public static void main(String[] args) {

		String s = "create table aaa(aaa, bbb, ccc";
		s = "create index aaa on table ( aaa ";

		String ss[] = s.split(PATTERN);

		for (String s1 : ss) {
			if (s1.trim().length() == 0)
				continue;
			System.out.println(s1);
		}

	}

	public SqlCreateBean(String qid, String database) {
		super(qid, database);
	}

	/**
	 * 쿼리를 이용하여 어떤 객체를 생성하는지 알아냅니다.<br>
	 * 
	 * 
	 * @param obj
	 * @return 크기가 2인 배열<br>
	 *         index 0 : 객체명<br>
	 *         index 1 : 객체종류<br>
	 * @throws Exception
	 */
	public Map<String, String> getNameAndType(Object obj) throws Exception {

		Map<String, String> map = new HashMap<String, String>();

		QueryResult esql = getOneSql(obj);

		String sql = esql.getSql();

		String ss[] = sql.split(PATTERN);
		if (ss[0].equalsIgnoreCase("create")) {
			if (ss[1].equalsIgnoreCase("index")) {
				map.put("index", ss[2]);
				map.put("table", ss[4]);
			} else if (ss[1].equalsIgnoreCase("table")) {
				map.put("table", ss[2]);
			}
		}

		return map;

	}

	@Override
	protected String getXmlTag() {
		return "create";
	}

}
