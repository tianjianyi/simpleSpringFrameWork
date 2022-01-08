package org.yifeng.util;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;


public class ClassUtil {

    public static final String FILE_PROTOCOL = "file";


    /**
     * 实例化class
     * @param clazz class类型
     * @param accessible 是否创建出私有class对象的实例
     * @param <T>
     * @return
     */
    public static <T> T newInstance(Class<?> clazz,boolean accessible){
        try {
            Constructor constructor=clazz.getDeclaredConstructor();
            constructor.setAccessible(accessible);
            return (T) constructor.newInstance();
        } catch (Exception e) {
//            log.warn("创建实例失败：{}",e.getMessage());
            throw new RuntimeException(e);
        }
    }


    /**
     * 提取包下类集合
     *
     * @param packageName 包名
     * @return
     */
    public static Set<Class<?>> extractPackageClass(String packageName) {

        // 1.获取类加载器
        ClassLoader classLoader = getClassLoader();
        // 2.通过类加载器获取到加载的资源
        URL url = classLoader.getResource(packageName.replace(".", "/"));
        if (url == null) {
            System.out.println("找不到相应包： " + packageName);
//            log.warn("找不到相应包：{}", packageName);
            return null;
        }

        // 根据不同资源类型，采用不同的方式获取资源集合
        Set<Class<?>> classSet = null;
        if (url.getProtocol().equalsIgnoreCase(FILE_PROTOCOL)) {
            classSet = new HashSet<Class<?>>();
            File packageDirectory = new File(url.getPath());
            extractClassFile(classSet, packageDirectory, packageName);

        }
        return classSet;
    }


    /**
     * 获取ClassLoader
     *
     * @return
     */
    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }


    public static void main(String[] args) {
        extractPackageClass("cn.airfei.controller");
    }


    /**
     * 获取class对象
     * @param className 包名+类名
     * @return
     */
    public static Class<?> loadClass(String className){
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
//            log.error("load class error:{}",e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 设置类的属性值
     * @param field 属性（成员变量）
     * @param target 目标类实例
     * @param value  成员变量的值
     * @param accessible  是否对私有变量赋值
     */
    public static  void setField(Field field,Object target,Object value,boolean accessible){
        field.setAccessible(accessible);
        try {
            field.set(target,value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 递归获取目标package里面所有class文件
     *
     * @param emptyClassSet
     * @param fileSource
     * @param packageName
     */
    private static void extractClassFile(final Set<Class<?>> emptyClassSet, File fileSource, final String packageName) {
        if (!fileSource.isDirectory()) {
            return;
        }
        File[] files = fileSource.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return true;
                } else {
                    // 获取文件的绝对路径
                    String absoluteFilePath = file.getAbsolutePath();
                    if (absoluteFilePath.endsWith(".class")){
                        addToClassSet(absoluteFilePath);
                    }
                }
                return false;
            }
            // 根据class的绝对路径获取并生成class对象，并放入classSet中
            private void addToClassSet(String absoluteFilePath) {
                absoluteFilePath = absoluteFilePath.replace(File.separator,".");
                String className=absoluteFilePath.substring(absoluteFilePath.indexOf(packageName));
                className=className.substring(0,className.lastIndexOf("."));

                // 通过反射机制获取对应的class对象并加入到classSet中
                Class targetClass=loadClass(className);
                emptyClassSet.add(targetClass);
            }

        });



        if (files!=null){
            for (File file:files){
                extractClassFile(emptyClassSet,file,packageName);
            }
        }


    }


}
