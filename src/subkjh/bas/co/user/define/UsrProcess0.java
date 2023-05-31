package subkjh.bas.co.user.define;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface UsrProcess0 {

	/** 파라메터명 */
	String[] para();
	
	/** 파라메터 종류 */
	String[] paraType();
	
}
