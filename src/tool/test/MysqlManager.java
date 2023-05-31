package tool.test;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class MysqlManager {

	// Logger
	private final static Logger LOGGER = Logger.getLogger(MysqlManager.class.getName());

	public static void main(String args[]) {
		MysqlManager mng = new MysqlManager();
		mng.connect();
	}

	public void connect() {

		//
		int assigned_port;
		final int local_port = 3309;

		// Remote host and port
		final int remote_port = 3306;
		final String remote_host = "175.123.142.155";

		try {
			JSch jsch = new JSch();

			// Create SSH session. Port 22 is your SSH port which
			// is open in your firewall setup.
			Session session = jsch.getSession("fems", remote_host, 2222);
			session.setPassword("fems12#$");

			// Additional SSH options. See your ssh_config manual for
			// more options. Set options according to your requirements.
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			config.put("Compression", "yes");
			config.put("ConnectionAttempts", "2");

			session.setConfig(config);

			// Connect
			session.connect();

			// Create the tunnel through port forwarding.
			// This is basically instructing jsch session to send
			// data received from local_port in the local machine to
			// remote_port of the remote_host
			// assigned_port is the port assigned by jsch for use,
			// it may not always be the same as
			// local_port.

			assigned_port = session.setPortForwardingL(local_port, remote_host, remote_port);
			
			System.out.println(assigned_port);

		} catch (JSchException e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
			return;
		}

		if (assigned_port == 0) {
			LOGGER.log(Level.SEVERE, "Port forwarding failed !");
			return;
		}

		// Database access credintials. Make sure this user has
		// "connect" access to this database;

		// these may be initialized somewhere else in your code.
		final String database_user = "vup";
		final String database_password = "vup";
		final String database = "vup";

		// Build the database connection URL.
		StringBuilder url = new StringBuilder("jdbc:mysql://localhost:");

		// use assigned_port to establish database connection
		url.append(assigned_port).append("/").append(database).append("?user=").append(database_user)
				.append("&password=").append(database_password);

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			System.out.println(url);
			java.sql.Connection connection = java.sql.DriverManager.getConnection(url.toString());

			java.sql.DatabaseMetaData metadata = connection.getMetaData();

			// Get all the tables and views
			String[] tableType = { "TABLE", "VIEW" };
			java.sql.ResultSet tables = metadata.getTables(null, null, "%", tableType);
			String tableName;
			while (tables.next()) {
				tableName = tables.getString(3);

				// Get the columns from this table
				java.sql.ResultSet columns = metadata.getColumns(null, tableName, null, null);

				String columnName;
				int dataType;
				while (columns.next()) {
					columnName = columns.getString(4);
					dataType = columns.getInt(5);

					// Your actual task;
				}
			}

		} catch (ClassNotFoundException | IllegalAccessException | InstantiationException | java.sql.SQLException e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		}

	}
}