package com.zl.personal.annotations;

import java.lang.annotation.*;

/**
 * 权限校验注解，用此注解标注的接口会认证token
 *
 * @author 郑龙
 * @date 2020/7/6 17:22
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Authentication {
    /**
     * 用于标注token参数的index顺序
     *
     * @return index
     */
    int index() default 0;

    /**
     * 用于表示token是否需要反射取值
     *
     * @return 默认为是
     */
    boolean reflect() default true;
}
