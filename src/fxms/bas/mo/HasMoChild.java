package fxms.bas.mo;

import fxms.bas.vo.SyncMo;

/**
 * 하위 자식을 가지는 MO인지를 판단한다.
 * 
 * @author subkjh
 *
 */
public interface HasMoChild {

	/**
	 * 하위 관리대상
	 * 
	 * @return
	 */
	public SyncMo getSyncMo();

}
