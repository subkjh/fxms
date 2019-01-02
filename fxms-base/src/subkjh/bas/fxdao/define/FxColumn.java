package subkjh.bas.fxdao.define;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import subkjh.bas.dao.define.COLUMN_OP;
import subkjh.bas.dao.define.COLUMN_TYPE;

@Retention(RetentionPolicy.RUNTIME)
public @interface FxColumn {

	public String name() default "";

	public COLUMN_TYPE type() default COLUMN_TYPE.AUTO;

	public String sequence() default "";

	public int size() default 0;

	public boolean nullable() default false;

	public String comment() default "";

	public String defValue() default "";

	public COLUMN_OP operator() default COLUMN_OP.all;
}
