package test.nms.handler;

import java.util.HashMap;
import java.util.Map;

import test.bas.impl.handler.HandlerTest;

public class NmsHandlerTest extends HandlerTest {

	public static void main(String[] args) {

		try {
			NmsHandlerTest c = new NmsHandlerTest();

			c.insertMoEquip();

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			Thread.sleep(50000);
			System.exit(0);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public NmsHandlerTest() throws Exception {
		super("localhost", "nms");
	}

	public void getNeStatus() throws Exception {
	}


	public void insertMoEquip() throws Exception {

//		moNo MO번호 Y
//		moName MO명 Y
//		moDispName MO표시명 N
//		moClass MO클래스 Y
//		moType MO유형 N
//		mngYn 관리여부 Y
//		upperMoNo 상위MO번호 Y
//		alarmCfgNo 알람조건번호 Y
//		modelNo 모델번호 Y
//		inloNo 설치위치번호 Y
//		moMemo MO메모 N
//		pollCycle 폴링주기 N
//		moOnlineStVal 관리대상온라인상태값 N
//		moOnlineStChgDtm 관리대상온라인상태변경일시 N
//		regUserNo 등록사용자번호 N
//		regDtm 등록일시 N
//		chgUserNo 수정사용자번호 N
//		chgDtm 수정일시 N
//		fxsvrIpAddr FX서버주소 Y
//		equipIpAddr 장비IP주소 Y
//		equipIpAddr2 장비IP주소2 N
//		inloMemo 설치위치메모 N
//		cntcNm 연락처명 N
//		cntcTelNo 연락처전화번호 N
//		equipMemo 장비메모 N
//		equipTid 장비TID N
//		instYmd 설치일자 N
//		syslogRecvYn SYSLOG수신여부 N
//		trapRecvYn TRAP수신여부 N
//		pingPollYn PING폴링여부 N
//		snmppingPollYn SNMPPING폴링여부 N
//		confAutoSyncYn 환경자동동기화여부 N
//		bgpRecvYn BGP수신여부 N
//		flowRecvYn FLOW수신여부 N
//		snmpPollCycle SNMP폴링주기 N
//		pingPollCycle PING폴링주기 N
//		snmpInfoStr SNMP정보문자열 N
//		telnetInfoStr TELNET정보문자열 N
//		bgpInfoStr BGP정보문자열 N
//		flowInfoStr FLOW정보문자열 N
//		usrData 사용자데이터 N
//		moNo MO번호 Y

		Map<String, Object> para = new HashMap<String, Object>();

		para.put("moName", "thingspire-gis-dev");
		para.put("fxsvrIpAddr", "localhost");
		para.put("equipIpAddr", "10.0.1.11");
		para.put("pollCycle", 60);

		para.put("snmpRead", "public");
		para.put("snmpPort", 161);
		para.put("snmpVer", "2c");

		call("insert-mo-equip", para);
	}

	public void addNetwork() throws Exception {

	}

	@SuppressWarnings("unchecked")
	public void setNetworkItem() throws Exception {

	}

	public void getNetworkItem() throws Exception {

	}

	public void updateNetwork() throws Exception {

	}

	public void deleteNetwork() throws Exception {

	}
}
