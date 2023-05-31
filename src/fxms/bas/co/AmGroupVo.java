package fxms.bas.co;

import java.util.ArrayList;
import java.util.List;

public class AmGroupVo {

	private List<AmUserVo> amList;

	private int amGrpNo;

	public AmGroupVo() {

	}

	public AmGroupVo(int amGrpNo) {
		this.amGrpNo = amGrpNo;
	}

	public int getAmGrpNo() {
		return amGrpNo;
	}

	public List<AmUserVo> getAmList() {

		if (amList == null) {
			amList = new ArrayList<AmUserVo>();
		}

		return amList;
	}

	public void setAmGrpNo(int amGrpNo) {
		this.amGrpNo = amGrpNo;
	}

}
