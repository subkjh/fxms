package fxms.bas.fxo;

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

	public void setName(String name);

	public FxPara getFxPara();

	/**
	 * 클래스가 생성되고 인수 및 조건이 모두 적용된 후에 호출된다.
	 */
	public void onCreated();

	/**
	 * XML의 para를 설정한다.
	 * 
	 * @param name
	 * @param value
	 */
	public void setPara(String name, String value);
}
