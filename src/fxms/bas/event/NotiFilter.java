package fxms.bas.event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fxms.bas.signal.Signal;

/**
 * 서비스별 어떤 노티를 받을지 정하는 필터
 * 
 * @author subkjh(Kim,JongHoon)
 *
 */
public class NotiFilter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1988142840351905774L;

	private List<Class<? extends FxEvent>> notiList;

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (Class<? extends FxEvent> e : notiList) {
			if (sb.length() > 0) {
				sb.append(",");
			}
			sb.append(e.getName());
		}

		return sb.toString();
	}

	public NotiFilter() {
		notiList = new ArrayList<Class<? extends FxEvent>>();
	}

	public void add(Class<? extends FxEvent> classOfEvent) {
		notiList.add(classOfEvent);
	}

	/**
	 * 받아도 되는 이벤트인지 확인한다.<br>
	 * 모든 Signal을 포함된다.<br>
	 * FxEvent는 추가된 내용만 포함된다.<br>
	 * 
	 * @param s 이벤트
	 * @return
	 */
	public boolean isNoti(FxEvent s) {

		if (s instanceof Signal)
			return true;

		for (Class<? extends FxEvent> e : notiList) {
			if (e.isInstance(s)) {
				return true;
			}
		}

		return false;
	}

}
