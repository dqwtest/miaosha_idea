package com.example.miaosha.redis;

import com.example.miaosha.domain.OrderInfo;
import com.example.miaosha.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
/** * 监听所有db的过期事件__keyevent@*__:expired" */

@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

    @Autowired
    OrderService orderService;

    @Autowired
    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    /**
     * 针对 redis 数据失效事件，进行数据处理
     * * @param message
     * * @param pattern
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        // 获取到失效的 key，进行取消订单业务处理
        // key 设置为订单信息
        String expiredKey = message.toString();
        // 如果订单信息
        if(expiredKey.startsWith(String.valueOf(OrderKey.getMiaoshaOrderByUidGid))) {
            OrderInfo orderInfo = RedisService.stringToBean(expiredKey, OrderInfo.class);
            System.out.println(expiredKey);
            // 取消订单
            orderService.deleteOrder(orderInfo);
            // 恢复库存

        }
    }
}

