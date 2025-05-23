//package com.aixming.zhida.rabbitmq;
//
//import com.rabbitmq.client.Channel;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//
///**
// * @author AixMing
// * @since 2025-05-14 21:40:12
// */
//@Component
//public class CodeQuestionSubmitMessageConsumer {
//
//    @Resource
//    private RabbitTemplate rabbitTemplate;
//
//
//    @RabbitListener(queues = {"ai_code_queue"}, ackMode = "MANUAL")
//    public void receiveMessage(String message, Channel channel) {
////        rabbitTemplate.convertAndSend(exchange, routingKey, message);
//    }
//
//}
