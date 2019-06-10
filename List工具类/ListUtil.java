package com.dsw.aop.utils;


import org.apache.commons.collections4.CollectionUtils;

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

    /**
     * 排序，可以使目标item固定在列首或列尾
     *
     * @param originList 原始数据列
     * @param fixedItem  固定的item
     * @param comparator 自定义比较器
     * @param isStart    固定元素是否在开头
     * @param <T>        模板类型
     * @return 排序后的list
     */
    public static <T> List<T> sort(List<T> originList, List<T> fixedItem
            , Comparator<T> comparator, boolean isStart) {
        //校验原始数据列
        ValidateUtil.validateParams(new BaseChainExecutor(), originList);

        //声明返回List
        List<T> newArray = new ArrayList<>();
        //对原始数据集进行copy
        List<T> originListCopy = new LinkedList<>(originList);

        //先从原始列表中移除
        if (CollectionUtils.isNotEmpty(fixedItem)) {
            originListCopy.removeIf(fixedItem::contains);
        }

        //排序并返回
        if (isStart) {
            //将固定元素排到列前
            if (CollectionUtils.isNotEmpty(fixedItem)) {
                newArray.addAll(fixedItem);
            }
            //后续元素默认升序排列
            newArray.addAll(originListCopy.stream().sorted(comparator).collect(Collectors.toList()));
        } else {
            //后续元素默认升序排列
            newArray.addAll(originListCopy.stream().sorted(comparator).collect(Collectors.toList()));
            //将固定元素排到列尾
            if (CollectionUtils.isNotEmpty(fixedItem)) {
                newArray.addAll(fixedItem);
            }
        }
        return newArray;
    }

    /**
     * 根据一定条件，拆分List符合裂项条件的元素，并扩充list
     *
     * @param originList 原始数据集
     * @param predicate  裂项条件
     * @param function   对符合列项条件的元素进行操作的方法
     * @param <T>        模板类型
     * @return 裂项并扩容后的list
     */
    public static <T, R> List<T> split(List<T> originList, Predicate<T> predicate
            , Function<T, R> function) {
        //拷贝一份List进行遍历
        List<T> originListCopy = new LinkedList<>(originList);

        //遍历
        originListCopy.forEach(t -> {
            if (predicate.test(t)) {
                //从原始列表中remove掉原始元素
                originList.remove(t);
                function.apply(t);

            }
        });

        //返回处理后的List
        return originList;
    }

}
