package com.zl.personal.utils.support;



import java.lang.annotation.*;

/**
 * 声明Java对象与数据库表字段的对应关系<br>
 * 主要因为IMS系统中封装的hibernate工具类查询出来的东西均为：Object[]，并不会自动封装成对象，用起来各种不方便。<br>
 * 因此这里考虑把查询结果使用{@link AutoBoxingUtil}工具类给转一下
 *
 * @author 郑龙
 * @date 2020/7/16 9:29
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Mapping {
    /**
     * 对应数据库表字段名称（保留，现在还没用到这个属性）
     *
     * @return 数据库表字段名称
     */
    String column() default "";

    /**
     * 当前属性在调用封装的hibernate工具类 方法后，返回的List&lt;Object[]&gt;中所在的索引序号
     *
     * @return resultList中的索引序号
     */
    int index();
}
