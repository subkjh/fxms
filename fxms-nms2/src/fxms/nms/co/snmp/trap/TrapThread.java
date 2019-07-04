package fxms.nms.co.snmp.trap;

import java.util.concurrent.LinkedBlockingQueue;

import subkjh.bas.co.log.Logger;

import com.adventnet.snmp.snmp2.SnmpPDU;
import com.adventnet.snmp.snmp2.SnmpVarBind;
import com.adventnet.snmp.snmp2.UDPProtocolOptions;

import fxms.bas.fxo.thread.FXTHREAD_STATUS;
import fxms.bas.fxo.thread.FxThread;
import fxms.nms.api.TrapApi;
import fxms.nms.co.snmp.mib.IFMIB;
import fxms.nms.co.snmp.mib.SNMPV2_MIB;
import fxms.nms.co.snmp.mib.TrapMib;
import fxms.nms.co.snmp.mo.TrapNode;
import fxms.nms.co.snmp.trap.actor.TrapActor;
import fxms.nms.co.snmp.trap.vo.RecvTrapVo;
import fxms.nms.co.snmp.trap.vo.TrapVo;
import fxms.nms.co.snmp.vo.OidValue;
import fxms.nms.co.snmp.vo.SNMP;
import fxms.nms.co.snmp.vo.SNMP.TrapType;
import fxms.nms.co.snmp.vo.SNMP.Type;
import fxms.nms.co.snmp.vo.SNMP.Version;

/**
 * 받은 트랩을 처리하는 스레드
 * 
 * @author subkjh
 * 
 */
public class TrapThread extends FxThread {

	private final SNMPV2_MIB MIB = new SNMPV2_MIB();
	private final IFMIB IFMIB = new IFMIB();
	private StdTrapParser trapParser;
	private LinkedBlockingQueue<RecvTrapVo> queue;

	/**
	 * 
	 * @param queue
	 *            받은 트랩이 입력되는 큐
	 * @param name
	 *            스레드명
	 * @param receiveAll
	 *            모두 받을지 알고 있는 장비에서 올라오는 트랩만 받을지 여부
	 */
	public TrapThread(LinkedBlockingQueue<RecvTrapVo> queue) {

		this.queue = queue;
		this.trapParser = new StdTrapParser();

	}

	@Override
	public void doWork() {

		TrapNode node;
		long ptime;
		RecvTrapVo pdu;
		TrapVo vo;

		while (true) {

			getCounter().setStatus(FXTHREAD_STATUS.Waiting);

			try {
				pdu = queue.take();
				vo = makeTrapVo(pdu);

				TrapApi.getApi().write2File(vo);

				Logger.logger.debug("{}", vo.toString());

			} catch (InterruptedException e) {
				continue;
			} catch (Exception e) {
				Logger.logger.error(e);
				continue;
			}

			if (vo != null) {

				try {
					getCounter().setStatus(FXTHREAD_STATUS.Running);

					ptime = System.currentTimeMillis();

					node = TrapApi.getApi().getTrapNode(vo);

					trapParser.parse(node, vo);

					if (node == null) {
						TrapApi.getApi().sendEventUnknownNode(vo.getIpAddress());
					} else {
						if (node.isTrapRecv() == false) {
							TrapApi.getApi().sendEventInvalidNode(node);
							node = null;
						}
					}

					// Thread.currentThread().setName("TrapNode-" +
					// vo.getIpAddress());

					Logger.logger.debug((node == null ? vo.getIpAddress() : node.toString()));

					if (node != null) {

						for (TrapActor actor : TrapApi.getApi().getActorList()) {

							Logger.logger.trace("{}", actor.getClass().getSimpleName());

							try {
								vo = actor.parse(node, vo);
								if (vo == null)
									break;
							} catch (Exception e) {
								Logger.logger.error(e);
							}
						}
					}

					getCounter().addOk(System.currentTimeMillis() - ptime);

					// Thread.currentThread().setName(getName());

				} catch (Exception e) {
					Logger.logger.error(e);
				}

				vo = null;
			}
		}
	}

