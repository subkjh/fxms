package fxms.bas.vo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.Naming;
import java.util.ArrayList;
import java.util.List;

import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.ObjectUtil;

public class FxMethod {

	public enum TYPE {
		URL, CLASS;
	}

	public static void main(String[] args) {
		FxMethod t = new FxMethod(TYPE.CLASS, FxMethod.class.getName(), "getMethod");
		try {
			System.out.println(t.invoke(t));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private TYPE type;
	private String target;
	/** 처리할 메소드 명 */
	private String method;
	/** 파라메터 */
	private List<Object> paraList = new ArrayList<Object>();

	private List<Class<?>> typeList = new ArrayList<Class<?>>();

	public FxMethod() {

	}

	public FxMethod(TYPE type, String target, String method, FxMethodPara... paras) {

		this.type = type;
		this.target = target;
		this.method = method;

		for (FxMethodPara para : paras) {
			addPara(para.getClassOfP(), para.getObj());
		}

	}

	public void addPara(Class<?> classOfPara, Object para) {
		paraList.add(para);
		typeList.add(classOfPara);
	}

	public String getMethod() {
		return method;
	}

	public Object[] getParaArr() {
		return paraList.toArray(new Object[paraList.size()]);
	}

	public Class<?>[] getParaClassArr() {
		return typeList.toArray(new Class<?>[typeList.size()]);
	}

	public List<Object> getParaList() {
		return paraList;
	}

	public List<Class<?>> getTypeList() {
		return typeList;
	}

	public Object invoke() throws Exception {

		Object o;
		if (type == TYPE.CLASS) {
			o = Class.forName(target).newInstance();
		} else {
			o = Naming.lookup(target);
		}
		return invoke(o);
	}

	public Object invoke(Object o) throws Exception {

		Method method = ObjectUtil.getMethod(o.getClass(), getMethod(), getParaClassArr());

		if (method == null) {
			Exception ex = new Exception("METHOD(" + getMethod() + ") NOT FOUND");
			Logger.logger.fail(ex.getMessage());
			throw ex;
		}

		try {
			Object obj = method.invoke(o, getParaArr());

			if (obj == null)
				Logger.logger.trace(method.getName() + "=null");
			else
				Logger.logger.trace(method.getName() + "=" + obj.getClass().getSimpleName());

			return obj;
		} catch (InvocationTargetException e) {
			Logger.logger.error(e.getTargetException());
			throw new Exception(e.getTargetException());
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		}
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public void setPara(FxMethodPara... paras) {
		for (FxMethodPara para : paras) {
			addPara(para.getClassOfP(), para.getObj());
		}
	}

	public void setParaList(List<Object> paraList) {
		this.paraList = paraList;
	}

	public void setTypeList(List<Class<?>> typeList) {
		this.typeList = typeList;
	}

	@Override
	public String toString() {
		String para = "";
		for (Object obj : paraList) {
			if (obj == null) {
				para += ",null";
			} else {
				para += "," + obj.getClass().getSimpleName() + ":" + obj;
			}
		}
		return method + "(" + (para.length() > 0 ? para.substring(1) : "") + ")";
	}
}
