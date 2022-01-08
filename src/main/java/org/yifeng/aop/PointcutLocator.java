package org.yifeng.aop;

import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.ShadowMatch;

import java.lang.reflect.Method;

/**
 * 解析Aspect表达式并且定位被织入目标
 */
public class PointcutLocator {

    /**
     * Pointcut解析器，直接给它赋值上Aspectj的所有表达式，以便对众多表达式的解析
     */
    private PointcutParser pointcutParser=PointcutParser.getPointcutParserSupportingSpecifiedPrimitivesAndUsingContextClassloaderForResolution(
            PointcutParser.getAllSupportedPointcutPrimitives()
    );

    /**
     * 表达式解析器
     */
    private PointcutExpression pointcutExpression;

    public PointcutLocator(String expression){
        this.pointcutExpression=pointcutParser.parsePointcutExpression(expression);
    }


    /**
     * 判断传入的Class对象是否是Aspect的目标代理类，即匹配Pointcut表达式（出筛）
     * @param targetClass
     * @return
     */
    public boolean roughMatches(Class<?> targetClass){
        /**
         * couldMatchJoinPointsInType 只能校验 within
         * 不能校验 execution,call,get,set
         */
        return pointcutExpression.couldMatchJoinPointsInType(targetClass);
    }

    /**
     * 判断传入的method对象是否是aspect 的目标代理方法，即匹配pointcut表达式（精筛）
     * @param method
     * @return
     */
    public boolean accurateMatches(Method method){
        ShadowMatch shadowMatch=pointcutExpression.matchesMethodExecution(method);
        if(shadowMatch.alwaysMatches()){
            return true;
        }
        return false;
    }


}



