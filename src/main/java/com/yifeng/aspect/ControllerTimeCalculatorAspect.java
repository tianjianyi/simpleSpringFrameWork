package com.yifeng.aspect;



import org.yifeng.aop.annotation.Aspect;
import org.yifeng.aop.annotation.Order;
import org.yifeng.aop.aspect.DefaultAspect;
import org.yifeng.core.annotation.Controller;

import java.lang.reflect.Method;

@Aspect(pointcut = "within(com.yifeng.controller.superadmin.*)")
@Order(0)
public class ControllerTimeCalculatorAspect extends DefaultAspect {
    private Long timestampCache;


    @Override
    public void before(Class<?> targetClass, Method method, Object[] args) throws Throwable {
        System.out.println("开始计时，执行的类是：{}，执行的方法是：{}，参数是：{}"+targetClass.getName()+method+args);
        timestampCache=System.currentTimeMillis();
    }

    @Override
    public Object afterReturning(Class<?> targetClass, Method method, Object[] args, Object returnValue) throws Throwable {
        long endTime=System.currentTimeMillis();

        long costTime=endTime-timestampCache;
        System.out.println("开始计时，执行的类是：{}，执行的方法是：{}，参数是：{}，执行时间是：{}"+targetClass.getName()+method+args+"秒：  "+costTime);

        return returnValue;
    }
}
