package org.yifeng.mvc;


import org.yifeng.aop.AspectWeaver;
import org.yifeng.core.BeanContainer;
import org.yifeng.inject.DependencyInject;
import org.yifeng.mvc.processor.RequestProcessor;
import org.yifeng.mvc.processor.RequestProcessorChain;
import org.yifeng.mvc.processor.impl.ControllerRequestProcessor;
import org.yifeng.mvc.processor.impl.JspRequestProcessor;
import org.yifeng.mvc.processor.impl.PreRequestProcessor;
import org.yifeng.mvc.processor.impl.StaticResourceRequestProcessor;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * 1:拦截所有请求(@WebServlet("/")可以拦截所有请求，×.jsp请求除外)，
 * 2：解析请求，3：派发给对应controller的方法里进行处理
 */
@WebServlet("/*")
public class DispatcherServlet extends HttpServlet {
    private final List<RequestProcessor> PROCESSOR = new ArrayList<>();


    @Override
    public void init() throws ServletException {
        // 初始化容器
        BeanContainer beanContainer=BeanContainer.getInstance();
        beanContainer.loadBeans("com.yifeng");
        new AspectWeaver().doAop();
        new DependencyInject().doIoc();

        // 初始化请求处理器
        PROCESSOR.add(new PreRequestProcessor());
        PROCESSOR.add(new StaticResourceRequestProcessor(getServletContext()));
        PROCESSOR.add(new JspRequestProcessor(getServletContext()));
        PROCESSOR.add(new ControllerRequestProcessor());

    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 创建责任链对象实例
        RequestProcessorChain requestProcessorChain=new RequestProcessorChain(PROCESSOR.iterator(),req,resp);

        // 通过责任链模式来依次调用处理器对请求进行处理
        requestProcessorChain.doRequestProcessorChain();

        // 对处理的结果进行渲染
        requestProcessorChain.doRender();

    }
}










