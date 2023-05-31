package test.bas.impl.handler;

import java.util.HashMap;
import java.util.Map;

public class ModelHandlerTest extends HandlerTest {

	public static void main(String[] args) {

		try {
			ModelHandlerTest c = new ModelHandlerTest();

//			c.updateModel();
//			c.selectModelList();
			c.selectModelGridList();
//			c.selectModelList();

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

	public ModelHandlerTest() throws Exception {
		super("localhost", "model");
	}

	public void insertModel() throws Exception {
//		mandatory :
//			modelNo : 모델번호
//			modelName : 모델명
//			modelClCd : 모델분류코드
//			optional :
//			vendrName : 제조사명
//			modelIdfyVal : 모델식별값
//			modelDescr : 모델설명
//			regUserNo : 등록사용자번호
//			regDtm : 등록일시
//			chgUserNo : 수정사용자번호
//			chgDtm : 수정일시

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("modelNo", 0);
		para.put("modelName", "연습용모델");
		para.put("modelClCd", "TS");
		para.put("vendrName", "띵스");
		para.put("modelDescr", "연습용");

		call("insert-model", para);
	}

	public void deleteModel() throws Exception {
		Map<String, Object> para = new HashMap<String, Object>();
		call("delete-model", para);
	}

	public void updateModel() throws Exception {
		
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("modelNo", 10000);
		para.put("modelName", "연습용모델2");
		para.put("modelClCd", "TS2");
		para.put("vendrName", "띵스2");
		para.put("modelDescr", "연습용2");
		
		call("update-model", para);
	}

	public void selectModelGridList() throws Exception {
		Map<String, Object> para = new HashMap<String, Object>();
//		para.put("modelClCd", "TS2");
//		para.put("inloClCd", "PLANT");
//		para.put("vendrName", "공통");
		call("select-model-mo-count-grid-list", para);

	}

	public void selectModelList() throws Exception {
		Map<String, Object> para = new HashMap<String, Object>();
//		para.put("modelClCd", "TS2");

		call("select-model-list", para);
	}
}
