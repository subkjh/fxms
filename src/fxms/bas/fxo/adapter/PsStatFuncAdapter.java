package fxms.bas.fxo.adapter;

import java.util.List;

import fxms.bas.fxo.FxActorImpl;
import fxms.bas.vo.PsItem;

/**
 * 성능 수집 내용을 이용하여 통계 데이터를 생성한다.
 * 
 * @author subkjh
 * @since 2022.04.14
 *
 */
public class PsStatFuncAdapter extends FxActorImpl {

	public class PsStatRet {
		public final int total;
		public final int acceptCount;
		public final Number sum;
		public final Number max;
		public final Number min;
		public final Number avg;

		public PsStatRet(int total, int acceptCount, Number sum, Number max, Number min, Number avg) {
			this.total = total;
			this.acceptCount = acceptCount;
			this.sum = sum;
			this.max = max;
			this.min = min;
			this.avg = avg;
		}

		public PsStatRet(PsItem item, List<Number> list) {

			this.total = list.size();

			int count = 0;
			Number min = list.get(0);
			Number max = list.get(0);
			Number sum = 0;
			for (Number num : list) {

				if (item.isAcceptable(num) == false)
					continue;

				count++;

				sum = sum.floatValue() + num.floatValue();

				if (num.floatValue() < min.floatValue()) {
					min = num;
				}
				if (num.floatValue() > max.floatValue()) {
					max = num;
				}

			}
			this.acceptCount = count;
			this.sum = sum;
			this.max = max;
			this.min = min;
			this.avg = sum.floatValue() / count;
		}
	}

	/**
	 * 통계를 생성한다.
	 * 
	 * @param valList 수집값 목록
	 * @return 통계값
	 * @throws Exception
	 */
	public PsStatRet compute(PsItem item, List<Number> list) throws Exception {
		return new PsStatRet(item, list);
	}

}
