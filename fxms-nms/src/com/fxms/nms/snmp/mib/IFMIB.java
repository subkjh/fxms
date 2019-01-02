package com.fxms.nms.snmp.mib;

public class IFMIB {

	/**
	 * Object ifNumber OID 1.3.6.1.2.1.2.1 Type Integer32
	 * 
	 * Permission read-only Status current MIB IF-MIB ; - View Supporting Images
	 * this link will generate a new window Description "The number of network
	 * interfaces (regardless of their current state) present on this system."
	 */
	public final String ifNumber = ".1.3.6.1.2.1.2.1.0";

	/**
	 * Object ifIndex <br>
	 * OID 1.3.6.1.2.1.2.2.1.1<br>
	 * Type InterfaceIndex <br>
	 * 
	 * Permission read-only <br>
	 * Status current <br>
	 * MIB IF-MIB ; - View Supporting Images this link will generate a new
	 * window Description "A unique value, greater than zero, for each
	 * interface. It is recommended that values are assigned contiguously
	 * starting from 1. The value for each interface sub-layer must remain
	 * constant at least from one re-initialization of the entity's network
	 * management system to the next re- initialization."
	 */
	public final String ifIndex = ".1.3.6.1.2.1.2.2.1.1";

	/**
	 * Object ifDescr <br>
	 * OID 1.3.6.1.2.1.2.2.1.2<br>
	 * Type DisplayString <br>
	 * 
	 * Permission read-only <br>
	 * Status current <br>
	 * MIB IF-MIB ; - View Supporting Images this link will generate a new
	 * window Description "A textual string containing information about the
	 * interface. This string should include the name of the manufacturer, the
	 * product name and the version of the interface hardware/software."
	 */
	public final String ifDescr = ".1.3.6.1.2.1.2.2.1.2";

