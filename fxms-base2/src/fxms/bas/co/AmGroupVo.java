package fxms.bas.co;

import java.util.ArrayList;
import java.util.List;

public class AmGroupVo {

	private List<AmUserVo> amList;

	private int amGroupNo;

	public int getAmGroupNo() {
		return amGroupNo;
	}

	public void setAmGroupNo(int amGroupNo) {
		this.amGroupNo = amGroupNo;
	}

	public AmGroupVo(int amGroupNo) {
		this.amGroupNo = amGroupNo;
	}

	public AmGroupVo() {

	}

	public List<AmUserVo> getAmList() {

		if (amList == null) {
			amList = new ArrayList<AmUserVo>();
		}

		return amList;
	}

}
