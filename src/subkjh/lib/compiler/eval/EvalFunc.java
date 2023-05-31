package subkjh.lib.compiler.eval;

import java.util.Collection;
import java.util.Map;

/**
 * 함수의 공통 내역을 가지고 있는 클래스
 * 
 * @author subkjh
 * 
 */
public abstract class EvalFunc {

	protected Object item;

	@SuppressWarnings("unchecked")
	public Number compute(Map<String, Object> varMap) throws Exception {

		if (item instanceof Number) {
			return (Number) item;
		}
		else {
			Object val = varMap.get((String) item);

			if (val instanceof Collection) {
				Collection<Object> c = (Collection<Object>) val;
				return compute(c);
			}
			else {
				return Eval.getNumber(val);
			}
		}
	}

	public Object getItem() {
		return item;
	}

	public void setItem(Object item) {
		this.item = item;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" + item + ")";
	}

	/**
	 * 각 함수의 특성에 맞게 값을 구합니다.
	 * 
	 * @param c
	 *            목록
	 * @return 계산된 값
	 * @throws Exception
	 */
	protected abstract Number compute(Collection<Object> c) throws Exception;
}
