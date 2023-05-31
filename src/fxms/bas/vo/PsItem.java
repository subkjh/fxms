package fxms.bas.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fxms.bas.poller.vo.NullVC;
import fxms.bas.poller.vo.VC;
import subkjh.dao.def.Column;

/**
 * @since 2017.06.16 09:27
 * @author subkjh autometic create by subkjh.dao
 *
 */

public class PsItem implements Serializable {

	public enum PS_VAL_TYPE {
		/** 순간값(MV, Moment Value) */
		MV,
		/** 누적값(AV,Accumulate Value) */
		AV,
		/** 계산된 값(CV,ComputedValue) */
		CV,
		/** 상태값(SV, State Value ) */
		SV;

		public static PS_VAL_TYPE getValType(String s) {
			for (PS_VAL_TYPE e : PS_VAL_TYPE.values()) {
				if (e.name().equalsIgnoreCase(s)) {
					return e;
				}
			}
			return MV;
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -1240475083267794332L;

	public static final String RESERVE_FUNCTIONS[] = new String[] { "MIN", "MAX", "SUM", "AVG" };

	private transient VC vc = new NullVC();
	private int dataScale = -1;
	private int dataLength = -1;

	/** 반올림에 사용되는 변수 */
	private double rounding = 0.5d;
	private String moTable;
	private String moColumn;
	private String moDateColumn;

	private String updateFilter;

	private Number nullVal;
	private Number maxVal;
	private Number minVal;
	private Number dftVal;

	private boolean isUse = true;
	private final String psFmt;
	private final String psTable;
	private final String psColumn;
	private final String psUnit;
	private final String psId;
	private final double psScale;
	private final String psName;
	private final String psKindCols[];
	private final String moClass; // 담당 MO분류
	private final String moType; // 담당 MO유형
	private final String psGrp;
	private final String psMemo;
	private final PS_VAL_TYPE psValType;

	public PsItem(String psId, String psName, String psTbl, String psCol, String psFmt, String psUnit,
			String psKindCols, Double psScale, String moClass, String moType, String psGrp, String psValType,
			String psMemo) {
		this.psId = psId;
		this.psName = psName;
		this.psTable = psTbl;
		this.psColumn = psCol;
		this.psFmt = psFmt;
		this.psUnit = psUnit;
		this.psMemo = psMemo;
		this.psScale = psScale == null || psScale.doubleValue() == 0 ? 1D : psScale.doubleValue();
		this.moClass = moClass != null ? moClass.trim() : null;
		this.moType = moType != null ? moType.trim() : null;
		this.psGrp = psGrp != null ? psGrp.trim() : null;
		this.psValType = PS_VAL_TYPE.getValType(psValType);

		if (psKindCols != null) {
			this.psKindCols = psKindCols.split(",");
			for (int i = 0; i < this.psKindCols.length; i++) {
				this.psKindCols[i] = this.psKindCols[i].trim().toUpperCase();
			}
		} else {
			this.psKindCols = new String[0];
		}

	}

	private void parsePsFormat() {

		dataScale = 0;
		dataLength = 19;

		if (getPsFmt() != null) {
			String ss[] = getPsFmt().split(",");
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

	/**
	 * 계산식을 적용한 값을 제공합니다.
	 * 
	 * @param value 원래 값
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

		long v = value.longValue();

		if (psScale == 1)
			return v;

		return v / psScale;
	}

	public String[] getPsKindCols() {
		return psKindCols;
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

	public Number getDftVal() {
		return dftVal;
	}

	public Number getMaxVal() {
		return maxVal;
	}

	public Number getMinVal() {
		return minVal;
	}

	public String getMoClass() {
		return moClass;
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

	public String getMoType() {
		return moType;

	}

	public Number getNullVal() {
		return nullVal;
	}

	public String getPsColumn() {
		return psColumn;
	}

	public String getPsFmt() {
		return psFmt;
	}

	public String getPsGrp() {
		return psGrp;
	}

	public String getPsId() {
		return psId;
	}

	public String getPsName() {
		return psName;
	}

	public String getPsTable() {
		return psTable;
	}

	public String getPsUnit() {
		return psUnit;
	}

	public PS_VAL_TYPE getPsValType() {
		return psValType;
	}

	/**
	 * 이 성능항목의 기본 컬럼을 조회한다.
	 * 
	 * @return
	 */
	public String getDefKindCol() {
		return this.psKindCols.length > 0 ? this.psKindCols[0] : null;
	}

//	/**
//	 * 해당 컬럼 생성
//	 * 
//	 * @param pstype
//	 * @return
//	 */
//	public Column makeColumn(PS_DATA_CD pstype, PsStatFunc func) {
//
//		Column column = new Column();
//		if (pstype == null || pstype == PS_DATA_CD.RAW) {
//			column.setName(getPsColumn());
//		} else {
//			column.setName(getPsColumn() + "_" + func.getStatColumnName());
//		}
//
//		column.setDataLength(getDataLength());
//		column.setDataScale(getDataScale());
//		column.setDataType("Number");
//		column.setNullable(true);
//
//		return column;
//	}

	public String[] getKindCols() {
		return psKindCols;
	}

//	/**
//	 * 성능이 가지고 있는 통계 컬럼을 조회한다.
//	 * 
//	 * @param pstype
//	 * @return
//	 */
//	public List<Column> makeColumns(PS_DATA_CD pstype) {
//		List<Column> colList = new ArrayList<Column>();
//
//		if (pstype == PS_DATA_CD.RAW) {
//			colList.add(makeColumn(pstype, null));
//
//		} else {
//			for (PsStatFunc func : getPsStatFuncList()) {
//				colList.add(makeColumn(pstype, func));
//			}
//		}
//
//		return colList;
//	}

	public String getUpdateFilter() {
		return updateFilter;
	}

	public boolean hasTable() {
		return this.psTable != null && this.psTable.trim().length() > 0;
	}

	public boolean existKindCol(String statFunc) {
		for (String s : this.psKindCols) {
			if (s.equalsIgnoreCase(statFunc)) {
				return true;
			}
		}
		return false;
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

	public boolean isUse() {
		return isUse;
	}

	/**
	 * 
	 * @param psKind
	 * @param func
	 * @return
	 */
	public Column makeColumn(PsKind psKind, String psKindCol) {

		Column column = new Column();
		if (psKind.isRaw()) {
			column.setName(getPsColumn());
		} else {
			column.setName(getPsColumn() + "_" + psKindCol);
		}

		column.setDataLength(getDataLength());
		column.setDataScale(getDataScale());
		column.setDataType("Number");
		column.setNullable(true);

		return column;
	}

	public List<Column> makeColumns(PsKind psKind) {
		List<Column> colList = new ArrayList<Column>();

		if (psKind.isRaw()) {
			colList.add(makeColumn(psKind, null));

		} else {
			for (String func : this.psKindCols) {
				colList.add(makeColumn(psKind, func));
			}
		}

		return colList;
	}

	public void setMoUpdateCol(String moTable, String moColumn, String moDateColumn) {
		this.moTable = moTable;
		this.moColumn = moColumn;
		this.moDateColumn = moDateColumn;
	}

	public void setUse(boolean isUse) {
		this.isUse = isUse;
	}

	public void setValue(Number nullVal, Number minVal, Number maxVal, Number dftVal) {
		this.nullVal = nullVal;
		this.maxVal = maxVal;
		this.minVal = minVal;
		this.dftVal = dftVal;
	}

	@Override
	public String toString() {
		return "PsItem(" + getPsId() + ")";
	}

	public String toStringConvert(Number value) {

		if (value == null)
			return "";

		if (vc != null && (vc instanceof NullVC) == false) {
			return vc.toStringConvert(value);
		}

		return value + "";
	}

	public String getPsMemo() {
		return psMemo;
	}

	/**
	 * 성능항목에 값이 맞는지 확인한다.
	 * 
	 * @param value
	 * @return
	 */
	public boolean isAcceptable(Number value) {

		if (value == null)
			return false;

		if (minVal != null && minVal.floatValue() > value.floatValue()) {
			return false;
		}

		if (maxVal != null && maxVal.floatValue() < value.floatValue()) {
			return false;
		}

		return true;
	}

	/**
	 * 성능의 자리수에 맞게 값을 정리한다.
	 * 
	 * @param value
	 * @return
	 */
	public Number format(Number value) {

		if (this.getDataScale() == 0) {
			return value.longValue();
		}

		try {
			return Float.valueOf(String.format("%." + this.getDataScale() + "f", value.floatValue()));
		} catch (Exception e) {
			return value;
		}

	}

}
