package fxms.bas.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InloExt extends Inlo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2767415853952046208L;

	private InloExt parent;

	private final List<InloExt> children = new ArrayList<InloExt>();

	public InloExt(int inloNo, String inloName, String inloClCd, String inloTypeCd, String inloLevelCd, int upperInloNo,
			String inloAllName, String inloTid) {
		super(inloNo, inloName, inloClCd, inloTypeCd, inloLevelCd, upperInloNo, inloAllName, inloTid);
	}

	public InloExt(Inlo inlo) {
		super(inlo.getInloNo(), inlo.getInloName(), inlo.getInloClCd(), inlo.getInloTypeCd(), inlo.getInloLevelCd(),
				inlo.getUpperInloNo(), inlo.getInloAllName(), inlo.getInloTid());
	}

	private void makeMemList(int inloNo, List<InloExt> children, Map<String, InloMem> map, int depth, String tag) {

		if (children != null) {

			InloMem mem;

			for (InloExt child : children) {

				// child 자신
				mem = new InloMem(child.getInloNo(), child.getInloNo(), 0);
				map.put(mem.toString(), mem);

				// 자식
				mem = new InloMem(inloNo, child.getInloNo(), depth);
				map.put(mem.toString(), mem);

				// 손자
				makeMemList(inloNo, child.getChildren(), map, depth + 1, "parent");

				// 자식의 자식
				makeMemList(child.getInloNo(), child.getChildren(), map, 1, "my");

			}

		}

	}

	public void addChild(InloExt child) {
		child.parent = this;
		children.add(child);
	}

	/**
	 * 
	 * @return
	 */
	public Collection<InloMem> makeMemList() {

		Map<String, InloMem> map = new HashMap<String, InloMem>();

		// 자신
		InloMem mem = new InloMem(getInloNo(), getInloNo(), 0);

		map.put(mem.toString(), mem);

		makeMemList(getInloNo(), this.getChildren(), map, 1, "top");

		return map.values();
	}

	public List<InloExt> getChildren() {
		return children;
	}

	public int size() {
		return size(0, this);
	}

	private int size(int nowSize, InloExt inlo) {

		int size = nowSize;

		for (InloExt child : inlo.getChildren()) {
			size = size(size, child);
		}

		return size + inlo.getChildren().size();
	}

	public InloExt getParent() {
		return parent;
	}

}
