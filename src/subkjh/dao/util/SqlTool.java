package subkjh.dao.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fxms.bas.impl.dbo.all.FX_TBL_DEF;
import subkjh.bas.co.utils.DateUtil;
import subkjh.bas.co.utils.FileUtil;
import subkjh.dao.database.DataBase;
import subkjh.dao.database.MySql;
import subkjh.dao.def.Column;
import subkjh.dao.def.Sequence;
import subkjh.dao.def.Table;

public class SqlTool {

	public static void main(String[] args) throws Exception {

		Column.JAVA_FIELD_STYLE_OBJECT = true;

		SqlTool tool = new SqlTool();

//		tool.makeXml2JavaQid("deploy/conf/sql/handler/VupHandler.xml", "fems.vup.dao.VupHandlerQid", "tmp");
//		tool.makeXml2JavaQid("deploy/conf/sql/handler/UserHandler.xml", "fxms.bas.impl.dao.UserHandlerQid", "tmp");
//		tool.makeXml2JavaQid("deploy/conf/sql/handler/PsHandler.xml", "fxms.bas.impl.dao.PsHandlerQid", "tmp");
//		tool.makeXml2JavaQid("deploy/conf/sql/handler/MoHandler.xml", "fxms.bas.impl.dao.MoHandlerQid", "tmp");
//		tool.makeXml2JavaQid("deploy/conf/sql/handler/ModelHandler.xml", "fxms.bas.impl.dao.ModelHandlerQid", "tmp");
//		tool.makeXml2JavaQid("deploy/conf/sql/handler/InloHandler.xml", "fxms.bas.impl.dao.InloHandlerQid", "tmp");
//		tool.makeXml2JavaQid("deploy/conf/sql/api/value-api-db.xml", "fxms.bas.impl.api.ValueApiDbQid", "tmp");
//		tool.makeXml2JavaQid("deploy/conf/sql/vup/vup_test.xml", "fems.vup.dao.VupTestQid", "tmp");
//		tool.makeXml2JavaQid("deploy/conf/sql/vup/gemvax.xml", "fems.vup.dao.GemvaxQid", "tmp");
//		tool.makeXml2JavaQid("deploy/conf/sql/fems/fems.xml", "fems.bas.dao.FemsQid", "tmp");
//		tool.makeXml2JavaQid("deploy/conf/sql/cron/CheckACron.xml", "fxms.bas.impl.dao.CheckACronQid", "tmp");
//		tool.makeXml2JavaQid("deploy/conf/sql/vup/Tisp2VupCron.xml", "fems.vup.dao.Tisp2VupCronQid", "tmp");

//		tool.printDropSql(new MySql(), "datas/setup/tables.txt");
		tool.printCreateSql(new MySql(), "datas/tables.txt");
//		tool.printAddColumnSql(new MySql(), "datas/tables.txt");
//		tool.makeSelectSampleSql(new MySql(), "datas/setup/tables.txt");
//		tool.makeInsertSampleSql(new MySql(), "datas/setup/tables.txt");
//		tool.makeUpdateSampleSql(new MySql(), "datas/setup/tables.txt");
//		tool.makeJavaSource(new File("datas/tables.txt"), "fxms.bas.impl.dbo.all", "tmp");
//		tool.makeJpaSource(new File("datas/tables.txt"), "fxms.bas.impl.dbo.all", "tmp");
//		tool.makeDtoSource(new File("datas/setup/tables.txt"), "fxms.bas.impl.dto", "tmp");
//		tool.makeDtoSource(FX_UR_UGRP.class, "tmp");
//		tool.printXml(AlarmCfgVo.class);
//
//		tool.printInsertSql(new MySql(), "datas/datas.txt");
		// tool.printSequenceSql(new MySql(), "datas/setup/sequence.txt");

		// System.out.println( new MakeQueryXml().make(FX_BR_RULE.class));
	}

