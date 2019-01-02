package com.jesper.seckill.aop;

import com.jesper.seckill.annotation.AnnotationType;
import com.jesper.seckill.util.ThreadPoolUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

/**
 * Spring AOP实现日志管理
 * @author Exrickx
 */
@Aspect
@Component
public class SystemLogAspect {

    private Logger log= LoggerFactory.getLogger(SystemLogAspect.class);

    private static final ThreadLocal<Date> beginTimeThreadLocal = new NamedThreadLocal<Date>("ThreadLocal beginTime");
    @Autowired(required=false)
    private HttpServletRequest request;

    /**
     * Controller层切点,注解方式
     */
    //@Pointcut("execution(* *..controller..*Controller*.*(..))")
    @Pointcut("@annotation(com.jesper.seckill.annotation.AnnotationType)")
    public void controllerAspect() {
        log.info("========controllerAspect===========");
    }

    /**
     * Service层切点,注解方式
     */
    @Pointcut("execution(* com.jesper.seckill.service..*.*(..))")
    public void serviceAspect() {
        log.info("========ServiceAspect===========");
    }


    /**
     * 前置通知 (在方法执行之前返回)用于拦截Controller层记录用户的操作的开始时间
     * @param joinPoint 切点
     * @throws InterruptedException
     */
    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint) throws InterruptedException{

        //线程绑定变量（该数据只有当前请求的线程可见）
        Date beginTime=new Date();
        beginTimeThreadLocal.set(beginTime);
    }


    /**
     * 后置通知(在方法执行之后返回) 用于拦截Controller层操作
     * @param joinPoint 切点
     */
    @After("controllerAspect()")
    public void after(JoinPoint joinPoint){
        try {
        	
            long beginTime = beginTimeThreadLocal.get().getTime();
            long endTime = System.currentTimeMillis();
            Long logElapsedTime = endTime - beginTime;

        	log.info("========请求地址==========="+request.getRequestURL());
        	log.info("========请求开始时间==========="+beginTime);
        	log.info("========请求结束时间==========="+endTime);

               
                //请求开始时间
                Date logStartTime = beginTimeThreadLocal.get();

            
                //请求耗时
              

                //调用线程保存至数据库
                ThreadPoolUtil.getPool().execute(new SaveSystemLogThread());
        } catch (Exception e) {
            log.error("AOP后置通知异常", e);
        }
    }

    /**
     * 异常通知 用于拦截service层记录异常日志
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(pointcut="serviceAspect()", throwing="e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {

    
      
                //调用线程保存至数据库
                ThreadPoolUtil.getPool().execute(new SaveSystemLogThread());
        
        

    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     * @param joinPoint 切点
     * @return 方法描述
     * @throws Exception
     */
    public static String getControllerMethodDescription(JoinPoint joinPoint) throws Exception{
        //获取目标类名
        String targetName = joinPoint.getTarget().getClass().getName();
        //获取方法名
        String methodName = joinPoint.getSignature().getName();
        //获取相关参数
        Object[] arguments = joinPoint.getArgs();
        //生成类对象
        Class targetClass = Class.forName(targetName);
        //获取该类中的方法
        Method[] methods = targetClass.getMethods();

        String type = "";

        for(Method method : methods) {
            if(!method.getName().equals(methodName)) {
                continue;
            }
            Class[] clazzs = method.getParameterTypes();
            if(clazzs.length != arguments.length) {
                //比较方法中参数个数与从切点中获取的参数个数是否相同，原因是方法可以重载哦
                continue;
            }
            type = method.getAnnotation(AnnotationType.class).type();
        }
        return type;
    }

    /**
     * 保存日志
     */
    private static class SaveSystemLogThread implements Runnable {

        @Override
        public void run() {

           
        }
    }
   
}
