package org.yifeng.core;

import org.yifeng.util.ValidationUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

public class ClassSetHellper {

    // bean容器
    private BeanContainer beanContainer;

    public ClassSetHellper() {
        beanContainer = BeanContainer.getInstance();
    }
    /**
     * 根据注释筛选bean的class集合
     *
     * @param annotation
     * @return
     */
//    public Set<Class<?>> getClassesByAnnotation(Class<? extends Annotation> annotation) {
////        Set<Class<?>> keySet = beanContainer.getClasses();
////        if (ValidationUtil.isEmpty(keySet)) {
////            System.out.println("没有任何class对象");
////            return null;
////        }
////        Set<Class<?>> classSet = new HashSet<>();
////        for (Class<?> c : keySet) {
////            if (c.isAnnotationPresent(annotation)) {
////                classSet.add(c);
////            }
////        }
////        return classSet.size() > 0 ? classSet : null;
//    }
//    /**
//     * 通过接口或父类或获取实现类或子类的集合，不包括其本身
//     *
//     * @param interfaceOrClass
//     * @return
//     */
//    public Set<Class<?>> getClassesBySuper(Class<?> interfaceOrClass) {
////        Set<Class<?>> keySet = beanContainer.getClasses();
////        if (ValidationUtil.isEmpty(keySet)) {
//////            log.warn("没有任何class对象");
////            return null;
////        }
////
////        Set<Class<?>> classSet = new HashSet<>();
////        for (Class<?> c : keySet) {
////            // 判断keySet里的元素是否是传入的接口或类的子类
////            if (interfaceOrClass.isAssignableFrom(c) && !c.equals(interfaceOrClass)) {
////                classSet.add(c);
////            }
////        }
////        return classSet.size() > 0 ? classSet : null;
//    }

}
