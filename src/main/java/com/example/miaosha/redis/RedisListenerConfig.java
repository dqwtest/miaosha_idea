package com.example.miaosha.redis;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
public class RedisListenerConfig {

    @Bean
    RedisMessageListenerContainer container(@Qualifier("redisConnectionFactory")
                                                    RedisConnectionFactory connectionFactory) {
        return new RedisMessageListenerContainer();
    }

}


