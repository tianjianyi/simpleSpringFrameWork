package org.yifeng.mvc.render.impl;

import org.yifeng.mvc.processor.RequestProcessorChain;
import org.yifeng.mvc.render.ResultRender;

import javax.servlet.http.HttpServletResponse;

public class ResourceNotFoundResultRender implements ResultRender {
    private String httpMethod;
    private String httpPath;


    public ResourceNotFoundResultRender(String method, String path) {
        this.httpMethod = method;
        this.httpPath = path;
    }

    @Override
    public void render(RequestProcessorChain requestProcessorChain) throws Exception {
        requestProcessorChain.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND,
                "获取不到对应的请求资源，请求路径【"+httpPath+"】请求方法【"+httpMethod+"】");
    }
}
