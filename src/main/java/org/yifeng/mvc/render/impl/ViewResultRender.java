package org.yifeng.mvc.render.impl;


import org.yifeng.mvc.processor.RequestProcessorChain;
import org.yifeng.mvc.render.ResultRender;
import org.yifeng.mvc.type.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


public class ViewResultRender implements ResultRender {
    public static final String VIEW_PATH = "/templates/";
    private ModelAndView modelAndView;


    public ViewResultRender(Object mv) {
        if (mv  instanceof ModelAndView){
            this.modelAndView= (ModelAndView) mv;
        }else if(mv instanceof String){
            new ModelAndView().setView((String)mv);
        }else {
            throw new RuntimeException("illegal request result type");
        }

    }

    @Override
    public void render(RequestProcessorChain requestProcessorChain) throws Exception {
        HttpServletRequest httpServletRequest= requestProcessorChain.getRequest();
        HttpServletResponse httpServletResponse=requestProcessorChain.getResponse();

        // 获取视图路径
        String path=modelAndView.getView();
        Map<String,Object> model=modelAndView.getModel();
        for (Map.Entry<String,Object> entry:model.entrySet()){
            httpServletRequest.setAttribute(entry.getKey(),entry.getValue());
        }
        httpServletRequest.getRequestDispatcher(VIEW_PATH +path).forward(httpServletRequest,httpServletResponse);
    }
}