	/**
	 * Object ifType <br>
	 * OID 1.3.6.1.2.1.2.2.1.3 <br>
	 * Type IANAifType <br>
	 * 1:other 2:regular1822 3:hdh1822 4:ddnX25 5:rfc877x25 6:ethernetCsmacd
	 * 7:iso88023Csmacd 8:iso88024TokenBus 9:iso88025TokenRing 10:iso88026Man
	 * 11:starLan 12:proteon10Mbit 13:proteon80Mbit 14:hyperchannel 15:fddi
	 * 16:lapb 17:sdlc 18:ds1 19:e1 20:basicISDN 21:primaryISDN
	 * 22:propPointToPointSerial 23:ppp 24:softwareLoopback 25:eon
	 * 26:ethernet3Mbit 27:nsip 28:slip 29:ultra 30:ds3 31:sip 32:frameRelay
	 * 33:rs232 34:para 35:arcnet 36:arcnetPlus 37:atm 38:miox25 39:sonet
	 * 40:x25ple 41:iso88022llc 42:localTalk 43:smdsDxi 44:frameRelayService
	 * 45:v35 46:hssi 47:hippi 48:modem 49:aal5 50:sonetPath 51:sonetVT
	 * 52:smdsIcip 53:propVirtual 54:propMultiplexor 55:ieee80212
	 * 56:fibreChannel 57:hippiInterface 58:frameRelayInterconnect 59:aflane8023
	 * 60:aflane8025 61:cctEmul 62:fastEther 63:isdn 64:v11 65:v36 66:g703at64k
	 * 67:g703at2mb 68:qllc 69:fastEtherFX 70:channel 71:ieee80211
	 * 72:ibm370parChan 73:escon 74:dlsw 75:isdns 76:isdnu 77:lapd 78:ipSwitch
	 * 79:rsrb 80:atmLogical 81:ds0 82:ds0Bundle 83:bsc 84:async 85:cnr
	 * 86:iso88025Dtr 87:eplrs 88:arap 89:propCnls 90:hostPad 91:termPad
	 * 92:frameRelayMPI 93:x213 94:adsl 95:radsl 96:sdsl 97:vdsl
	 * 98:iso88025CRFPInt 99:myrinet 100:voiceEM 101:voiceFXO 102:voiceFXS
	 * 103:voiceEncap 104:voiceOverIp 105:atmDxi 106:atmFuni 107:atmIma
	 * 108:pppMultilinkBundle 109:ipOverCdlc 110:ipOverClaw 111:stackToStack
	 * 112:virtualIpAddress 113:mpc 114:ipOverAtm 115:iso88025Fiber 116:tdlc
	 * 117:gigabitEthernet 118:hdlc 119:lapf 120:v37 121:x25mlp 122:x25huntGroup
	 * 123:trasnpHdlc 124:interleave 125:fast 126:ip 127:docsCableMaclayer
	 * 128:docsCableDownstream 129:docsCableUpstream 130:a12MppSwitch 131:tunnel
	 * 132:coffee 133:ces 134:atmSubInterface 135:l2vlan 136:l3ipvlan
	 * 137:l3ipxvlan 138:digitalPowerline 139:mediaMailOverIp 140:dtm 141:dcn
	 * 142:ipForward 143:msdsl 144:ieee1394 145:if-gsn 146:dvbRccMacLayer
	 * 147:dvbRccDownstream 148:dvbRccUpstream 149:atmVirtual 150:mplsTunnel
	 * 151:srp 152:voiceOverAtm 153:voiceOverFrameRelay 154:idsl
	 * 155:compositeLink 156:ss7SigLink 157:propWirelessP2P 158:frForward
	 * 159:rfc1483 160:usb 161:ieee8023adLag 162:bgppolicyaccounting
	 * 163:frf16MfrBundle 164:h323Gatekeeper 165:h323Proxy 166:mpls
	 * 167:mfSigLink 168:hdsl2 169:shdsl 170:ds1FDL 171:pos 172:dvbAsiIn
	 * 173:dvbAsiOut 174:plc 175:nfas 176:tr008 177:gr303RDT 178:gr303IDT
	 * 179:isup 180:propDocsWirelessMaclayer 181:propDocsWirelessDownstream
	 * 182:propDocsWirelessUpstream 183:hiperlan2 184:propBWAp2Mp
	 * 185:sonetOverheadChannel 186:digitalWrapperOverheadChannel 187:aal2
	 * 188:radioMAC 189:atmRadio 190:imt 191:mvl 192:reachDSL 193:frDlciEndPt
	 * 194:atmVciEndPt 195:opticalChannel 196:opticalTransport 197:propAtm
	 * 198:voiceOverCable 199:infiniband 200:teLink 201:q2931 202:virtualTg
	 * 203:sipTg 204:sipSig 205:docsCableUpstreamChannel 206:econet 207:pon155
	 * 208:pon622 209:bridge 210:linegroup 211:voiceEMFGD 212:voiceFGDEANA
	 * 213:voiceDID 214:mpegTransport 215:sixToFour 216:gtp 217:pdnEtherLoop1
	 * 218:pdnEtherLoop2 219:opticalChannelGroup 220:homepna 221:gfp
	 * 222:ciscoISLvlan 223:actelisMetaLOOP 224:fcipLink 225:rpr 226:qam 227:lmp
	 * 228:cblVectaStar 229:docsCableMCmtsDownstream 230:adsl2
	 * 231:macSecControlledIF 232:macSecUncontrolledIF 233:aviciOpticalEther
	 * 234:atmbond<br>
	 * 
	 * Permission read-only<br>
	 * Status current <br>
	 * MIB IF-MIB ; - View Supporting Images this link will generate a new
	 * window Description "The type of interface. Additional values for ifType
	 * are assigned by the Internet Assigned Numbers Authority (IANA), through
	 * updating the syntax of the IANAifType textual convention."
	 */
	public final String ifType = ".1.3.6.1.2.1.2.2.1.3";

	/**
	 * Object ifMtu <br>
	 * OID 1.3.6.1.2.1.2.2.1.4<br>
	 * Type Integer32 <br>
	 * 
	 * Permission read-only<br>
	 * Status current <br>
	 * MIB IF-MIB ; - View Supporting Images this link will generate a new
	 * window Description "The size of the largest packet which can be
	 * sent/received on the interface, specified in octets. For interfaces that
	 * are used for transmitting network datagrams, this is the size of the
	 * largest network datagram that can be sent on the interface."
	 */
	public final String ifMtu = ".1.3.6.1.2.1.2.2.1.4";

