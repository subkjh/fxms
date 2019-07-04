package fxms.bas.po.item;

import java.util.ArrayList;
import java.util.List;

import subkjh.bas.dao.data.Column;
import fxms.bas.co.def.PS_TYPE;
import fxms.bas.po.convertor.NullVC;
import fxms.bas.po.convertor.VC;

/**
 * @since 2017.06.16 09:27
 * @author subkjh autometic create by subkjh.dao
 *
 */

public class PsItem {

	private transient VC vc = new NullVC();

	private int dataScale = -1;
	private int dataLength = -1;
	private double unit;
	/** 반올림에 사용되는 변수 */
	private double rounding = 0.5d;

	private String statFuncs = "MAX";

	private String psTable;
	private String psColumn;
	private String psCode;
	private String psName;
	private String psFormat = "20,2";
	private String moTable;
	private String moColumn;
	private String moDateColumn;
	private String updateFilter;

	public PsItem() {
	}

	/**
	 * 계산식을 적용한 값을 제공합니다.
	 * 
	 * @param value
	 *            원래 값
	 * @return 계산식이 적용된 값
	 */
	public Number compute(Number value) {

		if (value == null)
			return value;

		if (vc != null && (vc instanceof NullVC) == false) {
			return vc.convert(value);
		}

		return value;
	}

	public Number convert(Number value) {

		long v = toLong(value);

		if (unit == 1)
			return v;

		return v / unit;
	}

	public void fillSql(StringBuffer target, StringBuffer source, PS_TYPE pstype) {

		String[] funcs = getRepresentativeValues();

		if (pstype == PS_TYPE.RAW) {
			for (String func : funcs) {
				target.append(getPsColumn() + "_" + func);
				source.append(func + "(" + getPsColumn() + ")");
			}
		} else {
			for (String func : funcs) {
				target.append(getPsColumn() + "_" + func);
				source.append(func + "(" + getPsColumn() + "_" + func + ")");
			}
		}

	}

	public int getDataLength() {
		if (dataLength < 0) {
			parsePsFormat();
		}

		return dataLength;
	}

	public int getDataScale() {
		if (dataScale < 0) {
			parsePsFormat();
		}

		return dataScale;
	}

	public String getMoColumn() {
		return moColumn;
	}

	public String getMoDateColumn() {
		return moDateColumn;
	}

	public String getMoTable() {
		return moTable;
	}

	public String getPsCode() {
		return psCode;
	}

	public String getPsColumn() {
		return psColumn;
	}

	public String getPsFormat() {
		return psFormat;
	}

	public String getPsName() {
		return psName;
	}

	public String getPsTable() {
		return psTable;
	}

	public String[] getRepresentativeValues() {
		if (getStatFuncs() == null || getStatFuncs().length() == 0) {
			return null;
		}
		return getStatFuncs().split(",");
	}

	public String getStatFuncs() {
		return statFuncs;
	}

	public String getUpdateFilter() {
		return updateFilter;
	}

	/**
	 * 
	 * @return Long형 값인지 여부
	 */
	public boolean isLongValue() {
		return getDataScale() <= 0;
	}

	/**
	 * 업데이트 가능한지 여부
	 * 
	 * @return
	 */
	public boolean isUpdate() {
		if (getMoTable() == null || getMoTable().trim().length() == 0 || getMoColumn() == null
				|| getMoColumn().trim().length() == 0)
			return false;
		return true;
	}

	public Column makeColumn(PS_TYPE pstype) {

		String funcs[];

		Column column = new Column();
		if (pstype == null || pstype == PS_TYPE.RAW) {
			funcs = new String[] { null };
		} else {
			funcs = getRepresentativeValues();
			if (funcs == null) {
				funcs = new String[] { null };
			}
		}

		for (String func : funcs) {
			column.setName(getPsColumn() + (func == null ? "" : "_" + func.toUpperCase()));
			column.setDataLength(getDataLength());
			column.setDataScale(getDataScale());
			column.setDataType("Number");
			column.setNullable(true);

			return column;
		}

		return null;
	}

	public List<Column> makeColumns(PS_TYPE pstype) {
		String funcs[];
		List<Column> colList = new ArrayList<Column>();

		if (pstype == PS_TYPE.RAW) {
			funcs = new String[] { null };
		} else {
			funcs = getRepresentativeValues();
			if (funcs == null) {
				funcs = new String[] { null };
			}
		}

		for (String func : funcs) {
			Column column = new Column();
			column.setName(getPsColumn() + (func == null ? "" : "_" + func.toUpperCase()));
			column.setDataLength(getDataLength());
			column.setDataScale(getDataScale());
			column.setDataType("Number");
			column.setNullable(true);

			colList.add(column);
		}

		return colList;
	}

	@Override
	public String toString() {
		return "PS(" + getPsCode() + ")";
	}

	public String toStringConvert(Number value) {

		if (value == null)
			return "";

		if (vc != null && (vc instanceof NullVC) == false) {
			return vc.toStringConvert(value);
		}

		return value + "";
	}

	private void parsePsFormat() {

		dataScale = 0;
		dataLength = 19;

		if (getPsFormat() != null) {
			String ss[] = getPsFormat().split(",");
			if (ss.length >= 1) {
				try {
					dataLength = Integer.parseInt(ss[0]);
				} catch (Exception e) {
				}
			}
			if (ss.length >= 2) {
				try {
					dataScale = Integer.parseInt(ss[1]);
				} catch (Exception e) {
				}
			}

		}

	}

	private long toLong(Number value) {

		if (unit == 0) {
			unit = 1;
			for (int i = 0; i < getDataScale(); i++) {
				unit *= 10d;
				rounding = rounding / 10;
			}
		}

		long v;
		if (value.doubleValue() < 0) {
			v = (long) ((value.doubleValue() - rounding) * unit);
		} else {
			v = (long) ((value.doubleValue() + rounding) * unit);
		}
		return v;
	}
}
