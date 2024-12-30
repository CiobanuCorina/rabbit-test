package com.example.rabbit.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

public class RabbitTestListener {
    private boolean failed;

    @RabbitListener(id = "myQueue1", queues = "#{queue1.name}")
    public void listen(String in) {
        System.out.println("Message read from myQueue1 : " + in);
        if (!failed && in.equals("ex")) {
            failed = true;
            throw new RuntimeException(in);
        }
        failed = false;
    }

    @RabbitListener(id = "myQueue2", queues = "#{queue2.name}")
    public String listenWithResponse(String in) {
        System.out.println("Message read from myQueue2 : " + in);
        return in.toUpperCase();
    }
}
