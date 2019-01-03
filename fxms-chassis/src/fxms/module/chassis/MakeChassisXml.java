package fxms.module.chassis;

import java.io.File;

import subkjh.bas.utils.FileUtil;

/**
 * 실장도 XML 작성에 도움이 되는 클래스
 * 
 * @author subkjh
 * 
 */
public class MakeChassisXml {

	public static void main(String[] args) {

		MakeChassisXml maker = new MakeChassisXml();
		maker.xmlMake6_4_6x2("datas/chassis/PORT2.txt");
		// maker.printSfpPort(30, 15, 30, 0, 12);
		// maker.printSfpPort(30, 45, 30, 0, 12);

	}

	private final String PORT = "<item x=\"$x\" y=\"$y\" moClass=\"INTERFACE\" field=\"ifIndex\" value=\"$ifIndex\" $tag />";
//	private final String PORT2 = "<item x=\"$x\" y=\"$y\" moClass=\"INTERFACE\" field=\"ifIndex\" value=\"$ifIndex\" $tag />";
	private final String SFP_PORT = "<item x=\"$x\" y=\"$y\" name=\"sfp_unlink\" moClass=\"INTERFACE\" field=\"ifIndex\" value=\"$ifIndex\" $tag />";

	private final String POWER = "<item x=\"$x\" y=\"$y\" name=\"power_empty\"  $tag >\n" //
			+ "<attr moClass=\"CARD\" field=\"cardType\" value=\"powerSupply\" />\n" //
			+ "<attr moClass=\"CARD\" field=\"slotNo\" value=\"$slotNo\" />\n" //
			+ "</item>";

	private final int XGAP = 28 + 1;
	private final int yGap = 24 + 3;
	private final int xTop = 30;
	private final int yTop = 18;

	void printPower(int x, int y, int xGap, int yGap, int count) {
		for (int i = 0; i < count; i++) {
			printPower(x, y, "", 0);
			x += xGap;
			y += yGap;
		}
	}

	void printSfpPort(int x, int y, int xGap, int yGap, int count) {
		for (int i = 0; i < count; i++) {
			printSfpPort(x, y, "", 0);
			x += xGap;
			y += yGap;
		}
	}

