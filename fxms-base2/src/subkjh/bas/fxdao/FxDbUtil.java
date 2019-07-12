package subkjh.bas.fxdao;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import subkjh.bas.dao.data.Column;
import subkjh.bas.dao.data.Index;
import subkjh.bas.dao.data.SoDo;
import subkjh.bas.dao.data.Table;
import subkjh.bas.dao.database.DataBase;
import subkjh.bas.dao.database.Oracle;
import subkjh.bas.dao.define.COLUMN_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxIndexes;
import subkjh.bas.fxdao.define.FxTable;
import subkjh.bas.fxdao.exception.NotFxTableException;

public class FxDbUtil {

	DataBase database = new Oracle();

	public static void main(String[] args) throws Exception {

		FxDbUtil util = new FxDbUtil();

//		util.insert();
		// util.delete();
		// util.createTable();
	}
//
//	void insert() throws Exception {
//		ServiceMo mo = new ServiceMo();
//
//		List<Table> tableList = getTableAll(mo);
//		QueryMaker maker = new QueryMaker();
//
//		for (Table table : tableList) {
//			System.out.println(maker.getInsertQueryResult(table, mo).getDebug());
//		}
//	}
//
//	void delete() throws Exception {
//		ServiceMo mo = new ServiceMo();
//		QueryMaker maker = new QueryMaker();
//		List<Table> tableList = getTableAll(mo);
//
//		for (int i = tableList.size() - 1; i >= 0; i--) {
//			System.out.println(tableList.get(i).getDebug());
//			System.out.println(maker.getDeleteSql(tableList.get(i)));
////			System.out.println(maker.getUpdateSql(tableList.get(i)));
//		}
//	}
//
//	void createTable() throws Exception {
//		List<String> sqlList;
//
//		ServiceMo t = new ServiceMo();
//		FxDbUtil util = new FxDbUtil();
//
//		Table table = util.getTable(t);
//		System.out.println(database.getSqlCreate(table));
//		sqlList = database.getSqlCreateIndex(table);
//		for (String sql : sqlList) {
//			System.out.println(sql);
//		}
//		sqlList = database.getSqlComment(table);
//		for (String sql : sqlList) {
//			System.out.println(sql);
//		}
//		System.out.println("-----------------------------------------------------------");
//
//		Mo t2 = new ServiceMo();
//		table = util.getTable(t2);
//		System.out.println(database.getSqlCreate(table));
//		sqlList = database.getSqlCreateIndex(table);
//		for (String sql : sqlList) {
//			System.out.println(sql);
//		}
//		sqlList = database.getSqlComment(table);
//		for (String sql : sqlList) {
//			System.out.println(sql);
//		}
//	}

	private Column getColumn(Object o, Field field) throws Exception {

		FxColumn annotation = field.getAnnotation(FxColumn.class);
		if (annotation == null)
			return null;
		Column column = new Column();

		field.setAccessible(true);

		try {
			column.setName(annotation.name());
			if (isEmpty(column.getName())) {
				column.setName(getColumnName(field.getName()));
			}
			column.setFieldName(field.getName());
			column.setFieldType(field.getType());
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

	private String getColumnName(String s) {
		return SoDo.getDaoName(s);
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

	public Table getTable(Object o) throws Exception {
		return getTable(o, o.getClass());
	}

	public List<Table> getTableAll(Object o) throws Exception {
		Table table;
		List<Table> tableList = new ArrayList<Table>();
		List<Class<?>> classList = new ArrayList<Class<?>>();
		Class<?> classOf = o.getClass();
		while (true) {
			classList.add(classOf);
			classOf = classOf.getSuperclass();
			if (classOf == null || classOf == Object.class) {
				break;
			}
		}

		for (int i = classList.size() - 1; i >= 0; i--) {
			try {
				table = getTable(o, classList.get(i));
				tableList.add(table);
			} catch (NotFxTableException e) {
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return tableList;
	}

	public Table getTable(Object o, Class<?> classOf) throws Exception {

		FxTable fxTable = classOf.getAnnotation(FxTable.class);
		if (fxTable == null)
			throw new NotFxTableException(classOf.getName());

		String idxColNames[];
		Column column;
		Table table = new Table();

		// set table
		table.setName(fxTable.name());
		table.setComment(fxTable.comment());

		// set indexes
		List<Index> idxList = getIndexes(classOf);
		if (idxList != null) {
			for (Index idx : idxList) {
				idx.setName(idx.getName());
				table.addIndex(idx);
			}
		}

		// set columns
		Field[] fields = classOf.getDeclaredFields();
		for (Field field : fields) {
			column = getColumn(o, field);
			if (column != null) {
				table.addColumn(column);
			}
		}

		// super class
		for (Index idx : table.getIndexList()) {
			idxColNames = idx.getColArr();
			for (String colName : idxColNames) {
				column = table.getColumn(colName);
				if (column == null) {
					column = getIndexColumn(o, o.getClass(), colName, table);
					if (column != null) {
						table.addColumn(column);
					}
				}

			}

		}

		// rename column of index

		for (Index idx : table.getIndexList()) {
			idxColNames = idx.getColArr();
			idx.setColumnNameList(null);

			for (String colName : idxColNames) {
				column = table.getColumn(colName);
				if (idx.isPk()) {
					column.setPk(true);
				}
				idx.addColumn(column.getName());
			}

			if (isEmpty(idx.getFkColumn()) == false) {
				column = table.getColumn(idx.getFkColumn());
				idx.setFkColumn(column.getName());
			}
		}

		return table;

	}

	private Column getIndexColumn(Object o, Class<?> classOf, String colName, Table table) throws Exception {
		Column column;
		classOf = classOf.getSuperclass();

		if (classOf == null) {
			throw new Exception("COLUMN(" + colName + ") Not Found");
		}

		Field field;
		try {
			field = classOf.getDeclaredField(colName);
			column = getColumn(o, field);
			if (column != null) {
				return column;
			}
		} catch (NoSuchFieldException e) {
			return getIndexColumn(o, classOf, colName, table);
		} catch (SecurityException e) {
			e.printStackTrace();
		}

		return null;

	}

	private boolean isEmpty(String s) {
		return s == null || s.isEmpty();
	}

	private Index makeIndex(FxIndex fxIdx) throws Exception {

		Index idx = new Index();
		idx.setName(fxIdx.name());
		idx.setColumns(Arrays.toString(fxIdx.columns()));
		idx.setFkColumn(fxIdx.fkColumn());
		idx.setFkTable(fxIdx.fkTable());
		idx.setType(fxIdx.type().name());

		return idx;
	}
}
