package tool;

import java.io.File;
import java.util.List;

import subkjh.bas.co.utils.FileUtil;

public class BaseDataMake {

	public static void main(String[] args) {
		BaseDataMake c = new BaseDataMake();
		c.opId();
	}

	void opId() {

		List<String> list = FileUtil.getLines(new File("datas/setup/base-data-opId.txt"));

		int index = 0;
		String ss[];
		for (String line : list) {
			if (index++ == 0) {
				continue;
			}
//			System.out.println(line);

			ss = line.split("\t");
			System.out.println("/** " + ss[2] + "*/");
			System.out.println(ss[0].replaceAll("-", "_") + "(\"" + ss[0] + "\"),");
			System.out.println();

		}

	}

	void alarmCode() {

		List<String> list = FileUtil.getLines(new File("setup/base-data-alarmCode.txt"));

//		int index = 0;
		String ss[];
		for (String line : list) {
//			if (index++ == 0) {
//				continue;
//			}
//			System.out.println(line);

			ss = line.split("\t");
			System.out.println("/** " + ss[3] + "*/");
			System.out.println(ss[1].replaceAll("-", "_") + "(" + ss[0] + "),");
			System.out.println();

		}

	}
}
