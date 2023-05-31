package fxms.bas.impl.dpo.mo;

import java.util.List;

import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.tree.Tree;
import fxms.bas.tree.TreeData;
import fxms.bas.vo.mapp.MappMoPs;

/**
 * 
 * @author subkjh
 *
 */
public class MoPsIdMakeTreeDfo implements FxDfo<Void, Tree<TreeData>> {

	@Override
	public Tree<TreeData> call(FxFact fact, Void data) throws Exception {

		Tree<TreeData> tree = fact.getObject(Tree.class, "tree");
		List<MappMoPs> list = fact.getObject(List.class, "moList");

		setMoPsIdTree(list, tree);

		return tree;
	}

	public void setMoPsIdTree(List<MappMoPs> list, Tree<TreeData> tree) throws Exception {

		// 관제점 관리대상 하위로 넣기
		for (MappMoPs mo : list) {

			tree.addChild("MO" + mo.getMoNo(), mo.getMappId(),
					new TreeData(mo.getPsId(), mo.getMappId() + " / " + mo.getPsName(), "PSID", "PSID"));
		}
	}

}
