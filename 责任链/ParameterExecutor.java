package com.sqh;

import org.apache.commons.lang3.StringUtils;

/**
 * 对需要验证的参数进行相应的处理
 *
 * @author 郑龙
 * @date 2019/5/29 9:13
 */
public class ParameterExecutor extends BaseChainExecutor {

    /**
     * 创建执行器
     */
    public static ParameterExecutor createExecutor() {
        ParameterExecutor executor = new ParameterExecutor();
        executor.addHandler(new DoubleHandler(), new LongHandler(), new IntegerHandler()
                , new StringHandler(), new DefaultHandler());
        return executor;
    }

    /**
     * Double 类型处理器
     */
    public static class DoubleHandler implements BaseChainOfResponsibility {
        /**
         * 责任划分标准
         *
         * @param obj 目标参数
         * @return true/false
         */
        @Override
        public boolean accept(Object obj) {
            return obj instanceof Double;
        }

        /**
         * 相应处理方法
         */
        @Override
        public void execute(Object obj) {
            if (obj == null || (Double) obj <= 0) {
                throw new IllegalArgumentException("参数不能为空且不能小于等于0！");
            }
        }
    }

    /**
     * Long 类型处理器
     */
    public static class LongHandler implements BaseChainOfResponsibility {
        /**
         * 责任划分标准
         *
         * @param obj 目标参数
         * @return true/false
         */
        @Override
        public boolean accept(Object obj) {
            return obj instanceof Long;
        }

        /**
         * 相应处理方法
         */
        @Override
        public void execute(Object obj) {
            if (obj == null || (Long) obj <= 0) {
                throw new IllegalArgumentException("参数不能为空且不能小于等于0！");
            }
        }
    }

    /**
     * Integer 类型处理器
     */
    public static class IntegerHandler implements BaseChainOfResponsibility {
        /**
         * 责任划分标准
         *
         * @param obj 目标参数
         * @return true/false
         */
        @Override
        public boolean accept(Object obj) {
            return obj instanceof Integer;
        }

        /**
         * 相应处理方法
         */
        @Override
        public void execute(Object obj) {
            if (obj == null || (Integer) obj <= 0) {
                throw new IllegalArgumentException("参数不能为空且不能小于等于0！");
            }
        }
    }

    /**
     * String 类型处理器
     */
    public static class StringHandler implements BaseChainOfResponsibility {
        /**
         * 责任划分标准
         *
         * @param obj 目标参数
         * @return true/false
         */
        @Override
        public boolean accept(Object obj) {
            return obj instanceof String;
        }

        /**
         * 相应处理方法
         */
        @Override
        public void execute(Object obj) {
            if (obj == null || StringUtils.isBlank(obj.toString())) {
                throw new IllegalArgumentException("参数不能为空！");
            }
        }
    }

    /**
     * 其他类型处理器
     */
    public static class DefaultHandler implements BaseChainOfResponsibility {
        /**
         * 责任划分标准
         *
         * @param obj 目标参数
         * @return true/false
         */
        @Override
        public boolean accept(Object obj) {
            return !(obj instanceof String) &&
                    !(obj instanceof Double) &&
                    !(obj instanceof Long) &&
                    !(obj instanceof Integer);
        }

        /**
         * 相应处理方法
         */
        @Override
        public void execute(Object obj) {
            if (obj == null) {
                throw new IllegalArgumentException("参数不能为空！");
            }
        }
    }
}
