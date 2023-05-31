package fxms.bas.fxo.adapter;

import java.util.HashMap;
import java.util.Map;

import fxms.bas.fxo.FxAttrApi;
import subkjh.bas.co.log.Logger;

/**
 * FxAdapter 공통 기능 구현된 클래스
 * 
 * @author subkjh
 *
 */
public class FxAdapterImpl implements FxAdapter {

	private final Map<String, Object> para;

	public FxAdapterImpl() {
		this.para = new HashMap<>();
	}

	public Map<String, Object> getAdapterPara() {
		return para;
	}

	@Override
	public void onCreated() throws Exception {

	}

	@Override
	public void setPara(Map<String, Object> o) {
		this.para.clear();
		if (o != null) {
			this.para.putAll(o);

			try {
				FxAttrApi.toObject(this.para, this);
			} catch (Exception e) {
				Logger.logger.error(e);
			}

		}
	}
}
