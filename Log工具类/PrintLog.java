package com.dsw.aop.config.annotation;

import com.dsw.aop.config.CommConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标注需要存储进数据库以及打印日志的方法
 *
 * @author 郑龙
 * @date 2019/5/5 9:23
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PrintLog {
    /**
     * 需要被记录Log(默认为true)
     */
    boolean required() default true;

    /**
     * 操作所属模块
     *
     * @return
     */
    CommConfig.MODULE module() default CommConfig.MODULE.EMPTY;

    /**
     * log内容
     *
     * @return
     */
    String content();
}
