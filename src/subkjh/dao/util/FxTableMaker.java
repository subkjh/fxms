package subkjh.dao.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.exp.AttrNotFoundException;
import subkjh.bas.co.utils.DateUtil;
import subkjh.bas.co.utils.ObjectUtil;
import subkjh.dao.def.Column;
import subkjh.dao.def.Column.COLUMN_TYPE;
import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxIndexes;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index;
import subkjh.dao.def.Table;
import subkjh.dao.exp.NotFxTableException;
import subkjh.dao.exp.ZeroColumnException;

/**
 * Java Class를 이용하여 Table 정보를 추출하는 클래스
 * 
 * @author subkjh
 *
 */
public class FxTableMaker {

	/**
	 * 캐슁할 때 사용할 맵
	 */
	public static Map<String, Object> tableMap = new HashMap<String, Object>();

	private static boolean isEmpty(Object o) {
		if (o == null) {
			return true;
		}

		return o.toString().isEmpty();
	}

	/**
	 * 
	 * @param <T>
	 * @param session
	 * @param datas
	 * @param classOfItem
	 * @return
	 * @throws Exception
	 */
	public static <T> T toObject(Map<String, Object> datas, Class<T> classOfItem, boolean checkMandatory)
			throws AttrNotFoundException, Exception {

		// 필수 항목 체크
		if (checkMandatory) {

			List<String> mandatoryColumnList = FxTableMaker.getColumnNameNotNullList(classOfItem);

			// 필요한 인자가 있는지 확인한다.
			// 없으면 AttrNotFoundException를 보낸다.
			StringBuffer sb = new StringBuffer();
			for (String attr : mandatoryColumnList) {
				if (datas.get(attr) == null) {
					if (sb.length() > 0) {
						sb.append(",");
					}
					sb.append(attr);
				}
			}
			if (sb.length() > 0) {
				throw new AttrNotFoundException(sb.toString());
			}
		}

		// 사용할 객체를 생성하고 거기에 값을 넣는다.
		T item = classOfItem.newInstance();
		if (datas != null) {
			ObjectUtil.toObject(datas, item);
		}

		return item;
	}

	/**
	 * Not Null인 컬럼 목록을 제공한다.
	 * 
	 * @param classOfT DBO 자바 클래스
	 * @return
	 * @throws Exception
	 */
	public static List<String> getColumnNameNotNullList(Class<?> classOfT) throws Exception {
		List<String> ret = new ArrayList<String>();
		List<Table> tblList = getTableAll(classOfT);
		for (Table tbl : tblList) {
			for (Column col : tbl.getColumnNotNullList()) {
				// 중복은 제거한다.
				if (ret.contains(col.getFieldName()) == false) {
					ret.add(col.getFieldName());
				}
			}
		}
		return ret;
	}

	// private Column getColumn(FX_TAB_COL col) throws Exception {
	//
	// Column column = new Column();
	// column.setName(col.getColName());
	// column.setComments(col.getColComment());
	// column.setOperator(col.isUpdatebleYn() ? COLUMN_OP.all :
	// COLUMN_OP.insert);
	// column.setSequence(col.getSeqName());
	// column.setDataLength(col.getColSize());
	// column.setNullable(col.isNullableYn());
	// column.setDataType(col.getColType());
	//
	// if (column.getDataScale() > 0) {
	// column.setDataTypeDefined(
	// column.getDataType() + "(" + column.getDataLength() + "," +
	// column.getDataScale() + ")");
	// } else {
	// column.setDataTypeDefined(column.getDataType() + "(" +
	// column.getDataLength() + ")");
	// }
	//
	// return column;
	// }

	public static List<String> getColumnNameNullableList(Class<?> classOfT) throws Exception {
		List<String> ret = new ArrayList<String>();
		List<Table> tblList = getTableAll(classOfT);
		for (Table tbl : tblList) {
			for (Column col : tbl.getColumnNullableList()) {
				// 중복은 제거한다.
				if (ret.contains(col.getFieldName()) == false) {
					ret.add(col.getFieldName());
				}
			}
		}
		return ret;
	}

	/**
	 * 클래스가 사용하는 DBO 클래스를 추출한다.
	 * 
	 * @param classOfT
	 * @return
	 */
	public static Class<?> getDboClass(Class<?> classOfT) {

		Class<?> classOf = classOfT;

		while (true) {

			FxTable fxTable = classOf.getAnnotation(FxTable.class);
			if (fxTable != null)
				return classOf;

			classOf = classOf.getSuperclass();
			if (classOf == null || classOf == Object.class) {
				break;
			}
		}

		return null;

	}

