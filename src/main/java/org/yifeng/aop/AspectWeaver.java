package org.yifeng.aop;



import org.yifeng.aop.annotation.Aspect;
import org.yifeng.aop.annotation.Order;
import org.yifeng.aop.aspect.AspectInfo;
import org.yifeng.aop.aspect.DefaultAspect;
import org.yifeng.core.BeanContainer;
import org.yifeng.util.ValidationUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Proxy;
import java.util.*;

public class AspectWeaver {

    private BeanContainer beanContainer;
    public AspectWeaver(){
        this.beanContainer=BeanContainer.getInstance();
    }

    public void doAop(){
        // 1.获取所有的切面类
        Set<Class<?>> aspectSet= beanContainer.getClassesByAnnotation(Aspect.class);
        if (ValidationUtil.isEmpty(aspectSet)){
            return;
        }
        // 拼装AspectInfoList
        List<AspectInfo> aspectInfoList=packAspectInfoList(aspectSet);

        // 遍历容器里类
        Set<Class<?>> classSet=beanContainer.getClasses();
        for (Class<?> targetClass:classSet){
            if (targetClass.isAnnotationPresent(Aspect.class)){
                continue;
            }
            // 粗筛符合条件的Aspect
            List<AspectInfo> roughMatchedAspectList=collectRoughMatchedAspectListForSpecificClass(aspectInfoList,targetClass);

            // 尝试进行Aspect的织入
            wrapIfNecessary(roughMatchedAspectList,targetClass);
        }

    }

    private void wrapIfNecessary(List<AspectInfo> roughMatchedAspectList, Class<?> targetClass) {
        if (ValidationUtil.isEmpty(roughMatchedAspectList)){
            return;
        }
        // 创建动态代理对象
        AspectListExecutor aspectListExecutor=new AspectListExecutor(targetClass,roughMatchedAspectList);
        Object proxyBean=ProxyCreator.createProxy(targetClass,aspectListExecutor);
        beanContainer.addBean(targetClass,proxyBean);
    }
//
    private List<AspectInfo> collectRoughMatchedAspectListForSpecificClass(List<AspectInfo> aspectInfoList, Class<?> targetClass) {
        List<AspectInfo> roughMatchedAspectList=new ArrayList<>();
        for (AspectInfo aspectInfo:aspectInfoList){
            if(aspectInfo.getPointcutLocator().roughMatches(targetClass)){
                roughMatchedAspectList.add(aspectInfo);
            }
        }
        return roughMatchedAspectList;
    }

    private List<AspectInfo> packAspectInfoList(Set<Class<?>> aspectSet) {
        List<AspectInfo> aspectInfoList=new ArrayList<>();

        for (Class<?> aspectClass:aspectSet){
            if (verifyAspect(aspectClass)){
                Order orderTag=aspectClass.getAnnotation(Order.class);
                Aspect aspectTag=aspectClass.getAnnotation(Aspect.class);
                DefaultAspect defaultAspect= (DefaultAspect) beanContainer.getBean(aspectClass);

                // 初始化表达式定位器
                PointcutLocator pointcutLocator=new PointcutLocator(aspectTag.pointcut());

                AspectInfo aspectInfo=new AspectInfo(orderTag.value(),defaultAspect,pointcutLocator);
                aspectInfoList.add(aspectInfo);

            }else {
                throw new RuntimeException("框架中一定要遵循给Aspect类添加@Aspect和@Order标签的规范，同时，必须继承自DefaultAspect.class");
            }
        }
        return aspectInfoList;
    }

//    private void weaveByCategory(Class<? extends Annotation> category, List<AspectInfo> aspectInfoList) {
//        // 1.获取被代理类的集合
//        Set<Class<?>> classSet=beanContainer.getClassesByAnnotation(category);
//        if(ValidationUtil.isEmpty(classSet)){
//            return;
//        }
//        // 2.遍历被代理类，分别为每个被代理类生成动态代理实例
//        for (Class<?> targetClass:classSet){
//            // 创建动态代理对象
//            AspectListExecutor aspectListExecutor=new AspectListExecutor(targetClass,aspectInfoList);
//            Object proxyBean=ProxyCreator.createProxy(targetClass,aspectListExecutor);
//            // 3.将动态代理对象实例添加到容器里，取代未代理前的类实例
//            beanContainer.addBean(targetClass,proxyBean);
//
//        }
//    }
//
//
//
    /**
     * 框架中一定要遵循给Aspect类添加@Aspect和@Order标签的规范，同时，必须继承自DefaultAspect.class 且不能是它本身
     * @param aspectClass
     * @return
     */
    private boolean verifyAspect(Class<?> aspectClass) {
        return aspectClass.isAnnotationPresent(Aspect.class)&&
                aspectClass.isAnnotationPresent(Order.class)&&
                DefaultAspect.class.isAssignableFrom(aspectClass);
//                && aspectClass.getAnnotation(Aspect.class).value() != Aspect.class;
    }
}
