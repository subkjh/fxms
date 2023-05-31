package subkjh.dao.util;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.FileUtil;
import subkjh.dao.QidDao;
import subkjh.dao.database.DataBase;
import subkjh.dao.def.Column;
import subkjh.dao.def.Column.COLUMN_OP;
import subkjh.dao.def.Index;
import subkjh.dao.def.Index.INDEX_TYPE;
import subkjh.dao.def.Query;
import subkjh.dao.def.Sequence;
import subkjh.dao.def.Table;
import subkjh.dao.def.View;
import subkjh.dao.exp.ColumnNotFoundException;
import subkjh.dao.model.DaoResult;
import subkjh.dao.model.SqlBean;
import subkjh.dao.model.SqlInitBean;
import subkjh.dao.model.SqlSelectBean;

public class SqlUtil {

	private int tableNo;

	/**
	 * 
	 * @param f
	 * @return
	 * @throws Exception
	 */
	public Map<Table, List<Object[]>> getDataList(File f) throws Exception {
		List<String> lineList = FileUtil.getLines(f);
		return getDataList(lineList);
	}

	public Map<Table, List<Object[]>> getDataList(List<String> lineList) throws Exception {
		List<Object[]> list = new ArrayList<Object[]>();
		for (String str : lineList) {
			list.add(str.split("\t"));
		}
		return getDataListObject(list);
	}

	public Map<Table, List<Object[]>> getDataListObject(List<Object[]> lineList) throws Exception {

		Table table = null;
		String step = "";
		Column column;
		String colunmName;
		Map<Table, List<Object[]>> dataMap = new HashMap<Table, List<Object[]>>();
		List<Object[]> dataList = null;

		for (Object ss[] : lineList) {

			if (ss.length < 2) {
				step = "";
				continue;
			}

			if (step.equals("table")) {
				for (int col = 0; col < ss.length; col++) {
					colunmName = getString(ss[col]);
					if (colunmName.trim().length() > 0) {
						if (colunmName.indexOf("삭제") < 0) {
							column = new Column();
							column.setColumnNo(col);
							column.setName(colunmName);
							table.addColumn(column);
						}
					}
				}
				step = "column";
			} else if ("table name".equalsIgnoreCase(getString(ss[0]))) {
				table = new Table();
				table.setName(getString(ss[1]));
				step = "table";
				table.setTableNo(dataMap.size());
				dataList = new ArrayList<Object[]>();
				dataMap.put(table, dataList);
			} else if (step.equals("column")) {
				dataList.add(ss);
			}
		}

		return dataMap;
	}

	/**
	 * 
	 * @param sheetList
	 * @return
	 * @throws Exception
	 */
	public List<Query> getQueryList(List<List<String>> sheetList) throws Exception {

		String tag = "init";

		List<Query> queryList = new ArrayList<Query>();
		Query query = null;
		for (List<String> colList : sheetList) {

			if (colList.size() < 1)
				continue;

			if (tag.equals("init")) {
				if ("Query Name".equals(colList.get(0))) {
					if (colList.size() > 2) {
						tag = "view";
						query = new Query();
						query.setName(colList.get(0));
						queryList.add(query);
					}
				}
			} else if (tag.equals("view")) {
				if ("Query".equals(colList.get(0))) {
					tag = "query";
				}
			} else if (tag.equals("query")) {
				// System.out.println(CClass.toString(cell));
				if (getString(colList.get(0)).equalsIgnoreCase("query end")) {
					tag = "init";
				} else {
					if (query.getQuery() == null) {
						query.setQuery(colList.get(0).trim());
					} else {
						query.setQuery(query.getQuery() + "\n" + colList.get(0).trim());
					}
				}
			}

		}

		// Collections.sort(viewList, new Comparator<View>() {
		//
		// @Override
		// public int compare(View o1, View o2) {
		// return o1.getName().compareTo(o2.getName());
		// }
		//
		// });

		return queryList;

	}

