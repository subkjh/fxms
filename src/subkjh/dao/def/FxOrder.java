package subkjh.dao.def;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FxOrder {

	public String[] columns() default {};

}