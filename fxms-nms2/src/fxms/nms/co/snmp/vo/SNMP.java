package fxms.nms.co.snmp.vo;

import com.adventnet.snmp.snmp2.SnmpAPI;

import fxms.bas.api.EventApi;
import fxms.nms.co.cd.NmsCode;
import fxms.nms.co.snmp.mib.TrapMib;

public class SNMP {

	public enum Type {

		PRIMITIVE((byte) 0x00), APPLICATION((byte) 0x40), CONTEXT((byte) 0x80), PRIVATE((byte) 0xC0), CONSTRUCTOR((byte) 0x20), UNIVERSAL(
				(byte) 0x00), BOOLEAN((byte) 0x01), INTEGER((byte) 0x02), BITSTRING((byte) 0x03), OCTETSTRING((byte) 0x04), NULL(
				(byte) 0x05), OBJECTID((byte) 0x06), SEQUENCE((byte) 0x30), IPADDRESS((byte) 0x40), COUNTER32((byte) 0x41), GAUGE32(
				(byte) 0x42), UNSIGNED32((byte) 0x42), TIMETICKS((byte) 0x43), OPAQUE((byte) 0x44), COUNTER64((byte) 0x46),

		;

		private Type(byte type) {
			this.type = type;
		}

		public static Type getOidType(byte type) {
			for (Type e : Type.values()) {
				if (e.type == type) {
					return e;
				}
			}

			return null;
		}

		private byte type;

		public byte getType() {
			return type;
		}

	}

	public enum Version {

		Ver1((byte) 0), Ver2c((byte) 1), Ver2((byte) 2), Ver3((byte) 3);

		private Version(byte b) {
			this.b = b;
		}

		public static Version getVersion(byte b) {
			for (Version e : Version.values()) {
				if (e.getByte() == b) {
					return e;
				}
			}
			return null;
		}

		private byte b;

		public byte getByte() {
			return b;
		}
	}

	public enum TrapType {
		coldStart, warmStart, linkUp, linkDown, authenticationFailure, egpNeighborLoss,
		
		
		etc;
	}

}
