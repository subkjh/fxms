package fxms.bas.mo.property;

public interface HasIp {

	public String getIpAddress();

	public static void main(String[] args) {
		long ipnum = getIpNum("10.0.0.1");
		int index = 0;
		while (index <= 100000) {
			System.out.println(getIpAddress(ipnum));
			ipnum += (Math.random() * 255);
			index++;
		}
	}

	public static long getIpNum(String ip) {
		int ipNum = -1;

		try {
			String ss[] = ip.split("\\.");
			ipNum = 0;
			for (int i = 0; i < ss.length; i++) {
				ipNum <<= 8;
				ipNum |= Integer.parseInt(ss[i]);
			}
		} catch (Exception e) {
			return -1;
		}

		return ipNum;
	}

	public static String getIpAddress(long num) {
		int ai[] = new int[4];
		for (int i = 0; i < 4; i++)
			ai[i] = (int) (num >> 8 * (3 - i) & 255L);

		return new String(ai[0] + "." + ai[1] + "." + ai[2] + "." + ai[3]);
	}

}
