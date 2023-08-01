package fxms.bas.impl.dpo.mo;

import java.util.List;
import java.util.Map;

import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.impl.dpo.co.CdCodeSelectDfo;
import fxms.bas.mo.Mo;
import fxms.bas.tree.Tree;
import fxms.bas.tree.TreeData;
import fxms.bas.vo.Code;
import subkjh.bas.co.lang.Lang;

/**
 * 
 * @author subkjh
 *
 */
public class MakeMoTreeDfo implements FxDfo<Void, Tree<TreeData>> {

	@SuppressWarnings("unchecked")
	@Override
	public Tree<TreeData> call(FxFact fact, Void data) throws Exception {

		Tree<TreeData> tree = fact.getObject(Tree.class, "tree");
		List<Mo> moList = fact.getObject(List.class, "moList");

		setMoTree(moList, tree);

		return tree;
	}

	public void setMoTree(List<Mo> moList, Tree<TreeData> tree) throws Exception {

		Map<String, Code> map = new CdCodeSelectDfo().selectCodeMap("MO_CLASS");

		String moParentId;
		Code code;

		// 관리대상 설치위치에 넣기
		for (Mo mo : moList) {

			code = map.get(mo.getMoClass());
			if (code == null) {
				moParentId = mo.getInloNo() + "MO";
				tree.addChild(mo.getInloNo() + "", moParentId,
						new TreeData("MO", Lang.get("Managed Object"), "LABEL", "MOPOOL"));
			} else {
				moParentId = mo.getInloNo() + mo.getMoClass();
				tree.addChild(mo.getInloNo() + "", moParentId,
						new TreeData(mo.getMoClass(), code.getCdName(), "LABEL", "MOPOOL"));
			}

			tree.addChild(moParentId, "MO" + mo.getMoNo(),
					new TreeData(mo.getMoNo(), mo.getMoName(), mo.getMoClass(), "MO"));
		}
	}

}
