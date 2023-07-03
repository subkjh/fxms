package fxms.bas.handler;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import fxms.bas.api.OpApi;
import fxms.bas.api.UserApi;
import fxms.bas.event.FxEvent;
import fxms.bas.event.TargetFxEvent;
import fxms.bas.exp.AttrNotFoundException;
import fxms.bas.exp.FxException;
import fxms.bas.exp.MethodNotFoundException;
import fxms.bas.exp.NotDefineException;
import fxms.bas.exp.NotFoundException;
import fxms.bas.fxo.FxAttrApi;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.FxmsUtil;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.handler.vo.FxResponse;
import fxms.bas.handler.vo.FxResponse.HttpStatusCode;
import fxms.bas.handler.vo.SessionVo;
import fxms.bas.impl.dbo.UserLogDbo;
import fxms.bas.impl.dpo.user.LogUserWorkHstDfo;
import fxms.bas.impl.jwt.FxJwtVo;
import fxms.bas.signal.Signal;
import subkjh.bas.co.lang.Lang;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.user.User;
import subkjh.bas.co.user.exception.OpDenyException;
import subkjh.bas.co.utils.DateUtil;
import subkjh.bas.co.utils.ObjectUtil;
import subkjh.dao.ClassDao;
import subkjh.dao.ClassDaoEx;
import subkjh.dao.QidDao;
import subkjh.dao.database.DBManager;
import subkjh.dao.def.Column;
import subkjh.dao.def.FxDaoCallBefore;
import subkjh.dao.def.FxTable;
import subkjh.dao.util.FxTableMaker;

/**
 * 공통 기능을 구현한 공용 HANDLER
 * 
 * @author subkjh
 *
 */
public abstract class BaseHandler extends FxHttpHandler {

	class MethodData {
		String name;
		Method method;
		Class<?> paraClassOf[];
	}

	protected final List<MethodData> methodList;

	public BaseHandler() {
		methodList = getMethodData();
	}

	/**
	 * QID를 실행하여 결과는 넘긴다.
	 * 
	 * @param qid
	 * @param whereObj
	 * @return
	 * @throws Exception
	 */
	public List<?> selectListQid(String qid, Object whereObj) throws Exception {
		QidDao tran = getQidDao();
		try {
			tran.start();
			Map<String, Object> para = makeWherePara(whereObj); // handler에서는 인자를 모두 map 형식으로 변경해서 사용함.
			return tran.selectQid2Res(qid, para);
		} catch (Exception e) {
			throw e;
		} finally {
			tran.stop();
		}
	}

	/**
	 * 
	 * @param type
	 */
	protected void broadcast(FxEvent fxEvent) {

		if (FxServiceImpl.fxService != null) {

			if (fxEvent instanceof Signal) {
				((Signal) fxEvent).setSender(getClass().getSimpleName());
			}

			FxServiceImpl.fxService.sendEvent(fxEvent, true, true);
		}

	}

