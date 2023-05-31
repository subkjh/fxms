package test.bas.impl.handler;

import java.util.List;
import java.util.Map;

import fxms.bas.fxo.FxmsUtil;
import fxms.bas.handler.vo.FxResponse;
import fxms.bas.impl.vo.AlarmCodeVo;
import subkjh.bas.co.utils.ObjectUtil;

public class AlarmCfgHandlerTest extends HandlerTest {

	public static void main(String[] args) {

		try {

			AlarmCfgHandlerTest c = new AlarmCfgHandlerTest("localhost");
//			c.insertAlarmCfg();
//			c.insertAlarmCfgMem();
			c.selectAlarmCfgGridList();
			c.selectAlarmCfgList();
			c.selectAlarmCfgMemListForMono();
			c.selectAlcdList();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public AlarmCfgHandlerTest(String host) throws Exception {
		super(host, "alarmcfg");
	}

	public void applyAlarmCfg() throws Exception {
		Map<String, Object> para = makePara("alarmCfgNo", 1000, "useYn", "Y");
		call("apply-alarm-cfg", para);
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

	public FxResponse insertAlarmCfg() throws Exception {
		Map<String, Object> para = makePara("alarmCfgName", "테스트알람조건22", "alarmCfgDesc", "테스트알람조건설명222");
		return call("insert-alarm-cfg", para);
	}

	public FxResponse insertAlarmCfgMem() throws Exception {
		Map<String, Object> para = makePara("alarmCfgNo", 100021, "alcdNo", 99999, "regMemo", "TEST");
		return call("insert-alarm-cfg-mem", para);
	}

	public FxResponse selectAlarmCfgGridList() throws Exception {
		Map<String, Object> para = makePara("moClass", "MO");
		return call("select-alarm-cfg-grid-list", para);
	}

	public void selectAlarmCfgList() throws Exception {
		Map<String, Object> para = makePara("moClass", "MO");
		call("select-alarm-cfg-list", para);
	}

	public FxResponse selectAlarmCfgMemListForMono() throws Exception {
		return call("select-alarm-cfg-mem-list-for-mono", makePara("moNo", 1002231));
	}

	public FxResponse selectAlcdList() throws Exception {
		FxResponse res = call("select-alcd-list", makePara());
		List<Map> list = (List) res.getDatas();
		for (Map map : list) {
			AlarmCodeVo vo = ObjectUtil.toObject(map, AlarmCodeVo.class);
			System.out.println(FxmsUtil.toJson(vo));
		}
		return res;
	}

	public void updateAlarmCfg() throws Exception {
	}

	public void updateAlarmCfgMem() throws Exception {
	}

}
