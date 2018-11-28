package com.jesper.seckill.service;
import com.jesper.seckill.dubbo_api.UserService;
import com.alibaba.druid.util.StringUtils;
import com.jesper.seckill.dubbo_dto.User;
import com.jesper.seckill.exception.GlobalException;
import com.jesper.seckill.mapper.UserMapper;
import com.jesper.seckill.redis.RedisService;
import com.jesper.seckill.redis.UserKey;
import com.jesper.seckill.result.CodeMsg;
import com.jesper.seckill.util.MD5Util;
import com.jesper.seckill.util.UUIDUtil;
import com.jesper.seckill.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by jiangyunxiong on 2018/5/22.
 */
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserMapper userMapper;

    @Autowired
    RedisService redisService;

    public static final String COOKIE_NAME_TOKEN = "token";

    public User getById(long id) {
        //对象缓存
        User user = redisService.get(UserKey.getById, "" + id, User.class);
        if (user != null) {
            return user;
        }
        //再存入缓存
        if (user != null) {
            redisService.set(UserKey.getById, "" + id, user);
        }
        return user;
    }


}
