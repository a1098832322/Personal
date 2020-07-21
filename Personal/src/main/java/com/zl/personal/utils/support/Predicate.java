package com.zl.personal.utils.support;

/**
 * 条件接口。支持一下1.5的开发环境。1.8后直接用JDK自带的就行了
 *
 * @author 郑龙
 * @date 2020/7/17 16:13
 */
public interface Predicate<T> {
    /**
     * 自定义测试方法<br>
     * <ul>
     *     <li><pre>true</pre>:符合条件</li>
     *      <li><pre>false</pre>:不符合条件</li>
     * </ul>
     *
     * @param t 目标对象
     * @return true/false 符合条件/不符合条件
     */
    boolean test(T t);
}
