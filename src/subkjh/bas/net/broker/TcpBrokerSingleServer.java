package subkjh.bas.net.broker;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import subkjh.bas.co.log.Logger;

/**
 * TCP Broker
 * 
 * @author subkjh
 * @since 2009-12-16
 */
public class TcpBrokerSingleServer extends Thread {

	public static void main(String[] args) {

		TcpBrokerSingleServer broker = new TcpBrokerSingleServer("TEST");
		try {
			broker.startBroker(null, 1000, new Socket(), 10);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/** 서버 소켓 */
	private ServerSocket serverSocket;
	private Socket socketSender;
	private Logger logger = Logger.logger;

	public TcpBrokerSingleServer(String name) {
		setName(name == null ? getClass().getSimpleName() : name);
	}

	@Override
	public void run() {

		logger.info("started");
		TcpRelay relay = null;

		try {
			final Socket readSocket = serverSocket.accept();

			try {
				relay = new TcpRelay(readSocket, socketSender, logger);
				relay.startRelay();
			} catch (Exception e) {
				logger.error(e);
			}
		} catch (Exception e) {
			logger.fail(e.getMessage());
		}

		if (relay != null) {
			while (relay.isAlive()) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
			}
		}

		if (serverSocket != null) {
			try {
				serverSocket.close();
			} catch (IOException e) {
			}
		}

		logger.info("finished");

	}

	/**
	 * 
	 * @param host
	 * @param port
	 * @param socketSender
	 * @param timeout
	 * @throws Exception
	 */
	public void startBroker(String host, int port, Socket socketSender, int timeout) throws Exception {

		serverSocket = new ServerSocket();
		serverSocket.setSoTimeout(timeout * 1000);
		if (host == null) {
			serverSocket.bind(new InetSocketAddress(port));
		} else {
			serverSocket.bind(new InetSocketAddress(host, port));
		}

		this.socketSender = socketSender;

		start();
	}

}
