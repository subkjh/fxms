package fxms.nms.co.snmp;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import com.adventnet.snmp.beans.ErrorMessages;
import com.adventnet.snmp.snmp2.SnmpAPI;
import com.adventnet.snmp.snmp2.SnmpException;
import com.adventnet.snmp.snmp2.SnmpOID;
import com.adventnet.snmp.snmp2.SnmpPDU;
import com.adventnet.snmp.snmp2.SnmpSession;
import com.adventnet.snmp.snmp2.SnmpString;
import com.adventnet.snmp.snmp2.SnmpVar;
import com.adventnet.snmp.snmp2.SnmpVarBind;
import com.adventnet.snmp.snmp2.UDPProtocolOptions;
import com.adventnet.snmp.snmp2.usm.USMUtils;

import fxms.bas.api.CoApi;
import fxms.nms.co.snmp.exception.SnmpErrorException;
import fxms.nms.co.snmp.exception.SnmpNotFoundOidException;
import fxms.nms.co.snmp.exception.SnmpTimeoutException;
import fxms.nms.co.snmp.vo.OidValue;
import fxms.nms.co.snmp.vo.SNMP;
import fxms.nms.mo.property.MoSnmppable;
import fxms.nms.mo.property.SnmpPass;
import subkjh.bas.co.lang.Lang;
import subkjh.bas.co.log.Logger;

public class SnmpUtilAdventNetImpl extends SnmpUtil {

	public static OidValue makeOidValue(String oid, SnmpVar snmpVar) {

		// System.out.println(oid + " = " + snmpVar);

		String value;
		byte type = SnmpAPI.NULLOBJ;
		byte bytes[] = null;

		if (snmpVar == null || snmpVar.toValue() == null)
			value = null;
		else {
			switch (snmpVar.getType()) {
			case SnmpAPI.COUNTER:
			case SnmpAPI.GAUGE:
				// case SnmpAPI.UNSIGNED32:
			case SnmpAPI.TIMETICKS:
			case SnmpAPI.COUNTER64:
			case SnmpAPI.INTEGER:
				value = snmpVar.toValue().toString();
				break;

			case SnmpAPI.OBJID:
			case SnmpAPI.IPADDRESS:
			case SnmpAPI.OPAQUE:
			case SnmpAPI.STRING:
				value = snmpVar.toString();
				break;

			case SnmpAPI.NULLOBJ:
			default:
				value = null;
			}

			type = snmpVar.getType();
			bytes = snmpVar.toBytes();
		}

		return new OidValue(oid, type, value, bytes);
	}

	private SnmpAPI api;
	private int retries = 1;
	private SnmpSession session;
	private SnmpClientImpl snmpClient;
	private SnmpPass snmpPass;
	private int timeout = 5000;

	public SnmpUtilAdventNetImpl() {

	}

	@Override
	public void close() {

		if (session != null) {
			session.close();
			session = null;

			logger.info("close");
		}

		if (api != null) {
			api.close();
			api = null;
		}
	}

	@Override
	public void open(String name, Logger logger) throws Exception {

		setLogger(logger);

		api = new SnmpAPI();
		api.setName(name + "SnmpAPI");
		api.setCharacterEncoding(SnmpUtil.CHARSET.displayName());

		session = new SnmpSession(api);
		session.setTimeout(timeout);
		session.setRetries(retries);

		snmpClient = new SnmpClientImpl();

		try {

			session.setName(name + "SnmpSession");
			session.open();

			logger.info(name + "SnmpSession open");

			session.addSnmpClient(snmpClient);
		} catch (SnmpException e) {
			close();
			throw e;
		}
	}

	@Override
	public void setTimeoutAndRetries(int timeout, int retries) {

		if (session != null) {
			session.setTimeout(timeout);
			session.setRetries(retries);
		}

		this.timeout = timeout;
		this.retries = retries;
	}

