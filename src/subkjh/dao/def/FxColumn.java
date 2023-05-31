package subkjh.dao.def;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import subkjh.dao.def.Column.COLUMN_OP;
import subkjh.dao.def.Column.COLUMN_TYPE;

/**
 * 테이블의 컬럼을 나타내는 인터페이스
 * 
 * @author subkjh
 *
 */
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
