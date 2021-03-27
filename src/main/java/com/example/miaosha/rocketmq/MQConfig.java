package com.example.miaosha.rocketmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MQConfig {

    /**
     * /usr/sbin/rabbitmq-plugins enable rabbitmq_management
     * mq页面
     */
    public static final String MIAOSHA_QUEUE = "miaosha.queue";
    public static final String ORDER_QUEUE = "miaosha.queue";

    public static final String QUEUE = "queue";
    public static final String TOPIC_QUEUE1 = "topic.queue1";
    public static final String TOPIC_QUEUE2 = "topic.queue2";
    public static final String TOPIC_EXCHANGE = "topicExchange";
    public static final String ROUTE_KEY1= "topic.key1";
    public static final String ROUTE_KEY2 = "topic.#";

    @Bean
    public Queue miaoshaQueue() {
        return new Queue(MIAOSHA_QUEUE, true);
    }

    @Bean
    public Queue queue() {
        // durable 开启持久化
        return new Queue(QUEUE, true);
    }

    /**
     * Topic 模式
     */
    @Bean
    public Queue topicQueue1() {
        // durable 开启持久化
        return new Queue(TOPIC_QUEUE1, true);
    }

    @Bean
    public Queue topicQueue2() {
        // durable 开启持久化
        return new Queue(TOPIC_QUEUE2, true);
    }

    @Bean
    public TopicExchange topicExchange() {
        // durable 开启持久化
        return new TopicExchange(TOPIC_EXCHANGE);
    }

    // 绑定队列和路由，通过KEY进行信息转发
    @Bean
    public Binding topicBinding1() {
        return BindingBuilder.bind(topicQueue1())
                .to(topicExchange()).with(ROUTE_KEY1);
    }

    @Bean
    public Binding topicBinding2() {
        return BindingBuilder.bind(topicQueue2())
                .to(topicExchange()).with(ROUTE_KEY2);
    }
}
