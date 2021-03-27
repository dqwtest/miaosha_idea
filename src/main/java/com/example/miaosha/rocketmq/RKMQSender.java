package com.example.miaosha.rocketmq;

import com.example.miaosha.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RKMQSender {

    private Logger logger = LoggerFactory.getLogger(RKMQSender.class);

    public void send(Object message) {
        String msg = RedisService.beanToString(message);
        logger.info("send message: " + msg);
    }

    public void sendTopic(Object message) {
        String msg = RedisService.beanToString(message);
        logger.info("send topic message: " + msg);
    }

    public void sendMiaoshaMessage(MiaoshaMessage mm) {
        String msg = RedisService.beanToString(mm);
        logger.info("send message: " + msg);
    }
}
