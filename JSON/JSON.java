package com.zl.json;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 和json相关的工具类
 *
 * @author 郑龙
 * @date 2019/4/11 9:14
 */
public class JSON extends com.alibaba.fastjson.JSON {
    /**
     * json对象类型
     */
    public static final int TYPE_OBJECT = 0;

    /**
     * json数组类型
     */
    public static final int TYPE_ARRAY = 1;

    /**
     * json object
     */
    private JSONObject jsonObject;

    /**
     * json array
     */
    private JSONArray jsonArray;

    private JSON(JSONObject jsonObject, int type) {
        this.jsonObject = Objects.requireNonNull(jsonObject);
        this.currentType = type;
    }

    private JSON(JSONArray jsonArray, int type) {
        this.jsonArray = Objects.requireNonNull(jsonArray);
        this.currentType = type;
    }

    /**
     * json有效标记
     */
    private boolean isValid = false;

    /**
     * 类型标记
     */
    private int currentType = -1;

    /********************************
     *           static             *
     ********************************/

    /**
     * 将json对象转换为实体对象
     *
     * @param object json对象
     * @param clazz  对象类型
     * @param <T>
     * @return 实体对象
     */
    public static <T> T parseObject(JSONObject object, Class<T> clazz) {
        return JSONObject.parseObject(object.toJSONString(), clazz);
    }

    /**
     * 将json数组转换为对象list
     *
     * @param array json数组
     * @param clazz 对象类型
     * @param <T>
     * @return 对象list
     */
    public static <T> List<T> parseArray(JSONArray array, Class<T> clazz) {
        return JSONArray.parseArray(array.toJSONString(), clazz);
    }


    /********************************
     *           Object             *
     ********************************/

    /**
     * 从String字符串初始化json
     *
     * @param jsonStr json字符串
     * @param type    标记标明是array还是object
     * @return
     */
    public static JSON from(String jsonStr, int type) {
        switch (type) {
            case TYPE_OBJECT:
                return new JSON(JSON.parseObject(jsonStr), type);
            case TYPE_ARRAY:
                return new JSON(JSON.parseArray(jsonStr), type);
            default:
                throw new IllegalArgumentException("类型参数异常！");
        }

    }

    /**
     * 判断json是否有效(默认是无效的)
     *
     * @param predicate 有效性判断
     */
    public JSON isPresent(Predicate predicate) {
        if (Objects.nonNull(predicate)) {
            switch (currentType) {
                case TYPE_OBJECT:
                    isValid = predicate.test(jsonObject);
                    break;
                case TYPE_ARRAY:
                    isValid = predicate.test(jsonArray);
                    break;
                default:
                    break;
            }

        }
        return this;
    }

    /**
     * 如果json有效则执行此方法
     *
     * @param consumer
     */
    public JSON ifPresent(Consumer consumer) {
        if (isValid) {
            switch (currentType) {
                case TYPE_OBJECT:
                    consumer.accept(jsonObject);
                    break;
                case TYPE_ARRAY:
                    consumer.accept(jsonArray);
                    break;
                default:
                    break;
            }

        }
        return this;
    }

    /**
     * 如果json无效则执行此方法
     *
     * @param consumer
     */
    public void orElse(Consumer consumer) {
        if (!isValid) {
            switch (currentType) {
                case TYPE_OBJECT:
                    consumer.accept(jsonObject);
                    break;
                case TYPE_ARRAY:
                    consumer.accept(jsonArray);
                    break;
                default:
                    break;
            }
        }
    }

}