	/**
	 * 
	 * @param db
	 * @throws Exception
	 */
	public void makeCreateSql(DataBase database) throws Exception {
		File f = new File("datas/setup/tables.txt");
		String tablesSql = "datas/setup/create.sql";
		if (f.exists() == false) {
			System.err.println(f.getPath() + " 파일이 존재하지 않습니다.");
			return;
		}

		MakeTable p = new MakeTable(database);

		String ret = p.getCreateTableQuery(new SqlUtil().getTableList(f));
		FileUtil.writeToFile(tablesSql, ret, false);
		System.out.println(tablesSql + "이 생성되었습니다.");
	}

	public void makeInsertSql(DataBase database) throws Exception {

		File f = new File("datas/setup/datas.txt");
		String tablesSql = "datas/setup/insert.sql";
		if (f.exists() == false) {
			System.err.println(f.getPath() + " 파일이 존재하지 않습니다.");
			return;
		}

		StringBuffer sb = new StringBuffer();
		MakeTable p = new MakeTable(database);

		p.printInsertQuery(f, new SysdateStringAdapter(), new SqlAdapter() {
			@Override
			public void onSql(String sql) throws Exception {
				sb.append(sql);
				sb.append(";\n");
			}
		});
		FileUtil.writeToFile(tablesSql, sb.toString(), false);
		System.out.println(tablesSql + "이 생성되었습니다.");
	}

	public void printInsertSql(DataBase database, String fileNm) throws Exception {

		File f = new File(fileNm);
		if (f.exists() == false) {
			System.err.println(f.getPath() + " 파일이 존재하지 않습니다.");
			return;
		}

		MakeTable p = new MakeTable(database);

		p.printInsertQuery(f, new SysdateStringAdapter(), new SqlAdapter() {
			@Override
			public void onSql(String sql) throws Exception {
				System.out.println(sql + ";");
			}
		});
	}

	public void printDropSql(DataBase database, String fileNm) throws Exception {

		File f = new File(fileNm);
		if (f.exists() == false) {
			System.err.println(f.getPath() + " 파일이 존재하지 않습니다.");
			return;
		}

		MakeTable p = new MakeTable(database);

		p.printDropTableQuery(database, f);
	}

	public void makeJavaSource(File f, String packageName, String folder) throws Exception {
		
		
		if (f.exists() == false) {
			System.err.println(f.getPath() + " 파일이 존재하지 않습니다.");
			return;
		}

		SqlUtil util = new SqlUtil();
		List<Table> tabList = util.getTableList(f);
		for (Table tab : tabList) {
			if (tab.getName().startsWith("[삭제]"))
				continue;
			tab.setPackageName(packageName);
			util.writeJavaCode(tab, tab.getName(), folder);
		}

		Table2QidXml maker = new Table2QidXml();
		String sql = maker.make(tabList.toArray(new Table[tabList.size()]));

		System.out.println(sql);
	}

	public void printXml(Class<?> classOf) throws Exception {
		List<Table> tabList = FxTableMaker.getTableAll(classOf);
		Table2QidXml maker = new Table2QidXml();
		String sql = maker.make(tabList.toArray(new Table[tabList.size()]));
		System.out.println(sql);
	}

	public void makeJpaSource(File f, String packageName, String folder) throws Exception {
		if (f.exists() == false) {
			System.err.println(f.getPath() + " 파일이 존재하지 않습니다.");
			return;
		}

		SqlUtil util = new SqlUtil();

		List<Table> tabList = util.getTableList(f);
		for (Table tab : tabList) {
			if (tab.getName().startsWith("[삭제]"))
				continue;
			tab.setPackageName(packageName);
			util.writeJpaCode(tab, folder);
		}

	}

	public void makeDtoSource(File f, String packageName, String folder) throws Exception {
		if (f.exists() == false) {
			System.err.println(f.getPath() + " 파일이 존재하지 않습니다.");
			return;
		}

		SqlUtil util = new SqlUtil();

		List<Table> tabList = util.getTableList(f);
		for (Table tab : tabList) {
			if (tab.getName().startsWith("[삭제]"))
				continue;
			tab.setPackageName(packageName);
			util.writeDtoCode(tab, folder);
		}

	}

	public void makeDtoSource(Class<?> classOf, String folder) throws Exception {

		List<Table> tabList = FxTableMaker.getTableAll(classOf);
		SqlUtil util = new SqlUtil();

		for (Table tab : tabList) {
			if (tab.getName().startsWith("[삭제]"))
				continue;
			tab.setPackageName("");
			util.writeDtoCode(tab, folder);
		}

	}

