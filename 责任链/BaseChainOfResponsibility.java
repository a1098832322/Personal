package com.sqh;

/**
 * 责任链接口
 *
 * @author 郑龙
 * @date 2019/5/29 9:11
 */
public interface BaseChainOfResponsibility {
    /**
     * 责任划分标准
     *
     * @param obj 目标参数
     * @return true/false
     */
    boolean accept(Object obj);

    /**
     * 相应处理方法
     *
     * @param obj 目标参数
     */
    void execute(Object obj);

}
