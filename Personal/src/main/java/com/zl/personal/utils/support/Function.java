package com.zl.personal.utils.support;

/**
 * JDK8中Function接口的一个本地定义，主要是为了兼容低版本开发环境。在JDK8环境中直接使用JDK自己的即可。
 *
 * @author 郑龙
 * @date 2020/7/20 9:11
 */
public interface Function<T, R> {
    /**
     * 执行方法
     *
     * @param t 原始类型
     * @return 返回值
     */
    R apply(T t);
}
