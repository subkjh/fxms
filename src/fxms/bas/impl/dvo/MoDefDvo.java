package fxms.bas.impl.dvo;

import fxms.bas.impl.dbo.all.FX_MO;
import fxms.bas.mo.FxMo;

/**
 * deploy/mo 폴더에 정의된 파일을 분석한 내용이다.
 * 
 * @author subkjh
 *
 */
public class MoDefDvo {

	private final String moClass;
	private final Class<? extends FxMo> javaClass;
	private final Class<? extends FX_MO> dboJavaClass;

	@SuppressWarnings("unchecked")
	public MoDefDvo(String moClass, String javaClass, String dboJavaClass) throws Exception {
		this.moClass = moClass;
		this.javaClass = (Class<? extends FxMo>) Class.forName(javaClass);
		this.dboJavaClass = (Class<? extends FX_MO>) Class.forName(dboJavaClass);
	}

	public MoDefDvo(String moClass, Class<? extends FxMo> javaClass, Class<? extends FX_MO> dboJavaClass) {
		this.moClass = moClass;
		this.javaClass = javaClass;
		this.dboJavaClass = dboJavaClass;
	}

	public String getMoClass() {
		return moClass;
	}

	public Class<? extends FxMo> getJavaClass() {
		return javaClass;
	}

	public Class<? extends FX_MO> getDboJavaClass() {
		return dboJavaClass;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(moClass).append("(").append(javaClass.getName()).append(",").append(dboJavaClass.getName())
				.append(")");
		return sb.toString();
	}

}
