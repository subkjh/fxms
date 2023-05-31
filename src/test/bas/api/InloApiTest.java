package test.bas.api;

import java.util.List;

import fxms.bas.impl.dpo.inlo.MakeInloTreeDfo;
import fxms.bas.impl.dpo.inlo.SelectInloListDfo;
import fxms.bas.vo.Inlo;
import fxms.bas.vo.InloExt;

public class InloApiTest {
	public static void main(String[] args) {

		try {
//			FxCfg.getCfg().setFxServiceName(VupService.class.getSimpleName());
			InloApiTest test = new InloApiTest();
			test.getInloAllIncludeLower();
//			 InloApi.getApi().initInloAllName(10000);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	void getInloAllIncludeLower() throws Exception {
		List<Inlo> list = new SelectInloListDfo().selectInlo(null); // 설치위치 조회
		List<InloExt> voList = new MakeInloTreeDfo().getInloTree(list); // 계층으로 생성
		for (InloExt inlo : voList) {
			System.out.println(inlo.getInloNo() + ", " + inlo.getInloAllName() + ", " + inlo.getChildren().size() + ", "
					+ inlo.size());
		}
	}
}
