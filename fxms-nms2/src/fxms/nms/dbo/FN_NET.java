package fxms.nms.dbo;

import subkjh.bas.dao.define.COLUMN_OP;
import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
 * @since 2018.06.28 10:34
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FN_NET", comment = "네트워크")
@FxIndex(name = "FN_NET__PK", type = INDEX_TYPE.PK, columns = { "NET_NO" })
public class FN_NET {

	public FN_NET() {
	}

	public static final String FX_SEQ_NETNO = "FX_SEQ_NETNO";
	@FxColumn(name = "NET_NO", size = 9, comment = "네트워크번호", sequence = "FX_SEQ_NETNO")
	private int netNo;

	@FxColumn(name = "NET_NAME", size = 100, comment = "네트워크명")
	private String netName;

	@FxColumn(name = "TOPOLOGY_TYPE", size = 10, comment = "토폴로지종류")
	private String topologyType;

	@FxColumn(name = "NET_DESCR", size = 200, nullable = true, comment = "네트워크설명")
	private String netDescr;

	@FxColumn(name = "BW_CODE", size = 20, nullable = true, comment = "대역폭코드 ")
	private String bwCode;

	@FxColumn(name = "INLO_NO", size = 9, nullable = true, comment = "설치위치번호(관할)", defValue = "-1")
	private int inloNo = -1;

	@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, nullable = true, comment = "등록운영자번호", defValue = "0")
	private int regUserNo = 0;

	@FxColumn(name = "REG_DATE", size = 14, operator = COLUMN_OP.insert, nullable = true, comment = "등록일시")
	private long regDate;

	/**
	 * 네트워크번호
	 * 
	 * @return 네트워크번호
	 */
	public int getNetNo() {
		return netNo;
	}

	/**
	 * 네트워크번호
	 * 
	 * @param netNo
	 *            네트워크번호
	 */
	public void setNetNo(int netNo) {
		this.netNo = netNo;
	}

	/**
	 * 네트워크명
	 * 
	 * @return 네트워크명
	 */
	public String getNetName() {
		return netName;
	}

	/**
	 * 네트워크명
	 * 
	 * @param netName
	 *            네트워크명
	 */
	public void setNetName(String netName) {
		this.netName = netName;
	}

	/**
	 * 토폴로지종류
	 * 
	 * @return 토폴로지종류
	 */
	public String getTopologyType() {
		return topologyType;
	}

	/**
	 * 토폴로지종류
	 * 
	 * @param topologyType
	 *            토폴로지종류
	 */
	public void setTopologyType(String topologyType) {
		this.topologyType = topologyType;
	}

	/**
	 * 네트워크설명
	 * 
	 * @return 네트워크설명
	 */
	public String getNetDescr() {
		return netDescr;
	}

	/**
	 * 네트워크설명
	 * 
	 * @param netDescr
	 *            네트워크설명
	 */
	public void setNetDescr(String netDescr) {
		this.netDescr = netDescr;
	}

	/**
	 * 대역폭코드
	 * 
	 * @return 대역폭코드
	 */
	public String getBwCode() {
		return bwCode;
	}

	/**
	 * 대역폭코드
	 * 
	 * @param bwCode
	 *            대역폭코드
	 */
	public void setBwCode(String bwCode) {
		this.bwCode = bwCode;
	}

	/**
	 * 설치위치번호(관할)
	 * 
	 * @return 설치위치번호(관할)
	 */
	public int getInloNo() {
		return inloNo;
	}

	/**
	 * 설치위치번호(관할)
	 * 
	 * @param inloNo
	 *            설치위치번호(관할)
	 */
	public void setInloNo(int inloNo) {
		this.inloNo = inloNo;
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
	 * 등록운영자번호
	 * 
	 * @param regUserNo
	 *            등록운영자번호
	 */
	public void setRegUserNo(int regUserNo) {
		this.regUserNo = regUserNo;
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
	 * 등록일시
	 * 
	 * @param regDate
	 *            등록일시
	 */
	public void setRegDate(long regDate) {
		this.regDate = regDate;
	}
}
