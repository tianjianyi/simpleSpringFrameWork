package org.yifeng.aop;

import lombok.Getter;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.yifeng.aop.aspect.AspectInfo;
import org.yifeng.util.ValidationUtil;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class AspectListExecutor implements MethodInterceptor {
    private Class<?> targetClass;

    @Getter
    private List<AspectInfo> sortedAspectInfoList;

    public AspectListExecutor(Class<?> targetClass, List<AspectInfo> aspectInfoList) {
        this.targetClass = targetClass;
        this.sortedAspectInfoList = sortAspectInfoList(aspectInfoList);
    }
    /**
     * 升序排序
     * @param aspectInfoList
     * @return
     */
    private List<AspectInfo> sortAspectInfoList(List<AspectInfo> aspectInfoList) {
        Collections.sort(aspectInfoList, new Comparator<AspectInfo>() {
            @Override
            public int compare(AspectInfo o1, AspectInfo o2) {
                return o1.getOrderIndex()-o2.getOrderIndex();
            }
        });
        return aspectInfoList;
    }
    @Override
    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        Object returnValue=null;
        collectAccurateMatchedAspectList(method);
        if (ValidationUtil.isEmpty(sortedAspectInfoList)){
            // 2.执行被代理类的方法
            returnValue=methodProxy.invokeSuper(proxy,args);
            return returnValue;
        }
        // 1.按照order的顺序升序执行完所有aspect的before方法
        invokeBeforeAdvices(method,args);
        try {
            // 2.执行被代理类的方法
            returnValue=methodProxy.invokeSuper(proxy,args);
            // 3.如果被代理类方法正常返回，这按照order的顺序降序执行完所有Aspect的afterReturning方法
            returnValue=invokeAfterReturningAdvices(method,args,returnValue);
        }catch (Exception e){
            // 4.如果被代理类方法抛出异常，则按照order的顺序降序执行完所有Aspect的afterThrowing方法
            invokeAfterThrowingAdvices(method,args,e);
        }
        return returnValue;
    }
    // 1.按照order的顺序升序执行完所有aspect的before方法
    private void invokeBeforeAdvices(Method method, Object[] args) throws Throwable {
        for (AspectInfo aspectInfo:sortedAspectInfoList){
            aspectInfo.getDefaultAspect().before(targetClass,method,args);
        }
    }
    // 3.如果被代理类方法正常返回，这按照order的顺序降序执行完所有Aspect的afterReturning方法
    private Object invokeAfterReturningAdvices(Method method, Object[] args, Object returnValue) throws Throwable {
        Object result=null;

        for (int i=sortedAspectInfoList.size()-1;i>=0;i--){
            result=sortedAspectInfoList.get(i).getDefaultAspect().afterReturning(targetClass,method,args,returnValue);
        }
        return result;
    }

    // 4.如果被代理类方法抛出异常，则按照order的顺序降序执行完所有Aspect的afterThrowing方法
    private void invokeAfterThrowingAdvices(Method method, Object[] args, Exception e) throws Throwable {
        for (int i=sortedAspectInfoList.size()-1;i>=0;i--){
            sortedAspectInfoList.get(i).getDefaultAspect().afterThrowing(targetClass,method,args,e);
        }
    }

    private void collectAccurateMatchedAspectList(Method method) {
        if (ValidationUtil.isEmpty(sortedAspectInfoList)){
            return;
        }
        Iterator<AspectInfo> it=sortedAspectInfoList.iterator();
        while (it.hasNext()){
            AspectInfo aspectInfo=it.next();
            if(!aspectInfo.getPointcutLocator().accurateMatches(method)){
                it.remove();
            }
        }
    }

}
