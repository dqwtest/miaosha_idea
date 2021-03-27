package com.example.miaosha.controller;
import com.example.miaosha.common.result.AbstractResult;
import com.example.miaosha.common.result.HttpResult;
import com.example.miaosha.domain.User;
import com.example.miaosha.redis.RedisService;
import com.example.miaosha.redis.UserKey;
import com.example.miaosha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @RequestMapping("/get/{userId}")
    @ResponseBody
    public HttpResult<User> getUserById(@PathVariable("userId") Integer id) {
        User user = userService.getById(id);
        HttpResult<User> result = HttpResult.build();
        if (null != user) {
            result.success(user);
        } else {
            result.withError("user not found");
        }
        return result;
    }

    @RequestMapping("/redis/get")
    @ResponseBody
    public HttpResult<Long> redisGet() {
        Long v1 = redisService.get(UserKey.getById, "key1", Long.class);
        HttpResult<Long> result = HttpResult.build();
        if (v1 != null) {
            result.success(v1);
        }
        return result;
    }


    @RequestMapping("/redis/set")
    @ResponseBody
    public HttpResult<String> redisSet() {
        User user = new User();
        user.setId(2);
        user.setName("testUser");
        boolean ret = redisService.set(UserKey.getById, ""+2, user);
        User user2 = redisService.get(UserKey.getById, ""+2, User.class);
        HttpResult<String> result = HttpResult.build();
        if (user2 != null) {
            result.success(user2.toString());
        }
        return result;
    }
}
