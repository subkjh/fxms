package fxms.module.restapi;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.Gson;

import fxms.bas.api.CoApi;
import fxms.bas.api.FxApi;
import fxms.bas.api.ServiceApi;
import fxms.bas.co.FxConfDao;
import fxms.bas.co.OpCode;
import fxms.bas.co.exp.NotDefineException;
import fxms.bas.co.exp.NotFoundException;
import fxms.bas.co.noti.TargetFxEvent;
import fxms.bas.co.vo.IsRegChg;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.fxo.service.app.AppService;
import fxms.module.restapi.vo.SessionVo;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.user.User.USER_TYPE;
import subkjh.bas.co.utils.FileUtil;
import subkjh.bas.co.utils.ObjectUtil;
import subkjh.bas.dao.data.Column;
import subkjh.bas.dao.data.Table;
import subkjh.bas.dao.database.DBManager;
import subkjh.bas.fxdao.FxDaoCallback;
import subkjh.bas.fxdao.control.FxDaoExecutor;
import subkjh.bas.fxdao.control.FxTableMaker;

public abstract class CommHandler extends FxHttpHandler {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private HashMap<String, Object> convert(Map<String, Object> map) {

		if (map == null) {
			return new HashMap<String, Object>();
		}

		HashMap<String, Object> newMap = new HashMap(map);
		Object value;
		String keys[] = newMap.keySet().toArray(new String[newMap.size()]);

