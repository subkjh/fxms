package fxms.bas.dbo;

import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
 * @since 2017.06.16 15:13
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FY_VBIO", comment = " 성능수집값테이블")
@FxIndex(name = "FY_VBIO__PK", type = INDEX_TYPE.PK, columns = { "MO_NO", "CLLT_DATE", "MO_INSTANCE" })
public class FY_VBIO  {

	public FY_VBIO() {
	}

	@FxColumn(name = "MO_NO", size = 19, comment = "컨테이너관리번호")
	private long moNo;

	@FxColumn(name = "CLLT_DATE", size = 14, comment = "수집일시")
	private long clltDate;

	@FxColumn(name = "MO_INSTANCE", size = 50, comment = "MO인스턴스")
	private String moInstance;

	@FxColumn(name = "TEMP", size = 4, nullable = true, comment = "온도")
	private double temp;

	@FxColumn(name = "PH", size = 5, nullable = true, comment = "pH")
	private double ph;

	@FxColumn(name = "CO2", size = 9, nullable = true, comment = "CO2 주입량")
	private int co2;

	@FxColumn(name = "PUMP_ONOFF", size = 5, nullable = true, comment = "펌프 On/Off")
	private double pumpOnoff;

	@FxColumn(name = "PUMP_HZ", size = 5, nullable = true, comment = "펌프 Hz")
	private double pumpHz;

	@FxColumn(name = "LED", size = 5, nullable = true, comment = "Light Intensity")
	private double led;

	@FxColumn(name = "BIOMASS_OUT", size = 19, nullable = true, comment = "바이오매스 측정량")
	private long biomassOut;

	@FxColumn(name = "ASTAZAN", size = 5, nullable = true, comment = "아스타잔틴 함유율")
	private double astazan;

	@FxColumn(name = "BIOMASS_IN", size = 19, nullable = true, comment = "바이오매스 접종량")
	private long biomassIn;

	@FxColumn(name = "COST", size = 19, nullable = true, comment = "매지비용")
	private long cost;

	/**
	 * 컨테이너관리번호
	 * 
	 * @return 컨테이너관리번호
	 */
	public long getMoNo() {
		return moNo;
	}

	/**
	 * 컨테이너관리번호
	 * 
	 * @param moNo
	 *            컨테이너관리번호
	 */
	public void setMoNo(long moNo) {
		this.moNo = moNo;
	}

	/**
	 * 수집일시
	 * 
	 * @return 수집일시
	 */
	public long getClltDate() {
		return clltDate;
	}

	/**
	 * 수집일시
	 * 
	 * @param clltDate
	 *            수집일시
	 */
	public void setClltDate(long clltDate) {
		this.clltDate = clltDate;
	}

	/**
	 * MO인스턴스
	 * 
	 * @return MO인스턴스
	 */
	public String getMoInstance() {
		return moInstance;
	}

	/**
	 * MO인스턴스
	 * 
	 * @param moInstance
	 *            MO인스턴스
	 */
	public void setMoInstance(String moInstance) {
		this.moInstance = moInstance;
	}

	/**
	 * 온도
	 * 
	 * @return 온도
	 */
	public double getTemp() {
		return temp;
	}

	/**
	 * 온도
	 * 
	 * @param temp
	 *            온도
	 */
	public void setTemp(double temp) {
		this.temp = temp;
	}

	/**
	 * pH
	 * 
	 * @return pH
	 */
	public double getPh() {
		return ph;
	}

	/**
	 * pH
	 * 
	 * @param ph
	 *            pH
	 */
	public void setPh(double ph) {
		this.ph = ph;
	}

	/**
	 * CO2 주입량
	 * 
	 * @return CO2 주입량
	 */
	public int getCo2() {
		return co2;
	}

	/**
	 * CO2 주입량
	 * 
	 * @param co2
	 *            CO2 주입량
	 */
	public void setCo2(int co2) {
		this.co2 = co2;
	}

	/**
	 * 펌프 On/Off
	 * 
	 * @return 펌프 On/Off
	 */
	public double getPumpOnoff() {
		return pumpOnoff;
	}

	/**
	 * 펌프 On/Off
	 * 
	 * @param pumpOnoff
	 *            펌프 On/Off
	 */
	public void setPumpOnoff(double pumpOnoff) {
		this.pumpOnoff = pumpOnoff;
	}

	/**
	 * 펌프 Hz
	 * 
	 * @return 펌프 Hz
	 */
	public double getPumpHz() {
		return pumpHz;
	}

	/**
	 * 펌프 Hz
	 * 
	 * @param pumpHz
	 *            펌프 Hz
	 */
	public void setPumpHz(double pumpHz) {
		this.pumpHz = pumpHz;
	}

	/**
	 * Light Intensity
	 * 
	 * @return Light Intensity
	 */
	public double getLed() {
		return led;
	}

	/**
	 * Light Intensity
	 * 
	 * @param led
	 *            Light Intensity
	 */
	public void setLed(double led) {
		this.led = led;
	}

	/**
	 * 바이오매스 측정량
	 * 
	 * @return 바이오매스 측정량
	 */
	public long getBiomassOut() {
		return biomassOut;
	}

	/**
	 * 바이오매스 측정량
	 * 
	 * @param biomassOut
	 *            바이오매스 측정량
	 */
	public void setBiomassOut(long biomassOut) {
		this.biomassOut = biomassOut;
	}

	/**
	 * 아스타잔틴 함유율
	 * 
	 * @return 아스타잔틴 함유율
	 */
	public double getAstazan() {
		return astazan;
	}

	/**
	 * 아스타잔틴 함유율
	 * 
	 * @param astazan
	 *            아스타잔틴 함유율
	 */
	public void setAstazan(double astazan) {
		this.astazan = astazan;
	}

	/**
	 * 바이오매스 접종량
	 * 
	 * @return 바이오매스 접종량
	 */
	public long getBiomassIn() {
		return biomassIn;
	}

	/**
	 * 바이오매스 접종량
	 * 
	 * @param biomassIn
	 *            바이오매스 접종량
	 */
	public void setBiomassIn(long biomassIn) {
		this.biomassIn = biomassIn;
	}

	/**
	 * 매지비용
	 * 
	 * @return 매지비용
	 */
	public long getCost() {
		return cost;
	}

	/**
	 * 매지비용
	 * 
	 * @param cost
	 *            매지비용
	 */
	public void setCost(long cost) {
		this.cost = cost;
	}
}
