package fxms.bas.handler;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Handler URL 대한 설명
 * 
 * @author subkjh
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface UrlDescr {

	String url();

	String descr();

}
