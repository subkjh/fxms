package fxms.bas.po.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fxms.bas.po.PsVo;
import subkjh.bas.co.log.Logger;

public class ValuePeekerMap extends HashMap<String, List<ValuePeeker>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2789183549873805265L;

	public ValuePeekerMap() {
	}

	public void addReq(long moNo, String moInstance, String psCode, ValuePeeker peeker) {
		String key = makeKey(moNo, moInstance, psCode);

		List<ValuePeeker> list = get(key);
		if (list == null) {
			list = new ArrayList<ValuePeeker>();
			Logger.logger.debug("key=[] added", key);
			put(key, list);
		}

		list.add(peeker);
	}

	public List<ValuePeeker> getPeekerList(PsVo vo) {

		String req = makeKey(vo.getMoNo(), vo.getMoInstance(), vo.getPsCode());

		return get(req);
	}

	public void removeReq(long moNo, String moInstance, String psCode, ValuePeeker peeker) {
		String key = makeKey(moNo, moInstance, psCode);

		List<ValuePeeker> list = get(key);
		if (list != null) {
			list.remove(peeker);
			if (list.size() == 0) {
				remove(key);
				Logger.logger.debug("key=[] removed", key);
			}
		}

	}

	private String makeKey(long moNo, String moInstance, String psCode) {
		StringBuffer sb = new StringBuffer();

		sb.append(moNo);
		if (moInstance != null) {
			sb.append("/");
			sb.append(moInstance);
		}
		sb.append("/");
		sb.append(psCode);

		return sb.toString();
	}

}