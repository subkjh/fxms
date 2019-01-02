package subkjh.bas.net.broker;

public class test {
	public static void main(String[] args) {

		test t = new test();
		t.test();

	}

	private void test() {
		String str = "5448 494e 4753 5049 5245 2020 4441 5441 2054 4553 5420 2045 4e44 20";

		String s = str.replaceAll(" ", "");
		char bytes[] = new char[s.length() / 2];
		for (int i = 0; i < s.length(); i += 2) {

			System.out.println((i/2) + "=" + s.substring(i, i + 2));

			bytes[i / 2] = (char) Short.valueOf(s.substring(i, i + 2), 16).byteValue();
		}

		System.out.println( new String(bytes));

	}

}