	/**
	 * Object ifSpeed <br>
	 * OID 1.3.6.1.2.1.2.2.1.5<br>
	 * Type Gauge32 <br>
	 * 
	 * Permission read-only<br>
	 * Status current <br>
	 * MIB IF-MIB ; - View Supporting Images this link will generate a new
	 * window Description "An estimate of the interface's current bandwidth in
	 * bits per second. For interfaces which do not vary in bandwidth or for
	 * those where no accurate estimation can be made, this object should
	 * contain the nominal bandwidth. If the bandwidth of the interface is
	 * greater than the maximum value reportable by this object then this object
	 * should report its maximum value (4,294,967,295) and ifHighSpeed must be
	 * used to report the interace's speed. For a sub-layer which has no concept
	 * of bandwidth, this object should be zero."
	 */
	public final String ifSpeed = ".1.3.6.1.2.1.2.2.1.5";

	/**
	 * Object ifPhysAddress<br>
	 * OID 1.3.6.1.2.1.2.2.1.6 <br>
	 * Type PhysAddress <br>
	 * 
	 * Permission read-only<br>
	 * Status current <br>
	 * MIB IF-MIB ; - View Supporting Images this link will generate a new
	 * window Description "The interface's address at its protocol sub-layer.
	 * For example, for an 802.x interface, this object normally contains a MAC
	 * address. The interface's media-specific MIB must define the bit and byte
	 * ordering and the format of the value of this object. For interfaces which
	 * do not have such an address (e.g., a serial line), this object should
	 * contain an octet string of zero length."
	 */
	public final String ifPhysAddress = ".1.3.6.1.2.1.2.2.1.6";

	/**
	 * Object ifAdminStatus <br>
	 * OID 1.3.6.1.2.1.2.2.1.7 <br>
	 * Type INTEGER <br>
	 * Permission read-write<br>
	 * Status current <br>
	 * Values 1 : up 2 : down 3 : testing<br>
	 * 
	 * MIB IF-MIB ; - View Supporting Images this link will generate a new
	 * window Description "The desired state of the interface. The testing(3)
	 * state indicates that no operational packets can be passed. When a managed
	 * system initializes, all interfaces start with ifAdminStatus in the
	 * down(2) state. As a result of either explicit management action or per
	 * configuration information retained by the managed system, ifAdminStatus
	 * is then changed to either the up(1) or testing(3) states (or remains in
	 * the down(2) state)."
	 */
	public final String ifAdminStatus = ".1.3.6.1.2.1.2.2.1.7";
	
	public static final int ifAdminStatus_up = 1;
	public static final int ifAdminStatus_down = 2;
	public static final int ifAdminStatus_testing = 3;

	/**
	 * Object ifOperStatus <br>
	 * OID 1.3.6.1.2.1.2.2.1.8 <br>
	 * Type INTEGER <br>
	 * Permission read-only<br>
	 * Status current <br>
	 * Values 1 : up 2 : down 3 : testing 4 : unknown 5 : dormant 6 : notPresent
	 * 7 : lowerLayerDown<br>
	 * 
	 * MIB IF-MIB ; - View Supporting Images this link will generate a new
	 * window Description "The current operational state of the interface. The
	 * testing(3) state indicates that no operational packets can be passed. If
	 * ifAdminStatus is down(2) then ifOperStatus should be down(2). If
	 * ifAdminStatus is changed to up(1) then ifOperStatus should change to
	 * up(1) if the interface is ready to transmit and receive network traffic;
	 * it should change to dormant(5) if the interface is waiting for external
	 * actions (such as a serial line waiting for an incoming connection); it
	 * should remain in the down(2) state if and only if there is a fault that
	 * prevents it from going to the up(1) state; it should remain in the
	 * notPresent(6) state if the interface has missing (typically, hardware)
	 * components."
	 */
	public final String ifOperStatus = ".1.3.6.1.2.1.2.2.1.8";

