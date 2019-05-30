package com.dsw.aop.utils;


import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 针对List进行数据处理
 *
 * @author 郑龙
 * @date 2019/5/13 13:58
 */
public class ListUtil {
    /**
     * 从原始集合中按条件分组
     *
     * @param originList 原始数据集
     * @param filter     条件
     * @param <T>        模板类型
     * @return 符合条件的item集合
     */
    public static <T> List<Map.Entry<Object, List<T>>> groupBy(List<T> originList
            , Function<T, Object> filter) {
        //筛选条件判空
        Objects.requireNonNull(filter);
        //定义一个map
        Map<Object, List<T>> map = new HashMap<>(16);

        //进行分组处理
        originList.forEach(t -> {
            List<T> list = map.get(filter.apply(t)) == null
                    ? new ArrayList<>()
                    : map.get(filter.apply(t));
            list.add(t);
            map.put(filter.apply(t), list);
        });

        return new ArrayList<>(map.entrySet());
    }

    /**
     * 将需要添加的list添加进原始list中
     *
     * @param originList 原始list
     * @param appendList 需要添加的list
     * @param <T>        模板类型
     * @return 添加新元素后的list
     */
    public static <T> List<T> add(List<T> originList, List<T> appendList) {
        originList.addAll(appendList);
        return originList;
    }

    /**
     * 求集合A与B的差集，并返回差集元素
     *
     * @param parentSet      集合A
     * @param childSet       集合B
     * @param parentFunction 定义需要从集合A元素中取出什么值来进行比对操作
     * @param childFunction  定义需要从集合B元素中取出什么值来进行比对操作
     * @param <T>            集合A类型
     * @param <R>            集合B类型
     * @return A-B的元素
     */
    public static <T, R> List<T> getDifferenceSet(List<T> parentSet
            , List<R> childSet, Function<T, Object> parentFunction
            , Function<R, Object> childFunction) {
        //copy原数组
        List<T> copy = new LinkedList<>(parentSet);
        //清除交集
        copy.removeIf(t -> {
            for (R r : childSet) {
                if (parentFunction.apply(t).equals(childFunction.apply(r))) {
                    return true;
                }
            }

            return false;
        });

        //返回差集
        return copy;
    }

    /**
     * 根据条件过滤，返回过滤后的数组
     *
     * @param originList 原始数组
     * @param predicate  过滤条件
     * @param <T>        模板类型
     * @return 过滤后的list
     */
    public static <T> List<T> filter(List<T> originList, Predicate<T> predicate) {
        return originList.stream().filter(predicate).collect(Collectors.toList());
    }

}
