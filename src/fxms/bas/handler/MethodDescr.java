package fxms.bas.handler;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Handler 메소드에 대한 설명
 * 
 * @author subkjh
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodDescr {

	String name(); // 메소드명

	String description(); // 메소드 설명
	
	
}
