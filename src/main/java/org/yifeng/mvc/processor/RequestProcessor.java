package org.yifeng.mvc.processor;



public interface RequestProcessor {

    boolean process(RequestProcessorChain requestProcessorChain) throws Exception;
}
