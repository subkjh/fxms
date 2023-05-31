package fxms.bas.mo;

import java.io.Serializable;

/**
 * 관리대상의 추가적인 데이터
 * 
 * @author subkjh
 *
 */
public interface MoAddableData extends Serializable {

	public void setMoNo(long moNo);

	public long getMoNo();
}
