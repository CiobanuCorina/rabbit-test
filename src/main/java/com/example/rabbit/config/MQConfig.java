//package com.example.rabbit.config;
//
//import org.springframework.amqp.core.*;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class MQConfig {
//    private static final String QUEUE = "myQueue";
//    private static final String EXCHANGE = "myExchange";
//
//    @Bean
//    public Queue myQueue() {
//        return new Queue(QUEUE, false);
//    }
//
//    @Bean
//    public Exchange exchange() {
//        return new TopicExchange(EXCHANGE, false, false);
//    }
//
//    @Bean
//    public Binding binding(Queue queue, Exchange exchange) {
//        return BindingBuilder.bind(queue).to(exchange).with("my.key").noargs();
//    }
//}
