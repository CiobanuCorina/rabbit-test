package com.example.rabbit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/send")
@RequiredArgsConstructor
public class SendController {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    private static final String EXCHANGE_NAME = "myExchange";

    @GetMapping("/ge1")
    public ResponseEntity<String> send1() {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "my.key", "Hello from RabbitMQ!");
        return ResponseEntity.ok("Message sent to the RabbitMQ Successfully");
    }
}
