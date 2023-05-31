package test.bas.impl.handler;

import java.util.List;
import java.util.Map;

import fxms.bas.fxo.FxmsUtil;
import fxms.bas.handler.vo.FxResponse;

public class AlarmHandlerTest extends HandlerTest {

	public static void main(String[] args) {

		try {

			AlarmHandlerTest c = new AlarmHandlerTest("localhost");
//			c.insertAlarmCfg();
			c.insertAlarmCfgMem();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private long alarmNo = 10029737;

	public AlarmHandlerTest(String host) throws Exception {
		super(host, "alarm");
	}

	public void ackAlarm() throws Exception {

		Map<String, Object> para = makePara("alarmNo", alarmNo);

//		alarmNo 알람번호 Y
//		ackDtm 확인일시 N
//		ackUserNo 확인사용자번호 N

		call("ack-alarm", para);

	}

	public void applyAlarmCfg() throws Exception {
		Map<String, Object> para = makePara("alarmCfgNo", 1000, "useYn", "Y");
		call("apply-alarm-cfg", para);
	}

	public void clearAlarm(long alarmNo) throws Exception {

//		alarmNo 알람번호 Y
//		rsnMemo 원인메모 N

		Map<String, Object> para = makePara("alarmNo", alarmNo, "rsnMemo", "테스트용");
		call("clear-alarm", para);
	}

	public void copyAlarmCfg() throws Exception {

//		alarmCfgNo 복사할 알람조건번호 Y
//		newAlarmCfgName 새로운 알람조건 이름 Y

		Map<String, Object> para = makePara("alarmCfgNo", 101, "newAlarmCfgName", "101복사분");
		call("copy-alarm-cfg", para);
	}

	public void deleteAlarmCfg() throws Exception {
		Map<String, Object> para = makePara("alarmCfgNo", 10000, "alcdNo", 23011, "alarmLevel", 3);

		call("delete-alarm-cfg", para);
	}

	public void deleteAlarmCfgMem() throws Exception {

//		alarmCfgNo 경보조건번호 Y
//		alcdNo 경보코드 Y
//		alarmLevel 경보등급 Y
//		
		Map<String, Object> para = makePara("alarmCfgNo", 10020, "alcdNo", 23011, "alarmLevel", 3);
		call("delete-alarm-cfg-mem", para);
	}

	public void fireAlarm() throws Exception {

//		moNo 관리대상번호 Y
//		alarmName 경보명 Y
//		alarmMsg 경보메시지 Y
//		moInstance MO인스턴스 N

		Map<String, Object> para = makePara("moNo", 1000, "alarmName", "TestAlarm", "alarmMsg", "테스트용 알람입니다.");
		call("fire-alarm", para);

	}

	public FxResponse insertAlarmCfg() throws Exception {
		Map<String, Object> para = makePara("alarmCfgName", "테스트알람조건22", "alarmCfgDesc", "테스트알람조건설명222");
		return call("insert-alarm-cfg", para);
	}

	public FxResponse insertAlarmCfgMem() throws Exception {
		Map<String, Object> para = makePara("alarmCfgNo", 10002, "alcdNo", 99999, "regMemo", "TEST");
		return call("insert-alarm-cfg-mem", para);
	}

	public void selectAlarm() throws Exception {
		Map<String, Object> para = makePara("alarmNo", 10029735);
		call("select-alarm", para);
	}

	public void selectAlarmCfgGridList() throws Exception {
		Map<String, Object> para = makePara();
		FxResponse response = call("select-alarm-cfg-grid-list", para);
		System.out.println(FxmsUtil.toJson(response));
	}

	public void selectAlarmCfgList() throws Exception {

		Map<String, Object> para = makePara();
		call("select-alarm-cfg-list", para);

	}

	public void selectAlarmCodeList() throws Exception {

//		moClass MO클래스 N

		Map<String, Object> para = makePara();
		call("select-alarm-code-list", para);
	}

	public FxResponse selectAlarmCurGridList() throws Exception {

//		moClass MO클래스 N

		Map<String, Object> para = makePara();
		return call("select-alarm-cur-grid-list", para);
	}

	public void selectAlarmHstList() throws Exception {

//		alcdNo 경보코드번호 N
//		inloNo 설치위치번호 N
//		modelNo 모델번호 N
//		moClass MO클래스 N
//		startOccurDtm 조회시작발생일시 Y
//		endOccurDtm 조회종료발생일시 Y
//		
		Map<String, Object> para = makePara("startOccurDtm", 20220527000000L, "endOccurDtm", 20220527015959L);
		call("select-alarm-hst-list", para);
	}

	public void setAlarmReason() throws Exception {

//		alarmNo 경보발생번호 Y
//		reasonRegDate 경보원인등록일시 N
//		reasonRegUserNo 경보원인등록운용자번호 N
//		reasonNo 경보원인번호 N
//		reasonName 경보원인명 N
//		reasonMemo 경보원인메모 N

		Map<String, Object> para = makePara("alarmNo", alarmNo, "reasonNo", 111, "reasonName", "test");
		call("set-alarm-reason", para);

	}

	public void updateAlarmCfg() throws Exception {
	}

	public void updateAlarmCfgMem() throws Exception {
	}

	void clearAll() throws Exception {
		FxResponse res = selectAlarmCurGridList();
		if (res.getCode() == 200) {
			for (Map map : (List<Map>) res.getDatas()) {
				System.out.println(FxmsUtil.toJson(map));
				clearAlarm(((Number) map.get("alarmNo")).longValue());
			}
		}
	}

}
