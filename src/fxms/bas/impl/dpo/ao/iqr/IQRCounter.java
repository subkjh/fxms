package fxms.bas.impl.dpo.ao.iqr;

import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author subkjh
 *
 */
public class IQRCounter {

	public class IQR {

		public final float q1;
		public final float q2;
		public final float q3;
		public final float iqr;
		public final float min;
		public final float max;
		public float datas[];

		public IQR(float q1, float q2, float q3) {
			this.q1 = q1;
			this.q2 = q2;
			this.q3 = q3;
			this.iqr = q3 - q1;

			this.min = q1 - (iqr * 1.5f);
			this.max = q3 + (iqr * 1.5f);
		}

		public float getMax(float rate) {
			return q3 + (iqr * rate);
		}

		public float getMin(float rate) {
			return q1 - (iqr * rate);
		}

		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("q1=").append(q1);
			sb.append(",q2=").append(q2);
			sb.append(",q3=").append(q3);
			sb.append(",iqr=").append(iqr);
			sb.append(",min=").append(min);
			sb.append(",max=").append(max);
			sb.append(",datas=").append(Arrays.toString(datas));
			return sb.toString();
		}
	}

	public static void main(String[] args) {
		IQRCounter c = new IQRCounter();
		IQR iqr;
		for (int i = 0; i < 100000; i++) {
			int count = (int) (Math.random() * 50);
			float datas[] = new float[count];
			for (int n = 0; n < count; n++) {
				datas[n] = (int) (Math.random() * 100f);
			}
			iqr = c.makeIqrRange(datas);
			if (iqr.datas.length > 0 && iqr.datas[iqr.datas.length - 1] > iqr.max) {
				System.out.println(iqr);
			}
		}

//		iqr = c.makeIqrRange(new int[] {});
//		System.out.println(iqr);
//		iqr = c.makeIqrRange(new int[] { 10 });
//		System.out.println(iqr);
//		iqr = c.makeIqrRange(new int[] { 10, 20 });
//		System.out.println(iqr);
//		iqr = c.makeIqrRange(new int[] { 10, 20, 30 });
//		System.out.println(iqr);
//		iqr = c.makeIqrRange(new int[] { 10, 20, 30, 40 });
//		System.out.println(iqr);
//		iqr = c.makeIqrRange(new int[] { 10, 20, 30, 40, 50 });
//		System.out.println(iqr);
//		iqr = c.makeIqrRange(new int[] { 10, 20, 30, 40, 50, 60 });
//		System.out.println(iqr);
//		iqr = c.makeIqrRange(new int[] { 10, 20, 30, 40, 50, 60, 70 });
//		System.out.println(iqr);
//		iqr = c.makeIqrRange(new int[] { 10, 20, 30, 40, 50, 60, 70, 80 });
//		System.out.println(iqr);
//		iqr = c.makeIqrRange(new int[] { 10, 20, 30, 40, 50, 60, 70, 80, 90 });
//		System.out.println(iqr);
//		iqr = c.makeIqrRange(new int[] { 10, 20, 30, 40, 50, 60, 70, 80, 90, 100 });
//		System.out.println(iqr);
//		iqr = c.makeIqrRange(new int[] { 0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100 });
//		System.out.println(iqr);
	}

	public IQRCounter() {
	}

	public IQR getIqrRange(List<Float> list) {

		float datas[] = new float[list.size()];
		for (int i = 0; i < datas.length; i++) {
			datas[i] = list.get(i);
		}

		return makeIqrRange(datas);

	}

	private float getQ(float datas[]) {
		int i = datas.length / 2;
		if (datas.length % 2 == 0) {
			return (datas[i] + datas[i - 1]) / 2;

		} else {
			return datas[i];
		}
	}

	private float getQ(float datas[], int startIndex, int endIndex) {
		float newDatas[] = new float[endIndex - startIndex];
		for (int i = 0; i < newDatas.length; i++) {
			newDatas[i] = datas[i + startIndex];
		}
		return (float) getQ(newDatas);
	}

	private IQR makeIqrRange(float datas[]) {

		Arrays.sort(datas);

		int q2Idx = 0;
		IQR ret;
		int size = datas.length;

		if (size == 0) {
			ret = new IQR(0, 0, 0);
		} else if (size == 1) {
			ret = new IQR(datas[0], datas[0], datas[0]);
		} else if (size == 2) {
			ret = new IQR(datas[0], (datas[0] + datas[1]) / 2, datas[1]);
		} else if (size == 3) {
			ret = new IQR(datas[0], datas[1], datas[2]);
		} else {

			try {

				// 1, 3, 4, 5, 5, 6, 7, 11 --> Q1 : ( 3 + 4 ) / 2, Q3 : ( 6 + 7
				// ) /
				// 2
				// 1, 3, 4, 5, 5, 6, 7, 11, 13 --> Q1 : 4, Q3 : 7

				// console.log("getIqrRange", field, list);

				q2Idx = size / 2;
				if (size % 2 == 0) {
					ret = new IQR(getQ(datas, 0, q2Idx) //
							, (datas[q2Idx] + datas[q2Idx - 1]) / 2 //
							, getQ(datas, q2Idx, datas.length));
				} else {
					ret = new IQR(getQ(datas, 0, q2Idx) //
							, datas[q2Idx] //
							, getQ(datas, q2Idx + 1, datas.length));
				}

			} catch (Exception e) {
				return null;
			}
		}

		ret.datas = datas;

		return ret;
	}

}
