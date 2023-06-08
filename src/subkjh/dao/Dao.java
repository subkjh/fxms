package subkjh.dao;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.ObjectUtil;
import subkjh.dao.def.DaoListener;
import subkjh.dao.model.ColList;
import subkjh.dao.model.ColList.Mapp;
import subkjh.dao.model.DaoResult;
import subkjh.dao.model.DaoResult.ResultKeyCase;
import subkjh.dao.model.RetMappVo;
import subkjh.dao.util.DaoUtil;
import subkjh.dao.util.FxTableMaker;

public class Dao {

	protected DaoListener daoListener;

	protected ColList makeColumns(ResultSet r, ResultKeyCase keyCase) throws Exception {

		String colName;
		ColList list = new ColList();
		int colCnt = r.getMetaData().getColumnCount();
		for (int i = 1; i <= colCnt; i++) {
			colName = r.getMetaData().getColumnLabel(i);
			list.add(colName, keyCase.getKey(colName));
		}

		return list;
	}

	/**
	 * Object 배열로 넘긴다.
	 * 
	 * @param r
	 * @param colCnt
	 * @return
	 * @throws Exception
	 */
	protected Object[] makeResultArray(ResultSet r, ColList columns) throws Exception {
		Object ret[] = new Object[columns.size()];
		for (int i = 1; i <= columns.size(); i++)
			ret[i - 1] = r.getObject(i);
		return ret;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected List makeResultList(ResultSet r, DaoResult result) throws Exception {

		List ret = new ArrayList();
		int size = 0;
		int rowNo = 0;
		long ptime = System.currentTimeMillis();

		try {

			// 컬럼 목록
			ColList columns = makeColumns(r, result.keyCase);
			if (columns.isEmpty()) {
				return ret;
			}

			// 컬럼 목록 통보
			onStart(columns);

			// 데이터 조회
			while (r.next()) {

				Object entry = null;

				size++;

				// primitive가 아니면 객체를 생성해서 그 객체에 값을 넣는다.
				if (DaoUtil.isPrimitive(result.javaClass) == false) {
					if (result.isMap()) {
						entry = makeResultMap(r, columns);
					} else {
						entry = makeResultObject(r, result, columns);
					}
				}
				// primitive이면 첫번째 내용을 그 값에 넣는다.
				else {

					if (columns.size() == 1) {
						
						if (result.javaClass == Long.class && r.getObject(1) != null) {
							entry = r.getLong(1);
						} else if (result.javaClass == Integer.class && r.getObject(1) != null) {
							entry = r.getInt(1);
						} else if (result.javaClass == String.class && r.getObject(1) != null) {
							entry = r.getString(1);
							
						} else {
							entry = r.getObject(1);
						}
					} else {
						entry = makeResultArray(r, columns);
					}
				}

				if (onSelected(rowNo, entry) == false) {
					ret.add(entry);
				}

				rowNo++;
				if (System.currentTimeMillis() > ptime + 5000) {
					ptime += 5000;
					if (Logger.logger != null && Logger.logger.isTrace()) {
						Logger.logger.trace("row count = " + rowNo);
					}
				}
			}

			onFinish(null); // 통보

			if (Logger.logger != null && Logger.logger.isTrace()) {
				Logger.logger.trace("COL-LENGTH(" + columns.size() + ")RESULT-CLASS("
						+ (result == null ? "NULL" : result.javaClass.getSimpleName()) + ")SIZE(" + size + ")");
			}

		} catch (Exception e) {
			onFinish(e); // 통보
			throw e;
		}

		return ret;
	}

	/**
	 * Result내용으로 Map을 채운다.<br>
	 * 
	 * Map의 key는 SELECT문의 컴럼명이며 모두 소문자 처리한다.
	 * 
	 * @param colCnt
	 * @param r
	 * @return
	 */
	protected Map<String, Object> makeResultMap(ResultSet r, ColList columns) throws Exception {

		Map<String, Object> ret = new HashMap<>();
		Object value;

		for (Mapp p : columns.getList()) {
			value = r.getObject(p.column);
			if (value instanceof BigDecimal) {
				ret.put(p.field, ((BigDecimal) value).doubleValue());
			} else if (value instanceof BigInteger) {
				ret.put(p.field, ((BigInteger) value).longValue());
			} else {
				ret.put(p.field, value);
			}
		}

		return ret;
	}

	/**
	 * ResultSet를 map을 이용하여 target에 채운다.
	 * 
	 * @param r
	 * @param target
	 * @param result
	 * @return
	 * @throws Exception
	 */
	protected Object makeResultObject(ResultSet r, DaoResult result, ColList columns) throws Exception {

		Field f;
		boolean isOk;
		List<RetMappVo> fields = result.getMapp();

		// 필드 속성이 없다면 배열로 넘긴다.
		if (fields == null || fields.size() == 0) {

			if (FxTableMaker.isFxTable(result.javaClass)) {
				// FX 테이블 클래스에 넣기
				return FxTableMaker.makeResult(r, result.javaClass);
			} else {

				Object target = result.javaClass.newInstance();
				for (Mapp p : columns.getList()) {
					try {
						f = target.getClass().getDeclaredField(p.field);
						setField(target, f, r.getObject(p.column));
					} catch (NoSuchFieldException e) {
						// 클래스에 필드가 없으면 무시한다.
					} catch (Exception e) {
						throw new Exception(p.field + "-" + p.column + " : " + e.getClass().getName());
					}
				}
				return target;
			}

		} else {
			Object target = result.javaClass.newInstance();

			for (RetMappVo af : fields) {

				isOk = false;

				if (af.isJavaMethod()) {

					try {
						Method methods[] = target.getClass().getMethods();
						for (Method m : methods) {
							if (m.getName().equals(af.getJavaField()) && m.getParameterTypes().length == 1) {
								setMethod(target, m, r.getObject(af.getColumn()));
								isOk = true;
								break;
							}
						}
					} catch (Exception e) {
						throw new Exception(af.getJavaField() + "-" + af.getColumn() + " : " + e.getMessage());
					}
				} else {
					f = target.getClass().getField(af.getJavaField());
					try {
						setField(target, f, r.getObject(af.getColumn()));
						isOk = true;
					} catch (Exception e) {
						throw new Exception(af.getJavaField() + "-" + af.getColumn() + " : " + e.getMessage());
					}
				}

				if (isOk == false) {
					throw new Exception(af.getJavaField() + "-" + af.getColumn() + " : There is no method or field");
				}
			}
			return target;
		}

	}

	private void onFinish(Exception ex) throws Exception {
		if (daoListener != null) {
			daoListener.onFinish(ex);
		}
	}

	private boolean onSelected(int rowNo, Object data) throws Exception {
		if (daoListener != null) {
			daoListener.onSelected(rowNo, data);
			return true;
		}
		return false;
	}

	private void onStart(ColList columns) throws Exception {
		if (daoListener != null) {
			daoListener.onStart(columns.toColumnArray());
		}
	}

	private void setField(Object target, Field f, Object value) throws Exception {
		ObjectUtil.setField(target, f, value);
	}

	private void setMethod(Object target, Method method, Object value) throws Exception {
		try {
			ObjectUtil.setMethod(target, method, value);
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		}
	}

}
