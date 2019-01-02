package fxms.bas.noti;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fxms.bas.alarm.dbo.Alarm;
import fxms.bas.mo.Mo;
import fxms.bas.signal.Signal;

/**
 * 
 * @author subkjh
 *
 */
public class NotiFilter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1988142840351905774L;

	private List<Class<? extends FxEvent>> notiList;
	private List<Class<? extends FxEvent>> excluseList;

	@SuppressWarnings("unchecked")
	public NotiFilter() {

		notiList = new ArrayList<Class<? extends FxEvent>>();
		excluseList = new ArrayList<Class<? extends FxEvent>>();

		add(Alarm.class, Mo.class);
	}

	@SuppressWarnings("unchecked")
	public void add(Class<? extends FxEvent>... sArr) {

		for (Class<? extends FxEvent> s : sArr) {
			notiList.add(s);
		}
	}

	@SuppressWarnings("unchecked")
	public void addExclude(Class<? extends FxEvent>... sArr) {

		for (Class<? extends FxEvent> s : sArr) {
			excluseList.add(s);
		}
	}

	public boolean isNoti(FxEvent s) {

		// Signal도 제외에 포함되어 있다면 우선 처리해 준다.
		for (Class<? extends FxEvent> e : excluseList) {
			if (e.isInstance(s)) {
				return false;
			}
		}

		if (s instanceof Signal)
			return true;

		if (notiList == null)
			return false;

		for (Class<? extends FxEvent> e : notiList) {
			if (e.isInstance(s)) {
				return true;
			}
		}
		return false;
	}

}
