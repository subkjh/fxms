package fxms.bas.impl.dpo.mo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.fxo.FxmsUtil;
import fxms.bas.impl.dpo.FxDpo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.impl.dpo.inlo.MakeInloTreeDfo;
import fxms.bas.impl.dpo.inlo.SelectInloListDfo;
import fxms.bas.mo.Mo;
import fxms.bas.tree.Tree;
import fxms.bas.tree.TreeData;
import fxms.bas.vo.Inlo;
import fxms.bas.vo.mapp.MappMoPs;

public class GetMoTreeDpo implements FxDpo<Map<String, Object>, Tree<TreeData>> {

	public static void main(String[] args) {
		Map<String, Object> para = new HashMap<String, Object>();
//		para.put("moClass", "NODE");
		para.put("inloNo in ", " ( select LOWER_INLO_NO from FX_CF_INLO_MEM where INLO_NO = " + 10000 + " )");
		GetMoTreeDpo dpo = new GetMoTreeDpo();

		try {
			Tree<TreeData> tree = dpo.getTree(para);
			tree.print();
			System.out.println(FxmsUtil.toJson(tree));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Tree<TreeData> run(FxFact fact, Map<String, Object> data) throws Exception {
		return getTree(data);

	}

	/**
	 * 관리대상 트리 데이터를 생성한다.
	 * 
	 * @param para
	 * @return
	 * @throws Exception
	 */
	public Tree<TreeData> getTree(Map<String, Object> para) throws Exception {

		Object mngDiv = para.remove("mngDiv");

		List<Inlo> allList = new SelectInloListDfo().selectInlo(para); // 설치위치
		Tree<TreeData> tree = new MakeInloTreeDfo().makeTree(allList); // 설치위치 계층화
		List<Mo> moList = new GetMoListDfo().selectMoList(para); // 관리대상 조회
		new MakeMoTreeDfo().setMoTree(moList, tree); // 관리대상을 트리에 추가

		if (mngDiv != null) {
			List<MappMoPs> list = new MoPsIdSelectDfo().selectMappMoPs(mngDiv.toString()); // 관제점 조회
			new MoPsIdMakeTreeDfo().setMoPsIdTree(list, tree); // 관제점 트리에 추가
		}

		return tree;
	}

}
