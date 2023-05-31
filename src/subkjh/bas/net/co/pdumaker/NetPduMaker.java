package subkjh.bas.net.co.pdumaker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import subkjh.bas.co.lang.Lang;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Loggable;
import subkjh.bas.co.log.Logger;
import subkjh.bas.net.co.vo.ByteParsedBean;
import subkjh.bas.net.co.vo.NetListener;
import subkjh.bas.net.co.vo.NetPdu;
import subkjh.bas.net.co.vo.NetPduFilter;
import subkjh.bas.net.co.vo.RecvBytes;

/**
 * 받은 바이트배열을 이용하여 PDU를 생성하는 메이커
 * 
 * @author subkjh
 * 
 * @param <PDU>
 */
public abstract class NetPduMaker<PDU extends NetPdu> implements Runnable, Loggable {

	private LinkedBlockingQueue<RecvBytes> dataQueue;
	private LinkedBlockingQueue<PDU> pduQueue;
	private boolean isContinue;
	private String name;
	private Thread thread;
	private Map<Object, byte[]> bytesMap;
	private long countPdu;
	/** 필터목록 */
	private List<NetPduFilter<PDU>> filterList;
	/** 필터목록 동기화 객체 */
	private Object lockObjFilter;
	private NetListener listener;
	protected Logger logger;

	/**
	 * 
	 * @param name
	 *            PDU메이커
	 * @param logger
	 *            로거
	 */
	public NetPduMaker(String name, Logger logger) {

		pduQueue = new LinkedBlockingQueue<PDU>();
		dataQueue = new LinkedBlockingQueue<RecvBytes>();
		bytesMap = new HashMap<Object, byte[]>();
		lockObjFilter = new Object();

		this.logger = (logger == null ? Logger.logger : logger);

		this.name = name;
		isContinue = true;
	}

	/**
	 * 
	 * @param filter
	 */
	public void addFilter(NetPduFilter<PDU> filter) {
		if (filterList == null) {
			filterList = new ArrayList<NetPduFilter<PDU>>();
		}

		synchronized (lockObjFilter) {
			if (filterList.contains(filter) == false) {
				filterList.add(filter);
				notify(NetListener.PduMakerFilterAdded, filter);
			}
		}
	}

	/**
	 * 메이커를 종료합니다.
	 */
	public void close() {

		isContinue = false;

		thread.interrupt();
	}

	public NetListener getListener() {
		return listener;
	}

	/**
	 * 
	 * @return 사용중인 로거
	 */
	public Logger getLogger() {
		return logger;
	}

	@Override
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return 패킷을 분석하여 적재되는 큐
	 */
	public LinkedBlockingQueue<PDU> getPduQueue() {
		return pduQueue;
	}

	@Override
	public String getState(LOG_LEVEL level) {
		return "byte queue[" + dataQueue.size() + "] pdu queue[" + pduQueue.size() + "]";
	}

	/**
	 * PDU메이커를 시작합니다.
	 */
	public void open() {

		if (thread != null && thread.isAlive())
			return;

		thread = new Thread(this);
		thread.setName(getName());
		thread.start();

	}

	/**
	 * PDU를 큐에 적재합니다.
	 * 
	 * @param pdu
	 *            생성된 PDU
	 */
	public void putPdu(PDU pdu) {

		if (filterList != null) {
			synchronized (lockObjFilter) {
				PDU pduNew = pdu;
				for (NetPduFilter<PDU> filter : filterList) {
					pduNew = filter.filter(pduNew);
					if (pduNew == null)
						return;
				}
			}
		}

		try {
			pduQueue.put(pdu);

			notify(NetListener.PduMakerPduAdded, pdu);

		} catch (InterruptedException e) {
			logger.error(e);
		}

	}

	/**
	 * 바이트배열을 큐에 적재하여 PDU를 만들 수 있도록 합니다.
	 * 
	 * @param recvBytes
	 *            받은 바이트배열
	 * @throws Exception
	 */
	public void putRecvBytes(RecvBytes recvBytes) throws Exception {

		if (thread.isAlive() == false)
			throw new Exception(Lang.get("패킷 처리자가 종료되었습니다."));

		try {

			dataQueue.put(recvBytes);

			notify(NetListener.PduMakerBytesAdded, recvBytes);

		} catch (InterruptedException e) {
			logger.error(e);
		}
	}

	public void removeFilter(NetPduFilter<PDU> filter) {
		if (filterList == null)
			return;

		synchronized (lockObjFilter) {
			filterList.remove(filter);

			notify(NetListener.PduMakerFilterRemove, filter);
		}
	}

	@Override
	public void run() {
		isContinue = true;

		RecvBytes recvBytes;

		logger.info("start");

		notify(NetListener.PduMakerOpend, null);

		while (isContinue) {
			try {
				recvBytes = dataQueue.take();

				if (recvBytes == null)
					continue;

				try {
					makeData2Pdu(recvBytes.getKey(), recvBytes.getBytes());
				} catch (Exception e) {
					logger.error(e);
				}

			} catch (InterruptedException e) {

			}

		}

		logger.info("finished " + getState(LOG_LEVEL.trace));

		notify(NetListener.PduMakerClosed, null);

	}

	/**
	 * 닫힌 키의 남은 바이트배열을 버립니다.
	 * 
	 * @param key
	 */
	public void setClosed(Object key) {
		if (bytesMap.remove(key) != null) {
			logger.trace("{}", key);
		}
	}

	public void setListener(NetListener listener) {
		this.listener = listener;
	}

	/**
	 * 입력된 바이이트배열을 가지고 PDU를 생성합니다.
	 * 
	 * @param bytes
	 *            입력된 바이트배열
	 * @return 생성결과<br>
	 *         생성된 PDU는 ByteParsedBean setPdu()에 넣고 처리하고 남은 바이트배열을 setBytes()에
	 *         넣고 리턴합니다.
	 * @throws Exception
	 */
	protected abstract ByteParsedBean<PDU> makePdu(byte bytes[]) throws Exception;

	/**
	 * 
	 * @param key
	 * 
	 * @param bytes
	 * 
	 */
	private void makeData2Pdu(Object key, byte bytes[]) {

		if (bytes == null || bytes.length == 0)
			return;

		byte bytesNew[];
		byte bytesOld[] = bytesMap.get(key);
		if (bytesOld != null) {
			bytesNew = new byte[bytesOld.length + bytes.length];
			System.arraycopy(bytesOld, 0, bytesNew, 0, bytesOld.length);
			System.arraycopy(bytes, 0, bytesNew, bytesOld.length, bytes.length);
		} else {
			bytesNew = bytes;
		}

		try {
			ByteParsedBean<PDU> parsedBean = null;
			while (true) {

				parsedBean = makePdu(bytesNew);

				if (parsedBean.getPdu() != null) {

					parsedBean.getPdu().setKey(key);

					countPdu++;

					putPdu(parsedBean.getPdu());

					if (logger.isTrace()) {
						logger.trace(parsedBean.getPdu().toString());
					}

					if (parsedBean.isBytes()) {
						bytesNew = parsedBean.getBytes();
					} else {
						break;
					}
				} else {
					break;
				}
			}

			if (parsedBean.isBytes()) {
				bytesMap.put(key, parsedBean.getBytes());
			} else {
				bytesMap.remove(key);
			}

		} catch (Exception e) {
			logger.error(e);
			bytesMap.remove(key);
			return;
		}
	}

	private void notify(String state, Object obj) {
		if (listener != null) {
			listener.onNetState(state, obj);
		}
	}
}
