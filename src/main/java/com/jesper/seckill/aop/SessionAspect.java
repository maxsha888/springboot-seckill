package com.jesper.seckill.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 数据过滤，切面处理类
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017年10月23日 下午13:33:35
 */
@Aspect
@Component
public class SessionAspect {

    /**
     * 切点
     */
    @Pointcut("@annotation(com.jesper.seckill.annotation.AnnotationType)")
    public void dataFilterCut() {

    }

    /**
     * 前置通知
     *
     * @param point 连接点
     */
    @Before("dataFilterCut()")
    public void sessionCtl(JoinPoint point) {
    		System.out.println("xxxxxx before");
            point.getSignature();
            point.getArgs();
            point.getTarget();
        }


   
}
