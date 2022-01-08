package org.yifeng.mvc.processor.impl;


import org.yifeng.core.BeanContainer;
import org.yifeng.mvc.annotation.RequestMapping;
import org.yifeng.mvc.annotation.RequestParam;
import org.yifeng.mvc.annotation.ResponseBody;
import org.yifeng.mvc.processor.RequestProcessor;
import org.yifeng.mvc.processor.RequestProcessorChain;
import org.yifeng.mvc.render.ResultRender;
import org.yifeng.mvc.render.impl.JsonResultRender;
import org.yifeng.mvc.render.impl.ResourceNotFoundResultRender;
import org.yifeng.mvc.render.impl.ViewResultRender;
import org.yifeng.mvc.type.ControllerMethod;
import org.yifeng.mvc.type.RequestPathInfo;
import org.yifeng.util.ConverterUtil;
import org.yifeng.util.ValidationUtil;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 将请求转发给对应的Controller进行处理
**/
public class ControllerRequestProcessor implements RequestProcessor {
    //IOC容器
    private BeanContainer beanContainer;
    //请求和Controller的映射
    private Map<RequestPathInfo, ControllerMethod> pathControllerMethodMap = new ConcurrentHashMap<>();

    /**
     * 依靠容器，建立起请求路径、请求方法与Controller方法实例的映射
     */
    public ControllerRequestProcessor() {
        this.beanContainer = BeanContainer.getInstance();
        // 获取被@RequestMapping标记的Controller类
        Set<Class<?>> requestMappingSet = beanContainer.getClassesByAnnotation(RequestMapping.class);
        // 建立映射
        initPathControllerMethodMap(requestMappingSet);
    }

    /**
     * 建立起请求路径、请求方法与Controller方法实例的映射
     *
     * @param requestMappingSet 需要被转发的Controller
     */
    private void initPathControllerMethodMap(Set<Class<?>> requestMappingSet) {
        if (ValidationUtil.isEmpty(requestMappingSet)) {//如果没有类被@RequestMapping标记
            return;
        }
        //1.遍历所有被@RequestMapping标记的类，获取类上面该注解的属性值作为一级路径
        for (Class<?> requestMappingClass : requestMappingSet) {
            //获取@RequestMapping注解
            RequestMapping requestMapping = requestMappingClass.getAnnotation(RequestMapping.class);
            //获取注解值
            String basePath = requestMapping.value();
            if (!basePath.startsWith("/")) {//如果不是以“/”开头，为了方便处理，加上“/”
                basePath = "/" + basePath;
            }
            //2.遍历类里所有被@RequestMapping标记的方法，获取方法上面该注解的属性值，作为二级路径
            Method[] methods = requestMappingClass.getDeclaredMethods();
            if (ValidationUtil.isEmpty(methods)) {
                continue;
            }
            for (Method method : methods) {
                if (method.isAnnotationPresent(RequestMapping.class)) {//获取方法被@RequestMapping标记的
                    //获取@RequestMapping注解
                    RequestMapping methodRequest = method.getAnnotation(RequestMapping.class);
                    //获取注解值
                    String methodPath = methodRequest.value();
                    if (!methodPath.startsWith("/")) {//如果不是以“/”开头，为了方便处理，加上“/”
                        methodPath = "/" + methodPath;
                    }
                    //拼接一级和二级路径路径
                    String url = basePath + methodPath;
                    //3.解析方法里被@RequestParam标记的参数，
                    // 获取该注解的属性值，作为参数名，
                    // 获取被标记的参数的数据类型，建立参数名和参数类型的映射
                    Map<String, Class<?>> methodParams = new HashMap<>();
                    //获取方法的参数
                    Parameter[] parameters = method.getParameters();
                    if (!ValidationUtil.isEmpty(parameters)) {
                        for (Parameter parameter : parameters) {
                            //获取方法参数上的注解属性
                            RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
                            //目前暂定为Controller方法里面所有的参数都需要@RequestParam注解
                            if (requestParam == null) {//如果方法参数没有注解，则暂时报错
                                throw new RuntimeException("The parameter must have @RequestParam");
                            }
                            methodParams.put(requestParam.value(), parameter.getType());
                        }
                    }
                    //4.将获取到的信息封装成RequestPathInfo实例和ControllerMethod实例，放置到映射表里
                    String httpMethod = String.valueOf(methodRequest.method());//获取注解中所要求的请求方法
                    RequestPathInfo requestPathInfo = new RequestPathInfo(httpMethod, url);
                    //如果出现了重复的路径
                    if (this.pathControllerMethodMap.containsKey(requestPathInfo)) {
                        System.out.println("duplicate url:"+requestPathInfo.getHttpPath()+
                                "registration，current class" + requestMappingClass.getName()+
                                "method"+method.getName()+" will override the former one");
                    }
                    ControllerMethod controllerMethod = new ControllerMethod(requestMappingClass, method, methodParams);
                    this.pathControllerMethodMap.put(requestPathInfo, controllerMethod);
                }
            }
        }


    }