		for (String key : keys) {

			value = newMap.get(key);
			if (value instanceof List) {
				List list = (List) value;
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i) instanceof Map) {
						list.set(i, convert((Map) list.get(i)));
					}
				}
			} else if (value instanceof Map) {
				newMap.put(key, convert((Map) value));
			}
		}
		return newMap;
	}

	private Method getJavaMethod(String javaMethodName) {

		for (Method method : getClass().getMethods()) {

			if (method.getName().equals(javaMethodName) == false)
				continue;

			if (method.getParameterTypes().length != 2)
				continue;

			if (method.getParameterTypes()[0].isAssignableFrom(SessionVo.class) //
					&& method.getParameterTypes()[1].isAssignableFrom(Map.class)) {
				return method;
			}
		}
		return null;
	}

	private String getJavaMethodName(String name) {

		String ss[] = name.toLowerCase().split("-|_");

		if (ss.length == 1) {
			return ss[0];
		}

		StringBuffer sb = new StringBuffer();
		for (String s : ss) {
			if (sb.length() > 0) {
				sb.append(s.toUpperCase().substring(0, 1));
				sb.append(s.substring(1));
			} else {
				sb.append(s);
			}
		}
		return sb.toString();
	}

	private String help() {

		List<String> list = new ArrayList<String>();

		for (Method method : getClass().getMethods()) {

			if (method.getParameterTypes().length != 2)
				continue;

			if (method.getParameterTypes()[0].isAssignableFrom(SessionVo.class) //
					&& method.getParameterTypes()[1].isAssignableFrom(Map.class)) {

				StringBuffer sb = new StringBuffer();
				char chs[] = method.getName().toCharArray();
				for (char ch : chs) {
					if (ch >= 'A' && ch <= 'Z') {
						sb.append("-");
						sb.append(ch);
					} else {
						sb.append(ch);
					}
				}
				list.add(sb.toString().toLowerCase());
			}
		}

		Collections.sort(list);
		FxTableMaker maker = new FxTableMaker();
		Class<?> classOfT;

		StringBuffer ret = new StringBuffer();
		ret.append("<h1>method-list</h1>");
		for (String methodName : list) {
			ret.append("<h2>");
			ret.append(methodName);
			ret.append("</h2>");
			ret.append("<p>");

			classOfT = getMethodClass(methodName);
			if (classOfT != null) {
				try {
					ret.append("<br>");
					ret.append(maker.getAttrInfo(classOfT));
				} catch (Exception e) {
				}
			}

			File file = new File(FxCfg.getFile(FxCfg.getHomeDeploy(), "test", methodName + ".txt"));
			Logger.logger.trace("[{}]{}", file.getName(), file.exists());

			if (file.exists()) {
				ret.append("<br>");
				ret.append("<h3>example</h3>\n");
				ret.append("<h4>request</h4>\n");
				ret.append("<h5>\n");
				ret.append(FileUtil.getString(file).trim());
				ret.append("\n</h5>\n");
				file = new File(FxCfg.getFile(FxCfg.getHomeDeploy(), "test", methodName + "-result.txt"));
				if (file.exists()) {
					ret.append("<h4>response</h4>\n");
					ret.append("<h5>\n");
					ret.append(FileUtil.getString(file).trim());
					ret.append("\n</h5>\n");
				}
			}

			ret.append("</p>\n");

		}

		return ret.toString();
	}

	/**
	 * 
	 * @param session    세션
	 * @param name       운용명
	 * @param parameters 인자
	 * @return 결과
	 * @throws Exception
	 */
	private Object onProcess(SessionVo session, String name, Map<String, Object> parameters) throws Exception {

		OpCode opcode = CoApi.getApi().getOpCode(name);

		String javaMethodName = getJavaMethodName(name);

		Logger.logger.info("session={}, op-code={}, method={} -> {}", session.getSessionId(), opcode, name,
				javaMethodName);

		Method javaMethod = getJavaMethod(javaMethodName);

		if (javaMethod == null) {
			String msg = "method=" + name + " not defined";
			Logger.logger.fail(msg);
			throw new Exception(msg);
		}

		long mstimeStart = System.currentTimeMillis();
		int retNo = 0;
		String retMsg = null;

		try {
			return javaMethod.invoke(this, new Object[] { (SessionVo) session, (Map<String, Object>) parameters });
		} catch (IllegalAccessException e) {
			retNo = -1;
			retMsg = e.getClass().getName();
			Logger.logger.error(e);
			throw e;
		} catch (IllegalArgumentException e) {
			retNo = -1;
			retMsg = e.getClass().getName();
			Logger.logger.error(e);
			throw e;
		} catch (InvocationTargetException e) {
			Throwable t = e.getTargetException();
			retNo = -1;
			retMsg = t.getClass().getName();
			Logger.logger.error(t);
			throw new Exception(t.getClass().getSimpleName() + ":" + t.getMessage());

		} finally {

			try {
				CoApi.getApi().logUserOp(session.getUserNo(), session.getSessionId(), opcode, null, null, retNo, retMsg,
						mstimeStart);
			} catch (Exception e) {
				Logger.logger.error(e);
			}

		}
	}

	protected <T> T add(SessionVo session, Map<String, Object> parameters, T item, FxDaoCallback<T> callback)
			throws Exception {

		ObjectUtil.toObject(parameters, item);

		if (item instanceof IsRegChg) {
			initRegChgVo(session, (IsRegChg) item);
		}

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();

			if (callback != null) {
				callback.onCall(tran, item);
			}

			tran.insertOfClass(item.getClass(), item);

			tran.commit();

			return item;

		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}
	}

	/**
	 * 업데이트할 PK를 확인한다.
	 * 
	 * @param classOfT
	 * @param parameters
	 * @throws Exception
	 */
	protected void checkUpdatePk(Class<?> classOfT, Map<String, Object> parameters) throws Exception {
		try {
			List<Table> tabList = new FxTableMaker().makeTableList(classOfT);
			List<Column> colList;
			Object value;
			StringBuffer sb = new StringBuffer();

			for (Table tab : tabList) {
				colList = tab.getPkColumns();
				for (Column col : colList) {
					value = parameters.get(col.getFieldName());
					if (value == null) {
						if (sb.length() > 0) {
							sb.append(",");
						}
						sb.append(col.getFieldName());
					}
				}
			}

			if (sb.length() > 0) {
				throw new NotDefineException(sb.toString());
			}
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		}
	}

	protected <T> T delete(SessionVo session, Map<String, Object> parameters, T item, FxDaoCallback<T> callback)
			throws Exception {

		ObjectUtil.toObject(parameters, item);

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();

			if (callback != null) {
				callback.onCall(tran, item);
			}

			tran.deleteOfClass(item.getClass(), item, null);

			tran.commit();

			return item;

		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}

	}

	protected AppService getAppService() throws Exception {

		try {
			return ServiceApi.getApi().getService(AppService.class);
		} catch (Exception e) {
			Logger.logger.error(e);
			throw new Exception("처리중 오류가 발생하였습니다.");
		}
	}

	protected int getInt(Map<String, Object> parameters, String name) throws Exception {
		Object val = parameters.get(name);
		if (val == null)
			throw new Exception(name + " is not defined");

		if (val instanceof Number) {
			return ((Number) val).intValue();
		} else {
			return Double.valueOf(val.toString()).intValue();
		}
	}

	protected int getInt(Map<String, Object> parameters, String name, int defVal) {
		Object val = parameters.get(name);
		if (val == null) {
			return defVal;
		}

		try {
			if (val instanceof Number) {
				return ((Number) val).intValue();
			} else {
				return Double.valueOf(val.toString()).intValue();
			}
		} catch (Exception e) {
			return defVal;
		}

	}

	protected long getLong(Map<String, Object> parameters, String name) throws Exception {
		Object val = parameters.get(name);
		if (val == null)
			throw new Exception(name + " is not defined");

		if (val instanceof Number) {
			return ((Number) val).longValue();
		} else {
			return Double.valueOf(val.toString()).longValue();
		}
	}

	protected long getLong(Map<String, Object> parameters, String name, long defVal) {
		Object val = parameters.get(name);
		if (val == null) {
			return defVal;
		}

		try {
			if (val instanceof Number) {
				return ((Number) val).longValue();
			} else {
				return Double.valueOf(val.toString()).longValue();
			}
		} catch (Exception e) {
			return defVal;
		}
	}

	/**
	 * 메소드에 사용되는 클래스를 조회한다.
	 * 
	 * @param methodName
	 * @return
	 */
	protected Class<?> getMethodClass(String methodName) {
		return null;
	}

	protected Number getNumber(Map<String, Object> map, String name) {
		Object val = map.get(name);
		if (val == null)
			return null;

		if (val instanceof Number) {
			return (Number) val;
		} else {
			return Double.valueOf(val.toString());
		}
	}

	protected byte[] getResult(String sessionId, Number seqno, String method, boolean isOk, Object obj) {

		Map<String, Object> ret = new HashMap<String, Object>();

		long nextSeqno = CoApi.getApi().getSessionMgr().getNextSeqno(sessionId);

		if (seqno != null) {
			ret.put("seqno", seqno.longValue());
		}
		if (method != null) {
			ret.put("method", method);
		}
		ret.put("next-seqno", nextSeqno);
		ret.put("is-ok", isOk ? "Y" : "N");

		if (isOk == false) {
			if (obj instanceof NotFoundException) {
				ret.put("errmsg", "not found");
			} else if (obj instanceof Exception) {
				ret.put("errmsg", ((Exception) obj).getMessage());
			} else {
				ret.put("errmsg", obj);
			}
		} else {
			ret.put("result", obj);
		}

		String value = new Gson().toJson(ret);

		if (isOk) {
			File file = new File(FxCfg.getFile(FxCfg.getHomeDeploy(), "test", method + "-result.txt"));
			if (file.exists() == false) {
				FileUtil.writeToFile(file.getPath(), value, false);
			}
		}

		return value.getBytes();
	}

	protected String getString(Map<String, Object> parameters, String name) throws Exception {
		Object val = parameters.get(name);
		if (val == null)
			throw new NotDefineException(name);
		return val.toString();
	}

	protected String getString(Map<String, Object> parameters, String name, String defVal) {
		Object val = parameters.get(name);
		if (val == null) {
			return defVal;
		}
		return val.toString();
	}

	/**
	 * 등록/수정 사용자 및 일자 설정
	 * 
	 * @param session
	 * @param item
	 */
	protected void initRegChgVo(SessionVo session, IsRegChg item) {
		item.setRegDate(FxApi.getDate(0));
		item.setRegUserNo(session.getUserNo());
		item.setChgDate(FxApi.getDate(0));
		item.setChgUserNo(session.getUserNo());
	}

	protected Map<String, Object> makePara(SessionVo session, Map<String, Object> parameters, Object... paras) {

		Map<String, Object> para = new HashMap<String, Object>(parameters);

		if (session.getUserType() != USER_TYPE.admin) {
			para.put("userNo", session.getUserNo());
		}

		for (int i = 0; i < paras.length; i += 2) {
			if (paras.length > i + 1) {
				para.put(paras[i].toString(), paras[i + 1]);
			}
		}

		return para;
	}

	protected Map<String, Object> makePara4Ownership(SessionVo session, Map<String, Object> parameters,
			Object... paras) {

		Map<String, Object> para = new HashMap<String, Object>(parameters);

		if (session.getUserType() != USER_TYPE.admin) {
			para.put("inloNo in",
					" ( select MEM_INLO_NO from FX_CF_INLO_MEM where INLO_NO = " + session.getMngInloNo() + " )");
		}

		for (int i = 0; i < paras.length; i += 2) {
			if (paras.length > i + 1) {
				para.put(paras[i].toString(), paras[i + 1]);
			}
		}

		return para;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected byte[] onProcess(InetSocketAddress client, Set<Entry<String, List<String>>> header, String body)
			throws Exception {

		String sessionId = null;
		Number seqno = null;
		String method = null;

		String hostname = client.getHostName();
		Gson son = new Gson();
		Map<String, Object> map = null;

		if (body == null) {
			StringBuffer sb = new StringBuffer();
			sb.append("<!DOCTYPE html>\n");
			sb.append("<html>\n");
			sb.append("<meta charset=\"UTF-8\">\n");
			sb.append("<body>\n");
			sb.append(help());
			sb.append("</body>\n");
			sb.append("</html>\n");
			return sb.toString().getBytes();
		}

		try {
			map = son.fromJson(body, HashMap.class);
		} catch (Exception e) {
			Logger.logger.error(e);
			return getResult(sessionId, seqno, method, false, "입력된 값이 올바른 JSON 형식이 아닙니다. 입력값(" + body + ")");
		}

		try {
			sessionId = getString(map, "session-id");
			seqno = getNumber(map, "seqno");
			method = getString(map, "method");
			Map<String, Object> parameters = (Map<String, Object>) map.get("parameters");

			Logger.logger.info("session-id={}, seqno={}, method={}, parameters={}", sessionId, seqno, method,
					parameters);

			SessionVo session = CoApi.getApi().getSessionMgr().get(hostname, sessionId,
					seqno == null ? -1 : seqno.longValue());

			if (session == null) {
				Logger.logger.fail("NotLoginException");
				return getResult(sessionId, seqno, method, false, "Not Login");
			}

			FxServiceImpl.fxService.send(new TargetFxEvent("method-call", method));

			// parameters가 StringMap이므로 변환하여 적용한다.
			Object value = onProcess(session, method, convert(parameters));

			File file = new File(FxCfg.getFile(FxCfg.getHomeDeploy(), "test", method + ".txt"));
			if (file.exists() == false) {
				FileUtil.writeToFile(file.getPath(), body, false);
			}

			return getResult(sessionId, seqno, method, true, value);

		} catch (Exception e) {
			Logger.logger.error(e);
			return getResult(sessionId, seqno, method, false, e);

		}
	}

	/**
	 * 하나의 데이터를 수정한다.
	 * 
	 * @param classOfT
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	protected Object update(Class<?> classOfT, SessionVo session, Map<String, Object> parameters) throws Exception {

		checkUpdatePk(classOfT, parameters);

		parameters.put("chgDate", FxApi.getDate(0));
		parameters.put("chgUserNo", session.getUserNo());
		FxConfDao.getDao().updateOfClass(classOfT, parameters);

		return parameters;
	}

	protected <T> T update(SessionVo session, Map<String, Object> parameters, T item, FxDaoCallback<T> callback)
			throws Exception {

		ObjectUtil.toObject(parameters, item);
		if (item instanceof IsRegChg) {
			initRegChgVo(session, (IsRegChg) item);
		}

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();

			if (callback != null) {
				callback.onCall(tran, item);
			}

			tran.updateOfClass(item.getClass(), item, null);

			tran.commit();

			return item;

		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}
	}

}
