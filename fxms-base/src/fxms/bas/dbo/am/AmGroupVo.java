package fxms.bas.dbo.am;

import java.util.ArrayList;
import java.util.List;

public class AmGroupVo extends FX_AM_GROUP {

	private List<AmUserVo> amList;

	public List<AmUserVo> getAmList() {

		if (amList == null) {
			amList = new ArrayList<AmUserVo>();
		}

		return amList;
	}

}