	/**
	 * Object ifLastChange <br>
	 * OID 1.3.6.1.2.1.2.2.1.9 <br>
	 * Type TimeTicks <br>
	 * Permission read-only<br>
	 * Status current <br>
	 * MIB IF-MIB ; - View Supporting Images this link will generate a new
	 * window Description "The value of sysUpTime at the time the interface
	 * entered its current operational state. If the current state was entered
	 * prior to the last re-initialization of the local network management
	 * subsystem, then this object contains a zero value."
	 */
	public final String ifLastChange = ".1.3.6.1.2.1.2.2.1.9";

	/**
	 * Object ifInOctets <br>
	 * OID 1.3.6.1.2.1.2.2.1.10<br>
	 * Type Counter32 <br>
	 * 
	 * Permission read-only<br>
	 * Status current <br>
	 * MIB IF-MIB ; - View Supporting Images this link will generate a new
	 * window Description "The total number of octets received on the interface,
	 * including framing characters.
	 * 
	 * Discontinuities in the value of this counter can occur at
	 * re-initialization of the management system, and at other times as
	 * indicated by the value of ifCounterDiscontinuityTime."
	 */
	public final String ifInOctets = ".1.3.6.1.2.1.2.2.1.10";

	/**
	 * Object ifInUcastPkts <br>
	 * OID 1.3.6.1.2.1.2.2.1.11 <br>
	 * Type Counter32 <br>
	 * 
	 * Permission read-only<br>
	 * Status current <br>
	 * MIB IF-MIB ; - View Supporting Images this link will generate a new
	 * window Description "The number of packets, delivered by this sub-layer to
	 * a higher (sub-)layer, which were not addressed to a multicast or
	 * broadcast address at this sub-layer.
	 * 
	 * Discontinuities in the value of this counter can occur at
	 * re-initialization of the management system, and at other times as
	 * indicated by the value of ifCounterDiscontinuityTime."
	 */
	public final String ifInUcastPkts = ".1.3.6.1.2.1.2.2.1.11";

	/**
	 * Object ifInNUcastPkts <br>
	 * OID 1.3.6.1.2.1.2.2.1.12 <br>
	 * Type Counter32 <br>
	 * 
	 * Permission read-only<br>
	 * Status deprecated <br>
	 * MIB IF-MIB ; - View Supporting Images this link will generate a new
	 * window Description "The number of packets, delivered by this sub-layer to
	 * a higher (sub-)layer, which were addressed to a multicast or broadcast
	 * address at this sub-layer.
	 * 
	 * Discontinuities in the value of this counter can occur at
	 * re-initialization of the management system, and at other times as
	 * indicated by the value of ifCounterDiscontinuityTime.
	 * 
	 * This object is deprecated in favour of ifInMulticastPkts and
	 * ifInBroadcastPkts."
	 */
	public final String ifInNUcastPkts = ".1.3.6.1.2.1.2.2.1.12";

	/**
	 * Object ifInDiscards <br>
	 * OID 1.3.6.1.2.1.2.2.1.13 <br>
	 * Type Counter32 <br>
	 * 
	 * Permission read-only<br>
	 * Status current <br>
	 * MIB IF-MIB ; - View Supporting Images this link will generate a new
	 * window Description "The number of inbound packets which were chosen to be
	 * discarded even though no errors had been detected to prevent their being
	 * deliverable to a higher-layer protocol. One possible reason for
	 * discarding such a packet could be to free up buffer space.
	 * 
	 * Discontinuities in the value of this counter can occur at
	 * re-initialization of the management system, and at other times as
	 * indicated by the value of ifCounterDiscontinuityTime."
	 */
	public final String ifInDiscards = ".1.3.6.1.2.1.2.2.1.13";

