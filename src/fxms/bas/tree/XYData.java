package fxms.bas.tree;

public class XYData<DATA> {

	private DATA data;

	private int x;

	private int y;

	public XYData(int x, int y, DATA data) {
		this.x = x;
		this.y = y;
		this.data = data;
	}

	public DATA getData() {
		return data;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setData(DATA data) {
		this.data = data;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public String toString() {
		return "(" + x + "," + y + ")" + data;
	}
}
