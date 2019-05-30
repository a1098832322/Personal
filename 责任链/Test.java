package com.sqh;

import java.util.ArrayList;

/**
 * 测试类
 *
 * @author 郑龙
 * @date 2019/5/30 15:56
 */
public class Test {
    /**
     * 责任链测试
     *
     * @param args args
     */
    public static void main(String[] args) {
        Long l = 1L;
        Double d = 0.00;
        Integer i = null;
        String str = "";

        ArrayList<Object> array = new ArrayList<>();
        array.add(l);
        array.add(d);
        array.add(i);
        array.add(str);

        validateParams(ParameterExecutor.createExecutor(), array.toArray(new Object[0]));
    }

    /**
     * 验证入参合法性
     *
     * @param executor 参数处理器
     * @param params   需要校验的入参
     * @throws IllegalArgumentException 参数非法异常
     */
    public static void validateParams(BaseChainExecutor executor, Object... params) {
        if (params == null) {
            throw new NullPointerException("需要验证的参数为null!");
        } else {
            for (Object parameter : params) {
                executor.process(parameter);
            }
        }
    }
}