	@Override
	public List<OidValue> snmpbulk(MoSnmppable snmpNode, int nonRepeaters, int maxRepetitions, String... oidArray)
			throws SnmpTimeoutException, SnmpNotFoundOidException, SnmpErrorException {

		SnmpPDU pdu = makeSnmpPdu(snmpNode);
		pdu.setCommand(SnmpAPI.GETBULK_REQ_MSG);

		List<SnmpOID> snmpOidList = new ArrayList<SnmpOID>();

		for (String oid : oidArray) {
			SnmpOID snmpOid = new SnmpOID(oid);
			pdu.addNull(snmpOid);
			snmpOidList.add(snmpOid);
		}

		// set non-repeaters
		pdu.setErrstat(nonRepeaters);

		// set max-repetitions
		pdu.setErrindex(maxRepetitions);

		SnmpPDU res_pdu = null;

		try {
			res_pdu = session.syncSend(pdu);
		} catch (SnmpException e) {
			throw new SnmpErrorException(0, 0, e.getMessage());
		}
		if (res_pdu == null)
			throw new SnmpTimeoutException(snmpNode.getIpAddress());

		if (res_pdu.getErrstat() != 0) {

			// 여러개를 동시에 했을 경우 하나라도 없으면 오류가 발생할 때 하나씩 처리합니다.
			if (res_pdu.getErrstat() == ErrorMessages.SNMP_ERR_NOSUCHNAME) {
				return snmpget(SnmpAPI.GET_REQ_MSG, snmpNode, oidArray);
			}
			throw new SnmpErrorException(res_pdu.getErrstat(), res_pdu.getErrindex(), res_pdu.getError());
		}

		return convert(res_pdu);

	}

	@Override
	public int snmpbulk(MoSnmppable snmpNode, SnmpListener snmpListener, int nonRepeaters, int maxRepetitions,
			String... oidArray) throws SnmpErrorException {

		SnmpPDU pdu = makeSnmpPdu(snmpNode);
		pdu.setCommand(SnmpAPI.GETBULK_REQ_MSG);

		List<SnmpOID> snmpOidList = new ArrayList<SnmpOID>();

		for (String oid : oidArray) {
			SnmpOID snmpOid = new SnmpOID(oid);
			pdu.addNull(snmpOid);
			snmpOidList.add(snmpOid);
		}

		// set non-repeaters
		pdu.setErrstat(nonRepeaters);

		// set max-repetitions
		pdu.setErrindex(maxRepetitions);

		int requestId;
		try {
			requestId = session.send(pdu);
		} catch (SnmpException e) {
			throw new SnmpErrorException(e.getMessage());
		} // Send PDU and receive response PDU

		snmpClient.addSnmpListener(requestId, snmpListener);

		return requestId;

	}

	/**
	 * 
	 * @param snmpNode
	 * @param oidList
	 * @param snmpListener
	 * @return 요청ID
	 * @throws SnmpException
	 */
	@Override
	public int snmpget(MoSnmppable snmpNode, SnmpListener snmpListener, String... oidArray) throws SnmpErrorException {

		SnmpPDU pdu = makeSnmpPdu(snmpNode);
		pdu.setCommand(SnmpAPI.GET_REQ_MSG);

		List<SnmpOID> snmpOidList = new ArrayList<SnmpOID>();

		for (String oid : oidArray) {
			SnmpOID snmpOid = new SnmpOID(oid);
			pdu.addNull(snmpOid);
			snmpOidList.add(snmpOid);
		}

		int requestId;
		try {
			requestId = session.send(pdu);
		} catch (SnmpException e) {
			throw new SnmpErrorException(e.getMessage());
		}

		snmpClient.addSnmpListener(requestId, snmpListener);

		return requestId;
	}

	@Override
	public List<OidValue> snmpgetnext(MoSnmppable snmpNode, String... oidArray)
			throws SnmpTimeoutException, SnmpNotFoundOidException, SnmpErrorException {

		if (oidArray.length > 50) {
			return snmpget(SnmpAPI.GETNEXT_REQ_MSG, snmpNode, oidArray);
		}

		SnmpPDU pdu = makeSnmpPdu(snmpNode);
		pdu.setCommand(SnmpAPI.GETNEXT_REQ_MSG);

		List<SnmpOID> snmpOidList = new ArrayList<SnmpOID>();

		for (String oid : oidArray) {
			SnmpOID snmpOid = new SnmpOID(oid);
			pdu.addNull(snmpOid);
			snmpOidList.add(snmpOid);
		}

		SnmpPDU res_pdu = null;

		try {
			res_pdu = session.syncSend(pdu);
		} catch (SnmpException e) {
			throw new SnmpErrorException(0, 0, e.getMessage());
		}
		if (res_pdu == null)
			throw new SnmpTimeoutException(snmpNode.getIpAddress());

		if (res_pdu.getErrstat() != 0) {

			// 여러개를 동시에 했을 경우 하나라도 없으면 오류가 발생할 때 하나씩 처리합니다.
			if (res_pdu.getErrstat() == ErrorMessages.SNMP_ERR_NOSUCHNAME) {
				return snmpget(SnmpAPI.GETNEXT_REQ_MSG, snmpNode, oidArray);
			}
			throw new SnmpErrorException(res_pdu.getErrstat(), res_pdu.getErrindex(), res_pdu.getError());
		}

		return convert(res_pdu);

	}