	/**
	 * 
	 * @param sheetList
	 * @return
	 * @throws Exception
	 */
	public List<Sequence> getSequenceList(List<List<String>> sheetList) throws Exception {

		String tag = "init";
		Sequence sequence = null;
		List<Sequence> sequenceList = new ArrayList<Sequence>();

		for (List<String> cells : sheetList) {

			if (cells.size() == 0)
				continue;

			if (tag.equals("init")) {
				if ("Sequence Name".equals(cells.get(0))) {
					tag = "sequence";
					continue;
				}

			} else if (tag.equals("sequence")) {

				if (cells.size() < 6)
					break;

				sequence = new Sequence();
				sequence.setSequenceName(cells.get(0));
				if (sequence.getSequenceName().startsWith("[삭제]"))
					continue;
				sequence.setValueMin(new Double(cells.get(3)).longValue());
				sequence.setValueMax(new Double(cells.get(4)).longValue());

				sequence.setIncBy(new Double(cells.get(2)).longValue());
				sequence.setCycle("Y".equalsIgnoreCase(cells.get(5)));

				sequenceList.add(sequence);

			}

		}

		Collections.sort(sequenceList, new Comparator<Sequence>() {

			@Override
			public int compare(Sequence o1, Sequence o2) {
				return o1.getSequenceName().compareTo(o2.getSequenceName());
			}

		});

		return sequenceList;

	}

	/**
	 * 
	 * @param database
	 * @return
	 * @throws Exception
	 */
	public List<Table> getTableAll(DataBase database) throws Exception {

		QidDao tran = database.createQidDao();
		try {
			tran.start();

			List<Table> tableList = new ArrayList<Table>();

			List<String> tabList = database.getTabNameList(tran, null);
			for (String tab : tabList) {
				tableList.add(database.getTable(tran, tab));
			}

			return tableList;

		} catch (Exception e) {
			tran.rollback();
			Logger.logger.error(e);
			throw new Exception("" + "\n" + e.getMessage());
		} finally {
			tran.stop();
		}
	}

