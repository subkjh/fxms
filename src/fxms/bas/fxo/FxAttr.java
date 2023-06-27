package fxms.bas.fxo;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 필드의 속성을 지정한다.
 * 
 * @author subkjh
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface FxAttr {

	/**
	 * 필드명 지정
	 * 
	 * @return
	 */
	String name() default "";

	/**
	 * 필수 여부
	 * 
	 * @return
	 */
	boolean required() default true;

	/**
	 * 속성 설명
	 * 
	 * @return
	 */
	String description() default "";

	/**
	 * 화면에 보일 명칭
	 * 
	 * @return
	 */
	String text() default "";

	/**
	 * 샘플 값
	 * 
	 * @return
	 */
	String example() default "";

	/**
	 * 지정할 값
	 * 
	 * @return
	 */
	String value() default "";

	/**
	 * 
	 * @return
	 */
	Class<?> exampleClass() default Void.class;

}