	/**
	 * 
	 * @param <T>
	 * @param session
	 * @param parameters
	 * @param classOfItem
	 * @return
	 * @throws Exception
	 */
	protected <T> T convert(SessionVo session, Map<String, Object> parameters, Class<T> classOfItem,
			boolean checkMandatory) throws AttrNotFoundException, Exception {

		if (classOfItem.getAnnotation(FxTable.class) != null) {
			T item = FxTableMaker.toObject(parameters, classOfItem, checkMandatory);
			// 추가, 수정 일자를 넣는다.
			int userNo = session == null ? User.USER_NO_SYSTEM : session.getUserNo();
			FxTableMaker.initRegChg(userNo, item);
			return item;
		} else {
			T ret = classOfItem.newInstance();
			FxAttrApi.toObject(parameters, ret);
			return ret;
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected HashMap<String, Object> convertToHashMap(Map<String, Object> map) {

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
						list.set(i, convertToHashMap((Map) list.get(i)));
					}
				}
			} else if (value instanceof Map) {
				newMap.put(key, convertToHashMap((Map) value));
			}
		}
		return newMap;
	}

	/**
	 * 저정소로부터 제거한다.
	 * 
	 * @param <T>
	 * @param item       삭제할 내용, 테이블 정보 및 PK값을 가지고 있다.
	 * @param callBefore 삭제하기 전에 호출한다.
	 * @return
	 * @throws Exception
	 */
	protected <T> T delete(T item, FxDaoCallBefore<T> callBefore) throws Exception {

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();

			if (callBefore != null) {
				callBefore.onCall(tran, item);
			}

			tran.deleteOfObject(item.getClass(), item);

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

	protected boolean delete(Class<?> classOfTable, Map<String, Object> para,
			FxDaoCallBefore<Map<String, Object>> callBefore) throws Exception {

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();

			if (callBefore != null) {
				callBefore.onCall(tran, para);
			}

			tran.deleteOfClass(classOfTable, para);

			tran.commit();

			return true;

		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}

	}

	/**
	 * 메소드를 가져온다.
	 * 
	 * @param javaMethodName
	 * @return
	 */
	protected MethodData getJavaMethod(String javaMethodName) {
		for (MethodData method : methodList) {
			if (method.method.getName().equals(javaMethodName)) {
				return method;
			}
		}
		return null;
	}

	/**
	 * 자바메소드명을 가져온다.
	 * 
	 * @param name
	 * @return
	 */
	protected String getJavaMethodName(String name) {

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

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	protected abstract QidDao getQidDao() throws Exception;

	/**
	 * 
	 * @param status
	 * @param obj
	 * @param method
	 * @return
	 */
	protected byte[] getResult(HttpStatusCode status, String message, Object data) {
		FxResponse response = new FxResponse(status, message, data);

		String value = FxmsUtil.toJson(response);

//		if (status == HttpStatusCode.OK && method != null) {
//			File file = new File(FxCfg.getFile(FxCfg.getHomeDeploy(), "test", method + "-result.txt"));
//			if (file.exists() == false) {
//				FileUtil.writeToFile(file.getPath(), value, false);
//			}
//		}
		try {
			return value.getBytes(CHARSET);
		} catch (UnsupportedEncodingException e) {
			return value.getBytes();
		}
	}

	/**
	 * 
	 * @param he
	 * @return
	 */
	protected String getSessionId(HttpExchange he) {
		String sessionId = getCookie(he, "FXMS-SESSIONID");
		if (sessionId != null) {
			return sessionId;
		}

		String jwt = getJwt(he);
		if (jwt != null) {
			try {
				FxJwtVo vo = new FxJwtVo(jwt);
				if (vo.getSessionId() != null) {
					return vo.getSessionId();
				}
			} catch (Exception e) {
			}
		}
		return null;
	}

	protected String getString(Map<String, Object> parameters, String name) throws Exception {
		Object val = parameters.get(name);
		if (val == null)
			throw new NotDefineException(name);
		return val.toString();
	}

	/**
	 * 가능한 메소드 및 파라메티를 제공한다.
	 * 
	 * @return
	 */
	protected String help() {

		FxTableMaker maker = new FxTableMaker();

		StringBuffer ret = new StringBuffer();
		ret.append("<h1>method-list</h1>");
		for (MethodData data : methodList) {

			MethodDescr descr = data.method.getAnnotation(MethodDescr.class);
			ret.append("<article>");
			ret.append("<h4>");
			if (descr != null) {
			
				ret.append("API Name : ").append(descr.name()).append("<br>");
				ret.append("API Description : ").append(descr.description()).append("<br>");
			}
			ret.append("API URL : ").append(data.name).append("<br>");
			ret.append("</h4>");
			
			ret.append("<h5>Input Parameters</h5>");

			List<Column> list = new ArrayList<Column>();
			for (Class<?> cls : data.paraClassOf) {
				try {
					list.addAll(maker.getColumnAll(cls));
				} catch (Exception e) {
				}
			}
			for (Column col : list) {
				ret.append(col.getFieldName() + "(" + col.getFieldType().getSimpleName() + ") : " + col.getComments()
						+ (col.isNullable() == false ? " (필수)" : ""));
				ret.append("<br>");
			}

			for (Class<?> cls : data.paraClassOf) {
				try {
					ret.append(FxAttrApi.toHelp(cls));
				} catch (Exception e) {
				}
			}

			for (Class<?> cls : data.paraClassOf) {
				if (Map.class.isAssignableFrom(cls)) {
					ret.append("Map 기타 데이터<br>");
				}
			}
			ret.append("</article>");


			ret.append("</p>\n");

		}

		return ret.toString();
	}

	/**
	 * 
	 * @param <T>
	 * @param item
	 * @param callBefore
	 * @return
	 * @throws Exception
	 */
	protected <T> T insert(T item, FxDaoCallBefore<T> callBefore) throws Exception {

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();

			if (callBefore != null) {
				callBefore.onCall(tran, item);
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

	protected <T> T insert(Class<T> classOfTable, Object dto) throws Exception {
		T obj = ObjectUtil.toObject(dto, classOfTable);
		FxTableMaker.initRegChg(0, obj);
		ClassDaoEx.open().insertOfClass(classOfTable, obj).close();
		return obj;
	}

	protected void insertList(Class<?> classOfTable, List<?> list) throws Exception {

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();

			tran.insertOfClass(classOfTable, list);

			tran.commit();

		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}
	}

	protected MethodData makeMethodData(Method method) {

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
		MethodData data = new MethodData();
		data.name = sb.toString().toLowerCase();
		data.method = method;
		data.paraClassOf = method.getParameterTypes();

		return data;
	}

	/**
	 * 관할 관리대상, 설치위치인 경우만 제공하기 위해 조건을 추가한다.
	 * 
	 * @param session
	 * @param parameters
	 * @param paras
	 * @return
	 */
	protected Map<String, Object> makePara4Ownership(SessionVo session, Map<String, Object> parameters,
			Object... paras) {

		Map<String, Object> para;
		if (parameters != null)
			para = new HashMap<String, Object>(parameters);
		else
			para = new HashMap<String, Object>();

		for (int i = 0; i < paras.length; i += 2) {
			if (paras.length > i + 1) {
				para.put(paras[i].toString(), paras[i + 1]);
			}
		}

		if (para.get("inloNo") != null) {
			Object inloNo = para.remove("inloNo");
			para.put("inloNo in", " ( select LOWER_INLO_NO from FX_CF_INLO_MEM where INLO_NO = " + inloNo + " )");

		} else {
			para.put("inloNo in",
					" ( select LOWER_INLO_NO from FX_CF_INLO_MEM where INLO_NO = " + session.getInloNo() + " )");
		}

		return para;
	}

	/**
	 * 세션없이 그냥 호출하는 경우
	 * 
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
//	private Object onProcess(Map<String, Object> parameters) throws Exception {
//
//	
//		String javaMethodName = getJavaMethodName(name);
//
//		Logger.logger.info("parameters : {}", parameters);
//
//		Method javaMethod = getJavaMethod(javaMethodName);
//
//		if (javaMethod == null) {
//			String msg = "method=" + name + " not defined";
//			Logger.logger.fail(msg);
//			throw new Exception(msg);
//		}
//
//		long mstimeStart = System.currentTimeMillis();
//		int retNo = 0;
//		String retMsg = null;
//
//		try {
//			return javaMethod.invoke(this, new Object[] { (SessionVo) null, (Map<String, Object>) parameters });
//		} catch (IllegalAccessException e) {
//			retNo = -1;
//			retMsg = e.getClass().getName();
//			Logger.logger.error(e);
//			throw e;
//		} catch (IllegalArgumentException e) {
//			retNo = -1;
//			retMsg = e.getClass().getName();
//			Logger.logger.error(e);
//			throw e;
//		} catch (InvocationTargetException e) {
//			Throwable t = e.getTargetException();
//			retNo = -1;
//			retMsg = t.getClass().getName();
//			Logger.logger.error(t);
//			throw new Exception(t.getClass().getSimpleName() + ":" + t.getMessage());
//
//		} finally {
//
//		}
//	}

	/**
	 * 객체를 맵 형식으로 변경한다.
	 * 
	 * @param whereObj
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Map<String, Object> makeWherePara(Object whereObj) {
		if (whereObj == null) {
			return null;
		} else if (Map.class.isAssignableFrom(whereObj.getClass())) {
			return (Map) whereObj;
		} else {
			return ObjectUtil.toMap(whereObj);
		}
	}

	@Override
	protected byte[] onProcess(HttpExchange he, InetSocketAddress client, String mapping, String body)
			throws Exception {

		// String hostname = client.getHostName();
		Map<String, Object> para = null;

		if (body.length() == 0 && mapping == null) {
			StringBuffer sb = new StringBuffer();
			sb.append("<!DOCTYPE html>\n");
			sb.append("<html>\n");
			sb.append("<meta charset=\"").append(CHARSET).append("\">\n");
			sb.append("<body>\n");
			sb.append(help());
			sb.append("</body>\n");
			sb.append("</html>\n");
			return sb.toString().getBytes();
		}

		try {
			para = FxmsUtil.toMapFromJson(body);
		} catch (Exception e) {
			Logger.logger.error(e);
			return getResult(HttpStatusCode.BadRequest, Lang.get("The request is not in JSON format."), null);
		}

		try {

			SessionVo session;
			String jwt = this.getJwt(he);
			if (jwt == null) {
				return getResult(HttpStatusCode.Unauthorized, Lang.get("No token information."), null); // 토큰 정보가 없습니다.
			}

			session = UserApi.getApi().getSession(jwt);
			if (session == null) {
				return getResult(HttpStatusCode.Unauthorized, Lang.get("You are not logged in."), null); // 로그인하지 않았습니다.
			}

			broadcast(new TargetFxEvent("method-call", mapping));

			// parameters가 StringMap이므로 변환하여 적용한다.
			Object value = process(session, mapping, body, convertToHashMap(para));

			return getResult(HttpStatusCode.OK, null, value);

		} catch (OpDenyException e) {
			return getResult(HttpStatusCode.Forbidden, e.getMessage(), null);
		} catch (NotDefineException e) {
			try {
				Object value = process(null, mapping, body, convertToHashMap(para));
				return getResult(HttpStatusCode.OK, null, value);
			} catch (Exception e2) {
				Logger.logger.error(e);
				return getResult(HttpStatusCode.InternalServerError, e.getMessage(), null);
			}
		} catch (NotFoundException e) {
			return getResult(HttpStatusCode.BadRequest, e.getMessage(), e.getValue());
		} catch (Exception e) {
			return getResult(HttpStatusCode.InternalServerError, e.getMessage(), null);
		} finally {
			he.getResponseHeaders().add("Content-Type", "application/json");
		}
	}

	protected <T> T update(T item, FxDaoCallBefore<T> callback) throws Exception {

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();

			if (callback != null) {
				callback.onCall(tran, item);
			}

			tran.updateOfClass(item.getClass(), item);

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

	private String getCookie(HttpExchange he, String name) {
		Headers header = he.getRequestHeaders();
		List<String> entry = header.get("Cookie");
		if (entry != null) {
			for (String str : entry) {
				String ss[] = str.split(";");
				for (String s : ss) {
					s = s.trim();
					if (s.startsWith(name)) {
						return s.replaceFirst(name + "=", "");
					}
				}
			}
		}
		return null;
	}

	/**
	 * Header.Authorization 찾고 없으면 Cookie에서 FXMS-JWT로 찾는다.
	 * 
	 * @param he
	 * @return
	 */
	private String getJwt(HttpExchange he) {
		Headers header = he.getRequestHeaders();

		List<String> entry = header.get("Authorization");
		if (entry != null) {
			for (String str : entry) {
				if (str.startsWith("Bearer ")) {
					return str.replaceFirst("Bearer ", "");
				}
			}
		}
		return getCookie(he, "FXMS-TOKEN");
	}

	private List<MethodData> getMethodData() {

		List<MethodData> list = new ArrayList<MethodData>();

		MethodData md;
		StringBuffer sb = new StringBuffer();
		Class<?> para[];

		for (Method method : getClass().getMethods()) {

			if (Modifier.isStatic(method.getModifiers()))
				continue;

			para = method.getParameterTypes();

			// 첫번째 인자는 SessionVo이여야 한다.
			if (para.length >= 1) {
				if (SessionVo.class.isAssignableFrom(para[0])) {
					md = makeMethodData(method);
					list.add(md);
					sb.append(Logger.makeSubString(md.name, md.method.getName()));
				}
			}
		}

		Collections.sort(list, new Comparator<MethodData>() {
			@Override
			public int compare(MethodData o1, MethodData o2) {
				return o1.name.compareTo(o2.name);
			}
		});

		Logger.logger.info(Logger.makeString(getClass().getSimpleName(), "Method=" + list.size(), sb.toString()));

		return list;
	}

	/**
	 * 
	 * @param session    세션
	 * @param opId       운용명
	 * @param parameters 인자
	 * @return 결과
	 * @throws Exception
	 */
	private Object process(SessionVo session, String opId, String body, Map<String, Object> parameters)
			throws Exception {

		// 권한 확인
		if (OpApi.getApi().isAccesable(session.getUgrpNo(), opId) == false) {
			throw new OpDenyException(session.getUserId(), session.getUgrpNo(), opId);
		}

		// 사용자 로그 처리
		UserLogDbo logObj = new UserLogDbo(session.getUserNo(), session.getSessionId(), opId, null);
		logObj.setUserName(session.getUserName());
		logObj.setStrtDtm(DateUtil.getDtm());

		// 메소드 확인
		String javaMethodName = getJavaMethodName(opId);
		MethodData methodData = getJavaMethod(javaMethodName);

		Logger.logger.info("handler={}, session={}, method={} -> {} :: {}", getClass().getSimpleName(),
				session.getSessionId(), opId, javaMethodName, methodData == null ? "not found" : "ok");

		if (methodData == null) {
			throw new MethodNotFoundException(opId);
		}

		Method javaMethod = methodData.method;

		try {

			// 메소드 파라메터 생성
			Object paraObj[] = new Object[methodData.paraClassOf.length];
			paraObj[0] = session;
			for (int i = 1; i < methodData.paraClassOf.length; i++) {
				if (methodData.paraClassOf[i].isInstance(parameters)) {
					paraObj[i] = parameters;
				} else if (methodData.paraClassOf[i].isInstance(logObj)) {
					paraObj[i] = logObj;
				} else if (methodData.paraClassOf[i] == String.class) {
					paraObj[i] = body;
				} else {
					paraObj[i] = convert(session, parameters, methodData.paraClassOf[i], true);
				}
			}

			// 메소드 호출
			return javaMethod.invoke(this, paraObj);
		} catch (InvocationTargetException e) {
			Throwable t = e.getTargetException();
			logObj.setRstNo(-1);
			logObj.setOutRet(t.getClass().getName());
			Logger.logger.error(t);
			throw new FxException(t.getClass().getSimpleName(), t.getMessage());
		} catch (Exception e) {
			logObj.setRstNo(-1);
			logObj.setOutRet(e.getClass().getName());
			Logger.logger.error(e);
			throw e;
		} finally {

			logObj.setEndDtm(DateUtil.getDtm());

			new LogUserWorkHstDfo().log(logObj);

		}
	}

	/*
	 * 
	 * protected <T> T setDelYn(T item, FxDaoCallBefore<T> callback) throws
	 * Exception {
	 * 
	 * ClassDao tran =
	 * DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();
	 * 
	 * try { tran.start();
	 * 
	 * if (callback != null) { callback.onCall(tran, item); }
	 * 
	 * tran.updateOfClass(item.getClass(), item);
	 * 
	 * tran.commit();
	 * 
	 * return item;
	 * 
	 * } catch (Exception e) { Logger.logger.error(e); tran.rollback(); throw e; }
	 * finally { tran.stop(); }
	 * 
	 * }
	 * 
	 * 
	 * protected void checkUpdatePk(Class<?> classOfT, Map<String, Object>
	 * parameters) throws Exception { try { List<Table> tabList = new
	 * FxTableMaker().getTables(classOfT); List<Column> colList; Object value;
	 * StringBuffer sb = new StringBuffer();
	 * 
	 * for (Table tab : tabList) { colList = tab.getPkColumns(); for (Column col :
	 * colList) { value = parameters.get(col.getFieldName()); if (value == null) {
	 * if (sb.length() > 0) { sb.append(","); } sb.append(col.getFieldName()); } } }
	 * 
	 * if (sb.length() > 0) { throw new NotDefineException(sb.toString()); } } catch
	 * (Exception e) { Logger.logger.error(e); throw e; } }
	 * 
	 */
}
