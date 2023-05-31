package subkjh.dao.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.FileUtil;
import subkjh.dao.exp.ParsingException;

/**
 * 쿼리를 분석하여 실제 사용할 쿼리와 인수명을 제공합니다.
 * 
 * @author subkjh
 * 
 */
public class SqlParser {

	public static void main(String[] args) throws Exception {
		String sql;
		// sql = " INSERT INTO IP_PERF_LANIF\n (" +
		// " T, PERF_TIME , DEVICE_ID , IF_ID , INBPS , OUTBPS , "
		// + " INPPS , OUTPPS , INEPS , INDPS , INUNIPS , OUTUNIPS ,"
		// +
		// " INNONUNIPS , OUTNONUNIPS , INBPS_USAGE, OUTBPS_USAGE, OUTEPS,
		// OUTDPS, TEST ) VALUES ("
		// + " 't', $perfTime, $deviceId, $ifId, $inbps, $outbps, -- 우리나라 \n" +
		// " $inpps, $outpps, $ineps, $indps, $inunips, $outunips,"
		// +
		// " $innonunips, $outnonunips, $ipbpsUsage, $outbpsUsage,
		// $outeps,$outdps, 'test' )";

		// sql =
		// "INSERT INTO #AAA(#{Field1}_aa,#{Field2aad},a,b,c,#d,#kk())values(
		// $field1,$field2,#a,#b,#c,$d,$kk())";
		sql = FileUtil.getString(new File("datas/test.sql"));

		// sql =
		// "CREATE INDEX CVSAS_STAT_ROUTE_CALL_D_#{YYYYMM}_DATE ON
		// CVSAS_STAT_ROUTE_CALL_D_#YYYYMM ( YYYYMMDD )";
		// sql = "DROP TABLE TABLE CVSAS_STAT_TID_CALL_#{TAG}_#{YYYYMMDD}";
		SqlParser parser = new SqlParser(sql);
		parser.parse();
		System.out.println(parser.getStrOrg());
		System.out.println(parser.getStrParsed());
		System.out.println(parser.getSqlVars());
	}

	/** 변수 목록 */
	private List<SqlVar> sqlVars;
	private int indexSingleQuotationMarks;
	// private int indexDoubleQuotationMarks;
	private int indexBrace = 0;
	/** 실제 쿼리 */
	private String strOrg;
	/** 사용할 쿼리 */
	private String strParsed = "";

	/**
	 * 
	 * @param strOrg
	 *            분설할 쿼리
	 */
	public SqlParser(String strOrg) {
		this.strOrg = strOrg;
		sqlVars = new ArrayList<SqlVar>();
	}

	/**
	 * @return the sqlVars
	 */
	public List<SqlVar> getSqlVars() {
		return sqlVars;
	}

	/**
	 * @return the str
	 */
	public String getStrOrg() {
		return strOrg;
	}

	/**
	 * @return the strParsed
	 */
	public String getStrParsed() {
		return strParsed;
	}

	/**
	 * 분석합니다.
	 * 
	 * @throws Exception
	 */
	public void parse() throws ParsingException {
		if (strOrg == null)
			return;

		char chs[] = strOrg.toCharArray();
		int index = 0;

		try {
			while (true) {
				index = getIndexNext(chs, index);
				if (index >= chs.length)
					break;
			}
		} catch (Exception e) {
			if (Logger.debug) {
				System.err.println(strOrg);
			}
			throw new ParsingException(e.getMessage());

		}

		if (indexSingleQuotationMarks > 0)
			throw new ParsingException(strOrg.substring(indexSingleQuotationMarks));

	}

	/**
	 * 배열에서 해당 문자가 있는 위치를 제공합니다.
	 * 
	 * @param chs
	 * @param ch
	 * @return
	 */
	private int getIndex(char chs[], int indexStart, char ch) {
		for (int i = indexStart; i < chs.length; i++)
			if (chs[i] == ch)
				return i;
		return -1;
	}

	private int getIndexNext(char chs[], int index) throws Exception {

		// 주석이면
		if (chs[index] == '-' && chs[index + 1] == '-') {
			int index2 = getIndex(chs, index, '\n');
			strParsed += "\n";
			if (index2 < 0)
				return chs.length;
			return index2 + 1;
		}

		if (chs[index] == '\\' && chs[index + 1] == '\\') {
			strParsed += chs[index + 2];
			return index + 3;
		}

		if (chs[index] == '$' || chs[index] == '#') {
			SqlVar sqlVar = new SqlVar();

			if (chs[index] == '#')
				sqlVar.type = SqlVar.VAR_TYPE_REPLACE;

			strParsed += chs[index] == '$' ? '?' : SqlVar.REPLACE_TAG;

			index++;
			indexBrace = 0;

			if (chs[index] == '{') {
				indexBrace = index;
				index++;
			}

			String var = "";

			for (; index < chs.length; index++) {

				if (isVarCharacter(chs[index])) {
					var += chs[index];
				} else {
					if (chs[index] == '(' && chs[index + 1] == ')') {
						index += 2;
						sqlVar.type |= SqlVar.VAR_TYPE_METHOD;
					} else if (chs[index] == '}') {
						if (indexBrace > 0) {
							index++;
							indexBrace = 0;
							// if (chs[index] == '(') continue;
						} else
							throw new Exception("index=" + index);
					}
					break;
				}
			}
			if (indexBrace > 0)
				new Exception("parsing error. CURLY-BRACKETS(" + strOrg.substring(0, indexBrace + 1) + ")");

			if (sqlVar.isMethod() == false)
				sqlVar.type |= SqlVar.VAR_TYPE_FIELD;

			sqlVar.name = var;
			sqlVars.add(sqlVar);

			return index;
		} else {
			char chLast = strParsed.length() > 0 ? strParsed.charAt(strParsed.length() - 1) : 0x00;

			// 연속해서 탭이나 공백이 있으면 하나만 유효합니다.
			if (chLast == ' ' || chLast == '\t' || chLast == '\n')
				if (chs[index] == ' ' || chs[index] == '\t')
					return index + 1;

			strParsed += chs[index];

			return index + 1;
		}
	}

	/**
	 * 변수명을 구성할 수 있는 문자열인지 여부
	 * 
	 * @param ch
	 * @return
	 */
	private boolean isVarCharacter(char ch) {
		return (ch >= 'a' && ch <= 'z') //
				|| (ch >= 'A' && ch <= 'Z') //
				|| (ch >= '0' && ch <= '9') //
				|| ch == '_';
	}

}
