package com.example.miaosha.controller;

import com.example.miaosha.common.result.HttpResult;
import com.example.miaosha.rocketmq.MQSender;
import com.example.miaosha.redis.RedisService;
import com.example.miaosha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/sample")
public class SampleController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private MQSender sender;


    @RequestMapping("/mq")
    @ResponseBody
    public HttpResult<String> mq() {
        sender.send("hello world");
        HttpResult<String> result = HttpResult.build();
        result.success("hello, world");
        return result;
    }

    @RequestMapping("/mq1")
    @ResponseBody
    public HttpResult<String> mq1() {
        sender.sendTopic("hello topic");
        HttpResult<String> result = HttpResult.build();
        result.success("topic1");
        return result;
    }
}
