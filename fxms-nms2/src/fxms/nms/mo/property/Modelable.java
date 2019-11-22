package fxms.nms.mo.property;

/**
 * 모델 속성
 * 
 * @author subkjh(Kim,JongHoon)
 *
 */
public interface Modelable {

	/**
	 * 같은 모델인지 여부를 전달한다.
	 * 
	 * @param o
	 * @return 같은 모델이면 true
	 */
	public boolean equalModel(Modelable o);

}
