package subkjh.lib.compiler.eval;

import java.util.Collection;

/**
 * 목록에서 최대값을 구합니다.
 * 
 * @author subkjh
 * 
 */
public class Max extends EvalFunc {

	@Override
	protected Number compute(Collection<Object> c) throws Exception {

		Number entry;
		Number entryMax = null;
		for (Object o : c) {
			entry = Eval.getNumber(o);
			if (entry == null) {
				return null;
			}

			if (entryMax == null) {
				entryMax = entry;
			}
			else {
				entryMax = Math.max(entry.doubleValue(), entryMax.doubleValue());
			}
		}

		return entryMax;
	}
}
