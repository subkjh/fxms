package fxms.rule;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 액션, 조건, 트리거에 대한 설명 선언
 * 
 * @author subkjh
 * @since 2023.02
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface FxRuleActionInfo {

	String name() default "이름이 없습니다.";

	String descr() default "설명이 없습니다.";

}
