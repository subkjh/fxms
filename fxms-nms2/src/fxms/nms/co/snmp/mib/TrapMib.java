package fxms.nms.co.snmp.mib;


public class TrapMib {

	public class GenericTrap {

		// coldStart - signifies that the sending protocol entity is
		// reinitializing itself such that the agent's
		// configuration or the protocol entity implementation may be altered.
		// warmStart - signifies that the sending protocol entity is
		// reinitializing itself such that neither the agent
		// configuration nor the protocol entity implementation is altered.
		// linkDown - signifies that the sending protocol entity recognizes a
		// failure in one of the communication links
		// represented in the agent's configuration.
		// linkUp - signifies that the sending protocol entity recognizes that
		// one of the communication links
		// represented in the agent's configuration has come up.
		// authenticationFailure - signifies that the sending protocol entity is
		// the addressee of a protocol message
		// that is not properly authenticated. While implementations of the SNMP
		// must be capable of generating this
		// trap, they must also be capable of suppressing the emission of such
		// traps via an implementation-specific
		// mechanism.
		// egpNeighborLoss - signifies that an EGP neighbor for whom the sending
		// protocol entity was an EGP peer has
		// been marked down and the peer relationship no longer obtains.
		// enterpriseSpecific - signifies that the sending protocol entity
		// recognizes that some enterprise-specific
		// event has occurred. The specific-trap field identifies the particular
		// trap which occurred.

		// coldStart(0),
		// warmStart(1),
		// linkDown(2),
		// linkUp(3),
		// authenticationFailure(4),
		// egpNeighborLoss(5),
		// enterpriseSpecific(6)

		public static final int AUTHENTICATION_FAILURE = 4;
		public static final int COLD_START = 0;
		public static final int EGP_NEIGHBOR_LOSS = 5;
		public static final int ENTERPRISE_SPECIFIC = 6;
		/** 2 */
		public static final int LINK_DOWN = 2;
		/** 3 */
		public static final int LINK_UP = 3;
		public static final int WARM_START = 1;

	}

	/**
	 * 
	 * @param trap
	 * @return
	 */
	public static String getGenericTrapName(int trap) {
		switch (trap) {
		case TrapMib.GenericTrap.COLD_START:
			return "Cold Start";
		case TrapMib.GenericTrap.WARM_START:
			return "Warm Start";
		case TrapMib.GenericTrap.LINK_DOWN:
			return "Link Down";
		case TrapMib.GenericTrap.LINK_UP:
			return "Link Up";
		case TrapMib.GenericTrap.AUTHENTICATION_FAILURE:
			return "Authentication Failure";
		case TrapMib.GenericTrap.EGP_NEIGHBOR_LOSS:
			return "Egp Neighbor Loss";
		default:
			return trap + "";
		}
	}
}
