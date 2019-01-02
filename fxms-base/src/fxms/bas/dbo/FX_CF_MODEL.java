package fxms.bas.dbo;

import subkjh.bas.dao.define.COLUMN_OP;
import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
 * @since 2017.06.16 15:13
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_CF_MODEL", comment = "모델테이블")
@FxIndex(name = "FX_CF_MODEL__PK", type = INDEX_TYPE.PK, columns = { "MODEL_NO" })
@FxIndex(name = "FX_CF_MODEL__UK_NM", type = INDEX_TYPE.UK, columns = { "MODEL_NAME" })
public class FX_CF_MODEL implements FX_COMMON {

	public static final String FX_SEQ_MODELNO = "FX_SEQ_MODELNO";

	@FxColumn(name = "MODEL_NO", size = 9, comment = "모델번호", sequence = "FX_SEQ_MODELNO")
	private int modelNo;
	@FxColumn(name = "MODEL_NAME", size = 100, nullable = true, comment = "모델명")
	private String modelName;

	@FxColumn(name = "DEV_TYPE", size = 30, nullable = true, comment = "모델종류")
	private String devType;

	@FxColumn(name = "VENDOR_NAME", size = 30, nullable = true, comment = "제조사명")
	private String vendorName;

	@FxColumn(name = "MODEL_ID_VAL", size = 200, nullable = true, comment = "모델식별값")
	private String modelIdVal;

	@FxColumn(name = "MODEL_CLASS", size = 30, nullable = true, comment = "모델분류")
	private String modelClass;

	@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, nullable = true, comment = "등록운영자번호", defValue = "0")
	private int regUserNo = 0;

	@FxColumn(name = "REG_DATE", size = 14, operator = COLUMN_OP.insert, nullable = true, comment = "등록일시")
	private long regDate;

	@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정운영자번호", defValue = "0")
	private int chgUserNo = 0;

	@FxColumn(name = "CHG_DATE", size = 14, nullable = true, comment = "수정일시")
	private long chgDate;

	public FX_CF_MODEL() {
	}

	/**
	 * 수정일시
	 * 
	 * @return 수정일시
	 */
	public long getChgDate() {
		return chgDate;
	}

	/**
	 * 수정운영자번호
	 * 
	 * @return 수정운영자번호
	 */
	public int getChgUserNo() {
		return chgUserNo;
	}

	/**
	 * 장비종류
	 * 
	 * @return 장비종류
	 */
	public String getDevType() {
		return devType;
	}

	public String getModelIdVal() {
		return modelIdVal;
	}

	public String getModelClass() {
		return modelClass;
	}

	public void setModelClass(String modelClass) {
		this.modelClass = modelClass;
	}

	/**
	 * 모델명
	 * 
	 * @return 모델명
	 */
	public String getModelName() {
		return modelName;
	}

	/**
	 * 모델번호
	 * 
	 * @return 모델번호
	 */
	public int getModelNo() {
		return modelNo;
	}

	/**
	 * 등록일시
	 * 
	 * @return 등록일시
	 */
	public long getRegDate() {
		return regDate;
	}

	/**
	 * 등록운영자번호
	 * 
	 * @return 등록운영자번호
	 */
	public int getRegUserNo() {
		return regUserNo;
	}

	/**
	 * 제조사명
	 * 
	 * @return 제조사명
	 */
	public String getVendorName() {
		return vendorName;
	}

	/**
	 * 수정일시
	 * 
	 * @param chgDate
	 *            수정일시
	 */
	public void setChgDate(long chgDate) {
		this.chgDate = chgDate;
	}

	/**
	 * 수정운영자번호
	 * 
	 * @param chgUserNo
	 *            수정운영자번호
	 */
	public void setChgUserNo(int chgUserNo) {
		this.chgUserNo = chgUserNo;
	}

	/**
	 * 장비종류
	 * 
	 * @param devType
	 *            장비종류
	 */
	public void setDevType(String devType) {
		this.devType = devType;
	}

	public void setModelIdVal(String modelIdVal) {
		this.modelIdVal = modelIdVal;
	}

	/**
	 * 모델명
	 * 
	 * @param modelName
	 *            모델명
	 */
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	/**
	 * 모델번호
	 * 
	 * @param modelNo
	 *            모델번호
	 */
	public void setModelNo(int modelNo) {
		this.modelNo = modelNo;
	}

	/**
	 * 등록일시
	 * 
	 * @param regDate
	 *            등록일시
	 */
	public void setRegDate(long regDate) {
		this.regDate = regDate;
	}

	/**
	 * 등록운영자번호
	 * 
	 * @param regUserNo
	 *            등록운영자번호
	 */
	public void setRegUserNo(int regUserNo) {
		this.regUserNo = regUserNo;
	}

	/**
	 * 제조사명
	 * 
	 * @param vendorName
	 *            제조사명
	 */
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
}