	/**
	 * 캐쉬를 사용하여 해당 클래스의 테이블을 조회한다.
	 * 
	 * @param classOfMain
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static synchronized List<Table> getTableAll(Class<?> classOfT) throws Exception {

		Object ret = tableMap.get(classOfT.getName());
		if (ret instanceof List) {
			return (List<Table>) ret;
		} else if (ret instanceof Exception) {
			throw (Exception) ret;
		}

		try {
			List<Table> list = new FxTableMaker().getTables(classOfT);
			tableMap.put(classOfT.getName(), list);
			return list;
		} catch (Exception e) {
			tableMap.put(classOfT.getName(), e);
			throw e;
		}
	}

	/**
	 * 등록/수정 사용자 및 일자 설정<br>
	 * userNo = -1이면 업데이트하지 않는다.
	 * 
	 * @param userNo
	 * @param item
	 */
	public static void initRegChg(int userNo, Object item) {

		long dtm = DateUtil.getDtm();

		try {
			Field f = item.getClass().getDeclaredField("regDtm");
			ObjectUtil.setField(item, f, dtm);
		} catch (Exception e) {
		}
		try {
			Field f = item.getClass().getDeclaredField("chgDtm");
			ObjectUtil.setField(item, f, dtm);
		} catch (Exception e) {
		}

		if (userNo >= 0) {
			try {
				Field f = item.getClass().getDeclaredField("regUserNo");
				ObjectUtil.setField(item, f, userNo);
			} catch (Exception e) {
			}
			try {
				Field f = item.getClass().getDeclaredField("chgUserNo");
				ObjectUtil.setField(item, f, userNo);
			} catch (Exception e) {
			}
		}
	}

	public static void initRegChgMap(int userNo, Map<String, Object> datas) {

		long dtm = DateUtil.getDtm();

		datas.put("regDtm", dtm);
		datas.put("chgDtm", dtm);
		datas.put("regUserNo", userNo);
		datas.put("chgUserNo", userNo);

	}