	@Override
	public List<OidValue> snmpset(MoSnmppable snmpNode, OidValue... oidValueArray)
			throws SnmpTimeoutException, SnmpNotFoundOidException, SnmpErrorException {

		SnmpPDU pdu = makeSnmpPdu(snmpNode);
		pdu.setCommand(SnmpAPI.SET_REQ_MSG);
		pdu.setCommunity(snmpNode.getSnmpPass().getSnmpWrite());

		List<SnmpOID> snmpOidList = new ArrayList<SnmpOID>();
		SnmpVarBind snmpVarBind, snmpVarBindErr = null;
		for (OidValue oidValue : oidValueArray) {
			try {
				snmpVarBind = makeSnmpVarBind(oidValue);
				pdu.addVariableBinding(snmpVarBind);
				snmpOidList.add(snmpVarBind.getObjectID());
			} catch (SnmpException e) {
				throw new SnmpErrorException(oidValue + "|" + e.getMessage());
			}
		}

		SnmpPDU res_pdu = null;

		try {
			res_pdu = session.syncSend(pdu);
		} catch (SnmpException e) {
			throw new SnmpErrorException(0, 0, e.getMessage());
		}
		if (res_pdu == null)
			throw new SnmpTimeoutException(snmpNode.getIpAddress());

		if (res_pdu.getErrstat() != 0) {
			try {
				snmpVarBindErr = pdu.getVariableBinding(res_pdu.getErrindex() - 1);
			} catch (Exception e) {
			}

			throw new SnmpErrorException(res_pdu.getErrstat(), res_pdu.getErrindex(),
					snmpVarBindErr + "|" + res_pdu.getError());
		}

		return convert(res_pdu, snmpOidList);
	}

