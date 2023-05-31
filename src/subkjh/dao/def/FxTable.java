package subkjh.dao.def;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 테이블 내용을 나타내는 객체를 지칭하는 인터페이스
 * 
 * @author subkjh
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface FxTable {

	public String name(); // 테이블명

	public String comment() default ""; // 테이블 설명

}