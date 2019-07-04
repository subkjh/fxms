package fxms.nms.fxactor.ping.data;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author subkjh
 * @since 2013.05.02
 */
public class Mtr implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4774162064845661372L;
	public String ip;
	public float loss;
	public float last;
	public float avg;
	public float best;
	public float wrst;
	public float stDev;

	public Mtr() {

	}

	public Mtr(List<String> strList) throws Exception {

		if (strList.size() == 7) {
			ip = strList.get(0);
			loss = Float.parseFloat(strList.get(1).replaceAll("%", ""));
			last = Float.parseFloat(strList.get(2));
			avg = Float.parseFloat(strList.get(3));
			best = Float.parseFloat(strList.get(4));
			wrst = Float.parseFloat(strList.get(5));
			stDev = Float.parseFloat(strList.get(6));

			// 2013.07.08 by subkjh - added
		} else if (strList.size() == 9) {
			ip = strList.get(1);
			loss = Float.parseFloat(strList.get(2).replaceAll("%", ""));
			last = Float.parseFloat(strList.get(4));
			avg = Float.parseFloat(strList.get(5));
			best = Float.parseFloat(strList.get(6));
			wrst = Float.parseFloat(strList.get(7));
			stDev = Float.parseFloat(strList.get(8));
		} else {
			throw new Exception("This format not implement");
		}
	}

	@Override
	public String toString() {
		return ip + "|" + loss + "|" + last + "|" + avg + "|" + best + "|" + wrst + "|" + stDev;
	}
}
