package fxms.module.chassis.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fxms.bas.mo.Mo;

/**
 * 명칭 : 실장도<br>
 * 
 * 화면에서 그릴 내용입니다.
 * 
 * @author subkjh
 * 
 */
public class Chassis implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4901411649717665443L;

	private String img;
	private int x;
	private int y;
	private int width;
	private int height;
	private Mo mo;
	/** 회전율 */
	private int rotate;
	private String name;
	private String tag;
	private List<Chassis> children;
	private String text;
	private int fontSize;
	private String fontName;

	public Chassis() {
		fontSize = 12;
		fontName = "System";
	}

	public List<Chassis> getChildren() {
		if (children == null) {
			children = new ArrayList<Chassis>();
		}
		return children;
	}

	public int getHeight() {
		return height;
	}

	/**
	 * HTML로 구성된 실장도를 제공합니다.
	 * 
	 * @return HTML 실장도
	 */
	public String getHtml() {
		StringBuffer sb = new StringBuffer();
		add(sb, "<!DOCTYPE html>");
		add(sb, "<html>");
		add(sb, "<body>");

		List<String> imgList = new ArrayList<String>();

		addImage(sb, this, imgList);

		if (getWidth() > 0 && getHeight() > 0) {
			add(sb, "<canvas id=\"myCanvas\" width=\"" + getWidth() + "\" height=\"" + getHeight()
					+ "\" style=\"border: 1px solid #d3d3d3;\" />");
		} else {
			add(sb, "<canvas id=\"myCanvas\" width=\"800\" height=\"100\" style=\"border: 1px solid #d3d3d3;\" />");
		}

		add(sb, "<script>");
		add(sb, "var c = document.getElementById(\"myCanvas\");");
		add(sb, "var ctx = c.getContext(\"2d\");");
		if (getHeight() > 1000) {
			add(sb, "ctx.scale(.5, .5);");
		}

		addDrawImage(sb, 0, 0, this);

		addText(sb, 0, 0, this);

		add(sb, "</script>");
		add(sb, "</body>");
		add(sb, "</html>");

		return sb.toString();
	}

	public String getImg() {
		if (tag == null)
			return img;
		int pos = img.lastIndexOf('.');
		if (pos < 0)
			return img;

		return img.substring(0, pos) + "_" + tag + img.substring(pos);
	}

	public Mo getMo() {
		return mo;
	}

	public String getName() {
		if (tag == null)
			return name;
		return name + "_" + tag;
	}

	public int getRotate() {
		return rotate;
	}

	public String getTag() {
		return tag;
	}

	public String getText() {
		return text;
	}

	public int getWidth() {
		return width;
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

	/**
	 * 실장도 구성 내역을 텍스트로 출력합니다.
	 * 
	 * @param prefix
	 *            프리픽스
	 */
	public void print(String prefix) {
		if (isText()) {
			System.out.println(prefix + x + "," + y + " " + text);
		} else {
			System.out.println(prefix + getName() + "(" + x + "," + y + "," + width + "," + height + ")" + mo + "|"
					+ rotate + "|" + getImg());
		}
		if (children != null) {
			for (Chassis chassis : children) {
				chassis.print("  " + prefix);
			}
		}
	}

	public void setChildren(List<Chassis> children) {
		this.children = children;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public void setMo(Mo mo) {
		this.mo = mo;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRotate(int rotate) {
		this.rotate = rotate;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public void setText(String text) {
		this.text = text;
		if (text == null)
			return;

		String ss[] = text.split(",");

		if (ss.length == 3) {
			try {
				fontSize = Integer.parseInt(ss[0]);
			} catch (Exception e) {
				fontSize = 12;
			}
			fontName = ss[1];
			this.text = ss[2];
		}

	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return "Chassis(" + getName() + "(" + x + "," + y + "," + width + "," + height + ")"
				+ (mo == null ? 0 : mo.getMoNo()) + "|" + rotate + "|" + getImg()
				+ (children == null ? "" : "|" + children.size() + ":" + children) + ")";

	}

	private void add(StringBuffer sb, String s) {
		sb.append(s + "\n");
	}

	private void addDrawImage(StringBuffer sb, int x, int y, Chassis chassis) {

		if (chassis.isText())
			return;

		add(sb, "var img = document.getElementById(\"" + chassis.getName() + "\");");
		if (chassis.getHeight() > 0 && chassis.getWidth() > 0) {
			add(sb, "ctx.drawImage(img," + (chassis.x + x) + ", " + (chassis.y + y) + ", " + chassis.getWidth() + ", "
					+ chassis.getHeight() + ");");
		} else {
			add(sb, "ctx.drawImage(img," + (chassis.x + x) + ", " + (chassis.y + y) + ");");
		}

		if (chassis.getChildren() != null) {
			for (Chassis c : chassis.getChildren()) {
				addDrawImage(sb, chassis.getX(), chassis.getY(), c);
			}
		}
	}

	private void addImage(StringBuffer sb, Chassis chassis, List<String> imgList) {

		if (chassis.isText())
			return;

		add(sb, "<img id=\"" + chassis.getName() + "\" src=\"" + chassis.getImg() + "\" style=\"display:none;\"  />");
		imgList.add(chassis.getName());

		if (chassis.getChildren() != null) {
			for (Chassis c : chassis.getChildren()) {
				if (imgList.contains(c.getName()) == false) {
					addImage(sb, c, imgList);
				}
			}
		}

	}

	private void addText(StringBuffer sb, int x, int y, Chassis chassis) {

		if (chassis.isText()) {
			add(sb, "ctx.strokeStyle=\"#FFFFFF\";");
			add(sb, "ctx.fillStyle=\"#FFFFFF\";");
			add(sb, "ctx.font=\"" + chassis.fontSize + "px " + chassis.fontName + "\";");
			// add(sb, "ctx.strokeText(\"" + chassis.text + "\"," + (chassis.x +
			// x) + "," + (chassis.y + y) + ");");

			add(sb, "ctx.fillText(\"" + chassis.text + "\"," + (chassis.x + x) + "," + (chassis.y + y) + ");");
		}

		if (chassis.getChildren() != null) {
			for (Chassis c : chassis.getChildren()) {
				addText(sb, chassis.getX(), chassis.getY(), c);
			}
		}
	}

}