	/**
	 * Object ifInErrors <br>
	 * OID 1.3.6.1.2.1.2.2.1.14<br>
	 * Type Counter32 <br>
	 * 
	 * Permission read-only<br>
	 * Status current <br>
	 * MIB IF-MIB ; - View Supporting Images this link will generate a new
	 * window Description "For packet-oriented interfaces, the number of inbound
	 * packets that contained errors preventing them from being deliverable to a
	 * higher-layer protocol. For character- oriented or fixed-length
	 * interfaces, the number of inbound transmission units that contained
	 * errors preventing them from being deliverable to a higher-layer protocol.
	 * 
	 * Discontinuities in the value of this counter can occur at
	 * re-initialization of the management system, and at other times as
	 * indicated by the value of ifCounterDiscontinuityTime."
	 */
	public final String ifInErrors = ".1.3.6.1.2.1.2.2.1.14";

	/**
	 * Object ifInUnknownProtos <br>
	 * OID 1.3.6.1.2.1.2.2.1.15 <br>
	 * Type Counter32 <br>
	 * 
	 * Permission read-only<br>
	 * Status current <br>
	 * MIB IF-MIB ; - View Supporting Images this link will generate a new
	 * window Description "For packet-oriented interfaces, the number of packets
	 * received via the interface which were discarded because of an unknown or
	 * unsupported protocol. For character-oriented or fixed-length interfaces
	 * that support protocol multiplexing the number of transmission units
	 * received via the interface which were discarded because of an unknown or
	 * unsupported protocol. For any interface that does not support protocol
	 * multiplexing, this counter will always be 0.
	 * 
	 * Discontinuities in the value of this counter can occur at
	 * re-initialization of the management system, and at other times as
	 * indicated by the value of ifCounterDiscontinuityTime."
	 */

	public final String ifInUnknownProtos = ".1.3.6.1.2.1.2.2.1.15";

	/**
	 * Object ifOutOctets <br>
	 * OID 1.3.6.1.2.1.2.2.1.16<br>
	 * Type Counter32 <br>
	 * 
	 * Permission read-only<br>
	 * Status current <br>
	 * MIB IF-MIB ; - View Supporting Images this link will generate a new
	 * window Description "The total number of octets transmitted out of the
	 * interface, including framing characters.
	 * 
	 * Discontinuities in the value of this counter can occur at
	 * re-initialization of the management system, and at other times as
	 * indicated by the value of ifCounterDiscontinuityTime."
	 */
	public final String ifOutOctets = ".1.3.6.1.2.1.2.2.1.16";

	/**
	 * Object ifOutUcastPkts <br>
	 * OID 1.3.6.1.2.1.2.2.1.17 <br>
	 * Type Counter32 <br>
	 * 
	 * Permission read-only<br>
	 * Status current <br>
	 * MIB IF-MIB ; - View Supporting Images this link will generate a new
	 * window Description "The total number of packets that higher-level
	 * protocols requested be transmitted, and which were not addressed to a
	 * multicast or broadcast address at this sub-layer, including those that
	 * were discarded or not sent.
	 * 
	 * Discontinuities in the value of this counter can occur at
	 * re-initialization of the management system, and at other times as
	 * indicated by the value of ifCounterDiscontinuityTime."
	 */
	public final String ifOutUcastPkts = ".1.3.6.1.2.1.2.2.1.17";

	/**
	 * Object ifOutNUcastPkts <br>
	 * OID 1.3.6.1.2.1.2.2.1.18 <br>
	 * Type Counter32 <br>
	 * 
	 * Permission read-only<br>
	 * Status deprecated <br>
	 * MIB IF-MIB ; - View Supporting Images this link will generate a new
	 * window Description "The total number of packets that higher-level
	 * protocols requested be transmitted, and which were addressed to a
	 * multicast or broadcast address at this sub-layer, including those that
	 * were discarded or not sent.
	 * 
	 * Discontinuities in the value of this counter can occur at
	 * re-initialization of the management system, and at other times as
	 * indicated by the value of ifCounterDiscontinuityTime.
	 * 
	 * This object is deprecated in favour of ifOutMulticastPkts and
	 * ifOutBroadcastPkts."
	 */
	public final String ifOutNUcastPkts = ".1.3.6.1.2.1.2.2.1.18";

