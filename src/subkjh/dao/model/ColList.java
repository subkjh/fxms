package subkjh.dao.model;

import java.util.ArrayList;
import java.util.List;

public class ColList {

	public class Mapp {
		public final String column;

		public final String field;

		public Mapp(String column, String field) {
			this.column = column;
			this.field = field;
		}
	}

	private final List<Mapp> list;

	public ColList() {
		this.list = new ArrayList<>();
	}

	public void add(String column, String field) {
		list.add(new Mapp(column, field));
	}

	public String[] toColumnArray() {
		String ret[] = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			ret[i] = list.get(i).column;
		}
		return ret;
	}

	public List<Mapp> getList() {
		return this.list;
	}

	public boolean isEmpty() {
		return list.size() == 0;
	}

	public int size() {
		return list.size();
	}
}
