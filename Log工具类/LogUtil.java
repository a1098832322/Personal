package com.dsw.aop.utils;

import com.alibaba.fastjson.JSON;
import com.dsw.aop.config.annotation.PrintLog;
import com.dsw.aop.dto.PrivilegeInfo;
import com.dsw.aop.service.CurrentUserService;
import com.dsw.aop.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * log输出配置,使用@Aspect使记录日志的功能面向切面，
 * 记录Controller的输入与输出参数以及将日志写入数据库
 *
 * @author 郑龙
 * @date 2019/4/30 11:35
 */
@Slf4j
@Aspect
@Component
public class LogUtil {
    /**
     * 写入日志表中的内容正文最大长度限制
     */
    private static final int MAX_CONTENT_LENGTH = 850;

    @Autowired
    private LogService logService;

    @Autowired
    private CurrentUserService currentUserService;

    /**
     * 需要log的横切点
     */
    @Pointcut(value = "@annotation(com.dsw.aop.config.annotation.PrintLog)")
    private void saveLogPointCut() {

    }

    /**
     * 普通Controller请求参数输出
     *
     * @param joinPoint
     */
    @Before("within(com.dsw.aop.controller.*)")
    public void before(JoinPoint joinPoint) {
        String[] args = toStringArray(joinPoint.getArgs());
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        //获取形参名
        DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
        String[] parameterNames = discoverer.getParameterNames(method);

        //拼接字符串
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < parameterNames.length; i++) {
            result.append(parameterNames[i]).append(" = ").append(args[i]).append(";\t");
        }

        log.info(method.getDeclaringClass().getName() + "." + method.getName()
                + ": 请求参数：" + result.toString());

    }

    /**
     * 普通Controller返回值输出
     *
     * @param joinPoint
     */
    @AfterReturning(value = "within(com.dsw.aop.controller.*)", returning = "result")
    public void after(JoinPoint joinPoint, Object result) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        log.info(method.getDeclaringClass().getName() + "." + method.getName()
                + ": 返回值：" + JSON.toJSONString(result));
    }

    /**
     * 对所有用@PrintLog标注的方法进行写日志操作
     *
     * @param joinPoint
     * @param result
     */
    @AfterReturning(value = "saveLogPointCut()", returning = "result")
    public void saveLog(JoinPoint joinPoint, Object result) {
        String[] args = toStringArray(joinPoint.getArgs());
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        //获取形参名
        DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
        String[] parameterNames = discoverer.getParameterNames(method);

        //拼接字符串
        StringBuilder resultStr = new StringBuilder();
        for (int i = 0; i < parameterNames.length; i++) {
            resultStr.append(parameterNames[i]).append(" = ").append(args[i]).append(";   ");
        }

        //拿到方法注解
        PrintLog printLog = method.getAnnotation(PrintLog.class);
        if (printLog != null && printLog.required()) {
            //将数据写入数据库
            try {
                //记录到数据库Log表
                PrivilegeInfo privilegeInfo = currentUserService.getPvgInfo();
                //result类型判断与转换
                result = classCovert(result);
                //转换JSON方便记录结果
                String returnedStr = JSON.toJSONString(result);
                //记录log
                if (returnedStr.length() > MAX_CONTENT_LENGTH) {
                    //如果JSON长度超过上限，则只取flag和message
                    if (((ResultUtil) result).isFlag()) {
                        returnedStr = JSON.toJSONString(ResultUtil.success(((ResultUtil) result).getMessage()));
                    } else {
                        returnedStr = JSON.toJSONString(ResultUtil.fail(((ResultUtil) result).getMessage()));
                    }

                }

                logService.log(privilegeInfo.getUserId()
                        , printLog.module()
                        , privilegeInfo.getName() + "执行操作："
                                + printLog.content()
                                + "   参数：[" + (resultStr.toString().length() > 100
                                ? resultStr.toString().substring(0, 100) + "..."
                                : resultStr.toString()) + "]"
                                + "   结果：[" + returnedStr + "]");
            } catch (Exception e) {
                //预留，对于日志异常可以进行一定的判断或处理
                throw e;
            }
        }


    }

    /**
     * 将任意输入参数转为String数组输出
     *
     * @param objects 传入参数
     * @return String[]
     */
    private String[] toStringArray(Object[] objects) {
        List<String> array = new ArrayList<>();
        for (Object obj : objects) {
            if (obj instanceof String) {
                array.add(obj.toString());
            } else {
                array.add(obj + "");
            }
        }

        return array.toArray(new String[0]);
    }

    /**
     * 结果类型转换
     *
     * @param result 方法返回值
     * @return 将返回值转换成ResultUtil类型并进行记录
     */
    private ResultUtil classCovert(Object result) {
        if (result instanceof ResultUtil) {
            return (ResultUtil) result;
        } else {
            return ResultUtil.success("成功！", result);
        }
    }
}
