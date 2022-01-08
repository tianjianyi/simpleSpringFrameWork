package org.yifeng.mvc.processor;

import lombok.Data;
import org.yifeng.mvc.render.ResultRender;
import org.yifeng.mvc.render.impl.DefaultResultRender;
import org.yifeng.mvc.render.impl.InternalErrorResultRender;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
/*
 * 1.以责任链的模式执行注册的请求处理器
 * 2.委派给特定的Render实例对处理后的结果进行渲染
 */
@Data
public class RequestProcessorChain {

    // 请求处理迭代器
    private Iterator<RequestProcessor> requestProcessorIterator;
    private HttpServletRequest request;
    private HttpServletResponse response;
    // http请求方法
    private String requestMethod;
    // http请求路径
    private String requestPath;
    // http响应状态码
    private int responseCode;
    // 请求结果渲染器
    private ResultRender resultRender;


    public RequestProcessorChain(Iterator<RequestProcessor> iterator, HttpServletRequest req, HttpServletResponse resp) {
        this.requestProcessorIterator=iterator;
        this.request=req;
        this.response=resp;

        this.requestMethod=req.getMethod();
        this.requestPath=req.getPathInfo();
        this.responseCode=HttpServletResponse.SC_OK;
    }
    /*
     * 责任链模式执行请求链
     */
    public void doRequestProcessorChain() {
        // 通过迭代器遍历注册的请求处理器实现类列表
        try{
            while (requestProcessorIterator.hasNext()){
                // 直到某个请求处理器执行后返回false为止
                if(!requestProcessorIterator.next().process(this)){
                    break;
                }
            }
        }catch (Exception e){
            // 期间如果出现异常，则交由内部处理异常渲染器处理
            this.resultRender=new InternalErrorResultRender(e.getMessage());
            System.out.println("doRequestProcessorChain error");
        }
    }

    public void doRender() {
        // 如果请求器处理实现类均未选择合适的渲染器，则使用默认
        if (this.resultRender==null){
            this.resultRender=new DefaultResultRender();
        }

        // 调用渲染器的render方法对结果进行渲染
        try {
            this.resultRender.render(this);
        } catch (Exception e) {
            System.out.println("doRender error:");
            throw new RuntimeException(e);
        }
    }
}
