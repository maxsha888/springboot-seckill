package com.jesper.seckill.service;
import com.jesper.seckill.dubbo_api.DemoService;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;


/**
 * Created by jiangyunxiong on 2018/5/22.
 */
@Service(interfaceClass = DemoService.class)
@Component
public class DemoServiceImpl implements DemoService{


    public static final String COOKIE_NAME_TOKEN = "token";

    public void test() {
    	System.out.println("demoService");
    }


}