	void xmlMakeHP5500() {

		int x = xTop;
		int y;

		int ifIndex = 1;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 8; j++) {

				y = yTop;
				print(x, y, "tag=\"180\"", ifIndex);

				y = yTop + yGap;
				ifIndex++;

				print(x, y, "", ifIndex);

				x += XGAP;
				ifIndex++;
			}
			x += 7;
		}
	}

	void xmlMake8x3(String filename) {

		String LINE = FileUtil.getString(new File(filename));

		int x = xTop;
		int y;

		int ifIndex = 1;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 8; j++) {

				y = yTop;
				print(LINE, x, y, "tag=\"180\"", ifIndex);

				y = yTop + yGap;
				ifIndex++;

				print(LINE, x, y, "", ifIndex);

				x += XGAP;
				ifIndex++;
			}
			x += 7;
		}
	}

	void xmlMakeRj8x3(String filename) {

		String LINE = FileUtil.getString(new File(filename));

		int x = 36;
		int y;

		int ifIndex = 1;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 8; j++) {

				y = yTop;
				print(LINE, x, y, "tag=\"180\"", ifIndex);

				y = yTop + yGap;
				ifIndex++;

				print(LINE, x, y, "", ifIndex);

				x += 32;
				ifIndex++;
			}
			x += 20;
		}
	}

	void xmlMakeRj8x3HP10508(String filename) {

		String LINE = FileUtil.getString(new File(filename));

		int x = 40;
		int y;

		int ifIndex = 1;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 8; j++) {

				y = yTop;
				print(LINE, x, y, "tag=\"180\"", ifIndex);

				y = yTop + yGap;
				ifIndex++;

				print(LINE, x, y, "", ifIndex);

				x += 32;
				ifIndex++;
			}
			x += 26;
		}
	}

	void xmlMake8x1(String filename) {

		String LINE = FileUtil.getString(new File(filename));

		int x = xTop + 36;
		int y;

		int ifIndex = 1;

		for (int j = 0; j < 8; j++) {

			y = yTop + 14;
			print(LINE, x, y, "", ifIndex);

			x += XGAP + 37 + 34;
			ifIndex++;
		}

	}

	void xmlMake6x2x2(String filename) {

		String LINE = FileUtil.getString(new File(filename));

		int x = 224;
		int y;

		int ifIndex = 1;

		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 6; j++) {

				y = yTop;
				print(LINE, x, y, "tag=\"180\"", ifIndex);

				y = yTop + yGap;
				ifIndex++;

				print(LINE, x, y, "", ifIndex);

				x += 34;
				ifIndex++;
			}
			x += 20;
		}

	}

	void xmlMake6x2x4(String filename) {

		String LINE = FileUtil.getString(new File(filename));

		int x = 30;
		int y;

		int ifIndex = 1;

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 6; j++) {

				y = yTop;
				print(LINE, x, y, "", ifIndex);

				y = yTop + yGap;
				ifIndex++;

				print(LINE, x, y, "", ifIndex);

				x += 34;
				ifIndex++;
			}
			x += 10;
		}

	}

	void xmlMake6_4_6x2(String filename) {

		String LINE = FileUtil.getString(new File(filename));

		int x = 130;
		int y;

		int ifIndex = 1;

		for (int j = 0; j < 6; j++) {

			y = yTop;
			print(LINE, x, y, "", ifIndex);

			y = yTop + yGap;
			ifIndex++;

			print(LINE, x, y, "", ifIndex);

			x += 34;
			ifIndex++;
		}
		x += 50;

		for (int j = 0; j < 4; j++) {

			y = yTop;
			print(LINE, x, y, "", ifIndex);

			y = yTop + yGap;
			ifIndex++;

			print(LINE, x, y, "", ifIndex);

			x += 34;
			ifIndex++;
		}
		x += 50;

		for (int j = 0; j < 6; j++) {

			y = yTop;
			print(LINE, x, y, "", ifIndex);

			y = yTop + yGap;
			ifIndex++;

			print(LINE, x, y, "", ifIndex);

			x += 34;
			ifIndex++;
		}
		x += 50;
	}

	void xmlMakeDSW2724G() {

		int x = xTop + 200;
		int y;

		int ifIndex = 1;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {

				y = yTop;
				print(x, y, "tag=\"180\"", ifIndex);

				y = yTop + yGap;
				ifIndex++;

				print(x, y, "", ifIndex);

				x += XGAP + 15;
				ifIndex++;
			}
			x += 7;
		}
	}

	void xmlMakeDSW3624XG() {

		int x = xTop + 50;
		int y;

		int ifIndex = 1;

		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 6; j++) {

				y = yTop;
				print(x, y, "tag=\"180\"", ifIndex);

				y = yTop + yGap;
				ifIndex++;

				print(x, y, "", ifIndex);

				x += XGAP + 15;
				ifIndex++;
			}
			x += 7;
		}
	}

	void xmlMakeHP5120() {

		int x = xTop;
		int y;

		int ifIndex = 1;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 8; j++) {

				ifIndex++;
				y = yTop;
				print(x, y, "tag=\"180\"", ifIndex);

				y = yTop + yGap;

				print(x, y, "", ifIndex - 1);

				x += XGAP;
				ifIndex++;
			}
			x += 7;
		}
	}

	void xmlMakeV6424() {

		int x = xTop + 200;
		int xGap = XGAP + 2;
		int y;

		int ifIndex = 1;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				y = yTop;
				print(x + (xGap * j), y, "tag=\"180\"", ifIndex);
				ifIndex++;
			}

			for (int j = 0; j < 4; j++) {
				y = yTop + yGap;
				print(x + (xGap * j), y, "", ifIndex);
				ifIndex++;
			}

			x += (xGap * 4);

			x += 10;
		}
	}

	void xmlMakeV2824() {

		int x = xTop + 250;
		int xGap = XGAP;
		int y;

		int ifIndex = 1;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				y = yTop;
				print(x + (xGap * j), y, "tag=\"180\"", ifIndex);
				ifIndex++;
			}

			for (int j = 0; j < 4; j++) {
				y = yTop + yGap;
				print(x + (xGap * j), y, "", ifIndex);
				ifIndex++;
			}

			x += (xGap * 4);

			x += 10;
		}
	}

	private void print(String LINE, int x, int y, String tag, int index) {
		String str;

		str = LINE.replaceFirst("\\$x", x + "");
		str = str.replaceFirst("\\$y", y + "");
		str = str.replaceFirst("\\$index", index + "");
		str = str.replaceFirst("\\$tag", tag);

		System.out.println(str);
	}

	private void print(int x, int y, String tag, int ifIndex) {
		String str;

		str = PORT.replaceFirst("\\$x", x + "");
		str = str.replaceFirst("\\$y", y + "");
		str = str.replaceFirst("\\$ifIndex", ifIndex + "");
		str = str.replaceFirst("\\$tag", tag);

		System.out.println(str);
	}

	private void printPower(int x, int y, String tag, int slotNo) {
		String str;

		str = POWER.replaceFirst("\\$x", x + "");
		str = str.replaceFirst("\\$y", y + "");
		str = str.replaceFirst("\\$slotNo", slotNo + "");
		str = str.replaceFirst("\\$tag", tag);

		System.out.println(str);
	}

	private void printSfpPort(int x, int y, String tag, int ifIndex) {
		String str;

		str = SFP_PORT.replaceFirst("\\$x", x + "");
		str = str.replaceFirst("\\$y", y + "");
		str = str.replaceFirst("\\$ifIndex", ifIndex + "");
		str = str.replaceFirst("\\$tag", tag);

		System.out.println(str);
	}

}
