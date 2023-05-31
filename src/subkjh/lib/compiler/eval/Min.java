package subkjh.lib.compiler.eval;

import java.util.Collection;

/**
 * 목록에서 최소값을 구합니다.
 * 
 * @author subkjh
 * 
 */
public class Min extends EvalFunc {

	@Override
	protected Number compute(Collection<Object> c) throws Exception {

		Number entry;
		Number entryMin = null;
		for (Object o : c) {
			entry = Eval.getNumber(o);
			if (entry == null) {
				return null;
			}

			if (entryMin == null) {
				entryMin = entry;
			}
			else {
				entryMin = Math.min(entry.doubleValue(), entryMin.doubleValue());
			}
		}

		return entryMin;
	}
}