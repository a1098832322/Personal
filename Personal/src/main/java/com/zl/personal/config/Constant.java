package com.zl.personal.config;

/**
 * 全局常量池
 *
 * @author 郑龙
 * @date 2020/4/17 13:03
 */
public class Constant {
    /**
     * redis中key过期时间（单位：秒，默认过期时间为30分钟）
     */
    public static final long REDIS_KEY_EXPIRED_SECOND = 60 * 30L;

    /**
     * AES密钥
     */
    public static final String AES_KEY = "3c56618748254dd4be6db5d9b7f9D23a";

}
