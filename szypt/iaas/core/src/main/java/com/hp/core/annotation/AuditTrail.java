package com.hp.core.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target({METHOD})
public @interface AuditTrail {
	String action() default "";
	boolean enabled() default true;
}