	/**
	 * Object ifOutDiscards <br>
	 * OID 1.3.6.1.2.1.2.2.1.19 <br>
	 * Type Counter32 <br>
	 * 
	 * Permission read-only<br>
	 * Status current <br>
	 * MIB IF-MIB ; - View Supporting Images this link will generate a new
	 * window Description "The number of outbound packets which were chosen to
	 * be discarded even though no errors had been detected to prevent their
	 * being transmitted. One possible reason for discarding such a packet could
	 * be to free up buffer space.
	 * 
	 * Discontinuities in the value of this counter can occur at
	 * re-initialization of the management system, and at other times as
	 * indicated by the value of ifCounterDiscontinuityTime."
	 */
	public final String ifOutDiscards = ".1.3.6.1.2.1.2.2.1.19";

	/**
	 * Object ifOutErrors <br>
	 * OID 1.3.6.1.2.1.2.2.1.20<br>
	 * Type Counter32 <br>
	 * 
	 * Permission read-only<br>
	 * Status current <br>
	 * MIB IF-MIB ; - View Supporting Images this link will generate a new
	 * window Description "For packet-oriented interfaces, the number of
	 * outbound packets that could not be transmitted because of errors. For
	 * character-oriented or fixed-length interfaces, the number of outbound
	 * transmission units that could not be transmitted because of errors.
	 * 
	 * Discontinuities in the value of this counter can occur at
	 * re-initialization of the management system, and at other times as
	 * indicated by the value of ifCounterDiscontinuityTime."
	 */
	public final String ifOutErrors = ".1.3.6.1.2.1.2.2.1.20";

	/**
	 * 
	 Object ifName<br>
	 * OID 1.3.6.1.2.1.31.1.1.1.1<br>
	 * Type DisplayString <br>
	 * Permission read-only<br>
	 * Status current<br>
	 * MIB IF-MIB ; - View Supporting Images this link will generate a new
	 * window Description "The textual name of the interface. The value of this
	 * object should be the name of the interface as assigned by the local
	 * device and should be suitable for use in commands entered at the device's
	 * `console'. This might be a text name, such as `le0' or a simple port
	 * number, such as `1', depending on the interface naming syntax of the
	 * device. If several entries in the ifTable together represent a single
	 * interface as named by the device, then each will have the same value of
	 * ifName. Note that for an agent which responds to SNMP queries concerning
	 * an interface on some other (proxied) device, then the value of ifName for
	 * such an interface is the proxied device's local name for it.<br>
	 * 
	 * If there is no local name, or this object is otherwise not applicable,
	 * then this object contains a zero-length string.<br>
	 */

	public final String ifName = ".1.3.6.1.2.1.31.1.1.1.1";

	/**
	 * Object ifHCInOctets<br>
	 * OID 1.3.6.1.2.1.31.1.1.1.6<br>
	 * Type Counter64<br>
	 * Permission read-only<br>
	 * Status current<br>
	 * MIB IF-MIB ; - View Supporting Images this link will generate a new
	 * window Description "The total number of octets received on the interface,
	 * including framing characters. This object is a 64-bit version of
	 * ifInOctets.<br>
	 * 
	 * Discontinuities in the value of this counter can occur at
	 * re-initialization of the management system, and at other times as
	 * indicated by the value of ifCounterDiscontinuityTime."<br>
	 */
	public final String ifHCInOctets = ".1.3.6.1.2.1.31.1.1.1.6";

	/**
	 * Object ifHCInUcastPkts OID 1.3.6.1.2.1.31.1.1.1.7 Type Counter64
	 * Permission read-only Status current MIB IF-MIB ; - View Supporting Images
	 * this link will generate a new window Description "The number of packets,
	 * delivered by this sub-layer to a higher (sub-)layer, which were not
	 * addressed to a multicast or broadcast address at this sub-layer. This
	 * object is a 64-bit version of ifInUcastPkts.
	 * 
	 * Discontinuities in the value of this counter can occur at
	 * re-initialization of the management system, and at other times as
	 * indicated by the value of ifCounterDiscontinuityTime."
	 */
	public final String ifHCInUcastPkts = ".1.3.6.1.2.1.31.1.1.1.7";

