package subkjh.bas.fxdao.control;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import subkjh.bas.dao.data.Column;
import subkjh.bas.dao.data.Index;
import subkjh.bas.dao.data.SoDo;
import subkjh.bas.dao.data.Table;
import subkjh.bas.dao.define.COLUMN_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxIndexes;
import subkjh.bas.fxdao.define.FxTable;
import subkjh.bas.fxdao.exception.NotFxTableException;
import subkjh.bas.fxdao.exception.ZeroColumnException;

public class FxTableMaker {

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

	/**
	 * 
	 * @param classOfMain
	 * @return
	 * @throws Exception
	 */
	public List<Table> makeTableList(Class<?> classOfMain) throws Exception {

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

				if (table.getColumnList().size() == 0) {
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

	public String getAttrInfo(Class<?> classOfMain) throws Exception {

		List<Table> tabList = makeTableList(classOfMain);
		StringBuffer man = new StringBuffer();
		StringBuffer opt = new StringBuffer();

		for (Table table : tabList) {
			table.fillColInfo(man, opt);
		}

		return man.toString() + "\n" + opt.toString();
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
				column.setName(getColumnName(field.getName()));
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
			column.setDataType(annotation.type() == COLUMN_TYPE.AUTO ? Column.getType(field).name() : annotation.type().name());
			if (column.getDataScale() > 0) {
				column.setDataTypeDefined(column.getDataType() + "(" + column.getDataLength() + "," + column.getDataScale() + ")");

			} else {
				column.setDataTypeDefined(column.getDataType() + "(" + column.getDataLength() + ")");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return column;

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

	private String getColumnName(String s) {
		return SoDo.getDaoName(s);
	}

	private Column getIndexColumn(Class<?> classOfTable, String columnName, Table table) throws Exception {

		Column column;
		classOfTable = classOfTable.getSuperclass();
		String fieldName = Column.getJavaFieldName(columnName, false);

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

		String idxColNames[];
		Column column;
		Table table = new Table();

		// set table
		table.setName(fxTable.name());
		table.setComment(fxTable.comment());

		// set indexes
		List<Index> idxList = getIndexes(classOfSub);
		if (idxList != null) {
			for (Index idx : idxList) {
				idx.setName(idx.getName());
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
			idxColNames = idx.getColArr();
			for (String colName : idxColNames) {
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

	private boolean isEmpty(String s) {
		return s == null || s.isEmpty();
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
