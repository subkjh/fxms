package fxms.module.chassis.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * 실장도 정의
 * 
 * @author subkjh
 * 
 */
public class ChassisDef extends _ChassisDef {

	private String name;
	private String img;

	private List<ChassisItem> itemList;

	/**
	 * 
	 * @param name
	 *            명칭
	 * @param img
	 *            이미지
	 * @param width
	 *            넓이
	 * @param height
	 *            높이
	 */
	public ChassisDef(String name, String img, String width, String height) {
		this.name = name;
		this.img = img;

		if (width != null && height != null) {
			try {
				setWidth(Integer.parseInt(width));
				setHeight(Integer.parseInt(height));
			} catch (Exception e) {
				setWidth(0);
				setHeight(0);
			}
		}
	}

	public void add(ChassisItem chassisItem) {
		if (itemList == null) {
			itemList = new ArrayList<ChassisItem>();
		}
		itemList.add(chassisItem);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ChassisDef) {
			return name.equals(((ChassisDef) obj).name);
		}
		return super.equals(obj);
	}

	public String getImg() {
		return img;
	}

	public List<ChassisItem> getItemList() {
		return itemList;
	}

	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		return 1;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public void setItemList(List<ChassisItem> itemList) {
		this.itemList = itemList;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return name + "|" + img + "|" + getWidth() + "|" + getHeight() + "|" + moAttrList;
	}
}
