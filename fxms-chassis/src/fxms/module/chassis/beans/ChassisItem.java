package fxms.module.chassis.beans;

/**
 * 
 * 실장도 구성 항목 정의
 * 
 * @author subkjh@naver.com(김종훈)
 *
 */
public class ChassisItem extends ChassisBase {

	private int x;
	private int y;
	private int rorate;
	private String name;
	private String tag;
	private String text;
	/** 이미지 그룹 */
	private String imgGroup;

	public ChassisItem() {

	}

	/**
	 * 
	 * @param name
	 *            실장도항목명칭
	 * @param x
	 *            X좌표
	 * @param y
	 *            Y좌표
	 * @param rotate
	 *            회전각도
	 * @param tag
	 *            태그
	 * @param width
	 *            넓이
	 * @param height
	 *            높이
	 * @param text
	 *            문자열
	 * @param imgGroup
	 *            이미지 그룹
	 * @throws Exception
	 */
	public ChassisItem(String name, String x, String y, String rotate, String tag, String width, String height,
			String text, String imgGroup) throws Exception {

		this.name = name;
		this.x = Integer.parseInt(x);
		this.y = Integer.parseInt(y);
		try {
			this.rorate = Integer.parseInt(rotate);
		} catch (Exception e) {
			this.rorate = 0;
		}
		this.tag = tag;

		if (width != null && height != null) {
			try {
				setWidth(Integer.parseInt(width));
				setHeight(Integer.parseInt(height));
			} catch (Exception e) {
				setWidth(0);
				setHeight(0);
			}
		}

		this.text = text;
		this.imgGroup = imgGroup;
	}

	public String getImgGroup() {
		return imgGroup;
	}

	public String getName() {
		return name;
	}

	public int getRorate() {
		return rorate;
	}

	public String getTag() {
		return tag;
	}

	public String getText() {
		return text;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean isText() {
		return text != null && text.trim().length() > 0;
	}

	public void setImgGroup(String imgGroup) {
		this.imgGroup = imgGroup;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRorate(int rorate) {
		this.rorate = rorate;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	@Override
	public String toString() {
		if (isText()) {
			return x + "|" + y + "|" + getText();

		} else {
			return name + "|" + x + "|" + y + "|" + getWidth() + "|" + getHeight();
		}
	}
}
