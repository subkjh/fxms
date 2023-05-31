package subkjh.dao.def;

import subkjh.dao.ClassDao;

/**
 * 뭔가를 처리하기 전에 호출하는 인터페이스
 * 
 * @author subkjh
 *
 * @param <DATA>
 */
public interface FxDaoCallBefore<DATA> {

	/**
	 * 
	 * @param tran
	 * @param data 실제처리할 데이터
	 * @throws Exception
	 */
	public void onCall(ClassDao tran, DATA data) throws Exception;
}
