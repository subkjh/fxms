package fxms.nms.fxactor.ping.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import subkjh.bas.co.log.Logger;

/**
 * MTR 결과
 * 
 * @author subkjh
 * @since 2013.05.02
 */
public class MtrPingResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8164149318320096102L;

	public static void main(String[] args) {
		StringBuffer sb = new StringBuffer();
		sb.append("devser25                          Snt: 3     Loss%  Last   Avg  Best  Wrst StDev\n");
		sb.append("167.1.21.1                                    0.0%   0.3   0.3   0.3   0.3   0.0\n");
		sb.append("112.216.24.145                                0.0%   0.8   0.8   0.8   0.8   0.0\n");
		sb.append("10.18.241.29                                  0.0%   0.6   0.6   0.6   0.6   0.0\n");
		sb.append("192.168.10.33                                 0.0%   0.5   0.6   0.5   0.6   0.0\n");
		sb.append("203.248.225.29                                2.0%   0.8   0.8   0.7   0.8   0.0\n");
		sb.append("210.120.48.217                                0.0%   0.7   0.7   0.7   0.7   0.0\n");
		sb.append("210.120.94.77                                 0.0%   4.2   4.6   3.7   5.9   1.1\n");
		sb.append("210.92.194.22                                 0.0%   1.2   1.1   1.0   1.2   0.1\n");
		sb.append("182.162.1.6                                   0.0%   1.2   1.1   1.1   1.2   0.1\n");
		sb.append("211.237.0.34                                  0.0%   1.4   1.5   1.4   1.6   0.1\n");
		sb.append("192.168.200.22                                0.0%   2.0   1.8   1.7   2.0   0.2\n");
		sb.append("110.45.215.15                                 0.0%   1.0   1.1   1.0   1.3   0.1\n");
		MtrPingResult c = new MtrPingResult("110.45.215.15", sb.toString());
		System.out.println(c);

		sb = new StringBuffer();
		sb.append("HOST: QMS2                        Loss%   Snt   Last   Avg  Best  Wrst StDev\n");
		sb.append("1. 211.40.178.177                0.0%     3    0.6   0.7   0.6   1.0   0.2\n");
		sb.append("2. 121.254.215.34                0.0%     3    0.9   1.3   0.9   1.8   0.5\n");
		sb.append("3. 211.174.48.105                0.0%     3    1.9   1.4   0.9   1.9   0.5\n");
		sb.append("4. 210.92.194.21                 0.0%     3    4.0   3.5   2.8   4.0   0.7\n");
		sb.append("5. 1.213.107.222                 0.0%     3    6.3   7.1   6.3   8.3   1.1\n");
		sb.append("6. 203.233.1.50                  0.0%     3    4.5   4.5   4.3   4.5   0.1\n");
		sb.append("7. 61.34.71.10                   0.0%     3    3.8   3.7   3.5   3.9   0.2\n");
		c = new MtrPingResult("61.34.71.10", sb.toString());
		System.out.println(c);

	}

	private List<Mtr> mtrList = new ArrayList<Mtr>();

	private Mtr mtr = new Mtr();

	public MtrPingResult() {

	}

	public MtrPingResult(String ip, String result) {
		// [root@devser25 ~]# mtr -r -n -c 3 110.45.215.15
		// devser25 Snt: 3 Loss% Last Avg Best Wrst StDev
		// 167.1.21.1 0.0% 0.3 0.3 0.3 0.3 0.0
		// 112.216.24.145 0.0% 0.8 0.8 0.8 0.8 0.0
		// 10.18.241.29 0.0% 0.6 0.6 0.6 0.6 0.0
		// 192.168.10.33 0.0% 0.5 0.6 0.5 0.6 0.0
		// 203.248.225.29 0.0% 0.8 0.8 0.7 0.8 0.0
		// 210.120.48.217 0.0% 0.7 0.7 0.7 0.7 0.0
		// 210.120.94.77 0.0% 4.2 4.6 3.7 5.9 1.1
		// 210.92.194.22 0.0% 1.2 1.1 1.0 1.2 0.1
		// 182.162.1.6 0.0% 1.2 1.1 1.1 1.2 0.1
		// 211.237.0.34 0.0% 1.4 1.5 1.4 1.6 0.1
		// 192.168.200.22 0.0% 2.0 1.8 1.7 2.0 0.2
		// 110.45.215.15 0.0% 1.0 1.1 1.0 1.3 0.1

		try {
			String sList[] = result.split("\n");
			List<String> itemList;
			Mtr line;
			for (String s : sList) {
				itemList = parse(s);

				try {
					line = new Mtr(itemList);
					if (line.ip.equals(ip)) {
						mtr = line;
					} else {
						mtrList.add(line);
					}
				} catch (Exception e) {
					Logger.logger.error(e);
				}
			}

		} catch (Exception e) {
		}

		for (Mtr e : mtrList) {
			if (e.loss > mtr.loss) mtr.loss = e.loss;
		}
	}

	public Mtr getMtr() {
		return mtr;
	}

	public List<Mtr> getMtrList() {
		return mtrList;
	}

	@Override
	public String toString() {
		return mtr == null ? "" : mtr.toString();
	}

	protected List<String> parse(String line) {
		String str = "";
		List<String> list = new ArrayList<String>();
		boolean all = false;

		for (char ch : line.toCharArray()) {
			if (all == false && ch <= ' ') {
				if (str.length() > 0) {
					if (str.startsWith("cmd")) {
						all = true;
						continue;
					} else {
						list.add(str);
						str = "";
					}
				}
			} else {
				str += (ch + "");
			}
		}

		list.add(str);

		return list;
	}
}
