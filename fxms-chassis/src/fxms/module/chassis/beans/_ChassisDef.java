package fxms.module.chassis.beans;

import java.util.ArrayList;
import java.util.List;

import fxms.bas.mo.Mo;


/**
 * 명칭 : 실장도 정의<br>
 * 
 * @author subkjh
 * 
 */
public class _ChassisDef {

	/** 어떤 경우에 이 실장도를 사용할지 확인용 조건 */
	protected List<MoAttr> moAttrList;
	private int width;
	private int height;

	/**
	 * 적용될 MO 속성을 추가합니다.
	 * 
	 * @param moAttr
	 *            속성
	 */
	public void addMoAttr(MoAttr moAttr) {
		if (moAttrList == null) {
			moAttrList = new ArrayList<MoAttr>();
		}

		moAttrList.add(moAttr);
	}

	/**
	 * 
	 * @return 높이
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * 
	 * @return 넓이
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * 
	 * @return 속성 존재 여부
	 */
	public boolean hasAttr() {
		return moAttrList != null && moAttrList.size() > 0;
	}

	/**
	 * 실장도 정의가 MO에 해당되는지 여부
	 * 
	 * @param parent
	 *            부모MO
	 * @param mo
	 *            MO
	 * @return 해당 여부
	 */
	public boolean match(Mo parent, Mo mo) {
		if (moAttrList == null) return false;

		for (MoAttr attr : moAttrList) {
			if (attr.match(parent, mo) == false) return false;
		}

		return true;
	}

	/**
	 * 
	 * @param height
	 *            높이
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * 
	 * @param width
	 *            넓이
	 */
	public void setWidth(int width) {
		this.width = width;
	}
}
