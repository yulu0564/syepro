package com.syepro.app.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * 代表该Annotation用于修饰成员变量
 *
 */
@Target(ElementType.FIELD)
/**
 * 
 * 代表该Annotation能在运行时被程序提取里面的信�?
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewInjection {
	/**
	 * 以无参的方法形式来声明Annotation的成员变量，方面名就是该成员变量的名字，返回值就是该成员变量的类�?
	 * @return
	 */
	int value();

    /* parent view id */
    int parentId() default 0;
}

