package org.yifeng.mvc.render.impl;


import org.yifeng.mvc.processor.RequestProcessorChain;
import org.yifeng.mvc.render.ResultRender;

public class DefaultResultRender implements ResultRender {

    @Override
    public void render(RequestProcessorChain requestProcessorChain) throws Exception {
        requestProcessorChain.getResponse().setStatus(requestProcessorChain.getResponseCode());
    }
}
