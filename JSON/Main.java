package com.zl;

import com.alibaba.fastjson.JSONObject;
import com.zl.json.JSON;

/**
 * 测试主类
 *
 * @author 郑龙
 * @date 2019年4月11日08:49:03
 */
public class Main {

    public static void main(String[] args) {
        String jsonStr = "{\"flag\":true,\"code\":0,\"message\":null,\"data\":{\"currentPage\":1,\"pageSize\":10,\"total\":52,\"offset\":0,\"lists\":[{\"id\":1,\"commodityName\":\"农夫山泉\",\"commodityType\":2,\"commodityPrice\":2,\"commodityTotal\":999999,\"commoditySurplus\":999981,\"isDeleted\":\"N\",\"commodityInfo\":\"农夫山泉有点甜\",\"commodityOtherImgUrls\":\"http://wishes-blog.cn:9002/commodity/%E5%86%9C%E5%A4%AB%E5%B1%B1%E6%B3%89.jpg\"},{\"id\":2,\"commodityName\":\"奥利奥\",\"commodityType\":1,\"commodityPrice\":3.52,\"commodityTotal\":999999,\"commoditySurplus\":999982,\"isDeleted\":\"N\",\"commodityInfo\":\"奥利奥饼干\",\"commodityOtherImgUrls\":\"http://wishes-blog.cn:9002/commodity/%E5%A5%A5%E5%88%A9%E5%A5%A5.jpg\"}]}}";
        String jsonArray = "[{\"id\":1,\"commodityName\":\"农夫山泉\",\"commodityType\":2,\"commodityPrice\":2,\"commodityTotal\":999999,\"commoditySurplus\":999981,\"isDeleted\":\"N\",\"commodityInfo\":\"农夫山泉有点甜\",\"commodityOtherImgUrls\":\"http://wishes-blog.cn:9002/commodity/%E5%86%9C%E5%A4%AB%E5%B1%B1%E6%B3%89.jpg\"},{\"id\":2,\"commodityName\":\"奥利奥\",\"commodityType\":1,\"commodityPrice\":3.52,\"commodityTotal\":999999,\"commoditySurplus\":999982,\"isDeleted\":\"N\",\"commodityInfo\":\"奥利奥饼干\",\"commodityOtherImgUrls\":\"http://wishes-blog.cn:9002/commodity/%E5%A5%A5%E5%88%A9%E5%A5%A5.jpg\"}]";

        JSON.from(jsonStr, JSON.TYPE_OBJECT).isPresent(object -> ((JSONObject) object).getBoolean("flag"))
                .ifPresent(object -> System.out.println(((JSONObject) object).getJSONObject("data").toJSONString()))
                .orElse(object -> System.out.println("无效"));

        JSON.from(jsonArray, JSON.TYPE_ARRAY).isPresent(o -> true)
                .ifPresent(o -> System.out.println(o.toString()))
                .orElse(o -> System.out.println("无效"));
    }
}
