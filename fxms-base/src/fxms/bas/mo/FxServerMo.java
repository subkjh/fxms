package fxms.bas.mo;

import java.io.Serializable;

import fxms.bas.api.FxApi;
import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;
import subkjh.bas.user.User;

/**
 * @since 2018.01.17 16:04
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_MO_FXSERVER", comment = "서버테이블")
@FxIndex(name = "FX_MO_FXSERVER__PK", type = INDEX_TYPE.PK, columns = { "MO_NO" })
@FxIndex(name = "FX_MO_FXSERVER__UK", type = INDEX_TYPE.UK, columns = { "MS_IPADDR" })
@FxIndex(name = "FX_MO_FXSERVER__FK_MO", type = INDEX_TYPE.FK, columns = {
		"MO_NO" }, fkTable = "FX_MO", fkColumn = "MO_NO")
public class FxServerMo extends Mo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4937769751394580926L;

	public static final String MO_CLASS = "FXSERVER";

	public static String getMoName(String msIpaddr, String serviceName) {
		return msIpaddr + "/" + serviceName;
	}

	public static void set(FxServerMo mo, String msName, String msIpaddr, String msDescr) {
		
		mo.setMngYn(true);
		mo.setMoName(msName);
		mo.setMoAname(msName);
		mo.setMsIpaddr(msIpaddr);
		mo.setChgDate(FxApi.getDate(0));
		mo.setChgUserNo(User.USER_NO_SYSTEM);
		mo.setRegDate(FxApi.getDate(0));
		mo.setRegUserNo(User.USER_NO_SYSTEM);
		
		mo.setMsDescr(msDescr);
		mo.setMsIpaddr(msIpaddr);
		mo.setMsName(msName);
	}

	public FxServerMo() {
		setMoClass(MO_CLASS);
	}

	@FxColumn(name = "MS_NAME", size = 50, comment = "관리서버명")
	private String msName;

	@FxColumn(name = "MS_IPADDR", size = 39, comment = "관리서버주소")
	private String msIpaddr;

	@FxColumn(name = "MS_DESCR", size = 100, nullable = true, comment = "관리서버설명")
	private String msDescr;

	@FxColumn(name = "MAX_MO_COUNT", size = 9, nullable = true, comment = "최대관할MO수", defValue = "100000")
	private Number maxMoCount = 100000;

	@FxColumn(name = "CUR_MO_COUNT", size = 9, nullable = true, comment = "현재관할MO수", defValue = "0")
	private Number curMoCount = 0;

	/**
	 * 관리서버명
	 * 
	 * @return 관리서버명
	 */
	public String getMsName() {
		return msName;
	}

	/**
	 * 관리서버명
	 * 
	 * @param msName
	 *            관리서버명
	 */
	public void setMsName(String msName) {
		this.msName = msName;
	}

	/**
	 * 관리서버주소
	 * 
	 * @return 관리서버주소
	 */
	public String getMsIpaddr() {
		return msIpaddr;
	}

	/**
	 * 관리서버주소
	 * 
	 * @param msIpaddr
	 *            관리서버주소
	 */
	public void setMsIpaddr(String msIpaddr) {
		this.msIpaddr = msIpaddr;
	}

	/**
	 * 관리서버설명
	 * 
	 * @return 관리서버설명
	 */
	public String getMsDescr() {
		return msDescr;
	}

	/**
	 * 관리서버설명
	 * 
	 * @param msDescr
	 *            관리서버설명
	 */
	public void setMsDescr(String msDescr) {
		this.msDescr = msDescr;
	}

	/**
	 * 최대관할MO수
	 * 
	 * @return 최대관할MO수
	 */
	public Number getMaxMoCount() {
		return maxMoCount;
	}

	/**
	 * 최대관할MO수
	 * 
	 * @param maxMoCount
	 *            최대관할MO수
	 */
	public void setMaxMoCount(Number maxMoCount) {
		this.maxMoCount = maxMoCount;
	}

	/**
	 * 현재관할MO수
	 * 
	 * @return 현재관할MO수
	 */
	public Number getCurMoCount() {
		return curMoCount;
	}

	/**
	 * 현재관할MO수
	 * 
	 * @param curMoCount
	 *            현재관할MO수
	 */
	public void setCurMoCount(Number curMoCount) {
		this.curMoCount = curMoCount;
	}
}
