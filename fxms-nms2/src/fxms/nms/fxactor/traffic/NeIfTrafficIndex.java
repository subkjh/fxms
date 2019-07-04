package fxms.nms.fxactor.traffic;

public interface NeIfTrafficIndex {
	
	public static final int inBroadcastPkts = 1;
	public static final int inDiscards = 2;
	public static final int inErrors = 3;
	public static final int inMulticastPkts = 4;
	public static final int inNonUcastPkts = 5;
	public static final int inOctets = 6;
	public static final int inUcastPkts = 7;
	
	public static final int outBroadcastPkts = 21;
	public static final int outDiscards = 22;
	public static final int outErrors = 23;
	public static final int outMulticastPkts = 24;
	public static final int outNonUcastPkts = 25;
	public static final int outOctets = 26;
	public static final int outUcastPkts = 27;
}