	/**
	 * Object ifHCInMulticastPkts OID 1.3.6.1.2.1.31.1.1.1.8 Type Counter64
	 * Permission read-only Status current MIB IF-MIB ; - View Supporting Images
	 * this link will generate a new window Description "The number of packets,
	 * delivered by this sub-layer to a higher (sub-)layer, which were addressed
	 * to a multicast address at this sub-layer. For a MAC layer protocol, this
	 * includes both Group and Functional addresses. This object is a 64-bit
	 * version of ifInMulticastPkts.
	 * 
	 * Discontinuities in the value of this counter can occur at
	 * re-initialization of the management system, and at other times as
	 * indicated by the value of ifCounterDiscontinuityTime."
	 */
	public final String ifHCInMulticastPkts = ".1.3.6.1.2.1.31.1.1.1.8";

	/**
	 * 
	 Object ifHCInBroadcastPkts OID 1.3.6.1.2.1.31.1.1.1.9 Type Counter64
	 * Permission read-only Status current MIB IF-MIB ; - View Supporting Images
	 * this link will generate a new window Description "The number of packets,
	 * delivered by this sub-layer to a higher (sub-)layer, which were addressed
	 * to a broadcast address at this sub-layer. This object is a 64-bit version
	 * of ifInBroadcastPkts.
	 * 
	 * Discontinuities in the value of this counter can occur at
	 * re-initialization of the management system, and at other times as
	 * indicated by the value of ifCounterDiscontinuityTime."
	 */

	public final String ifHCInBroadcastPkts = ".1.3.6.1.2.1.31.1.1.1.9";

	/**
	 * Object ifHCOutOctets OID 1.3.6.1.2.1.31.1.1.1.10 Type Counter64
	 * Permission read-only Status current MIB IF-MIB ; - View Supporting Images
	 * this link will generate a new window Description "The total number of
	 * octets transmitted out of the interface, including framing characters.
	 * This object is a 64-bit version of ifOutOctets.
	 * 
	 * Discontinuities in the value of this counter can occur at
	 * re-initialization of the management system, and at other times as
	 * indicated by the value of ifCounterDiscontinuityTime."
	 */

	public final String ifHCOutOctets = ".1.3.6.1.2.1.31.1.1.1.10";

	/**
	 * Object ifHCOutUcastPkts OID 1.3.6.1.2.1.31.1.1.1.11 Type Counter64
	 * Permission read-only Status current MIB IF-MIB ; - View Supporting Images
	 * this link will generate a new window Description "The total number of
	 * packets that higher-level protocols requested be transmitted, and which
	 * were not addressed to a multicast or broadcast address at this sub-layer,
	 * including those that were discarded or not sent. This object is a 64-bit
	 * version of ifOutUcastPkts.
	 * 
	 * Discontinuities in the value of this counter can occur at
	 * re-initialization of the management system, and at other times as
	 * indicated by the value of ifCounterDiscontinuityTime."
	 */
	public final String ifHCOutUcastPkts = ".1.3.6.1.2.1.31.1.1.1.11";

	/**
	 * Object ifHCOutMulticastPkts OID 1.3.6.1.2.1.31.1.1.1.12 Type Counter64
	 * Permission read-only Status current MIB IF-MIB ; - View Supporting Images
	 * this link will generate a new window Description "The total number of
	 * packets that higher-level protocols requested be transmitted, and which
	 * were addressed to a multicast address at this sub-layer, including those
	 * that were discarded or not sent. For a MAC layer protocol, this includes
	 * both Group and Functional addresses. This object is a 64-bit version of
	 * ifOutMulticastPkts.
	 * 
	 * Discontinuities in the value of this counter can occur at
	 * re-initialization of the management system, and at other times as
	 * indicated by the value of ifCounterDiscontinuityTime."
	 */
	public final String ifHCOutMulticastPkts = ".1.3.6.1.2.1.31.1.1.1.12";

