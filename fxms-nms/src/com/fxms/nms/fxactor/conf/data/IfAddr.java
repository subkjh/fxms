package com.fxms.nms.fxactor.conf.data;

public class IfAddr {
	/**
	 * 인터페이스에 설정된 BroadCast 주소
	 */
	public String bastAddr;
	public int bcastAddrIv;
	/**
	 * 인터페이스 인덱스 ( SNMP INDEX)
	 */
	public int index;
	/**
	 * 인터페이스에 설정된 IP 주소
	 */
	public String ipaddr;

	/**
	 * 인터페이스에 설정된 SubNetMask
	 */
	public String netmask;

	public IfAddr() {
	}

	/**
	 * IP 주소와 SubNetMask를 이용하여 BoradCast 주소값을 구한다.
	 * 
	 * @param bv
	 *            int NetworkAddress의 마지막 비트값
	 */
	public void calcBcastAddr(int bv) {
		this.bcastAddrIv = bv;
		try {
			int[] baddb = new int[4];
			int[] ipb = this.getByteIp(ipaddr);
			int[] netb = this.getByteIp(netmask);

			for (int i = 0; i < baddb.length; i++) {
				baddb[i] = (ipb[i] & netb[i]);
			}

			baddb[baddb.length - 1] = (baddb[baddb.length - 1] + bv);
			bastAddr = "" + baddb[0] + "." + baddb[1] + "." + baddb[2] + "." + baddb[3];
		} catch (Exception e) {
			bastAddr = "";
		}

	}

	/**
	 * 문자열 주소값을 byte 값으로 변환한다.<br>
	 * 문자열 1.2.3.4<br>
	 * bip[0] = 1;<br>
	 * bip[1] = 2;<br>
	 * bip[2] = 3;<br>
	 * bip[3] = 4;<br>
	 * 
	 * @param ip
	 *            String IP 주소
	 * @throws Exception
	 * @return int[]
	 */
	public int[] getByteIp(String ip) throws Exception {
		int[] bip = new int[4];
		String[] temp = ip.split("\\.");
		for (int i = 0; i < bip.length; i++) {
			bip[i] = Integer.parseInt(temp[i]);
		}
		return bip;
	}
}
