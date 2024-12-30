package com.example.rabbit;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.junit.RabbitAvailable;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@RabbitAvailable(queues = "rabbitAvailableTests.queue", management = true)
public class RabbitMockTest {
    @Test
    public void test(ConnectionFactory connectionFactory) throws Exception {
        Connection conn = connectionFactory.newConnection();
        Channel channel = conn.createChannel();
        AMQP.Queue.DeclareOk declareOk = channel.queueDeclarePassive("rabbitAvailableTests.queue");
        assertThat(declareOk.getConsumerCount()).isEqualTo(0);
        channel.close();
        conn.close();
    }
}
