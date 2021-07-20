package com.example.babacirclecommunity.weChatPay.rabbitmq;

import com.example.babacirclecommunity.weChatPay.dao.OrderMapper;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author MQ
 * @date 2021/6/30 17:03
 */
//@Component
//@RabbitListener(queues = "q.order.dlx")
public class TimeoutOrderListener {
/**
    @Autowired
    private OrderMapper orderMapper;

    @RabbitHandler
    public void onMessage(@Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag,String id, Channel channel) throws IOException {
        System.out.println(id+"订单超时");
        int i = orderMapper.updateOrderStatus(2, id);
        if(i<=0){
            System.out.println("修改状态失败");
        }
        //确认消息
        channel.basicAck(deliveryTag,true);

    }
    */
}
