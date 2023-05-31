package fxms.bas.fxo;

import java.util.HashMap;
import java.util.Map;

/**
 * FxMS Filter, Adapter
 *
 * @author subkjh
 *
 */
public abstract class FxActorImpl implements FxActor {

	private Map<String, Object> para;
	private FxMatch match;
	private String name;

	public FxActorImpl() {
		para = new HashMap<>();
		name = getClass().getSimpleName();
		match = new FxMatch();
	}
	
	@Override
	public Map<String, Object> getPara() {
		return para;
	}

	@Override
	public FxMatch getMatch() {
		return match;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void onCreated() throws Exception {

	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

}
