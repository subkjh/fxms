package subkjh.dao.def;

public interface DaoListener {

	/**
	 * 조회 시작을 알림
	 * 
	 * @param columnNames 조회되는 컬럼
	 * @throws Exception
	 */
	public void onStart(String colNames[]) throws Exception;

	/**
	 * 조회되는 내용
	 * 
	 * @param rowNo 열번호로 0부터 시작함
	 * @param data  조회된 데이터
	 * 
	 * @return 계속 여부
	 */
	public void onSelected(int rowNo, Object data) throws Exception;

	/**
	 * 처리 종료
	 */
	public void onFinish(Exception ex) throws Exception;

	public void onExecuted(Object data, Exception ex) throws Exception;

}
