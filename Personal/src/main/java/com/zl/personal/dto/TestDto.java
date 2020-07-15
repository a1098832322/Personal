package com.zl.personal.dto;

import lombok.Data;

/**
 * 测试用DTO
 *
 * @author 郑龙
 * @date 2020/7/15 9:44
 */
@Data
public class TestDto {
    /**
     * 账号
     */
    private String userName;

    /**
     * 密码
     */
    private transient String password;

    /**
     * 组织单位编号
     */
    private String orgId;

    /**
     * 消息体
     */
    private String message;
}
