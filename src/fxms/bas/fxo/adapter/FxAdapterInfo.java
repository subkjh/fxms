package fxms.bas.fxo.adapter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 아답터를 실행할 수 있는 관리대상 정보를 정의한다.
 * 
 * @author subkjh
 * @since 2023.02
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface FxAdapterInfo {

	String descr() default "";

	int pollCycle() default 60;

	String moJson() default "";
	
	String service() default "";

}
