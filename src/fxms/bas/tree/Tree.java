package fxms.bas.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tree<DATA> {

	public static void main(String[] args) {
		Tree<String> tree = new Tree<String>();
		tree.addChild(null, "100", "100");
		tree.addChild("100", "110", "110");
		tree.addChild("100", "120", "120");
		tree.addChild("120", "121", "121");
		tree.addChild(null, "200", "200");
		tree.addChild(null, "300", "300");
		tree.addChild("300", "310", "310");
		tree.addChild("300", "320", "320");
		tree.addChild("320", "321", "321");
		tree.addChild(null, "400", "400");

		int parent = 400;
		for (int i = parent + 1; i <= 410; i++) {
			tree.addChild(parent + "", i + "", i + "");
			parent = i;
		}

		tree.addChild("320", "322", "322");
		tree.addChild("320", "323", "323");
		tree.addChild("320", "324", "324");

//		System.out.println(tree.getTreeAll());
//		System.out.println(tree.getDeep());
//		System.out.println(tree.getDataList2Level(13));

//		System.out.println(tree.getXYPrint(tree.makeXyData(0, 0, 12, 10)));
	}

	private transient Map<String, TreeItem<DATA>> dataMap = new HashMap<String, TreeItem<DATA>>();
	private List<TreeItem<DATA>> trees = new ArrayList<TreeItem<DATA>>();
//	private transient List<TreeItem<DATA>> allList = new ArrayList<TreeItem<DATA>>();

	/**
	 * 
	 * @param parentId 부모 ID. 없으면 null
	 * @param childId  자신의 ID
	 * @param data     데이터
	 */
	public boolean addChild(String parentId, String childId, DATA data) {

		TreeItem<DATA> child = new TreeItem<DATA>(childId, data);

		// 이미 있는 경우
		if (dataMap.get(childId) != null) {
			return false;
		}

		TreeItem<DATA> parent = dataMap.get(parentId);

		if (parent != null) {
			parent.addChild(child);
		} else {
			this.trees.add(child);
		}

//		allList.add(child);
		dataMap.put(child.getTreeId(), child);
		return true;
	}

//	private void fillChildren(TreeItem<DATA> data, List<TreeItem<DATA>> list) {
//		if (data.getChildren() != null) {
//			for (TreeItem<DATA> o : data.getChildren()) {
//				list.add(o);
//				fillChildren(o, list);
//			}
//		}
//	}
//
//	public List<TreeItem<DATA>> getChildAll(TreeItem<DATA> data) {
//		List<TreeItem<DATA>> list = new ArrayList<TreeItem<DATA>>();
//		fillChildren(data, list);
//		return list;
//	}

//	public TreeItem<DATA> getData(String id) {
//		return dataMap.get(id);
//	}
//
//	public List<DATA> getDataList2Level(int level) {
//		List<DATA> retList = new ArrayList<DATA>();
//
//		for (TreeItem<DATA> o : allList) {
//			if (o.getLevel() == level) {
//				retList.add(o.getData());
//			}
//		}
//
//		return retList;
//	}
//
//	/**
//	 * 루트에서 가장 뒤에 있는 데이터까지의 깊이
//	 * 
//	 * @return
//	 */
//	public int getDeep() {
//
//		if (allList.size() == 0) {
//			return 0;
//		}
//
//		int max = 0;
//
//		for (TreeItem<DATA> o : allList) {
//			if (max < o.getLevel()) {
//				max = o.getLevel();
//			}
//		}
//
//		return max + 1;
//	}
//
//	public String getTreeAll() {
//		return getTreeString("", rootList);
//	}
//
//	public String getTreeString(String prefix, List<TreeItem<DATA>> dataList) {
//		if (dataList == null) {
//			return "";
//		}
//
//		StringBuffer sb = new StringBuffer();
//		for (TreeItem<DATA> o : dataList) {
//			sb.append(prefix).append(o);
//			if (o.getChildren() != null) {
//				sb.append(getTreeString("\t", o.getChildren()));
//			}
//			sb.append("\n");
//		}
//
//		return sb.toString();
//
//	}
//
//	private String getSpace(int count) {
//		StringBuffer sb = new StringBuffer();
//		for (int i = 0; i < count; i++) {
//			sb.append(" ");
//		}
//		return sb.toString();
//	}
//
//	public String getXYPrint(List<XYData<DATA>> list) {
//		Collections.sort(list, new Comparator<XYData<DATA>>() {
//
//			@Override
//			public int compare(XYData<DATA> o1, XYData<DATA> o2) {
//
//				if (o1.getY() == o2.getY()) {
//					return o1.getX() - o2.getX();
//				}
//				return o1.getY() - o2.getY();
//			}
//		});
//
//		StringBuffer sb = new StringBuffer();
//		XYData<DATA> dataPrev = null;
//		for (XYData<DATA> data : list) {
//
//			sb.append(getSpace(data.getX()));
//			sb.append(data);
//
//			sb.append("\n");
//
//			dataPrev = data;
//		}
//
//		return sb.toString();
//
//	}
//
//	public List<XYData<DATA>> makeXyData(int baseX, int baseY, int gapX, int gapY) {
//
//		List<XYData<DATA>> retList = new ArrayList<XYData<DATA>>();
//
//		int nextY = baseY;
//		for (TreeItem<DATA> o : rootList) {
//			nextY = makeXyData(baseX, nextY, gapX, gapY, o, retList);
//		}
//
//		return retList;
//	}
//
//	private int makeXyData(int baseX, int baseY, int gapX, int gapY, TreeItem<DATA> parent, List<XYData<DATA>> list) {
//
//		int nextY;
//
//		XYData<DATA> data = new XYData<DATA>(baseX + gapX, baseY + gapY, parent.getData());
//		list.add(data);
//
//		nextY = data.getY();
//		if (parent.getChildren() != null) {
//			int indexY = 0;
//			for (TreeItem<DATA> child : parent.getChildren()) {
//				nextY = makeXyData(baseX + gapX, baseY + (gapY * indexY), gapX, gapY, child, list);
//				indexY++;
//			}
//		}
//
//		return nextY;
//	}

	public void print() throws Exception {

		for (TreeItem<DATA> tree : this.trees) {
			print("", tree);
		}
	}

	private void print(String prev, TreeItem<DATA> tree) throws Exception {

		System.out.println(prev + tree.getData());

		if (tree.getChildren() == null)
			return;

		for (TreeItem<DATA> child : tree.getChildren()) {
			print(prev + "    ", child);
		}

	}
}
