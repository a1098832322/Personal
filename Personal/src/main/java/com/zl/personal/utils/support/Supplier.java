package com.zl.personal.utils.support;

/**
 * 1.8中的Supplier的一个本地化实现，用于支持低版本JDK<br>
 * Support 1.5
 *
 * @author 郑龙
 * @date 2020/7/21 12:14
 */
public interface Supplier<T> {

    /**
     * get result
     * @return result value
     */
    T get();
}
