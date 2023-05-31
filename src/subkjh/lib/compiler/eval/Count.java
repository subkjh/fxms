package subkjh.lib.compiler.eval;

import java.util.Collection;

/**
 * 목록의 수를 구합니다.
 * 
 * @author subkjh
 * 
 */
public class Count extends EvalFunc {

	@Override
	protected Number compute(Collection<Object> c) throws Exception {
		return c.size();
	}
}