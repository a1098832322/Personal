package com.zl.personal.utils.support;

/**
 * 1.8中的Consumer的一个本地化版本，用于支持低版本JDK<br>
 * Support 1.5
 *
 * @author 郑龙
 * @date 2020/7/21 11:30
 */
public interface Consumer<T> {
    /**
     * 值处理方法
     *
     * @param value 值
     */
    void apply(T value);
}
