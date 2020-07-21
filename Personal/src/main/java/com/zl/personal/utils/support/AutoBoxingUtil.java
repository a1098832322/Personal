package com.zl.personal.utils.support;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;

/**
 * 自动装箱工具类。
 * 提供一种将{@link HttpServletRequest}中请求参数(parameter)自动装箱成包装类的方法<br>
 * 提供将hibernate查询结果（主要是List<Object[]>类型）自动装箱成包装类的方法
 *
 * @author 郑龙
 * @date 2020/7/15 9:17
 */
public class AutoBoxingUtil {
    /**
     * 通过反射方法获取{@link HttpServletRequest} 中的参数，并装箱成目标<pre>dto</pre>对象
     *
     * @param request {@link HttpServletRequest}
     * @param dto     目标dto对象
     * @param <T>     模板方法中的模板类型
     * @return 目标dto对象
     * @throws IllegalAccessException 反射set value异常
     */
    public static <T> T getRequestParameters(HttpServletRequest request, T dto) throws IllegalAccessException {
        Class<?> dtoClass = dto.getClass();
        for (Field field : dtoClass.getDeclaredFields()) {
            field.setAccessible(true);
            field.set(dto, getOrDefault(request, field.getName(), null));
        }

        return dto;
    }

    /**
     * 通过反射方法获取{@link HttpServletRequest} 中的参数，并装箱成<pre>dto</pre>对象
     *
     * @param request  {@link HttpServletRequest}
     * @param dtoClass 目标dto的类型
     * @param <T>      模板方法中的模板类型
     * @return dto对象
     * @throws IllegalAccessException 反射set value异常
     * @throws InstantiationException 通过{@link Class#newInstance()}方法创建对象异常
     */
    public static <T> T getRequestParameters(HttpServletRequest request, Class<T> dtoClass) throws IllegalAccessException, InstantiationException {
        T dto = dtoClass.newInstance();
        return getRequestParameters(request, dto);
    }

    /**
     * 从{@link HttpServletRequest}中取值，当值为空时，返回预设的默认值
     *
     * @param request       当前Request请求
     * @param parameterName 目标参数名
     * @param defaultValue  默认值
     * @return 目标值或默认值
     */
    public static String getOrDefault(HttpServletRequest request, String parameterName, String defaultValue) {
        //从request中尝试取值
        String value = request.getParameter(parameterName);

        return getOrDefault(new Function<String, String>() {
            @Override
            public String apply(String s) {
                return s;
            }
        }, value, defaultValue);
    }

    /**
     * 当值不为<b>null</b>时返回当前值，否则返回指定默认值
     *
     * @param function     自定义方法{@link Function}
     * @param originObject 对象
     * @param defaultValue 指定的默认是
     * @param <T>          对象类型
     * @param <R>          返回值类型
     * @return 默认值或有效值
     */
    public static <T, R> R getOrDefault(Function<T, R> function, T originObject, R defaultValue) {
        R value = function.apply(originObject);

        if (value instanceof String) {
            return  StringUtils.isBlank((String) value) ? defaultValue : value;
        } else {
            return value == null ? defaultValue : value;
        }
    }

    /**
     * 将hibernate查询出的结果自动装箱成包装类
     *
     * @param resultListItem 当前属性在调用封装的hibernate工具类 方法后，返回的List&lt;Object[]&gt;中的item
     * @param databaseObject 包装类对象
     * @param <T>            模板类型
     * @return 包装类对象
     * @throws IllegalAccessException 反射设置属性值失败
     */
    public static <T> T autoBoxingDatabaseObject(Object[] resultListItem, T databaseObject) throws IllegalAccessException {
        //遍历获取所有字段属性
        for (Field field : databaseObject.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            //尝试获取字段注解
            Mapping mapping = field.getAnnotation(Mapping.class);
            if (mapping != null) {
                //读取索引
                int index = mapping.index();
                try {
                    //尝试设置值
                    if (resultListItem[index] == null
                            && String.class.equals(field.getType())) {
                        //当字符串类型的属性值为空时，将值设置为“”（String类型默认值为空字符串，其他类型为null）
                        field.set(databaseObject, "");
                    } else {
                        field.set(databaseObject, resultListItem[index]);
                    }
                } catch (Exception e) {
                    //当有异常时（通常会是index不合法或者字段与值类型不匹配等异常导致）
                    field.set(databaseObject, null);

                }
            }
        }

        return databaseObject;
    }
}
