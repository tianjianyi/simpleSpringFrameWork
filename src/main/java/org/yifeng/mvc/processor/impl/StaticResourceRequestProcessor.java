package org.yifeng.mvc.processor.impl;


import org.yifeng.mvc.processor.RequestProcessor;
import org.yifeng.mvc.processor.RequestProcessorChain;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;

/*
 * 静态资源请求处理
 */
public class  StaticResourceRequestProcessor implements RequestProcessor {

    public static final String DEFAULT_TOMCAT_SERVLET = "default";
    public static final String STATIC_RESOURCE_PREFIX = "/static/";
    private RequestDispatcher requestDispatcher;

    public StaticResourceRequestProcessor(ServletContext servletContext){
        this.requestDispatcher=servletContext.getNamedDispatcher(DEFAULT_TOMCAT_SERVLET);
        if (this.requestDispatcher==null){
            throw new RuntimeException("there is no default tomcat servlet");
        }
        System.out.println("the default servlet for static resource is {}"+ DEFAULT_TOMCAT_SERVLET);
//        log.info("the default servlet for static resource is {}", DEFAULT_TOMCAT_SERVLET);
    }

    @Override
    public boolean process(RequestProcessorChain requestProcessorChain) throws Exception {
        // 通过请求路径判断是否是请求的静态资源(webapp/static)，如果是则转发给default servlet处理
        if (isStaticResource(requestProcessorChain.getRequestPath())){
            System.out.println("111111");
            requestDispatcher.forward(requestProcessorChain.getRequest(),requestProcessorChain.getResponse());
            return false;
        }
        return true;
    }

    private boolean isStaticResource(String requestPath) {
        return requestPath.startsWith(STATIC_RESOURCE_PREFIX);
    }
}








