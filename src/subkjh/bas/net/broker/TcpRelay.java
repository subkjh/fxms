package subkjh.bas.net.broker;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import subkjh.bas.co.log.Logger;

/**
 * 
 * @author subkjh
 * 
 */

public class TcpRelay {

	class Reader extends Thread {
		Reader() {
			setName(getTcpRelayName() + "-R");
		}

		@Override
		public void run() {
			byte buff[] = new byte[4096];
			int len;
			try {
				while (true) {
					len = targetIn.read(buff);
					if (len < 0) break;

					if (logger.isTrace()) {
						logger.trace(TcpRelay.toString(buff, len));
					}
					if (len > 0) {
						clientOut.write(buff, 0, len);
						clientOut.flush();
					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}

			stopRelay();
		}
	}

	class Writer extends Thread {

		Writer() {
			setName(getTcpRelayName() + "-W");
		}

		@Override
		public void run() {
			byte buff[] = new byte[4096];
			int len;
			try {
				while (true) {
					len = clientIn.read(buff);
					if (len < 0) break;

					if (logger.isTrace()) {
						logger.trace(TcpRelay.toString(buff, len));
					}

					if (len > 0) {
						targetOut.write(buff, 0, len);
						targetOut.flush();
					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}

			stopRelay();
		}
	}

	private static String toString(byte buff[], int len) {
		String ret = "";
		for (int i = 0; i < len; i++) {
			ret += String.format("%2x ", buff[i]);
		}

		return ret;
	}

	private InputStream clientIn;
	private OutputStream clientOut;
	private Logger logger;
	private Reader reader;
	private Socket socketReader;
	private Socket socketSender;
	private InputStream targetIn;
	private OutputStream targetOut;
	private Writer writer;

	public TcpRelay(Socket readSocket, Socket sendSocket, Logger logger) throws Exception {
		this.logger = logger;
		this.socketReader = readSocket;
		this.socketSender = sendSocket;

		clientIn = readSocket.getInputStream();
		clientOut = readSocket.getOutputStream();

		targetIn = sendSocket.getInputStream();
		targetOut = sendSocket.getOutputStream();

	}

	public boolean isAlive() {
		return reader.isAlive() && writer.isAlive();
	}

	public void startRelay() {

		logger.info("start");

		reader = new Reader();
		reader.start();

		writer = new Writer();
		writer.start();

	}

	public void stopRelay() {

		try {
			socketSender.close();
		} catch (IOException e) {
		}

		try {
			socketReader.close();
		} catch (IOException e) {
		}

		logger.info("close");
	}

	String getTcpRelayName() {
		return "Relay-" + socketReader.getInetAddress().getHostAddress() + ":" + socketReader.getPort() + "="
				+ socketSender.getInetAddress().getHostAddress() + ":" + socketSender.getPort();
	}
}