	@Override
	public List<OidValue> snmpwalk(MoSnmppable snmpNode, String oid)
			throws SnmpTimeoutException, SnmpNotFoundOidException, SnmpErrorException {

		List<OidValue> valueList = new ArrayList<OidValue>();
		SnmpPDU pduSend = makeSnmpPdu(snmpNode);
		pduSend.setCommand(SnmpAPI.GETNEXT_REQ_MSG);
		SnmpPDU pduRecv;
		SnmpOID snmpOid = new SnmpOID(oid);
		pduSend.addNull(snmpOid);
		int rootoid[] = (int[]) snmpOid.toValue();
		OidValue oidValue;

		while (true) // until received OID isn't in sub-tree
		{

			try {
				pduRecv = session.syncSend(pduSend);
			} catch (SnmpException e) {
				throw new SnmpErrorException(0, 0, e.getMessage());
			}

			if (pduRecv == null)
				throw new SnmpTimeoutException(snmpNode.getIpAddress());

			// System.out.println(CClass.toString(pduRecv));
			// System.out.println(CClass.toString(pduRecv.getVariableBindings()));

			checkPdu(pduRecv);

			// stop if outside sub-tree
			if (!isInSubTree(rootoid, pduRecv)) {
				break;
			}

			int version = pduRecv.getVersion();

			if (version == SnmpAPI.SNMP_VERSION_1) {

				oidValue = makeOidValue(pduRecv.getVariableBinding(0).getObjectID().toString(),
						pduRecv.getVariableBinding(0).getVariable());
				valueList.add(oidValue);

				if (Logger.debug)
					System.out.println(oidValue.toString());

			} else if ((version == SnmpAPI.SNMP_VERSION_2C) || (version == SnmpAPI.SNMP_VERSION_3)) {

				Enumeration<?> e = pduRecv.getVariableBindings().elements();

				while (e.hasMoreElements()) {
					int error = 0;
					SnmpVarBind varbind = (SnmpVarBind) e.nextElement();
					// check for error
					if ((error = varbind.getErrindex()) != 0) {

						if ((byte) error == SnmpAPI.ENDOFMIBVIEWEXP)
							break;

						throw new SnmpErrorException(
								"Error Indication in response: " + SnmpException.exceptionString((byte) error));
					}

					// 2013.01.17 by subkjh. OID가 같으면 브레이크 함.
					if (oid.equals(varbind.getObjectID().toString()))
						break;

					oidValue = makeOidValue(varbind.getObjectID().toString(), varbind.getVariable());
					valueList.add(oidValue);
					if (Logger.debug) {
						System.out.println(oidValue.toString());
					}
				}
			} else {
				throw new SnmpErrorException("Invalid Version Number");
			}

			SnmpOID first_oid = pduRecv.getObjectID(0);

			if (Logger.debug) {
				System.out.println(oid + "|" + first_oid.getVarObject().toString());
			}

			// 2013.01.17 by subkjh. OID가 같으면 브레이크 함.
			if (oid.equals(first_oid.getVarObject().toString()))
				break;
			if (first_oid.getVarObject().toString().startsWith(oid) == false)
				break;

			pduSend.setReqid(0);
			pduSend.getVariableBindings().clear();
			pduSend.addNull(first_oid);

		} // end of while true

		return valueList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OidValue>[] snmpwalk(MoSnmppable snmpNode, String... oidArray)
			throws SnmpTimeoutException, SnmpNotFoundOidException, SnmpErrorException {
		List<OidValue>[] listArray = new ArrayList[oidArray.length];

		for (int i = 0; i < oidArray.length; i++) {
			listArray[i] = snmpwalk(snmpNode, oidArray[i]);
		}

		return listArray;
	}

	@Override
	protected List<OidValue> doSnmpget(MoSnmppable snmpNode, String... oidArray)
			throws SnmpTimeoutException, SnmpNotFoundOidException, SnmpErrorException {

		SnmpPDU pdu = makeSnmpPdu(snmpNode);
		pdu.setCommand(SnmpAPI.GET_REQ_MSG);

		List<SnmpOID> snmpOidList = new ArrayList<SnmpOID>();
		SnmpPDU res_pdu = null;

		for (String oid : oidArray) {
			SnmpOID snmpOid = new SnmpOID(oid);
			pdu.addNull(snmpOid);
			snmpOidList.add(snmpOid);
		}

		try {
			res_pdu = session.syncSend(pdu);
		} catch (SnmpException e) {
			throw new SnmpErrorException(0, 0, e.getMessage());
		}
		if (res_pdu == null)
			throw new SnmpTimeoutException(snmpNode.getIpAddress());

		if (res_pdu.getErrstat() != 0) {

			// 여러개를 동시에 했을 경우 하나라도 없으면 오류가 발생할 때 하나씩 처리합니다.
			if (res_pdu.getErrstat() == ErrorMessages.SNMP_ERR_NOSUCHNAME) {
				return snmpget(SnmpAPI.GET_REQ_MSG, snmpNode, oidArray);
			}

			throw new SnmpErrorException(res_pdu.getErrstat(), res_pdu.getErrindex(), res_pdu.getError());
		}

		return convert(res_pdu, snmpOidList);

	}

	@Override
	protected void finalize() {
		if (session != null) {
			session.close();
			session = null;
		}

		if (api != null) {
			api.close();
			api = null;
		}
	}

	private void checkPdu(SnmpPDU pdu) throws SnmpErrorException {

		if (pdu.getErrstat() == ErrorMessages.SNMP_ERR_NOERROR)
			return;

		if (pdu.getErrstat() == ErrorMessages.SNMP_ERR_AUTHORIZATIONERROR) {
			throw new SnmpErrorException(Lang.get("SNMP 인증 오류가 발생되었습니다."));
		}

		throw new SnmpErrorException(pdu.getErrstat(), pdu.getErrindex(), "Error Indication in response: "
				+ SnmpException.exceptionString((byte) pdu.getErrstat()) + "\nErrindex: " + pdu.getErrindex());
	}

	@SuppressWarnings("unchecked")
	private List<OidValue> convert(SnmpPDU snmpPdu) {

		List<OidValue> valueList = new ArrayList<OidValue>();
		for (SnmpVarBind varBind : (Vector<SnmpVarBind>) snmpPdu.getVariableBindings()) {

			valueList.add(makeOidValue(varBind.getObjectID().toString(), varBind.getVariable()));
		}
		return valueList;
	}

	private List<OidValue> convert(SnmpPDU snmpPdu, List<SnmpOID> snmpOidList) {

		List<OidValue> valueList = new ArrayList<OidValue>();
		SnmpVar snmpVar;
		OidValue oidValue;
		for (SnmpOID snmpOid : snmpOidList) {

			snmpVar = snmpPdu.getVariable(snmpOid);
			oidValue = makeOidValue(snmpOid.toString(), snmpVar);
			valueList.add(oidValue);
		}
		return valueList;
	}

	private SnmpPass getSnmpPass4Default() {
		if (snmpPass == null) {
			snmpPass = new SnmpPass();
			try {
				CoApi api = CoApi.getApi();

				snmpPass.setSnmpAuthPwd(api.getVarValue("SNMP_AUTH_PASSWORD", ""));
				snmpPass.setSnmpAuthProtocol(api.getVarValue("Snmp_Auth_Protocol", SnmpPass.AUTH_PROTOCOL_NO_AUTH));
				snmpPass.setSnmpRead(api.getVarValue("SNMP_COMM_READ", ""));
				snmpPass.setSnmpWrite(api.getVarValue("SNMP_COMM_WRITE", ""));
				snmpPass.setSnmpContextId(api.getVarValue("SNMP_CONTEXT_ID", ""));
				snmpPass.setSnmpContextName(api.getVarValue("SNMP_CONTEXT_NAME", ""));
				snmpPass.setSnmpPort(api.getVarValue("SNMP_PORT", 161));
				snmpPass.setSnmpPrivPwd(api.getVarValue("SNMP_PRIV_PASSWORD", ""));
				snmpPass.setSnmpPrivProtocol(api.getVarValue("SNMP_PRIV_PROTOCOL", SnmpPass.PRIV_PROTOCOL_NO_PRIV));
				snmpPass.setSnmpUserName(api.getVarValue("SNMP_USER_NAME", "nprism"));
				snmpPass.setSnmpVer(api.getVarValue("SNMP_VER", SnmpPass.VER2c));

			} catch (Exception e) {
				logger.error(e);
			}
		}
		return snmpPass;
	}

	/** check if first varbind oid has rootoid as an ancestor in MIB tree */
	private boolean isInSubTree(int[] rootoid, SnmpPDU pdu) {
		SnmpOID objID = pdu.getObjectID(0);
		if (objID == null) {
			return false;
		}

		int oid[] = (int[]) objID.toValue();
		if (oid == null) {
			return false;
		}
		if (oid.length < rootoid.length) {
			return false;
		}

		for (int i = 0; i < rootoid.length; i++) {
			if (oid[i] != rootoid[i]) {
				return false;
			}
		}
		return true;
	}

	private SnmpPDU makeSnmpPdu(MoSnmppable snmpNode) throws SnmpErrorException {

		SnmpPass snmpPass = snmpNode.getSnmpPass();
		if (snmpPass == null || snmpPass.isValidSnmp() == false) {
			snmpPass = SnmpPass.defSnmp;
		}

		if (Logger.debug) {
			System.out.println(snmpNode.getIpAddress() + ":" + snmpPass.getSnmpPort() + "|" + snmpPass.getSnmpVer()
					+ "|" + snmpPass.getSnmpRead());
		}

		SnmpPDU pdu = new SnmpPDU();
		UDPProtocolOptions udpOpt = new UDPProtocolOptions();
		udpOpt.setRemoteHost(snmpNode.getIpAddress());
		udpOpt.setRemotePort(snmpPass.getSnmpPort());
		pdu.setProtocolOptions(udpOpt);
		pdu.setVersion(snmpPass.getSnmpVer());
		pdu.setCommunity(snmpPass.getSnmpRead());

		if (snmpPass.getSnmpVer() == SnmpPass.VER3) {

			try {
				/*
				 * public static void init_v3_parameters(java.lang.String userName, byte[]
				 * engineID, int authProtocol, java.lang.String authPassword, java.lang.String
				 * privPassword, ProtocolOptions po, SnmpSession session, boolean validateUser,
				 * int privProtocol) throws SnmpException A comprehensive initialisation routine
				 * that creates new SNMPv3 user entries and performs time synchronization. Since
				 * the engineID is accepted as an argument, the SNMPv3 discovery will not be
				 * done. Hence an SnmpEngineEntry will not be created and added to the
				 * SnmpEngineTable. If the engineID specified is null or of zero length, then
				 * the method will automatically do a discovery and add an SnmpEngineEntry to
				 * the SnmpEngineTable. This method will do a time synchronization and hence
				 * will create a new USMUserEntry and will add it to the USMUserTable. After
				 * this method is called successfully ( without any exception ) with a proper
				 * engineID as the argument, then a valid USMUserEntry will be present in the
				 * USMUserTable and no entry will be added to the SnmpEngineTable. Thus whenever
				 * an SNMPv3 request is sent for this particular agent, this engineID should be
				 * specified in the request using the setEngineID method.
				 * 
				 * Parameters: userName - The string representing the SnmpV3 principal. engineID
				 * - The engineID of the remote SNMPv3 entity ( the agent ). authProtocol - The
				 * authentication protocol. Should be one of the constants, MD5_AUTH, SHA_AUTH
				 * or NO_AUTH. These constants are defined in USMUserEntry. authPassword - The
				 * authentication password which is converted to a localized key. privPassword -
				 * The privacy password which is converted into a localized private key. po -
				 * The ProtocolOptions instance session - The SnmpSession instance. This should
				 * be in open state. The discovery and timeSync messages are sent over this
				 * session. validateUser - The boolean value. If this value is set to true, and
				 * if this method is called for an NO_AUTH_NO_PRIV user, then a get request will
				 * be sent to know if the user exits. AUTH_PRIV user, then a get request will be
				 * sent to know if the priv password is correct. privProtocol - The PrivProtocol
				 * value. Should be one of the constants, CBC_DES, CFB_AES_128, CFB_AES_192,
				 * CFB_AES_256, CBC_3DES or NO_PRIV, defined in USMUserEntry. Throws:
				 * SnmpException - is thrown on error. Since: AdventNet SNMP API 4 SP4 ( Release
				 * 4.0.4 )
				 */

				if (snmpPass.getSnmpUserName() == null || snmpPass.getSnmpUserName().trim().length() == 0) {
					snmpPass = getSnmpPass4Default();
				}

				pdu.setUserName(snmpPass.getSnmpUserName().getBytes());
				if (snmpPass.getSnmpContextName() != null) {
					pdu.setContextName(snmpPass.getSnmpContextName().getBytes());
				}
				if (snmpPass.getSnmpContextId() != null) {
					pdu.setContextID(snmpPass.getSnmpContextId().getBytes());
				}

				USMUtils.init_v3_parameters(snmpPass.getSnmpUserName(), null, snmpPass.getSnmpAuthProtocol(),
						snmpPass.getSnmpAuthPwd(), snmpPass.getSnmpPrivPwd(), udpOpt, session, false,
						snmpPass.getSnmpPrivProtocol());

			} catch (Exception exp) {
				throw new SnmpErrorException(Lang.get("SNMP Ver3 변수 설정을 못하였습니다.", exp.getMessage()));
			}

		}

		return pdu;
	}

	private SnmpVarBind makeSnmpVarBind(OidValue oidValue) throws SnmpException {

		SnmpVar var;

		if (oidValue.getType() == SNMP.Type.OCTETSTRING) {
			var = new SnmpString(oidValue.getBytes());
		} else {
			var = SnmpVar.createVariable(oidValue.getValue(), oidValue.getType().getType());
		}

		SnmpOID oid = new SnmpOID(oidValue.getOid());

		return new SnmpVarBind(oid, var);
	}

	private List<OidValue> snmpget(byte req, MoSnmppable snmpNode, String... oidArray)
			throws SnmpTimeoutException, SnmpNotFoundOidException, SnmpErrorException {

		SnmpPDU pdu = makeSnmpPdu(snmpNode);
		pdu.setCommand(req);

		List<OidValue> oidValueList = new ArrayList<OidValue>();

		SnmpPDU res_pdu = null;

		for (String oid : oidArray) {

			pdu.getVariableBindings().clear();

			SnmpOID snmpOid = new SnmpOID(oid);
			pdu.addNull(snmpOid);

			try {
				res_pdu = session.syncSend(pdu);
			} catch (SnmpException e) {
				throw new SnmpErrorException(e.getMessage());
			} // Send PDU and receive response PDU

			if (res_pdu == null)
				throw new SnmpTimeoutException(snmpNode.getIpAddress());

			if (res_pdu.getErrstat() != 0) {
				if (res_pdu.getErrstat() == ErrorMessages.SNMP_ERR_NOSUCHNAME) {
					oidValueList.add(makeOidValue(oid, null));
				} else {
					throw new SnmpErrorException(res_pdu.getErrstat(), res_pdu.getErrindex(), res_pdu.getError());
				}
			} else {
				oidValueList.add(makeOidValue(oid, res_pdu.getVariable(0)));
			}

		}

		return oidValueList;
	}
}
