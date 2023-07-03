package subkjh.dao.def;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FxOrder {

	/** order by 목록. column desc, column asc 가능 */
	public String[] columns() default {};

}