package subkjh.bas.net.co.vo;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 소켓을 이용하여 자료의 보낸 수량을 가지고 있는 클래스
 * 
 * @author subkjh
 * 
 */
public class CountComm {

	private final SimpleDateFormat FMT = new SimpleDateFormat("dd.HH:mm:ss");

	private String name;

	private long countBytesSend;

	private long countBytesRecv;

	private long mstimeSend;

	private long mstimeRecv;

	public CountComm() {

	}

	public void addRecv(int size) {
		countBytesRecv += size;
		mstimeRecv = System.currentTimeMillis();
	}

	public void addSend(int size) {
		countBytesSend += size;
		mstimeSend = System.currentTimeMillis();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "comm=" + countBytesSend + "/" + countBytesRecv + ":" + getDate(mstimeSend) + "/" + getDate(mstimeRecv);
	}

	private String getDate(long mstime) {
		if (mstime == 0) return "-";
		return FMT.format(new Date(mstime));
	}

}
