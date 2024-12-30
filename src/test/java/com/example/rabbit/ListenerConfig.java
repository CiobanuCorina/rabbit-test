package com.example.rabbit;

import com.example.rabbit.service.RabbitTestListener;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.test.RabbitListenerTest;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RabbitListenerTest(capture = true)
public class ListenerConfig {
    private static final String QUEUE1 = "myQueueTest1";
    private static final String QUEUE2 = "myQueueTest2";
//    private static final String EXCHANGE = "myExchange";
    @Bean
    public RabbitTestListener listener() {
        return new RabbitTestListener();
    }
    @Bean
    public Queue queue1() {
        return new AnonymousQueue();
    }

    @Bean
    public Queue queue2() {
        return new AnonymousQueue();
    }


//    @Bean
//    public Exchange exchange() {
//        return new TopicExchange(EXCHANGE, false, false);
//    }
//
//    @Bean
//    public Binding binding(Queue queue, Exchange exchange) {
//        return BindingBuilder.bind(queue).to(exchange).with("my.key").noargs();
//    }
}
