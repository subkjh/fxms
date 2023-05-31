package fxms.bas.vo;

import java.io.Serializable;

public class FxMethodPara implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5772264878164542064L;
	
	private Class<?> classOfP;
	private Object obj;

	public FxMethodPara() {

	}

	public FxMethodPara(Class<?> classOfP, Object obj) {
		this.classOfP = classOfP;
		this.obj = obj;
	}

	public FxMethodPara(Object o) {

		this.obj = o;

		if (o != null) {
			classOfP = o.getClass();
		}
	}

	public Class<?> getClassOfP() {
		return classOfP;
	}

	public Object getObj() {
		return obj;
	}

}
