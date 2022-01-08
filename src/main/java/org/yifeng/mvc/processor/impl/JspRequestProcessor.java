package org.yifeng.mvc.processor.impl;


import org.yifeng.mvc.processor.RequestProcessor;
import org.yifeng.mvc.processor.RequestProcessorChain;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;

public class JspRequestProcessor implements RequestProcessor {

    private static final String JSP_SERVLET = "jsp";
    private static final String JSP_RESOURCE_PREFIX = "/templates";

    public RequestDispatcher jspServlet;
    public JspRequestProcessor(ServletContext servletContext) {
        jspServlet = servletContext.getNamedDispatcher(JSP_SERVLET);
        if(null == jspServlet){
            throw new RuntimeException("no jsp servlet");
        }
    }

    @Override
    public boolean process(RequestProcessorChain requestProcessorChain) throws Exception {
        if(isJspResource(requestProcessorChain.getRequestPath())){
            jspServlet.forward(requestProcessorChain.getRequest(), requestProcessorChain.getResponse());
            return false;
        }
        return true;
    }

    private boolean isJspResource(String url) {
        return url.startsWith(JSP_RESOURCE_PREFIX);
    }
}
