package fxms.bas.tree;

import java.util.ArrayList;
import java.util.List;

public class TreeItem<DATA> {

	private String treeId;
	private DATA data;
	private List<TreeItem<DATA>> children;
	private int level = 0;

	public TreeItem(String treeId, DATA data) {
		this.treeId = treeId;
		this.data = data;
	}

	/**
	 * 
	 * @return 트리 고유 ID
	 */
	public String getTreeId() {
		return treeId;
	}

	/**
	 * 트리 객체
	 * 
	 * @return
	 */
	public DATA getData() {
		return data;
	}

	public void addChild(int index, TreeItem<DATA> child) {

		if (child != null) {

			if (children == null) {
				children = new ArrayList<TreeItem<DATA>>();
			}

			child.level = this.level + 1;
			children.add(index, child);
		}
	}

	public int getLevel() {
		return level;
	}

	public void addChild(TreeItem<DATA> child) {
		addChild(getChildCount(), child);
	}

	public int getChildCount() {
		return children == null ? 0 : children.size();
	}

	public List<TreeItem<DATA>> getChildren() {
		return children;
	}

	@Override
	public String toString() {
		return level + ")" + treeId;
	}

}
