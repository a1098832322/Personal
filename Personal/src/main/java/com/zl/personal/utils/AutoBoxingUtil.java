package com.zl.personal.utils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;

/**
 * 自动装箱工具类。
 * 提供一种将{@link HttpServletRequest}中请求参数(parameter)自动装箱成包装类的方法
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
     * @see
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
    private static String getOrDefault(HttpServletRequest request, String parameterName, String defaultValue) {
        //从request中尝试取值
        String value = request.getParameter(parameterName);
        return value == null ? defaultValue : value;
    }
}
