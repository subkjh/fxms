package fxms.nms;

public class NmsCodes {

	public enum PsItem {

		// INTERFACE

		IfInBps, IfInBytes, IfInUsage

		, IfInEps, IfInErrors, IfInDps, IfInPpsUnicast, IfInPpsNonunicast, IfInPps, IfInPackets

		, IfOutBps, IfOutBytes, IfOutUsage

		, IfOutEps, IfOutErrors, IfOutDps, IfOutPpsUnicast, IfOutPpsNonunicast, IfOutPps, IfOutPackets

		, IfSpeedQos, IfUsage

		, IfOperStatus, IfAdminStatus, IfLastChange

		, IfIcmpStatus

		// NE

		, NeIfInUsgae, NeIfOutUsgae

		, NeSnmpStatus

		, NeIcmpStatus, NeIcmpEchoTime
		
		, NeSysUptime

		// NE MTR
		, NeMtrAvg, NeMtrBest, NeMtrWrst, NeMtrLoss

		// DNS
		, DnsStatus, DnsQueryTime

		// TCP
		, TcpPortListen

		// URL
		, UrlStatus, UrlResponseTime

		// PROCESS
		, ProcessCpuTime, ProcessMemUsed, ProcessCount, ProcessStatus, ProcessOnRate

		// TUNNEL
		, TunnelStatus

		// VPN
		, VpnPingStatus

		// IP
		, IpIcmpStatus

		;
	}

	public enum AlarmCode {

		// NE
		NeSnmpTimeout

		;

	}


	

}