	/**
	 * Object ifHCOutBroadcastPkts OID 1.3.6.1.2.1.31.1.1.1.13 Type Counter64
	 * Permission read-only Status current MIB IF-MIB ; - View Supporting Images
	 * this link will generate a new window Description "The total number of
	 * packets that higher-level protocols requested be transmitted, and which
	 * were addressed to a broadcast address at this sub-layer, including those
	 * that were discarded or not sent. This object is a 64-bit version of
	 * ifOutBroadcastPkts.
	 * 
	 * Discontinuities in the value of this counter can occur at
	 * re-initialization of the management system, and at other times as
	 * indicated by the value of ifCounterDiscontinuityTime."
	 */
	public final String ifHCOutBroadcastPkts = ".1.3.6.1.2.1.31.1.1.1.13";

	/**
	 * 
	 Object ifHighSpeed<br>
	 * OID 1.3.6.1.2.1.31.1.1.1.15<br>
	 * Type Gauge32 <br>
	 * 
	 * Permission read-only<br>
	 * Status current <br>
	 * MIB IF-MIB ; - View Supporting Images this link will generate a new
	 * window Description "An estimate of the interface's current bandwidth in
	 * units of 1,000,000 bits per second. If this object reports a value of `n'
	 * then the speed of the interface is somewhere in the range of `n-500,000'
	 * to `n+499,999'. For interfaces which do not vary in bandwidth or for
	 * those where no accurate estimation can be made, this object should
	 * contain the nominal bandwidth. For a sub-layer which has no concept of
	 * bandwidth, this object should be zero." <br>
	 */
	public final String ifHighSpeed = ".1.3.6.1.2.1.31.1.1.1.15";

	/**
	 * Object ifAlias OID 1.3.6.1.2.1.31.1.1.1.18 Type DisplayString Permission
	 * read-write Status current MIB IF-MIB ; - View Supporting Images this link
	 * will generate a new window Description "This object is an 'alias' name
	 * for the interface as specified by a network manager, and provides a
	 * non-volatile 'handle' for the interface.
	 * 
	 * On the first instantiation of an interface, the value of ifAlias
	 * associated with that interface is the zero-length string. As and when a
	 * value is written into an instance of ifAlias through a network management
	 * set operation, then the agent must retain the supplied value in the
	 * ifAlias instance associated with the same interface for as long as that
	 * interface remains instantiated, including across all re-
	 * initializations/reboots of the network management system, including those
	 * which result in a change of the interface's ifIndex value.
	 * 
	 * An example of the value which a network manager might store in this
	 * object for a WAN interface is the (Telco's) circuit number/identifier of
	 * the interface.
	 * 
	 * Some agents may support write-access only for interfaces having
	 * particular values of ifType. An agent which supports write access to this
	 * object is required to keep the value in non-volatile storage, but it may
	 * limit the length of new values depending on how much storage is already
	 * occupied by the current values for other interfaces."
	 */

	public final String ifAlias = ".1.3.6.1.2.1.31.1.1.1.18";
	
	
	
	/**
	 * Object linkDown <br>
	 * OID 1.3.6.1.6.3.1.1.5.3<br>
	 * Status current <br>
	 * MIB IF-MIB ; - View Supporting Images<br>
	 * Trap Components<br>
	 * ifIndex<br>
	 * ifAdminStatus<br>
	 * ifOperStatus <br>
	 * 
	 * Description "A linkDown trap signifies that the SNMP entity, acting
	 * in an agent role, has detected that the ifOperStatus object for one
	 * of its communication links is about to enter the down state from some
	 * other state (but not from the notPresent state). This other state is
	 * indicated by the included value of ifOperStatus."
	 */

	public final String linkDown = ".1.3.6.1.6.3.1.1.5.3";

	/**
	 * Object linkUp <br>
	 * OID 1.3.6.1.6.3.1.1.5.4<br>
	 * Status current <br>
	 * MIB IF-MIB ; - View Supporting Images<br>
	 * Trap Components <br>
	 * ifIndex<br>
	 * ifAdminStatus<br>
	 * ifOperStatus <br>
	 * 
	 * Description "A linkUp trap signifies that the SNMP entity, acting in
	 * an agent role, has detected that the ifOperStatus object for one of
	 * its communication links left the down state and transitioned into
	 * some other state (but not into the notPresent state). This other
	 * state is indicated by the included value of ifOperStatus."
	 */
	public final String linkUp = ".1.3.6.1.6.3.1.1.5.4";
	
	

}
