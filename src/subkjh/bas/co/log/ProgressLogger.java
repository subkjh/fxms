package subkjh.bas.co.log;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ProgressLogger extends Thread {

	private static ProgressLogger printer;

	public static void doIn(String msg) {
		System.out.print(msg);
		System.out.flush();
	}

	public static void end(String msg) {
		if (printer != null) {
			printer.isContinue = false;
			printer = null;
		}
		System.out.println(" " + msg);
	}

	public static void print(List<Map<String, Object>> mapList) {
		print(mapList, 0);
	}

	public static void print(List<Map<String, Object>> mapList, int pageSize) {
		if (mapList == null || mapList.size() == 0) {
			System.out.println("0 rows");
			return;
		}

		Map<String, Object> map = mapList.get(0);
		String nameArr[] = map.keySet().toArray(new String[map.size()]);
		Arrays.sort(nameArr);

		int max[] = new int[nameArr.length];

		for (int i = 0; i < max.length; i++) {
			max[i] = nameArr[i].length();
		}

		for (Map<String, Object> e : mapList) {
			for (int i = 0; i < nameArr.length; i++) {
				max[i] = Math.max(max[i], (e.get(nameArr[i]) + "").length());
			}
		}

		StringBuffer line = new StringBuffer();
		line.append("+-");
		for (int i = 0; i < max.length; i++) {
			for (int n = 0; n < max[i]; n++)
				line.append("-");
			line.append("-+");
			if (i < max.length - 1)
				line.append("-");
		}

		System.out.println(line);
		System.out.print("| ");
		for (int i = 0; i < nameArr.length; i++) {
			System.out.format("%-" + max[i] + "s", nameArr[i]);
			if (i < nameArr.length)
				System.out.print(" | ");
		}
		System.out.println("\n" + line);

		int n = 0;
		int index = 0;

		for (Map<String, Object> e : mapList) {
			System.out.print("| ");
			for (int i = 0; i < nameArr.length; i++) {
				System.out.format("%-" + max[i] + "s", e.get(nameArr[i]));
				if (i < nameArr.length)
					System.out.print(" | ");
			}
			System.out.println();

			index++;
			n++;

			if (pageSize >= 0 && n == pageSize) {
				System.out.println("--- more ( " + index + " of " + mapList.size() + " ) ---");
				try {
					int ret = System.in.read();
					if (ret == 'q')
						break;
				} catch (Exception e2) {
				}
				n = 0;
			}
		}

		System.out.println(line);
	}

	public static void start(String msg) {
		System.out.print(msg);
		System.out.flush();

		if (printer != null) {
			printer.isContinue = false;
		}

		printer = new ProgressLogger();
		printer.start();
	}

	private boolean isContinue;

	public ProgressLogger() {
		isContinue = true;
	}

	@Override
	public void run() {

		while (isContinue) {

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}

			if (isContinue == false)
				return;

			System.out.print(".");
			System.out.flush();

		}
	}

}
