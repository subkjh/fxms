package fxms.bas.fxo;

import java.util.Map;

/**
 * FxMS Model
 * 
 * @author subkjh
 *
 */
public interface FxActor extends FxObject {

	/**
	 * 
	 * @return 이름
	 */
	public String getName();

	/**
	 * 
	 * @param name
	 */
	public void setName(String name);

	/**
	 * 클래스가 생성되고 인수 및 조건이 모두 적용된 후에 호출된다.
	 */
	public void onCreated() throws Exception;

	/**
	 * 파라메터를 제공한다.
	 * 
	 * @return
	 */
	public Map<String, Object> getPara();

	/**
	 * 매칭 여부를 검사하지 않을 경우 null로 리턴한다.
	 * 
	 * @return
	 */
	public FxMatch getMatch();

}
