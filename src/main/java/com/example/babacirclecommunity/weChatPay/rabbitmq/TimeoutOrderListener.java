package com.example.babacirclecommunity.weChatPay.rabbitmq;

import com.example.babacirclecommunity.weChatPay.dao.OrderMapper;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author MQ
 * @date 2021/6/30 17:03
 */
@Component
@RabbitListener(queues = "q.order.dlx")
public class TimeoutOrderListener {

    @Autowired
    private OrderMapper orderMapper;

    @RabbitHandler
    public void onMessage(String id){
        System.out.println(id+"订单超时");
        int i = orderMapper.updateOrderStatus(2, id);
        if(i<=0){
            System.out.println("修改状态失败");
        }
    }
}
