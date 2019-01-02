package fxms.module.usertree.vo;

import java.util.ArrayList;
import java.util.List;

import fxms.bas.mo.Mo;

/**
 * 트리용 MO VO
 * 
 * @author SUBKJH-DEV
 *
 */
public class UserTreeMoVo {

	private List<Object> parentList;

	private Mo mo;

	public UserTreeMoVo(Mo mo) {
		this.mo = mo;
	}

	public Mo getMo() {
		return mo;
	}

	/**
	 * 이 MO를 포함하고 있는 부모 목록을 조회한다.
	 * 
	 * @return 부모 목록
	 */
	public List<Object> getParentList() {

		if (parentList == null) {
			parentList = new ArrayList<Object>();
		}

		return parentList;
	}

}