	/**
	 * FxTable르 선언된 클래스인지 여부를 확인한다.
	 * 
	 * @param classOfT
	 * @return
	 */
	public static synchronized boolean isFxTable(Class<?> classOfT) {
		try {
			getTableAll(classOfT);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// private Index makeIndex(FX_TAB_IDX fxIdx) throws Exception {
	//
	// Index idx = new Index();
	// idx.setName(fxIdx.getIdxName());
	// idx.setColumns(fxIdx.getColNameList());
	// idx.setFkColumn(fxIdx.getFkColNameList());
	// idx.setFkTable(fxIdx.getFkTabName());
	// idx.setType(fxIdx.getIdxType());
	//
	// return idx;
	// }

	/**
	 * 테이블 정보 생성
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	// public Table getTable(FxTableVo vo) throws Exception {
	//
	// String idxColNames[];
	// Column column;
	// Table table = new Table();
	//
	// // set table
	// table.setName(vo.getTabName());
	// table.setComment(vo.getTabComment());
	//
	// // set indexes
	// for (FX_TAB_IDX idx : vo.getIndexes()) {
	// table.add(makeIndex(idx));
	// }
	//
	// // set columns
	// for (FX_TAB_COL col : vo.getColumns()) {
	// table.add(getColumn(col));
	// }
	//
	// // rename column of index
	// for (Index idx : table.getIndexList()) {
	// idxColNames = idx.getColArr();
	// idx.setColumnNameList(null);
	//
	// for (String colName : idxColNames) {
	// column = table.getColumn(colName);
	// if (idx.isPk()) {
	// column.setPk(true);
	// }
	// idx.addColumn(column.getName());
	// }
	//
	// if (isEmpty(idx.getFkColumn()) == false) {
	// column = table.getColumn(idx.getFkColumn());
	// idx.setFkColumn(column.getName());
	// }
	// }
	//
	// return table;
	//
	// }

//	public static void main(String[] args) {
//		FxTableMaker maker = new FxTableMaker();
//
//		try {
////			List<Table> tblList = maker.makeTableList(FX_AL_ALARM_HST.class);
////			for ( Table tbl : tblList) {
////				
////				System.out.println(tbl.getColumn("RLSE_YN"));
////			}
//
////			System.out.println(maker.getColumnNameNotNullList(FnMoEquipDbo.class));
////			System.out.println(maker.getColumnNameNullableList(FnMoEquipDbo.class));
//			// System.out.println(maker.getTableAll(DeleteDiagramNodeDbo.class));
////			List<Column> list = maker.getColumnAll(FX_MO_SENSOR.class);
////			for (Column col : list) {
////				System.out.println(col.getFieldName() + "(" + col.getFieldType().getSimpleName() + ") : "
////						+ col.getComments() + ", nullable=" + col.isNullable());
////			}
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}

	/**
	 * 
	 * @param r             데이터베이스로부터 조회한 결과셋
	 * @param classOfResult 담을 객체
	 * @return
	 * @throws Exception
	 */
	public static Object makeResult(ResultSet r, Class<?> classOfResult) throws Exception {

		if (classOfResult == null) {
			return r;
		} else if (Number.class.isAssignableFrom(classOfResult)) {
			if (classOfResult == Float.class) {
				return r.getFloat(1);
			} else if (classOfResult == Double.class) {
				return r.getDouble(1);
			} else if (classOfResult == Integer.class) {
				return r.getInt(1);
			} else if (classOfResult == Long.class) {
				return r.getLong(1);
			} else {
				return r.getBigDecimal(1);
			}
		}

		List<Table> tabList = FxTableMaker.getTableAll(classOfResult);

		Object target = classOfResult.newInstance();
		for (Table tab : tabList) {
			for (Column column : tab.getColumns()) {
				Field field = column.getField();
				if (field == null)
					continue;
				Object value = r.getObject(column.getName());

				try {
					ObjectUtil.setField(target, field, value);

				} catch (Exception e) {

					// Number에 공백을 넣으려고 할 경우 무시한다.
					if (ObjectUtil.isNumber(field.getType()) && isEmpty(value)) {
						continue;
					}

					throw new Exception(e.getClass().getName() + "(" + target.getClass().getSimpleName() + "."
							+ field.getName() + " : " + e.getMessage() + ")");
				}
			}
		}

		return target;
	}

	private Column getColumn(Field field) throws Exception {

		FxColumn annotation = field.getAnnotation(FxColumn.class);
		if (annotation == null)
			return null;

		Column column = new Column();

		field.setAccessible(true);

		try {
			column.setName(annotation.name());
			if (isEmpty(column.getName())) {
				column.setName(DaoUtil.getColumnName(field.getName()));
			}
			column.setField(field);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			column.setComments(annotation.comment());
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			column.setOperator(annotation.operator());
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			column.setSequence(annotation.sequence());
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			int size = annotation.size();
			if (size == 0) {
				column.setDataLength(Column.getDataLength(field));

			} else {
				column.setDataLength(size);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			column.setNullable(annotation.nullable());
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			column.setDataDefault(annotation.defValue());
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			column.setDataType(
					annotation.type() == COLUMN_TYPE.AUTO ? Column.getType(field).name() : annotation.type().name());
			if (column.getDataScale() > 0) {
				column.setDataTypeDefined(
						column.getDataType() + "(" + column.getDataLength() + "," + column.getDataScale() + ")");

			} else {
				column.setDataTypeDefined(column.getDataType() + "(" + column.getDataLength() + ")");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return column;

	}

	private List<Column> getColumns(Class<?> classOfSub) throws Exception {

		Column column;
		List<Column> list = new ArrayList<Column>();

		// set columns
		Field[] fields = classOfSub.getDeclaredFields();
		for (Field field : fields) {
			column = getColumn(field);
			if (column != null) {
				list.add(column);
			}
		}

		return list;
	}

	private Column getIndexColumn(Class<?> classOfTable, String columnName, Table table) throws Exception {

		Column column;
		classOfTable = classOfTable.getSuperclass();
		String fieldName = DaoUtil.getJavaFieldName(columnName, false);

		if (classOfTable == null) {
			throw new Exception("Table(" + table.getName() + ") FIELD(" + fieldName + ") Not Found");
		}

		Field field;
		try {
			field = classOfTable.getDeclaredField(fieldName);
			column = getColumn(field);
			if (column != null) {
				return column;
			}
		} catch (NoSuchFieldException e) {
			return getIndexColumn(classOfTable, columnName, table);
		} catch (SecurityException e) {
			e.printStackTrace();
		}

		return null;

	}

	private List<Index> getIndexes(Class<?> classOf) throws Exception {

		List<Index> idxList = new ArrayList<Index>();

		Annotation aArray[] = classOf.getAnnotations();

		if (aArray == null) {
			return null;
		}

		for (Annotation a : aArray) {
			if (a instanceof FxIndex) {
				idxList.add(makeIndex((FxIndex) a));
			} else if (a instanceof FxIndexes) {
				for (FxIndex i : ((FxIndexes) a).value()) {
					idxList.add(makeIndex(i));
				}
			}
		}

		return idxList;
	}

	private Table getTable(Class<?> classOfMain, Class<?> classOfSub) throws Exception {

		FxTable fxTable = classOfSub.getAnnotation(FxTable.class);
		if (fxTable == null)
			throw new NotFxTableException(classOfSub.getName());

		Column column;
		Table table = new Table();

		// set table
		table.setName(fxTable.name());
		table.setComment(fxTable.comment());

		// set indexes
		List<Index> idxList = getIndexes(classOfSub);
		if (idxList != null) {
			for (Index idx : idxList) {
				table.addIndex(idx);
			}
		}

		// set columns
		Field[] fields = classOfSub.getDeclaredFields();
		for (Field field : fields) {
			column = getColumn(field);
			if (column != null) {
				table.addColumn(column);
			}
		}

		// super class
		for (Index idx : table.getIndexList()) {
			for (String colName : idx.getColumnNames()) {
				column = table.getColumn(colName);
				if (column == null) {
					column = getIndexColumn(classOfMain, colName, table);
					if (column != null) {
						table.addColumn(column);
					}
				}

			}

		}

		// rename column of index

		for (Index idx : table.getIndexList()) {

			String oldCols[] = idx.getColumnNames().toArray(new String[idx.getColumnNames().size()]);
			idx.clearColumns();

			for (String colName : oldCols) {
				column = table.getColumn(colName);
				if (idx.isPk()) {
					column.setPk(true);
				}
				idx.addColumn(column.getName());
			}

			// 외부 컬럼이 이 테이블에 있을리 없음. 주석 처리함
			/*
			 * if (isEmpty(idx.getFkColumn()) == false) { column =
			 * table.getColumn(idx.getFkColumn()); idx.setFkColumn(column.getName()); }
			 */
		}

		return table;

	}

	private boolean isEmpty(String s) {
		return s == null || s.isEmpty();
	}

	private Index makeIndex(FxIndex fxIdx) throws Exception {

		Index idx = new Index(fxIdx.name(), fxIdx.type());
		idx.setColumns(Arrays.toString(fxIdx.columns()));
		idx.setFkColumn(fxIdx.fkColumn());
		idx.setFkTable(fxIdx.fkTable());

		return idx;
	}

	/**
	 * 
	 * @param classOfMain
	 * @return
	 * @throws Exception
	 */
	public String getAttrInfo(Class<?> classOfMain, boolean isWeb) throws Exception {

		List<Table> tabList = getTables(classOfMain);
		StringBuffer sb = new StringBuffer();

		for (Table table : tabList) {
			sb.append(table.getAllColumnInfo(isWeb));
		}

		return sb.toString();
	}

	public List<Column> getColumnAll(Class<?> classOfMain) throws Exception {

		List<Column> list = new ArrayList<Column>();
		List<Class<?>> classList = new ArrayList<Class<?>>();
		Class<?> classOf = classOfMain;

		while (true) {
			classList.add(classOf);
			classOf = classOf.getSuperclass();
			if (classOf == null || classOf == Object.class) {
				break;
			}
		}

		for (int i = classList.size() - 1; i >= 0; i--) {
			try {
				list.addAll(getColumns(classList.get(i)));
			} catch (NotFxTableException e) {
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	public String getTableName(Class<?> classOf) {
		FxTable table = classOf.getAnnotation(FxTable.class);
		return table != null ? table.name() : null;
	}

	/**
	 * 
	 * @param classOfMain
	 * @return
	 * @throws Exception
	 */
	public List<Table> getTables(Class<?> classOfMain) throws Exception {

		Table table;
		List<Table> tableList = new ArrayList<Table>();
		List<Class<?>> classList = new ArrayList<Class<?>>();
		Class<?> classOf = classOfMain;

		while (true) {
			classList.add(classOf);
			classOf = classOf.getSuperclass();
			if (classOf == null || classOf == Object.class) {
				break;
			}
		}

		for (int i = classList.size() - 1; i >= 0; i--) {
			try {
				table = getTable(classOfMain, classList.get(i));

				if (table.getColumns().size() == 0) {
					throw new ZeroColumnException(table.getName());
				}
				tableList.add(table);
			} catch (NotFxTableException e) {
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (tableList.size() == 0) {
			throw new Exception(classOfMain.getName() + " is not have tables.");
		}

		return tableList;
	}
}
