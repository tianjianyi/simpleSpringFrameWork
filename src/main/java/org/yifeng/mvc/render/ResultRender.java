package org.yifeng.mvc.render;


import org.yifeng.mvc.processor.RequestProcessorChain;

public interface ResultRender {
    void render(RequestProcessorChain requestProcessorChain) throws Exception;
}
