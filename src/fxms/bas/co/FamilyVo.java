package fxms.bas.co;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author subkjh(김종훈)
 *
 * @param <PARENT>
 * @param <CHILD>
 */
public abstract class FamilyVo<PARENT, CHILD> {

	public enum Status {
		nothing, added, changed, deleted;
	}

	class Data {
		Status status;
		CHILD child;

		public Data(CHILD child, Status status) {
			this.child = child;
			this.status = status;
		}
	}

	private PARENT parent;

	private Map<String, List<Data>> children;

	public FamilyVo(PARENT parent) {
		this.parent = parent;
		children = new HashMap<String, List<Data>>();
	}

	public void addOrgChild(String tag, CHILD c) {
		addChild(tag, new Data(c, Status.nothing));
	}

	public int addOrgChildList(String tag, List<CHILD> list) {
		if (list != null) {
			for (CHILD c : list) {
				addChild(tag, new Data(c, Status.nothing));
			}
			return list.size();
		} else {
			return 0;
		}
	}

	public void addDetectedChild(String tag, CHILD c) {

		CHILD old = get(tag, c);

		if (old != null) {
			removeChild(tag, old);
			addChild(tag, new Data(c, Status.changed));
		} else {
			addChild(tag, new Data(c, Status.added));
		}

	}

	public int addDetectedChildList(String tag, List<CHILD> list) {
		if (list != null) {
			for (CHILD c : list) {
				addDetectedChild(tag, c);
			}
			return list.size();
		} else {
			return 0;
		}
	}

	public CHILD get(String tag, CHILD c) {

		List<Data> list = (List<Data>) children.get(tag);

		if (list != null && list.size() != 0) {
			for (Data e : list) {
				if (equipChild(tag, c, e.child)) {
					return e.child;
				}
			}
			return null;
		} else {
			return null;
		}

	}

	/**
	 *
	 * @param tag
	 * @param condition
	 * @return
	 */
	public CHILD getChild(String tag, Object condition) {
		List<Data> list = (List<Data>) children.get(tag);

		if (list != null && list.size() != 0) {
			for (Data e : list) {
				if (isMatch(tag, e.child, condition)) {
					return e.child;
				}
			}
			return null;
		} else {
			return null;
		}
	}

	public List<CHILD> getChildren(String tag, Status status) {

		List<CHILD> ret = new ArrayList<CHILD>();

		List<Data> list = children.get(tag);
		if (list != null) {
			for (Data data : list) {
				if (status == null || status == data.status) {
					ret.add(data.child);
				}
			}
		}

		return ret;
	}

	public PARENT getParent() {
		return parent;
	}

	public List<String> getTagList() {
		List<String> tagList = new ArrayList<String>();

		for (String tag : children.keySet()) {
			tagList.add(tag);
		}

		return tagList;
	}

	public CHILD removeChild(String tag, CHILD child) {

		List<Data> list = (List<Data>) this.children.get(tag);

		for (int index = list.size() - 1; index >= 0; --index) {
			if (equipChild(tag, child, list.get(index).child)) {
				return (CHILD) list.remove(index).child;
			}
		}

		return null;
	}

	public void setStatus(String tag, Status _beanStatusOld, Status _beanStatusNew) {

		List<String> tagList = new ArrayList<String>();

		if (tag == null) {
			for (String e : getTagList()) {
				tagList.add(e);
			}
		} else {
			tagList.add(tag);
		}

		for (String e : tagList) {
			List<Data> list = children.get(e);
			if (list != null) {
				for (Data data : list) {
					if (_beanStatusOld == null || data.status == _beanStatusOld) {
						data.status = _beanStatusNew;
					}
				}
			}
		}
	}

	public int size(String tag) {
		List<Data> list = children.get(tag);
		return list == null ? 0 : list.size();
	}

	public int sizeAll() {
		int count = 0;

		for (List<Data> list : children.values()) {
			count += list.size();
		}

		return count;
	}

	public Map<String, Object> toMap() {

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("parent", parent);
		map.put("tag-class-list", getTagList());

		for (String tag : getTagList()) {
			map.put(tag + "-list", getChildren(tag, null));
		}

		return map;
	}

	public String getSizeString(String tag) {
		StringBuffer sb = new StringBuffer();

		setCountMsg(sb, "nothing", getChildren(tag, Status.nothing).size());
		setCountMsg(sb, "added", getChildren(tag, Status.added).size());
		setCountMsg(sb, "changed", getChildren(tag, Status.changed).size());
		setCountMsg(sb, "deleted", getChildren(tag, Status.deleted).size());

		return sb.toString();
	}

	private void setCountMsg(StringBuffer sb, String tag, int size) {
		if (size > 0) {
			if (sb.length() > 0) {
				sb.append(", ");
			}
			sb.append(tag).append("=").append(size);
		}
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (String tag : getTagList()) {
			sb.append(tag);
			sb.append(" : ");
			sb.append("nothing=").append(getChildren(tag, Status.nothing).size());
			sb.append(", added=").append(getChildren(tag, Status.added).size());
			sb.append(", changed=").append(getChildren(tag, Status.changed).size());
			sb.append(", deleted=").append(getChildren(tag, Status.deleted).size());
			sb.append("\n");
		}
		return sb.toString();
	}

	protected abstract boolean equipChild(String tag, CHILD c1, CHILD c2);

	protected abstract boolean isMatch(String tag, CHILD c1, Object condition);

	private void addChild(String tag, Data data) {

		List<Data> list = (List<Data>) this.children.get(tag);
		if (list == null) {
			list = new ArrayList<Data>();
			this.children.put(tag, list);
		}

		list.add(data);
	}
}
