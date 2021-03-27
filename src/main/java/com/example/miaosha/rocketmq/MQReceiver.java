package com.example.miaosha.rocketmq;

import com.example.miaosha.domain.MiaoshaOrder;
import com.example.miaosha.domain.MiaoshaUser;
import com.example.miaosha.redis.RedisService;
import com.example.miaosha.service.GoodsService;
import com.example.miaosha.service.MiaoshaService;
import com.example.miaosha.service.OrderService;
import com.example.miaosha.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQReceiver {

    private Logger logger = LoggerFactory.getLogger(MQReceiver.class);

    @Autowired
    AmqpTemplate amqpTemplate;

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;

    @RabbitListener(queues = MQConfig.MIAOSHA_QUEUE)
    public void receive(String message) {
        logger.info("receive message: " + message);
        MiaoshaMessage mm = RedisService.stringToBean(message, MiaoshaMessage.class);
        MiaoshaUser user = mm.getUser();
        long goodsId = mm.getGoodsId();

        // 判断是否已经秒杀到了
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoods(user.getId(), goodsId);
        if(order != null) {
            return;
        }

        // 减库存 下订单 写入秒杀订单
        miaoshaService.miaosha(user, goodsId);
//        if (orderInfo == null) {
//            return;
//        }
//        return orderInfo;
    }

//    @RabbitListener(queues = MQConfig.QUEUE)
//    public void receive(String message) {
//        // String msg = RedisService.stringToBean(message, );
//        logger.info("receive message: " + message);
//    }
//
//    @RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
//    public void receive1(String message) {
//        // String msg = RedisService.stringToBean(message, );
//        logger.info("queue 1 receive message: " + message);
//    }
//
//    @RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
//    public void receive2(String message) {
//        // String msg = RedisService.stringToBean(message, );
//        logger.info("queue 2 receive message: " + message);
//    }
}
