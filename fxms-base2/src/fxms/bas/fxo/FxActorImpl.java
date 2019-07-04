package fxms.bas.fxo;

/**
 * FxMS Filter, Adapter
 * 
 * @author subkjh
 *
 */
public abstract class FxActorImpl implements FxActor {

	private FxPara para;
	private String name;

	public FxActorImpl() {
		para = new FxPara();
	}

	@Override
	public FxPara getFxPara() {
		return para;
	}

	@Override
	public void setPara(String name, String value) {
		getFxPara().set(name, value);
	}

	public String getName() {
		return name;
	}

	public Object getPara(String name) {
		return para.get(name);
	}

	public String getParaStr(String name) {
		return para.getString(name);
	}

	public boolean match(Object obj) {
		return para.match(obj);
	}

	@Override
	public void onCreated() {
	}

	public void setName(String name) {
		this.name = name;
	}
}
