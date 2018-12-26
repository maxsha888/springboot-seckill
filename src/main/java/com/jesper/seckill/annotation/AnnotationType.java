package com.jesper.seckill.annotation;

import java.lang.annotation.*;

/**
 * 标志该注解类型 
 *
 * @author shyf
 * @email 939961241@qq.com
 * @date 2017年10月23日 下午13:13:23
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AnnotationType {

    /**
     * 注解类型
     */
    String type() default "";

 
}