	public void makeXml2JavaQid(String xmlFile, String javaClassName, String folder) {
		if (new File(xmlFile).exists() == false) {
			System.out.println("fild not found " + xmlFile);
		}
		new Xml2JavaQid().make(xmlFile, javaClassName, folder);
	}

	public void printCreateSql(DataBase database, String fileNm) throws Exception {
		File f = new File(fileNm);
		if (f.exists() == false) {
			System.err.println(f.getPath() + " 파일이 존재하지 않습니다.");
			return;
		}

		List<Table> tableList = new SqlUtil().getTableList(f);
		MakeTable p = new MakeTable(database);
		String ret = p.getCreateTableQuery(tableList);
		System.out.println(ret);
	}

	public void makeSelectSampleSql(DataBase database, String fileNm) throws Exception {
		File f = new File(fileNm);
		if (f.exists() == false) {
			System.err.println(f.getPath() + " 파일이 존재하지 않습니다.");
			return;
		}

		QueryMaker p = new QueryMaker();
		for (Table table : new SqlUtil().getTableList(f)) {
			System.out.println(p.getSelectSampleSql(table));
		}
	}

	public void makeInsertSampleSql(DataBase database, String fileNm) throws Exception {
		File f = new File(fileNm);
		if (f.exists() == false) {
			System.err.println(f.getPath() + " 파일이 존재하지 않습니다.");
			return;
		}

		QueryMaker p = new QueryMaker();
		for (Table table : new SqlUtil().getTableList(f)) {
			System.out.println(p.getInsertSampleSql(table));
		}
	}

	public void makeUpdateSampleSql(DataBase database, String fileNm) throws Exception {
		File f = new File(fileNm);
		if (f.exists() == false) {
			System.err.println(f.getPath() + " 파일이 존재하지 않습니다.");
			return;
		}

		QueryMaker p = new QueryMaker();
		for (Table table : new SqlUtil().getTableList(f)) {
			System.out.println(p.getUpdateSampleSql(table));
		}
	}

	public void printAddColumnSql(DataBase database, String fileNm) throws Exception {
		File f = new File(fileNm);
		if (f.exists() == false) {
			System.err.println(f.getPath() + " 파일이 존재하지 않습니다.");
			return;
		}

		MakeTable p = new MakeTable(database);
		String ret = p.getAddColumnQuery(new SqlUtil().getTableList(f));
		System.out.println(ret);
	}

	public void printSequenceSql(DataBase database, String fileNm) throws Exception {
		File f = new File(fileNm);
		if (f.exists() == false) {
			System.err.println(f.getPath() + " 파일이 존재하지 않습니다.");
			return;
		}

		Sequence sequence = null;

		// SEQ_NAME INC_VAL MIN_VAL MAX_VAL NEXT_VAL CYCLE_YN

		List<String> lineList = FileUtil.getLines(f);
		for (String line : lineList) {
			try {
				String ss[] = line.split("\t");
				sequence = new Sequence();
				sequence.setSequenceName(ss[0]);
				sequence.setIncBy(new Double(ss[1]).longValue());
				sequence.setValueMin(new Double(ss[2]).longValue());
				sequence.setValueMax(new Double(ss[3]).longValue());
				sequence.setValueNext(new Double(ss[4]).longValue());
				sequence.setCycle("Y".equalsIgnoreCase(ss[5]));

				System.out.println(database.getSqlCreate(sequence) + ";\n\n");
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	public void insert_FX_TBL(DataBase database, File f) {
		List<Table> tableList;
		List<FX_TBL_DEF> defList = new ArrayList<>();
		long dtm = DateUtil.getDtm();
		try {
			tableList = new SqlUtil().getTableList(f);

			for (Table tab : tableList) {
				FX_TBL_DEF def = new FX_TBL_DEF();
				def.setChgDtm(dtm);
				def.setRegDtm(dtm);
				def.setChgUserNo(0);
				def.setRegUserNo(0);
				def.setResevYn("Y");
				def.setTblCmnt(tab.getComment());
				def.setTblName(tab.getName());
				defList.add(def);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
