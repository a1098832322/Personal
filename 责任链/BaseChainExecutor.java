package com.sqh;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 基础责任链执行器
 *
 * @author 郑龙
 * @date 2019/5/29 9:44
 */
public class BaseChainExecutor<T extends BaseChainOfResponsibility> {
    /**
     * 定义装载处理器的list
     */
    private List<T> handlerList = new ArrayList<>();

    public void addHandler(T handler) {
        this.handlerList.add(handler);
    }

    public void addHandler(T... handlers) {
        this.handlerList.addAll(Arrays.asList(handlers));
    }

    public void addHandler(List<T> handlerList) {
        this.handlerList.addAll(handlerList);
    }

    /**
     * 按责任装配对应的处理器
     *
     * @param obj 参数
     */
    public void process(Object obj) {
        for (T handler : handlerList) {
            if (handler.accept(obj)) {
                handler.execute(obj);
                break;
            }
        }
    }
}