	@Override
	protected void doInit() {

	}

	private TrapVo makeTrapVo(RecvTrapVo recvVo) throws Exception {

		TrapVo vo = new TrapVo();
		SnmpPDU pdu = recvVo.pdu;
		vo.setMstimeRecv(recvVo.mstimeRecv);

		String ipAddress;
		try {

			if (pdu.getVersion() == SNMP.Version.Ver1.getByte()) {
				ipAddress = pdu.getAgentAddress().getHostAddress();
			} else {
				UDPProtocolOptions opt = (UDPProtocolOptions) pdu.getProtocolOptions();
				ipAddress = opt.getRemoteHost();
			}

		} catch (Exception e) {
			UDPProtocolOptions opt = (UDPProtocolOptions) pdu.getProtocolOptions();
			ipAddress = opt.getRemoteHost();
		}
		vo.setIpAddress(ipAddress);

		vo.setVer(Version.getVersion((byte) pdu.getVersion()));
		// vo.setTrapType(pdu.getTrapType());

		if (pdu.getVersion() == SNMP.Version.Ver1.getByte()) {
			trap1(pdu, vo);
		} else {
			vo.setTrapOid(pdu.getTrapOID().toString());
			vo.setUptime(pdu.getUpTime());

			trap2(pdu, vo);
		}

		OidValue data;
		try {
			int index = 0;
			SnmpVarBind bind;
			while (true) {

				bind = pdu.getVariableBinding(index);
				if (bind == null)
					break;

				data = new OidValue(bind.getObjectID().toString(), Type.getOidType(bind.getVariable().getType()), bind
						.getVariable().toString(), bind.getVariable().toBytes());

				vo.add(data);

				index++;

			}

		} catch (Exception e) {
		}

		return vo;

	}

	private void trap1(SnmpPDU snmpPdu, TrapVo vo) {

		switch (snmpPdu.getTrapType()) {
		case TrapMib.GenericTrap.COLD_START:
			vo.setTrapType(TrapType.coldStart);
			break;

		case TrapMib.GenericTrap.WARM_START:
			vo.setTrapType(TrapType.warmStart);
			break;

		case TrapMib.GenericTrap.LINK_DOWN:
			vo.setTrapType(TrapType.linkDown);
			break;

		case TrapMib.GenericTrap.LINK_UP:
			vo.setTrapType(TrapType.linkUp);
			break;

		case TrapMib.GenericTrap.AUTHENTICATION_FAILURE:
			vo.setTrapType(TrapType.authenticationFailure);
			break;

		case TrapMib.GenericTrap.EGP_NEIGHBOR_LOSS:
			vo.setTrapType(TrapType.egpNeighborLoss);
			break;

		default:
			break;
		}

	}

	private void trap2(SnmpPDU pdu, TrapVo vo) {

		if (pdu.getVersion() != SNMP.Version.Ver2c.getByte())
			return;

		String trapOid;
		try {
			trapOid = pdu.getVariable(1).toString();

			if (trapOid.equals(MIB.coldStart)) {
				vo.setTrapType(TrapType.coldStart);
			} else if (trapOid.equals(MIB.warmStart)) {
				vo.setTrapType(TrapType.warmStart);
			} else if (trapOid.equals(MIB.egpNeighborLoss)) {
				vo.setTrapType(TrapType.egpNeighborLoss);
			} else if (trapOid.equals(MIB.authenticationFailure)) {
				vo.setTrapType(TrapType.authenticationFailure);
			} else if (trapOid.equals(IFMIB.linkDown)) {
				vo.setTrapType(TrapType.linkDown);
			} else if (trapOid.equals(IFMIB.linkUp)) {
				vo.setTrapType(TrapType.linkUp);
			} else {
				vo.setTrapType(TrapType.etc);
			}

		} catch (Exception e) {
			Logger.logger.error(e);
		}

	}

}
