package fxms.bas.impl.dpo.inlo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.fxo.FxmsUtil;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.tree.Tree;
import fxms.bas.tree.TreeData;
import fxms.bas.vo.Inlo;
import fxms.bas.vo.InloExt;

/**
 * 
 * @author subkjh
 *
 */
public class MakeInloTreeDfo implements FxDfo<List<Inlo>, Tree<TreeData>> {

	public static void main(String[] args) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("moClass", "NODE");
		MakeInloTreeDfo dpo = new MakeInloTreeDfo();
		try {
			Tree<TreeData> tree = dpo.makeTree(new SelectInloListDfo().selectInlo(para));
			tree.print();
			System.out.println(FxmsUtil.toJson(tree));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setTree(Tree<TreeData> tree, String parentId, InloExt inlo) throws Exception {

		tree.addChild(parentId, inlo.getInloNoStr(),
				new TreeData(inlo.getInloNoStr(), inlo.getInloName(), inlo.getInloClCd(), "INLOPOOL"));

		for (InloExt child : inlo.getChildren()) {
			setTree(tree, inlo.getInloNoStr(), child);
		}

	}

	@Override
	public Tree<TreeData> call(FxFact fact, List<Inlo> data) throws Exception {
		return makeTree(data);
	}

	/**
	 * 상하 관계를 갖는 위치를 가져온다.
	 * 
	 * @param locList
	 * @return
	 * @throws Exception
	 */
	public List<InloExt> getInloTree(List<Inlo> locList) throws Exception {

		Map<Integer, InloExt> tmp = new HashMap<>();
		for (Inlo inlo : locList) {
			tmp.put(inlo.getInloNo(), new InloExt(inlo));
		}

		InloExt parent;

		for (InloExt inlo : tmp.values()) {
			// 자신과 UPPER이 같으면 무시
			if (inlo.getInloNo() != inlo.getUpperInloNo() && inlo.getUpperInloNo() > 0) {
				parent = tmp.get(inlo.getUpperInloNo());
				if (parent != null) {
					parent.addChild(inlo);
				}
			}
		}

		List<InloExt> ret = new ArrayList<>();
		for (InloExt inlo : tmp.values()) {
			if (inlo.getParent() == null) {
				ret.add(inlo);
			}
		}

		return ret;

	}

	/**
	 * 설치위치 정보를 트리 구조 데이터를 만든다.
	 * 
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public Tree<TreeData> makeTree(List<Inlo> list) throws Exception {

		List<InloExt> inlos = getInloTree(list);

		Tree<TreeData> tree = new Tree<TreeData>();

		for (InloExt inlo : inlos) {
			setTree(tree, null, inlo);
		}

		return tree;
	}
}
