package tool.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import fxms.bas.api.PsApi;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.service.AppServiceImpl;
import fxms.bas.fxo.service.WebServiceImpl;
import fxms.bas.impl.dbo.all.FX_MO_FXSERVICE;
import fxms.bas.vo.FxTableVo;
import fxms.bas.vo.PsItem;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;
import subkjh.dao.util.FxTableMaker;

public class ApiTest {

	JSch jsch;
	Session session;

	public static void main(String[] args) {

		FxCfg.DB_CONFIG = "VUPDB";
		FxCfg.DB_PSVALUE = FxCfg.DB_CONFIG;
		FxCfg.isTest = true;
		Logger.logger.setLevel(LOG_LEVEL.trace);
		ApiTest test = new ApiTest();
//		test.go();
		test.testTable();

	}

	public void testMoApi() {
		try {
			Map<String, Object> para = new HashMap<String, Object>();
//			para.put("fxsvrIpAddr", FxCfg.getCfg().getIpAddress());
//			para.put("moClass", FxServiceMo.MO_CLASS);
			para.put("mngYn", "Y");

//			List<FxServerMo> list = MoApi.getApi().getMoList(para, FxServerMo.class);
//			for (FxServerMo mo : list) {
//				System.out.println(mo);
//			}

//			MoLocation loc = MoApi.getApi().getMoLocation(0);
//			MoApi.getApi().setInloMember();
//			List<MoLocationMem> memList = new ArrayList<MoLocationMem>();
//
//			for (MoLocation child : loc.getChildren()) {
//				memList.addAll(child.makeMemList());
//			}
//			System.out.println(memList);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void testAppService() {
//		FX.MS.main(new String[0]);
		AppServiceImpl.main(new String[0]);
	}

	public void testExtService() {
		WebServiceImpl.main(new String[0]);
	}

	public int go() {
		String user = "fems";
		String password = "fems12#$";
		String host = "175.123.142.155";
		int port = 2222;
		try {
			jsch = new JSch();
			session = jsch.getSession(user, host, port);
			session.setPassword(password);

			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			config.put("Compression", "yes");
			config.put("ConnectionAttempts", "2");

			session.setConfig(config);

//			session.setConfig("kex", "diffie-hellman-group1-sha1"); 
			System.out.println("Establishing Connection...");
			session.connect();
			System.out.println("connected");

			int lport = 3306;
			int rport = 3306;
			int assinged_port = session.setPortForwardingL(lport, host, rport);
			System.out.println("localhost:" + assinged_port + " -> " + host + ":" + rport);
			return assinged_port;
		} catch (Exception e) {
			System.err.print(e);
			System.exit(0);
			return -1;
		}
	}

	void testTable() {
		try {
			List<String> list = FxTableMaker.getColumnNameNotNullList(FX_MO_FXSERVICE.class);
			System.out.println(list);
		} catch (Exception e) {
			// TODO: handle exception
		}

		try {
			List<String> list = FxTableMaker.getColumnNameNotNullList(FxTableVo.class);
			System.out.println(list);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	void testPsItem() {
		try {
			List<PsItem> list = PsApi.getApi().getPsItemList("SENSOR", "TEMP");
			System.out.println(list);
		} catch (Exception e) {
			System.exit(0);
		}
	}

	public void testSsh() {

		int port;
		try {
			port = go();
		} catch (Exception ex) {
			ex.printStackTrace();
			return;
		}

		Connection con = null;
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost:" + port + "/vup";
		String dbUser = "vup";
		String dbPasswd = "vup";
		try {
			Class.forName(driver).newInstance();
			url += "?user=vup&password=vup";
			System.out.println("connecting database..." + url);
			con = DriverManager.getConnection(url);
//			con = DriverManager.getConnection(url, dbUser, dbPasswd);
			System.out.println("Database connection established");
			System.out.println("DONE");
			try {
				Statement st = con.createStatement();
				String sql = "show tables";
				ResultSet rs = st.executeQuery(sql);
				while (rs.next()) {
					System.out.println(rs.getObject(1));
				}
			} catch (SQLException s) {
				System.out.println("SQL statement is not executed!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