    /**
     * 处理请求
     *
     * @param requestProcessorChain 责任链
     * @return 返回一定成功
     * @throws Exception 处理出错会抛出异常
     */
    @Override
    public boolean process(RequestProcessorChain requestProcessorChain) throws Exception {
        //1.解析HttpServletRequest的请求方法,请求路径，获取对应的ControllerMethod实例
        String method = requestProcessorChain.getRequestMethod();//获取请求的方法类型
        String path = requestProcessorChain.getRequestPath();//获取请求路径
        ControllerMethod controllerMethod = this.pathControllerMethodMap.get(new RequestPathInfo(method, path));//查找对应的路径和方法
        if (controllerMethod == null) {//找不到请求路径
            requestProcessorChain.setResultRender(new ResourceNotFoundResultRender(method, path));
            return false;
        }
        //2.解析请求参数，并传递给获取到的ControllerMethod实例去执行
        Object result = invokeControllerMethod(controllerMethod, requestProcessorChain.getRequest());
        //3.根据处理的结果，选择对应的render进行渲染
        setResultRender(result, controllerMethod, requestProcessorChain);
//        System.out.println("controllerMethod");
        return false;
    }

    /**
     * 根据处理的结果，选择对应的render进行渲染
     *
     * @param result                处理的结果
     * @param controllerMethod      执行的Controller及其方法
     * @param requestProcessorChain 请求处理链
     */
    private void setResultRender(Object result, ControllerMethod controllerMethod, RequestProcessorChain requestProcessorChain) {
        if (result == null) {
            return;
        }
        ResultRender resultRender;
        boolean isJson = controllerMethod.getInvokeMethod().isAnnotationPresent(ResponseBody.class);
        if (isJson) {
            resultRender = new JsonResultRender(result);
        } else {
            resultRender = new ViewResultRender(result);
        }
        requestProcessorChain.setResultRender(resultRender);
    }

    /**
     * 解析请求参数，并传递给获取到的ControllerMethod实例去执行
     *
     * @param controllerMethod 需要执行的Controller配置
     * @param request          http请求
     * @return 处理的结果
     */
    private Object invokeControllerMethod(ControllerMethod controllerMethod, HttpServletRequest request) {
        //1.从请求里获取GET或者POST的参数名及其对应的值
        Map<String, String> requestParamMap = new HashMap<>();
        System.out.println(requestParamMap);
        //GET，POST方法的请求参数获取方式
        Map<String, String[]> parameterMap = request.getParameterMap();
//        System.out.println("parameterMap");
//        System.out.println(parameterMap);
        for (Map.Entry<String, String[]> parameter : parameterMap.entrySet()) {
            if (!ValidationUtil.isEmpty(parameter.getValue())) {
                //只支持一个参数对应一个值的形式
                requestParamMap.put(parameter.getKey(), parameter.getValue()[0]);
            }
        }
        //2.根据获取到的请求参数名及其对应的值，以及controllerMethod里面的参数和类型的映射关系，去实例化出方法对应的参数
        List<Object> methodParam = new ArrayList<>();
        Map<String, Class<?>> methodParameterMap = controllerMethod.getMethodParameters();

        for (String paramName : methodParameterMap.keySet()) {
            Class<?> type = methodParameterMap.get(paramName);
            String requestValue = requestParamMap.get(paramName);
            Object value;
            //只支持String 以及基础类型char,int,short,byte,double,long,float,boolean,及它们的包装类型
            if (requestValue == null) {
                //将请求里的参数值转成适配于参数类型的空值
                value = ConverterUtil.primitiveNull(type);
            } else {
                value = ConverterUtil.convert(type, requestValue);
            }
            methodParam.add(value);
        }
        //3.执行Controller里面对应的方法并返回结果
        Object controller = beanContainer.getBean(controllerMethod.getControllerClass());
        Method invokeMethod = controllerMethod.getInvokeMethod();
        invokeMethod.setAccessible(true);
        Object result;
        try {
            if (methodParam.size() == 0) {
                result = invokeMethod.invoke(controller);
            } else {
                result = invokeMethod.invoke(controller, methodParam.toArray());
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            //如果是调用异常的话，需要通过e.getTargetException()
            // 去获取执行方法抛出的异常
            throw new RuntimeException(e.getTargetException());
        }
        return result;
    }


}