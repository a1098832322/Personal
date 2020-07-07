package com.zl.personal.aopwatcher;

import com.zl.personal.annotations.Authentication;
import com.zl.personal.utils.AESUtil;
import com.zl.personal.utils.PermissionException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Field;

/**
 * 一个基于AOP的token验证注解监视器
 *
 * @author 郑龙
 * @date 2020/7/6 17:27
 */
@Aspect
@Slf4j
public class AuthenticationWatcher {
    /**
     * 需要认证的字段名
     */
    public static final String FIELD_NAME = "token";

    /**
     * AOP切入点为{@link Authentication}注解
     */
    @Pointcut("@annotation(com.zl.personal.annotations.Authentication)")
    public void pointCut() {

    }

    /**
     * 前置切点方法
     *
     * @param joinPoint 切入点
     */
    @Before("pointCut()")
    public void before(JoinPoint joinPoint) throws IllegalAccessException {
        //权限认证状态
        boolean flag = false;

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Authentication authentication = methodSignature.getMethod().getAnnotation(Authentication.class);
        int tokenIndex = authentication.index();
        boolean isReflect = authentication.reflect();

        Object param = joinPoint.getArgs()[tokenIndex];


        //判断是否需要反射解析取值
        if (isReflect) {
            for (Field f : param.getClass().getDeclaredFields()) {
                if (FIELD_NAME.equals(f.getName())) {
                    try {
                        f.setAccessible(true);
                        //尝试取值
                        String token = AESUtil.aESDecode((String) f.get(param));
                        //log
                        log.debug(f.get(param) + " ----> " + token);

                        // TODO 通过权限服务使用token反查用户身份
                       // flag = userServices.validate(token);

                    } catch (Exception e) {
                        log.error("读取字段token值失败！\n传入token参数：" + f.get(param) + "\n异常原因：", e);
                    } finally {
                        f.setAccessible(false);
                    }

                    break;
                }
            }
        } else {
            //不需要反射取值
            try {
                //尝试取值
                String token = AESUtil.aESDecode((String) param);
                //log
                log.debug(param + " ----> " + token);
                // TODO 通过权限服务使用token反查用户身份
                // flag = userServices.validate(token);
            } catch (Exception e) {
                log.error("AOP反射读取字段token值失败！\n传入token参数：" + param + "\n异常原因：", e);
            }
        }

        if (!flag) {
            throw new PermissionException("token认证不通过！需要重新登录！");
        } 
    }
}
