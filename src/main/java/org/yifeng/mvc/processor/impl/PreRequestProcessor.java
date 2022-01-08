package org.yifeng.mvc.processor.impl;


import org.yifeng.mvc.processor.RequestProcessor;
import org.yifeng.mvc.processor.RequestProcessorChain;

public class PreRequestProcessor  implements RequestProcessor {

    @Override
    public boolean process(RequestProcessorChain requestProcessorChain) throws Exception {
        // 设置请求编码，将其统一设置成utf-8
        requestProcessorChain.getRequest().setCharacterEncoding("UTF-8");

        // 将请求路径的末尾/删除，为后续匹配controller做准备
        String requestPath=requestProcessorChain.getRequestPath();
        if (requestPath.length()>1&&requestPath.endsWith("/")){
            requestProcessorChain.setRequestPath(requestPath.substring(0,requestPath.length()-1));
        }
        System.out.println("preprocess request: " + requestProcessorChain.getRequestMethod()
                +" " + requestProcessorChain.getRequestPath());
//        log.info("preprocess request {} {}",requestProcessorChain.getRequestMethod(),requestProcessorChain.getRequestPath());
        return true;
    }
}
