package test.bas.api;

import java.util.HashMap;
import java.util.Map;

import fxms.bas.api.VarApi;
import fxms.bas.fxo.FxmsUtil;
import fxms.bas.impl.api.VarApiDfo;
import fxms.bas.signal.ReloadSignal.ReloadType;
import subkjh.bas.co.utils.DateUtil;

public class VarApiTest {

	public static void main(String[] args) {

		VarApi.api = new VarApiDfo();

		VarApiTest test = new VarApiTest();
//		test.select();
//		test.initTimeVar();
		test.checkUpdateTime();
	}

	void select() {
		try {
			System.out.println(FxmsUtil.toJson(VarApi.getApi().getVarList("TIME")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void test2() {

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("varGrpName", "TIME");
		para.put("edtblYn", "N");
		para.put("varDesc", "처리시간 관리");
		para.put("varMemo", "시스템에서 자동 관리함");
		for (ReloadType data : ReloadType.values()) {
			try {
				para.put("varVal", DateUtil.getDtm());
				para.put("varDispName", "업데이트시간 " + data.name());
				VarApi.getApi().setVarValue("fxms.data.updated.time." + data.name(), para, false);

				para.put("varVal", DateUtil.getDtm(System.currentTimeMillis() + 5000L));
				para.put("varDispName", "적용시간 " + data.name());
				VarApi.getApi().setVarValue("fxms.data.applied.time." + data.name(), para, false);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	void initTimeVar() {
		Map<String, Object> varInfo = new HashMap<String, Object>();
		varInfo.put("varGrpName", "TIME");
		varInfo.put("varDesc", "데이터가 최종 업데이트 된 시간을 나타낸다.");
		varInfo.put("varTypeCd", "S");
		varInfo.put("varTypeVal", "HSTIME");
		for (ReloadType type : ReloadType.values()) {
			String varName = VarApi.UPDATED_TIME_VAR + type;
			try {
				varInfo.put("varDispName", type + " 데이터 최종 변경 시간");
				VarApi.getApi().updateVarInfo(varName, varInfo);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	void checkUpdateTime() {
		for ( ReloadType type : ReloadType.values()) {
		VarApi.getApi().appliedData(type, DateUtil.getDtm(System.currentTimeMillis() - 60000));
		}
		
		System.out.println(VarApi.getApi().getUpdatedData());
	}
}
