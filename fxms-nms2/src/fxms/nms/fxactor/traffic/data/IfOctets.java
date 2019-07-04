package fxms.nms.fxactor.traffic.data;

import java.io.Serializable;
import java.math.BigInteger;

public class IfOctets implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -24774219116280317L;

	public static void main(String[] args) {
		IfOctets t = new IfOctets();
		String s = t.getString();
		System.out.println(s);
		try {
			t.setString(s);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public long moNo;

	public long unixtime;
	public BigInteger ifHCInOctets;
	public BigInteger ifHCInBroadcastPkts;
	public BigInteger ifHCInMulticastPkts;
	public BigInteger ifHCInUcastPkts;
	public long ifInOctets = -1;
	public long ifInUcastPkts = -1;
	public long ifInNUcastPkts = -1;
	public long ifInErrors = -1;

	public long ifInDiscards = -1;

	public BigInteger ifHCOutOctets;
	public BigInteger ifHCOutBroadcastPkts;
	public BigInteger ifHCOutMulticastPkts;
	public BigInteger ifHCOutUcastPkts;
	public long ifOutOctets = -1;
	public long ifOutUcastPkts = -1;
	public long ifOutNUcastPkts = -1;
	public long ifOutErrors = -1;
	public long ifOutDiscards = -1;

	public IfOctets() {

	}

	public IfOctets(String str) throws Exception {
		setString(str);
	}

	public long getMoNo() {
		return moNo;
	}

	public String getString() {
		StringBuffer sb = new StringBuffer();
		sb.append(moNo);
		sb.append("|");
		sb.append(unixtime);
		sb.append("|");
		sb.append(ifHCInOctets);
		sb.append("|");
		sb.append(ifHCInBroadcastPkts);
		sb.append("|");
		sb.append(ifHCInMulticastPkts);
		sb.append("|");
		sb.append(ifHCInUcastPkts);
		sb.append("|");
		sb.append(ifInOctets);
		sb.append("|");
		sb.append(ifInUcastPkts);
		sb.append("|");
		sb.append(ifInNUcastPkts);
		sb.append("|");
		sb.append(ifInErrors);
		sb.append("|");
		sb.append(ifInDiscards);

		sb.append("|");
		sb.append(ifHCOutOctets);
		sb.append("|");
		sb.append(ifHCOutBroadcastPkts);
		sb.append("|");
		sb.append(ifHCOutMulticastPkts);
		sb.append("|");
		sb.append(ifHCOutUcastPkts);
		sb.append("|");
		sb.append(ifOutOctets);
		sb.append("|");
		sb.append(ifOutUcastPkts);
		sb.append("|");
		sb.append(ifOutNUcastPkts);
		sb.append("|");
		sb.append(ifOutErrors);
		sb.append("|");
		sb.append(ifOutDiscards);

		return sb.toString();
	}

	public void setString(String str) throws Exception {

		String s[] = str.trim().split("\\|");

		moNo = Long.parseLong(s[0]);
		unixtime = Long.parseLong(s[1]);

		ifHCInOctets = (s[2].equalsIgnoreCase("null") ? null : new BigInteger(s[2]));
		ifHCInBroadcastPkts = (s[3].equalsIgnoreCase("null") ? null : new BigInteger(s[3]));
		ifHCInMulticastPkts = (s[4].equalsIgnoreCase("null") ? null : new BigInteger(s[4]));
		ifHCInUcastPkts = (s[5].equalsIgnoreCase("null") ? null : new BigInteger(s[5]));

		ifInOctets = Long.parseLong(s[6]);
		ifInUcastPkts = Long.parseLong(s[7]);
		ifInNUcastPkts = Long.parseLong(s[8]);
		ifInErrors = Long.parseLong(s[9]);
		ifInDiscards = Long.parseLong(s[10]);

		ifHCOutOctets = (s[11].equalsIgnoreCase("null") ? null : new BigInteger(s[11]));
		ifHCOutBroadcastPkts = (s[12].equalsIgnoreCase("null") ? null : new BigInteger(s[12]));
		ifHCOutMulticastPkts = (s[13].equalsIgnoreCase("null") ? null : new BigInteger(s[13]));
		ifHCOutUcastPkts = (s[14].equalsIgnoreCase("null") ? null : new BigInteger(s[14]));

		ifOutOctets = Long.parseLong(s[15]);
		ifOutUcastPkts = Long.parseLong(s[16]);
		ifOutNUcastPkts = Long.parseLong(s[17]);
		ifOutErrors = Long.parseLong(s[18]);
		ifOutDiscards = Long.parseLong(s[19]);
	}

}
