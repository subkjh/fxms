package subkjh.bas.fxdao.define;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FxIndexes {
	public FxIndex[] value();
}
