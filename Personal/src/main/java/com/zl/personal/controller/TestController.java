package com.zl.personal.controller;

import com.zl.personal.dto.TestDto;
import com.zl.personal.utils.support.AutoBoxingUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 一些Controller层的测试方法
 *
 * @author 郑龙
 * @date 2020/7/15 9:15
 */
@Api(tags = "一些Controller层的测试方法")
@RestController
@RequestMapping("/test")
public class TestController {
    /**
     * 将{@link HttpServletRequest}中的请求参数装箱对象测试
     *
     * @param request {@link HttpServletRequest}
     * @return 自动装箱对象
     */
    @ApiOperation(value = "将HttpServletRequest中的请求参数装箱对象测试", produces = "application/json")
    @GetMapping("/getDto")
    public TestDto getDto(HttpServletRequest request) throws IllegalAccessException {
        return AutoBoxingUtil.getRequestParameters(request, new TestDto());
    }

    /**
     * 将{@link HttpServletRequest}中的请求参数装箱对象测试
     *
     * @param request {@link HttpServletRequest}
     * @return 自动装箱对象
     */
    @ApiOperation(value = "将HttpServletRequest中的请求参数装箱对象测试", produces = "application/json")
    @GetMapping("/getDtoByDtoClass")
    public TestDto getDtoByDtoClass(HttpServletRequest request) throws IllegalAccessException, InstantiationException {
        return AutoBoxingUtil.getRequestParameters(request, TestDto.class);
    }
}