	/**
	 * setup/deploy/conf/data.txt 형식을 이용한 테이블 목록 생성
	 * 
	 * @param f
	 * @return
	 * @throws Exception
	 */
	public List<Table> getTableList(File f) throws Exception {

		List<String> lineList = FileUtil.getLines(f);

		String tag = "table";
		Table table = new Table();
		List<Table> tabList = new ArrayList<Table>();
		String ss[];
		String tmp;

		for (String str : lineList) {

			ss = str.split("\t");

			if (ss.length < 3)
				continue;

			// if (tag.equals("table")) {

			if ("TBL Name".equals(ss[0])) {
				if (ss[1] == null || ss[1].trim().length() == 0)
					continue;
				if (ss[1].startsWith("[삭제]"))
					continue;
				if (ss.length != 4) {
					System.err.println(Arrays.toString(ss));
					continue;
				}

				table = new Table();
				table.setName(ss[1]);
				table.setComment(ss[3]);
				tabList.add(table);
				tag = "column";
				continue;
			}

			// } else

			if (tag.equals("column")) {

				if ("Comment".equals(ss[0]))
					continue;

				if (ss[0].startsWith("[삭제]"))
					continue;

				if ("Index Name".equals(ss[0])) {
					tag = "index";
					continue;
				}

				try {
					Column col = new Column();
					col.setComments(ss[0]);
					col.setName(ss[1].toUpperCase());
					col.setDataTypeDefined(ss[2].toLowerCase());
					if (ss.length > 3)
						col.setNullable(ss[3].equalsIgnoreCase("yes"));

					// 파일에 작성된 디폴트값의 따움표(')를 없앤다.
					if (ss.length > 4) {
						String ret = ss[4] == null || ss[4].trim().length() == 0 ? null
								: ss[4].trim().replaceAll("'|‘|’", "");
						col.setDataDefault(ret);
					}

					if (ss.length > 5) {
						tmp = ss[5].toLowerCase();
						if (tmp.equalsIgnoreCase("not update")) {
							col.setOperator(COLUMN_OP.insert);
						} else if (tmp.startsWith("sequence")) {
							col.setSequence(tmp.replaceAll("sequence", "").trim().toUpperCase());
						} else if (tmp.startsWith("ex")) {
							col.setExample(ss[5]);
						} else {
							col.setOperator(COLUMN_OP.all);
						}
					}
					table.addColumn(col);

				} catch (Exception e) {
					e.printStackTrace();
					throw e;
				}

			} else if (tag.equals("index")) {

				if (ss[0].startsWith("[삭제]"))
					continue;

				if (ss[0].startsWith("System")) {
					tag = "table";
				} else if (ss[0].trim().length() > 0) {
					Index idx = new Index(ss[0].trim(), INDEX_TYPE.getType(ss[1] == null ? "KEY" : ss[1].toString()));
					idx.setColumns(ss[2]);
					table.addIndex(idx);
				}
			}
		}

		for (Table tab : tabList) {
			try {
				tab.validate();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return tabList;
	}

	/**
	 * 
	 * @param sheetList
	 * @return
	 * @throws Exception
	 */
	public List<Table> getTableList4Excel(List<List<String>> sheetList) throws Exception {

		String tag = "table";
		Table table = null;
		List<Table> tabList = new ArrayList<Table>();
		String first;

		for (List<String> colList : sheetList) {

			if (colList.size() < 2)
				continue;

			first = colList.get(0);

			if (tag.equals("table")) {
				if ("Table Name".equals(colList.get(0)) && colList.size() == 4) {
					table = new Table();
					table.setName(colList.get(1));
					table.setComment(colList.get(3));
					tabList.add(table);
					tag = "column";
				}
			} else if (tag.equals("column")) {

				if ("Comment".equals(first))
					continue;
				if (first.startsWith("[삭제]"))
					continue;

				if ("Index Name".equals(colList.get(0))) {
					tag = "index";
					continue;
				}

				try {
					Column col = new Column();
					col.setComments(colList.get(0));
					col.setName(colList.get(1).toUpperCase());
					col.setDataTypeDefined(colList.get(2));
					col.setNullable(colList.get(3).equalsIgnoreCase("yes"));
					col.setDataDefault(colList.size() > 4 ? colList.get(4) : null);
					table.addColumn(col);
				} catch (Exception e) {
					System.out.println(colList);
					throw e;
				}

			} else if (tag.equals("index")) {

				if (first.startsWith("[삭제]"))
					continue;

				if (first.startsWith("System")) {
					tag = "table";
				} else if (first.trim().length() > 0) {
					Index idx = new Index(colList.get(0), INDEX_TYPE.getType(colList.get(1)));
					idx.setColumns(colList.get(2));
					table.addIndex(idx);
				}
			}

		}

		for (Table tab : tabList) {
			tab.validate();
		}

		return sort(null, tabList);
	}

	/**
	 * SQL을 이용한 쿼리 생성
	 * 
	 * @param f
	 * @return
	 * @throws Exception
	 */
	public List<Table> getTableList4Sql(List<String> lineList) throws Exception {

		Table table = null;
		List<Table> tabList = new ArrayList<Table>();
		String ss[];
		Column col;
		int pos;
		char chs[];

		for (String str : lineList) {
			if (str.trim().length() == 0) {
				table = null;
				continue;
			}
			chs = str.toCharArray();

			if (chs[0] == '-' || chs[0] == ' ') {
				table = null;
				continue;
			}

			if (str.startsWith("tablespace")) {
				table = null;
				continue;
			}

			if (str.startsWith("insert")) {
				table = null;
				continue;
			}

			if (str.startsWith("create table")) {
				table = new Table();
				ss = str.split(" ");
				table.setName(getValidName(ss[2].trim()));
				tabList.add(table);
				continue;
			}

			if (table != null) {
				ss = str.split(" ");
				col = new Column();
				col.setName(getValidName(ss[0]));
				col.setDataType(ss[1]);
				col.setNullable(str.indexOf("not null") >= 0 ? false : true);

				pos = str.indexOf("default");
				if (pos > 0) {
					col.setDataDefault(str.substring(pos + 7));
				}

				table.addColumn(col);
			}
		}

		setIndexes(tabList, lineList);

		setFk(tabList, lineList);

		for (Table tab : tabList) {
			tab.validate();
		}

		return tabList;
	}

	/**
	 * 
	 * @param sheetList
	 * @return
	 * @throws Exception
	 */
	public List<View> getViewList(List<List<String>> sheetList) throws Exception {

		String tag = "init";
		View view = null;
		List<View> viewList = new ArrayList<View>();

		for (List<String> colList : sheetList) {

			if (colList.size() < 1)
				continue;

			if (tag.equals("init")) {
				if ("View Name".equals(colList.get(0))) {
					if (colList.size() > 2) {
						tag = "view";
						view = new View();
						view.setName(colList.get(1));
						viewList.add(view);
					}
				}
			} else if (tag.equals("view")) {
				if ("Query".equals(colList.get(0))) {
					tag = "query";
				}
			} else if (tag.equals("query")) {
				if (getString(colList.get(0)).equalsIgnoreCase("query end")) {
					tag = "init";
				} else {
					if (view.getQuery() == null) {
						view.setQuery(getString(colList.get(0)).trim());
					} else {
						view.setQuery(view.getQuery() + "\n" + getString(colList.get(0)).trim());
					}
				}
			}

		}

		return viewList;

	}

	public List<Table> sort(DataBase db, List<Table> tabList) throws Exception {

		Collections.sort(tabList, new Comparator<Table>() {

			@Override
			public int compare(Table o1, Table o2) {
				return o1.getName().compareTo(o2.getName());
			}

		});

		List<String> nameList = new ArrayList<String>();
		for (Table tab : tabList) {
			nameList.add(tab.getName());
		}

		List<Table> sortedList = new ArrayList<Table>();

		List<String> createdTabList = new ArrayList<String>();
		List<Table> pList = sortCreateTable(db, tabList, createdTabList, sortedList, nameList);
		while (pList.size() > 0) {
			pList = sortCreateTable(db, pList, createdTabList, sortedList, nameList);
		}

		return sortedList;

	}

	public File writeJavaQid(String xmlFile, String javaClassName, String folder) {

		try {
			QidParser parser = new QidParser();
			parser.parse(xmlFile);
			return writeJavaQid(parser, xmlFile, javaClassName, folder);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public File writeJavaQid(InputStream is, String xmlFile, String javaClassName, String folder) {

		try {
			QidParser parser = new QidParser();
			parser.parse(is, null);
			return writeJavaQid(parser, xmlFile, javaClassName, folder);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	private File writeJavaQid(QidParser parser, String xmlFile, String javaClassName, String folder) {

		Collection<SqlBean> list;
		try {
			list = parser.getQidList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		List<SqlBean> sqlBeans = new ArrayList<SqlBean>();
		for (SqlBean bean : list)
			sqlBeans.add(bean);

		Collections.sort(sqlBeans, new Comparator<SqlBean>() {
			@Override
			public int compare(SqlBean o1, SqlBean o2) {
				return o1.getQid().compareTo(o2.getQid());
			}
		});

		String ret = "";

		DaoResult result;
		String remarks;
		SqlBean sqlBean;
		boolean isImport = false;

		for (int index = 0, size = sqlBeans.size(); index < size; index++) {

			sqlBean = sqlBeans.get(index);

			if (sqlBean instanceof SqlInitBean)
				continue;

			remarks = "";

			// remarks = sqlBean.getVars();

			result = sqlBean instanceof SqlSelectBean ? ((SqlSelectBean) sqlBean).getResultMap() : null;

			ret += "/**\n";
			ret += "* para : " + remarks + "<br>\n";
			if (result != null)
				ret += "* result : " + result.rID + "=" + result.javaClass.getName() + "<br>\n";

			while (true) {
				ret += "* ---------------------------------------------------------------------------------- <br>\n";
				ret += "* database : " + sqlBean.getDatabase() + "<br>\n";
				ret += "* sql <br><br>\n ";

				// if (sqlBean.getHeader() != null) {
				// ret += "*-------------------------------------------------------<br>\n";
				// ret += "* header : " + sqlBean.getHeader() + "<br>\n";
				// }
				//
				// ret += "* " + sqlBean.getSql().replaceAll("\n", "<br>") + " <br>\n";
				//
				// if (sqlBean.getTestList() != null) {
				// ret += "*-------------------------------------------------------<br>\n";
				// for (TestBean e : sqlBean.getTestList()) {
				// ret += "* test : " + e.toString().trim() + " : " + e.getSql().trim() +
				// "<br>\n";
				// }
				// }
				//
				// if (sqlBean.getFooter() != null) {
				// ret += "*-------------------------------------------------------<br>\n";
				// ret += "* footer : " + sqlBean.getFooter() + "<br>\n";
				// }

				if (sqlBeans.size() > index + 1 && sqlBeans.get(index + 1).getQid().equals(sqlBean.getQid())) {
					index++;
					sqlBean = sqlBeans.get(index);
				} else {
					break;
				}

			}

			ret += "*/\n";

			// if (sqlBean.hasTest()) {
			// StringBuffer para = new StringBuffer();
			// for (TestBean t : sqlBean.getTestList()) {
			// if (para.length() > 0)
			// para.append(", ");
			// para.append("\"" + t.getVar() + "\"");
			// }
			//
			// ret += "@RetreivePara(para={" + para + "})\n";
			// isImport = true;
			// }

			ret += "public final String " + sqlBean.getQid() + " = \"" + sqlBean.getQid() + "\";\n";
			ret += "\n";

		}

		String className = DaoUtil.getJavaClassSimpleName(javaClassName);

		String filename = xmlFile.replaceFirst("deploy/conf/sql/", "");

		// 3. DAO 만들기
		String daoContents = "package " + DaoUtil.getJavaPackageName(javaClassName) + ";\n";

		if (isImport)
			daoContents += "import subkjh.def.RetreivePara;\n";

		daoContents += "\n";
		daoContents += "/**" + "\n";
		daoContents += "* File : " + filename + "<br>" + "\n";
		daoContents += "* @since " + getDate() + "\n";
		daoContents += "* @author subkjh " + "\n";
		daoContents += "*\n";
		daoContents += "*/" + "\n";
		daoContents += "\n";
		daoContents += "\n";

		daoContents += "public class " + className + " { \n\n";
		daoContents += "/** 쿼리 모임 화일명. " + filename + "*/\n";
		daoContents += "public static final String QUERY_XML_FILE = \"" + filename + "\";\n\n";

		daoContents += "public " + className + "() { \n";
		daoContents += "} \n";
		daoContents += ret;

		daoContents += "}";

		String filenameJava = folder + File.separator + className + ".java";

		FileUtil.writeToFile(filenameJava, daoContents, false);

		return new File(filenameJava);
	}

	public String makeSql(Table table) throws Exception {
		Table2QidXml make = new Table2QidXml();
		return make.make(table);
	}

	public String makeSql(Class<?> classOf) throws Exception {
		Table2QidXml make = new Table2QidXml();
		return make.make(classOf);
	}

	public void writeJavaQid(File folder, String pkg) {
		String targetFolder;
		String name;
		for (File f : folder.listFiles()) {

			if (f.isDirectory()) {

				writeJavaQid(f, pkg + "." + f.getName());
			} else {
				if (f.getName().indexOf("xml") < 0)
					continue;

				targetFolder = "tmp/" + pkg.replaceAll("\\.", "/");

				name = f.getName().substring(0, f.getName().length() - 4);
				name = pkg + "." + DaoUtil.getJavaFieldName(name, true) + "Qid";
				System.out.println(targetFolder + "\t" + name);
				new File(targetFolder).mkdirs();
				try {
					new QidParser().parse(f.getPath());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void writeJavaCode(DataBase database, String tableName, String className, String folder) throws Exception {
		Table table = database.getTable(null, tableName);
		writeJavaCode(table, className, folder);
	}

	public void writeJavaCode(Table table, String javaClassName, String folder) throws Exception {

		table.setClassName(javaClassName);
		MakeJavaSource makeJavaSource = new MakeJavaSource();
		String ret = makeJavaSource.make(table);

		String filename = folder + File.separator + javaClassName.replaceAll("\\.", "\\" + File.separator) + ".java";

		File file = new File(filename);
		if (file.getParentFile().exists() == false) {
			file.getParentFile().mkdirs();
		}

		FileUtil.writeToFile(filename, ret, false);

		System.out.println(filename + " was made");
	}

	public void writeJpaCode(Table table, String folder) throws Exception {

		table.setClassName(DaoUtil.getJavaFieldName(table.getName(), true));

		MakeJpaSource makeJavaSource = new MakeJpaSource();
		String ret = makeJavaSource.make(table);

		String filename = folder + File.separator + table.getClassName() + ".java";

		File file = new File(filename);
		if (file.getParentFile().exists() == false) {
			file.getParentFile().mkdirs();
		}

		FileUtil.writeToFile(filename, ret, false);

		System.out.println(filename + " was made");
	}

	public void writeDtoCode(Table table, String folder) throws Exception {

		table.setClassName(DaoUtil.getJavaFieldName(table.getName(), true));

		MakeDtoSource makeJavaSource = new MakeDtoSource();
		String ret = makeJavaSource.make(table);

		String filename = folder + File.separator + table.getClassName() + "Dto.java";

		File file = new File(filename);
		if (file.getParentFile().exists() == false) {
			file.getParentFile().mkdirs();
		}

		FileUtil.writeToFile(filename, ret, false);

		ret = makeJavaSource.makeSwagger(table);
		filename = folder + File.separator + table.getClassName() + "Dto2.java";
		file = new File(filename);
		FileUtil.writeToFile(filename, ret, false);

		System.out.println(filename + " was made");

		makeJavaSource.printParameters(table);
	}

	private String getColumns(String s) {
		int pos = s.indexOf("(");
		int pos2 = s.indexOf(")");

		return s.substring(pos + 1, pos2).trim();
	}

	private String getFkColumns(String s) {
		int pos = s.indexOf("(");
		int pos2 = s.indexOf(")");
		String buf;
		String col;

		String fk = s.substring(pos + 1, pos2).trim();

		pos = s.indexOf("references");

		buf = s.substring(pos + 10);
		col = getColumns(buf);

		pos = s.indexOf("references");

		buf = buf.replaceFirst("EMS\\.", "");
		buf = buf.replaceFirst(col, "");
		buf = buf.replaceFirst("\\(\\);", "");

		fk = fk + ":" + col + "@" + buf;

		return fk;
	}

	private String getString(Object obj) {
		return obj == null ? "" : (obj instanceof String ? (String) obj : obj.toString().trim());
	}

	private String parseTableName(String s) {
		int pos = s.indexOf("on");
		int pos2 = s.indexOf("(");

		return s.substring(pos + 3, pos2).trim();
	}

	private String getValidName(String s) {
		s = s.replaceAll("\\(|,|;|\\)", "");
		return s;
	}

	private void setFk(List<Table> tableList, List<String> lineList) throws Exception {

		Map<String, Table> map = new HashMap<String, Table>();
		for (Table tab : tableList) {
			map.put(tab.getName(), tab);
		}

		Table table = null;
		Index idx;
		String ss[];
		char chs[];

		for (String str : lineList) {
			if (str.trim().length() == 0) {
				table = null;
				continue;
			}
			chs = str.toCharArray();

			if (chs[0] == '-' || chs[0] == ' ') {
				table = null;
				continue;
			}

			if (str.startsWith("alter table") && str.contains("foreign key")) {
				ss = str.split(" ");
				idx = new Index(ss[5].trim(), INDEX_TYPE.FK);
				table = map.get(getValidName(ss[2].trim()));
				idx.setColumns(getFkColumns(str));
				table.addIndex(idx);
				continue;
			}

		}

	}

	private void setIndexes(List<Table> tableList, List<String> lineList) throws Exception {

		Map<String, Table> map = new HashMap<String, Table>();
		for (Table tab : tableList) {
			map.put(tab.getName(), tab);
		}

		Table table = null;
		Index idx;
		String ss[];
		char chs[];

		for (String str : lineList) {
			if (str.trim().length() == 0) {
				table = null;
				continue;
			}
			chs = str.toCharArray();

			if (chs[0] == '-' || chs[0] == ' ') {
				table = null;
				continue;
			}

			if (str.startsWith("alter table")) {
				ss = str.split(" ");

				idx = new Index(ss[5].trim(), str.indexOf("primary") > 0 ? INDEX_TYPE.PK : INDEX_TYPE.KEY);
				table = map.get(getValidName(ss[2].trim()));
				idx.setColumns(getColumns(str));

				table.addIndex(idx);
				continue;
			}

			if (str.startsWith("create index")) {
				ss = str.split(" ");

				idx = new Index(ss[2].trim(), INDEX_TYPE.KEY);
				table = map.get(parseTableName(str));
				idx.setColumns(getColumns(str));
				table.addIndex(idx);
				continue;
			}

			if (str.startsWith("create unique index")) {
				ss = str.split(" ");
				idx = new Index(ss[3].trim(), INDEX_TYPE.UK);
				table = map.get(parseTableName(str));
				idx.setColumns(getColumns(str));
				table.addIndex(idx);
				continue;
			}
		}

	}

	private List<Table> sortCreateTable(DataBase db, List<Table> tabList, List<String> createdTabList,
			List<Table> sortedList, List<String> allList) throws Exception {

		List<Table> retList = new ArrayList<Table>();
		TAB: for (Table tab : tabList) {
			if (tab.isContainsFk()) {
				for (Index idx : tab.getIndexList()) {
					if (idx.isFk() && createdTabList.contains(idx.getFkTable()) == false) {
						retList.add(tab);
						if (allList.contains(idx.getFkTable()) == false) {
							if (db == null || (db != null && db.existTable(idx.getFkTable()) == false)) {
								throw new ColumnNotFoundException(idx.getFkTable(), idx.getFkColumn());
							}
						}
						continue TAB;
					}
				}
			}

			sortedList.add(tab);
			createdTabList.add(tab.getName());
			tab.setTableNo(tableNo);
			tableNo++;
		}

		return retList;
	}

	private static final SimpleDateFormat YYYYMMDDHHMMSS = new SimpleDateFormat("yyyy.MM.dd HH:mm");

	/**
	 * 눈으로 확인할 수 있는 시간 형식으로 넘긴다.
	 * 
	 * @return yyyyMMddHHmmss의 값
	 */
	String getDate() {
		return YYYYMMDDHHMMSS.format(new Date(System.currentTimeMillis()));
	}
}
