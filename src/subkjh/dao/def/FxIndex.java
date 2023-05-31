package subkjh.dao.def;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import subkjh.dao.def.Index.INDEX_TYPE;

@Retention(RetentionPolicy.RUNTIME)
@Repeatable(FxIndexes.class)
public @interface FxIndex {

	public String name() default "PK";

	public INDEX_TYPE type() default INDEX_TYPE.PK;

	public String[] columns();

	public String fkTable() default "";

	public String fkColumn() default "";

}