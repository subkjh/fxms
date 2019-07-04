package subkjh.bas.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.log.ProgressLogger;
import subkjh.bas.dao.control.DbTrans;
import subkjh.bas.dao.database.DBManager;
import subkjh.bas.dao.database.DataBase;

public class SqlRunner {

	public static void main(String[] args) {

		// TROO.debug = true;
		Logger.logger = Logger.createLogger(".", "sqlrunner");
		Logger.logger.setPrintOutConsole(false);
		Logger.logger.setLevel(LOG_LEVEL.info);

		try {
			DBManager.getMgr().addDataBase(new File("deploy/conf/databases.xml"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		SqlRunner sql = new SqlRunner();

		sql.run();
	}

	private DataBase database;
	private DbTrans tran;
	private boolean isContinue;

	public SqlRunner() {
	}

	public void run() {
		String command = null;
		Scanner sc = new Scanner(System.in);
		isContinue = true;
		StringBuffer sb = new StringBuffer();

		while (isContinue) {

			System.out.print("FX: ");
			command = sc.nextLine();

			if (command == null || command.length() == 0) {
				continue;
			}

			if (command.charAt(command.length() - 1) == ';') {
				sb.append(command.substring(0, command.length() - 1));
				run(sb.toString());
				sb = new StringBuffer();
			} else {
				sb.append(command);
				sb.append("\n");
			}
		}

		sc.close();

		System.out.println("exit " + this.getClass().getSimpleName());
	}

	private List<String> cmdList = new ArrayList<String>();

	private void run(String command) {
		String sql = command.trim();

		if (cmdList.size() > 10) {
			cmdList.remove(0);
		}

		printCommand(command);

		try {
			if (sql.startsWith("open")) {
				open(sql.substring(4).trim());
			} else if (sql.startsWith("exit")) {
				close();
				isContinue = false;
			} else if (sql.startsWith("close")) {
				close();
			} else if (sql.startsWith("select")) {
				select(sql);
			} else if (sql.startsWith("history")) {
				showHistory(sql);
				return;
			} else if (sql.startsWith("desc") || sql.startsWith("show")) {
				select(sql);
			} else {
				execute(sql);
			}

			cmdList.add(command);

		} catch (Exception e) {
			printError(e.getMessage());
		}

	}

	private void showHistory(String s) throws Exception {
		String ss[] = s.split(" ");

		if (ss.length == 1) {
			for (int i = 0; i < cmdList.size(); i++) {
				printResult(i + ") " + cmdList.get(i));
			}
		} else if (ss.length == 2) {
			run(cmdList.get(Integer.parseInt(ss[1])));
		}
	}

	private void select(String sql) throws Exception {
		List<Map<String, Object>> mapList = tran.selectSql2Map(sql, null);
		ProgressLogger.print(mapList, 50);
	}

	private void execute(String sql) throws Exception {

		int ret = tran.executeSql(sql, null);

		System.out.println(
				"---------------------------------------------------------------------------------------------");
		System.out.println(ret);
		System.out.println(
				"---------------------------------------------------------------------------------------------");

	}

	private void close() {
		if (tran != null) {
			tran.stop();
			tran = null;
			printResult("closed " + database.getName());
		}

	}

	private void open(String name) {

		close();

		database = DBManager.getMgr().getDataBase(name);

		tran = database.createDbTrans();

		try {
			tran.start();
			printResult("opened " + name);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void printResult(Object obj) {
		System.out.println("   | " + obj);
	}

	private void printError(Object obj) {
		System.out.println("  e| " + obj);
	}

	private void printCommand(Object obj) {
		System.out.println("  >> " + obj);
	}
}
