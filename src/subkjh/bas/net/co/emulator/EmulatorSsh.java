package subkjh.bas.net.co.emulator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;

public class EmulatorSsh extends Emulator {

	private JSch jsch;
	private Session session;
	private Channel channel;

	public static void main(String[] args) throws Exception {
		EmulatorSsh e = new EmulatorSsh();
		e.connect("125.7.128.42", 63822, "subkjh", "rlawhdgns");
		System.out.println("--------------------------------------------------");
		System.out.println(e.cmdln("ps -ef"));
//		System.out.println("--------------------------------------------------");
//		System.out.println(e.cmdln("nprismstatus"));
//		System.out.println("--------------------------------------------------");
		e.disconnect();
	}

	public EmulatorSsh() {

	}

	@Override
	protected void doConnect(String host, int port, String userId, String password) throws Exception {
		jsch = new JSch();

		session = jsch.getSession(userId, host, port);
		session.setPassword(password);
		session.setUserInfo(new MyUserInfo());

		// It must not be recommended, but if you want to skip host-key
		// check,
		// invoke following,
		// session.setConfig("StrictHostKeyChecking", "no");

		// session.connect();
		session.connect(30000); // making a connection with timeout.

		channel = session.openChannel("shell");

		if (channel instanceof ChannelShell) {
			((ChannelShell) channel).setPtySize(1024, 40, 0, 0);
		}

		channel.connect(3 * 1000);

		if (logger != null)
			logger.debug("connection ok");

	}

	@Override
	protected void _disconnect() {
		try {
			if (channel != null) {
				channel.disconnect();
				channel = null;
			}

			if (session != null) {
				session.disconnect();
				session = null;
			}

			if (jsch != null) {
				jsch = null;
			}
		} catch (Exception ex) {
		}

	}

	@Override
	protected InputStream getInputStream() {
		if (channel == null)
			return null;
		try {
			return channel.getInputStream();
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	protected OutputStream getOutputStream() {
		if (channel == null)
			return null;
		try {
			return channel.getOutputStream();
		} catch (IOException e) {
			return null;
		}
	}

	class MyUserInfo implements UserInfo, UIKeyboardInteractive {
		public String getPassword() {
			return null;
		}

		public boolean promptYesNo(String str) {
			return true;
		}

		public String getPassphrase() {
			return null;
		}

		public boolean promptPassphrase(String message) {
			return false;
		}

		public boolean promptPassword(String message) {
			return false;
		}

		public void showMessage(String message) {
			getLogger().error(message);
		}

		public String[] promptKeyboardInteractive(String destination, String name, String instruction, String[] prompt,
				boolean[] echo) {
			return null;
		}
	}
}
