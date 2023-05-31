package subkjh.dao.def;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FxIndexes {
	public FxIndex[] value();
}
