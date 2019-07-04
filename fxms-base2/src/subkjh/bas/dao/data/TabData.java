package subkjh.bas.dao.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TabData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8327550987180866711L;

	private String colArr[];

	private List<Object[]> dataList;

	private String name;

	public TabData() {
		dataList = new ArrayList<Object[]>();
	}

	public void addCol(String col) {
		if (colArr == null) {
			colArr = new String[] { col };
		}
		else {
			String tmp[] = new String[colArr.length + 1];
			System.arraycopy(colArr, 0, tmp, 0, colArr.length);
			tmp[tmp.length - 1] = col;
			colArr = tmp;
		}

	}

	public void addDataArr(Object[] data) {
		dataList.add(data);
	}

	public boolean containsCol(String col) {
		if (colArr == null) return false;

		for (String s : colArr) {
			if (s.equals(col)) return true;
		}

		return false;
	}

	public List<Map<String, Object>> convertMap() {

		List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
		Map<String, Object> e;
		for (Object o[] : dataList) {
			e = new HashMap<String, Object>();
			for (int i = 0; i < colArr.length; i++) {
				e.put(colArr[i], o[i]);
			}
			ret.add(e);
		}

		return ret;
	}

	public List<Map<String, Object>> convertMapJf() {
		String jfCol[] = new String[colArr.length];
		for (int i = 0; i < jfCol.length; i++) {
			jfCol[i] = getFieldName(colArr[i]);
		}

		List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
		Map<String, Object> e;
		for (Object o[] : dataList) {
			e = new HashMap<String, Object>();
			for (int i = 0; i < jfCol.length; i++) {
				e.put(jfCol[i], o[i]);
			}
			ret.add(e);
		}

		return ret;
	}

	/**
	 * 컬림의 값이 비교값과 같은 첫번째 열을 제공합니다.
	 * 
	 * @param colIndex
	 *            컬럼
	 * @param value
	 *            비교값
	 * @return
	 */
	public Object[] findData(int colIndex, Object value) {
		for (Object o[] : dataList) {
			if (o[colIndex].equals(value)) return o;
		}

		return null;
	}

	public String[] getColArr() {
		return colArr;
	}

	public List<Object[]> getDataList() {
		return dataList;
	}

	public int getIndexCol(String col) {
		if (colArr == null) return -1;

		for (int i = 0; i < colArr.length; i++) {
			if (colArr[i].equals(col)) return i;
		}

		return -1;
	}

	public String getName() {
		return name;
	}

	public void print() {
		print(0);
	}

	public void print(int pageSize) {

		if (colArr == null) return;

		int max[] = new int[colArr.length];

		for (int i = 0; i < max.length; i++) {
			max[i] = colArr[i].length();
		}

		for (Object row[] : dataList) {
			for (int i = 0; i < colArr.length; i++) {
				max[i] = Math.max(max[i], (row[i] + "").length());
			}
		}

		if (name != null) System.out.println(name);

		StringBuffer line = new StringBuffer();
		line.append("+-");
		for (int i = 0; i < max.length; i++) {
			for (int n = 0; n < max[i]; n++)
				line.append("-");
			line.append("-+");
			if (i < max.length - 1) line.append("-");
		}

		System.out.println(line);
		System.out.print("| ");
		for (int i = 0; i < colArr.length; i++) {
			System.out.format("%-" + max[i] + "s", colArr[i]);
			if (i < colArr.length) System.out.print(" | ");
		}
		System.out.println("\n" + line);

		int n = 0;
		int index = 0;

		for (Object row[] : dataList) {
			System.out.print("| ");
			for (int i = 0; i < colArr.length; i++) {
				System.out.format("%-" + max[i] + "s", (row[i] == null ? "" : row[i].toString()));
				if (i < colArr.length) System.out.print(" | ");
			}
			System.out.println();

			index++;
			n++;

			if (pageSize >= 0 && n == pageSize) {
				System.out.println("--- more ( " + index + " of " + dataList.size() + " ) ---");
				try {
					int ret = System.in.read();
					if (ret == 'q') break;
				}
				catch (Exception e2) {
				}
				n = 0;
			}
		}

		System.out.println(line);
	}

	public String getPrintString() {

		if (colArr == null) return "";

		StringBuffer sb = new StringBuffer();

		int max[] = new int[colArr.length];

		for (int i = 0; i < max.length; i++) {
			max[i] = colArr[i].length();
		}

		for (Object row[] : dataList) {
			for (int i = 0; i < colArr.length; i++) {
				max[i] = Math.max(max[i], (row[i] + "").length());
			}
		}

		if (name != null) {
			sb.append(name);
			sb.append("\n");
		}

		StringBuffer line = new StringBuffer();
		line.append("+-");
		for (int i = 0; i < max.length; i++) {
			for (int n = 0; n < max[i]; n++)
				line.append("-");
			line.append("-+");
			if (i < max.length - 1) line.append("-");
		}

		sb.append(line);
		sb.append("\n");
		sb.append("| ");

		for (int i = 0; i < colArr.length; i++) {
			sb.append(String.format("%-" + max[i] + "s", colArr[i]));
			if (i < colArr.length) sb.append(" | ");
		}

		sb.append("\n" + line);
		sb.append("\n");

		for (Object row[] : dataList) {
			sb.append("| ");
			for (int i = 0; i < colArr.length; i++) {
				sb.append(String.format("%-" + max[i] + "s", (row[i] == null ? "" : row[i].toString())));
				if (i < colArr.length) sb.append(" | ");
			}
			sb.append("\n");
		}

		sb.append(line);
		sb.append("\n");
		
		return sb.toString();
	}

	public void setColArr(String[] colArr) {
		this.colArr = colArr;
	}

	public void setData(int colIndexComp, Object dataComp, int colIndex, Object data) {
		for (Object[] e : dataList) {
			if (e[colIndexComp].equals(dataComp)) {
				e[colIndex] = data;
				return;
			}
		}
	}

	public void setDataList(List<Object[]> dataList) {
		this.dataList = dataList;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int size() {
		return dataList == null ? 0 : dataList.size();
	}

	private String getFieldName(String s) {
		String field = (s.charAt(0) + "").toLowerCase();
		int index = 1;

		for (int size = s.length(); index < size; index++) {
			if (s.charAt(index) == '_') {
				index++;
				field += (s.charAt(index) + "").toUpperCase();
			}
			else {
				field += (s.charAt(index) + "").toLowerCase();

			}
		}

		return field;
	}
}
