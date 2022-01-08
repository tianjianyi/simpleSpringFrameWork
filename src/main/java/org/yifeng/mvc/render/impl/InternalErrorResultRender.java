package org.yifeng.mvc.render.impl;


import org.yifeng.mvc.processor.RequestProcessorChain;
import org.yifeng.mvc.render.ResultRender;

import javax.servlet.http.HttpServletResponse;

public class InternalErrorResultRender implements ResultRender {
    private String errorMsg;

    public InternalErrorResultRender(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public void render(RequestProcessorChain requestProcessorChain) throws Exception {
        requestProcessorChain.getResponse().sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,this.errorMsg);
    }
}
