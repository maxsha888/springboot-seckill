package com.jesper.seckill.controller;
import com.alibaba.dubbo.config.annotation.Reference;
import com.jesper.seckill.annotation.AnnotationType;
import com.jesper.seckill.aop.SystemLogAspect;
import com.jesper.seckill.api.DemoService;
import com.jesper.seckill.rabbitmq.MQSender;
import com.jesper.seckill.redis.RedisKey;
import com.jesper.seckill.redis.RedisService;
import com.jesper.seckill.result.Result;
import com.jesper.seckill.service.GoodsService;
import com.jesper.seckill.util.JedisUtil;
import com.jesper.seckill.vo.GoodsVo;

import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by jiangyunxiong on 2018/5/18.
 */
@Controller
@RequestMapping("/demo")
public class DemoController {
	
	 private Logger log= LoggerFactory.getLogger(DemoController.class);

	 
	 @Autowired
	    GoodsService goodsService;
	//若是用dubbo 注解 用 @Reference
	@Autowired
    DemoService demoService;
    @Autowired
    RedisService redisService;


    @Autowired
    MQSender sender;

    @AnnotationType(type="session")
    @RequestMapping("/test")
    @ResponseBody
    public Result<String> mq() {
        demoService.test();
        log.info("redis test");
        return Result.success("Hello，world"); 
    }
    
    /**
     * redis 测试
     * @return
     */
    @RequestMapping("/redis")
    @ResponseBody
    public Result<String> redisTest() {
       
    	
    	  //查询全部商品列表
        List<GoodsVo> goodsList = goodsService.getGoodsList();
        for (GoodsVo goods:goodsList){
        	JedisUtil.zadd(RedisKey.TestKey, goods.getGoodsStock(), goods.getGoodsName());//zadd库存排行
        }
    	
         Set<String> set=JedisUtil.zrangeByScore(RedisKey.TestKey, 0, 100);
         String setString=RedisService.beanToString(set);
         
        return Result.success("Hello，world"+setString); 
    }

//    @RequestMapping("/mq/topic")
//    @ResponseBody
//    public Result<String> topic() {
//        sender.sendTopic("hello,imooc");
//        return Result.success("Hello，world");
//    }
//    @Autowired
//    UserService userService;
//
//    @RequestMapping("/")
//    @ResponseBody
//    String home() {
//        return "Hello World";
//    }
//
//    @RequestMapping("/hello")
//    @ResponseBody
//    public Result<String> hello() {
//        return Result.success("hello, Jesper");
//    }
//
//    @RequestMapping("/Error")
//    @ResponseBody
//    public Result<String> error() {
//        return Result.error(CodeMsg.SERVER_ERROR);
//    }
//
//    @RequestMapping("/Thymeleaf")
//    public String thymeleaf(Model model) {
//        model.addAttribute("name", "Jesper");
//        return "hello";
//    }
//
//    @RequestMapping("/redis/get")
//    @ResponseBody
//    public Result<User> redisGet() {
//        User user = redisService.get(UserKey.getById, ""+1, User.class);
//        return Result.success(user);
//    }
//
//    @RequestMapping("/redis/set")
//    @ResponseBody
//    public Result<Boolean> redisSet() {
//        User user = new User();
//        user.setId(1);
//        user.setName("Jesper");
//        Boolean b1 = redisService.set(UserKey.getById, ""+1, user);
//        return Result.success(b1);
//    }
//
//    @RequestMapping("/db/doubleInsert")
//    @ResponseBody
//    public Result<Boolean> doubleInsert() {
//        try {
//            userService.doubleInsert();
//            return Result.success(true);
//        } catch (Exception e) {
//            return Result.error(CodeMsg.PRIMARY_ERROR);
//        }
//    }
//
//    @RequestMapping("/db/get")
//    @ResponseBody
//    public Result<User> dbGet() {
//        User user = userService.getById(1);
//        return Result.success(user);
//    }

}
