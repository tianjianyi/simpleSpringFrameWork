package org.yifeng.inject;

import org.yifeng.core.BeanContainer;
import org.yifeng.core.ClassSetHellper;
import org.yifeng.inject.annotation.Autowired;
import org.yifeng.util.ClassUtil;
import org.yifeng.util.ValidationUtil;

import java.lang.reflect.Field;
import java.util.Set;

public class DependencyInject {

    // bean容器
    private BeanContainer beanContainer;

    public DependencyInject() {
        beanContainer = BeanContainer.getInstance();
    }

//    ClassSetHellper classSetHellper = new ClassSetHellper();

    /**
     * 执行ioc
     */
    public void doIoc(){
        if (ValidationUtil.isEmpty(beanContainer.getClasses())){
            System.out.println("容器为空...");
            return;
        }
//         1.遍历Bean容器中所有Class对象
        for (Class<?> clazz : beanContainer.getClasses()){
//             2.遍历Class对象中所有成员变量
            Field[] files=clazz.getDeclaredFields();
            if (ValidationUtil.isEmpty(files)){
                continue;
            }

            for (Field field:files){
                // 3.找出被Autowired标记的成员变量
                if(field.isAnnotationPresent(Autowired.class)){
                    Autowired autowired=field.getAnnotation(Autowired.class);
                    String autowiredValue=autowired.value();

                    // 4.获取成员变量的类型
                    Class<?> fieldClass=field.getType();
                    // 5.获取成员变量的类型在容器对应的实例
                    Object fieldValue= getFieldInstance(fieldClass,autowiredValue);
                    if (fieldValue == null) {
                        throw new RuntimeException("没有找到对应实例："+fieldValue.toString());
                    }else {
                        // 6.通过反射将对应成员变量实例注入到成员变量所在类的实例里
                        Object targetBean=beanContainer.getBean(clazz);
                        // 1. 成员变量 2. 类实例 3. 成员变量的值 4.是否允许设置私有属性
                        ClassUtil.setField(field,targetBean,fieldValue,true);
                    }


                }
            }
        }
    }
    /**
     * 根据class在beanContainer里获取取其实例或实现类
     * @param fieldClass
     * @return
     */
    private Object getFieldInstance(Class<?> fieldClass,String autowiredValue){
        Object fieldValue=beanContainer.getBean(fieldClass);
        if (fieldValue!=null){
            return fieldValue;
        }else {
            Class<?> implementedClass=getImplementedClass(fieldClass,autowiredValue);
            if (implementedClass!=null){
                return beanContainer.getBean(implementedClass);
            }else {
                return null;
            }
        }
    }
    /**
     * 获取接口的实现类
     * @param fieldClass
     * @param autowiredValue
     * @return
     */
    private Class<?> getImplementedClass(Class<?> fieldClass,String autowiredValue){
        Set<Class<?>> classSet=beanContainer.getClassesBySuper(fieldClass);
        if (!ValidationUtil.isEmpty(classSet)){
            if (ValidationUtil.isEmpty(autowiredValue)){
                if (classSet.size()==1){
                    return classSet.iterator().next();
                }else {
                    throw new RuntimeException("接口存在多个实现类，您需要指定@Autowired(value) 具体类："+fieldClass.getName());
                }
            }else {
                for (Class<?> c:classSet){
                    if(autowiredValue.equals(c.getSimpleName())){
                        return c;
                    }
                }
            }
        }
        return null;
    }
}
