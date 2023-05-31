package subkjh.lib.compiler.eval;

import java.util.Collection;

/**
 * 목록의 평균을 구합니다.
 * 
 * @author subkjh
 * 
 */
public class Avg extends EvalFunc {

	@Override
	protected Number compute(Collection<Object> c) throws Exception {

		double sum = 0;
		Number entry;
		for (Object o : c) {
			entry = Eval.getNumber(o);
			if (entry == null) {
				return null;
			}

			sum += entry.doubleValue();
		}

		return sum / c.size();
	}
}
