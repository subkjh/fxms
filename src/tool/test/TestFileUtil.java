package tool.test;

import java.io.File;
import java.util.List;

import subkjh.bas.co.utils.FileUtil;

public class TestFileUtil {

	public static void main(String[] args) {
		List<String> list = FileUtil.getLines(new File("datas/setup/temp.txt"));
		for (String line : list) {
			System.out.println("drop table " + line + ";");
		}
	}
}
