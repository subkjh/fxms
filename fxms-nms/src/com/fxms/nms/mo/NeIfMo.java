package com.fxms.nms.mo;

import com.fxms.nms.dbo.FN_MO_IF;

import fxms.bas.mo.Mo;
import fxms.bas.mo.property.MoNeedManager;
import fxms.bas.noti.FxEvent;
import subkjh.bas.utils.ObjectUtil;

/**
 * 네트워크 인터페이스 MO
 * 
 * @author subkjh
 * 
 */
public class NeIfMo extends FN_MO_IF implements MoNeedManager {

	public static final String BW_UNDEFINED = "Undefined";
	/**
	 * MO분류. INTERFACE
	 */
	public static final String MO_CLASS = "IF";

	/**
	 * 
	 */
	private static final long serialVersionUID = -2767135382505130722L;
	private static final long UNIT_G = 1000L * 1000 * 1000;
	private static final long UNIT_K = 1000L;
	private static final long UNIT_M = 1000L * 1000;
	private static final long UNIT_T = 1000L * 1000 * 1000 * 1000;

	public static void main(String[] args) {
		NeIfMo mo = new NeIfMo();
		try {
			Object value;
			value = ObjectUtil.get(mo, "bit64");
			System.out.println(value);
			mo.setBit64Yn(true);
			System.out.println(ObjectUtil.get(mo, "bit64"));
			System.out.println(ObjectUtil.get(mo, "ifIndex"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param speed
	 * @return
	 */
	public static String makeBwCode(long speed) {

		if (speed < 0)
			return "Undefined";

		if (speed >= UNIT_T)
			return String.format("%dT", speed / UNIT_T);
		if (speed >= UNIT_G)
			return String.format("%dG", speed / UNIT_G);
		if (speed >= UNIT_M)
			return String.format("%dM", speed / UNIT_M);
		if (speed >= UNIT_K)
			return String.format("%dK", speed / UNIT_K);

		return speed + "";
	}

	/**
	 * 
	 * @param ifName
	 *            인터페이스명
	 * @param ifAlias
	 *            인터페이스별칭
	 * @param ifDescr
	 *            인터페이스설명
	 * @param ipAddress
	 *            IP 주소
	 * @param ifNetmask
	 *            넷마스크
	 * @param ifSpeed
	 *            속도
	 * @param isBit64
	 *            64비트여부
	 * @param ifStatusAdmin
	 *            관리상태
	 * @param ifStatusOper
	 *            운용상태
	 * @param mac
	 *            맥주소
	 * @param ifType
	 *            종류
	 * @param ifIndex
	 *            인덱스
	 * @return 생성된 MoInterface
	 */
	public static void set(NeIfMo port, String ifName, String ifAlias, String ifDescr, String ipAddress, String ifNetmask,
			long ifSpeed, boolean isBit64, int ifStatusAdmin, int ifStatusOper, String mac, int ifType, int ifIndex) {

		port.setIfName(ifName);
		port.setIfAlias(ifAlias);
		port.setIfDescr(ifDescr);
		port.setIfIndex(ifIndex);
		port.setIfNetmask(ifNetmask);
		port.setIpAddress(ipAddress);
		port.setIfType(ifType);
		port.setIfSpeed(ifSpeed);
		port.setBit64Yn(isBit64);

		if (port.getIfDescr() == null || port.getIfDescr().trim().length() == 0) {
			port.setIfDescr("Undefined");
		}

		if (port.getIfAlias() == null || port.getIfAlias().trim().length() == 0) {
			port.setIfAlias(port.getIfDescr());
		}

		if (port.getIfName() == null || port.getIfName().trim().length() == 0) {
			port.setIfName(port.getIfAlias());
		}

		port.setMacAddress(mac);

		port.setIfStatusAdmin(ifStatusAdmin);
		port.setIfStatusOper(ifStatusOper);

		String bw = makeBwCode(port.getIfSpeed());
		port.setBwCode(bw);

		if (port.getIfSpeedQos() <= 0) {
			port.setBwCodeQos(BW_UNDEFINED);
			port.setIfSpeedQos(-1);
		}

		port.setMoName(port.getIfName().trim());
		port.setMoAname(port.getIfAlias().trim());
		port.setStatus(FxEvent.STATUS.added);
		port.setMngYn(true);

	}

	public NeIfMo() {
		setIfSpeedQos(-1);
		setBwCodeQos(BW_UNDEFINED);
		setMoClass(MO_CLASS);
	}

	/**
	 * 실제 사용하는 속도를 제공합니다.<br>
	 * qos가 설정되어 있으면 qos 그렇치 않으면 ifSpeed입니다.
	 * 
	 * @return 실제 속도
	 */
	public long getIfSpeedReal() {
		return getIfSpeedQos() >= 0 ? getIfSpeedQos() : getIfSpeed();
	}

	@Override
	public long getManagerMoNo() {
		return getUpperMoNo();
	}

	@Override
	public String getMoClass() {
		return MO_CLASS;
	}

	/**
	 * 입력된 값을 이용하여 사용율을 구합니다.
	 * 
	 * @param bps
	 *            계산할 값
	 * @return 사용율
	 */
	public float getUsage(long bps) {

		if (bps <= 0)
			return 0;
		long speed = getIfSpeedReal();
		if (speed <= 0)
			return 0;

		float rate = bps * 100F / speed;

		return rate > 100 ? 100 : rate;
	}

	@Override
	public void setUserChgAttr(Mo moOld) {

		super.setUserChgAttr(moOld);

		if (moOld instanceof NeIfMo) {

			// 자동으로 QOS를 알 수 없다면 운용자가 설정한 값을 계속 사용합니다.

			if (getIfSpeedQos() < 0) {
				NeIfMo portOld = (NeIfMo) moOld;
				setBwCodeQos(portOld.getBwCodeQos());
				setIfSpeedQos(portOld.getIfSpeedQos());
			}
		}
	}

}
