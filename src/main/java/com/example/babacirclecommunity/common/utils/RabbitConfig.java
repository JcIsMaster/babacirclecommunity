package com.example.babacirclecommunity.common.utils;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author MQ
 * @date 2021/6/29 16:33
 */
@Configuration
public class RabbitConfig {

    @Bean
    public Queue queue(){
        Map<String,Object> props =new HashMap<>(5);
        // 消息的生存时间 10s
        props.put("x-message-ttl", 10000);
        // 设置该队列所关联的死信交换器（当队列消息到期后依然没有消费，则加入死信 队列）
        props.put("x-dead-letter-exchange", "ex.order.dlx");
        // 设置该队列所关联的死信交换器的routingKey，如果没有特殊指定，使用原队列的routingKey
        props.put("x-dead-letter-routing-key", "order.dlx");
        Queue queue = new Queue("q.order", true, false, false, props);
        return queue;
    }




}
