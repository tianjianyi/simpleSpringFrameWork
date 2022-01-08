package com.yifeng.aspect;


import org.yifeng.aop.annotation.Aspect;
import org.yifeng.aop.annotation.Order;
import org.yifeng.aop.aspect.DefaultAspect;
import org.yifeng.core.annotation.Controller;

import java.lang.reflect.Method;

//@Slf4j
@Aspect(pointcut = "within(com.yifeng.controller.superadmin.*)")
@Order(10)
public class ControllerInfoRecordAspect extends DefaultAspect {
    @Override
    public void before(Class<?> targetClass, Method method, Object[] args) throws Throwable {
//        log.info("开始计时，执行的类是：{}，执行的方法是：{}，参数是：{}",targetClass.getName(),method,args);
        System.out.println("qian");
        super.before(targetClass, method, args);
    }

    @Override
    public Object afterReturning(Class<?> targetClass, Method method, Object[] args, Object returnValue) throws Throwable {
//        log.info("开始计时，执行的类是：{}，执行的方法是：{}，参数是：{}",targetClass.getName(),method,args);
        System.out.println("返会");
        return super.afterReturning(targetClass, method, args, returnValue);
    }

    @Override
    public void afterThrowing(Class<?> targetClass, Method method, Object[] args, Throwable e) throws Throwable {
//        log.info("开始计时，执行的类是：{}，执行的方法是：{}，参数是：{}",targetClass.getName(),method,args);
        System.out.println("异常");
        super.afterThrowing(targetClass, method, args, e);
    }
}
