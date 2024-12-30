package com.example.rabbit;

import com.github.fridujo.rabbitmq.mock.compatibility.MockConnectionFactoryFactory;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.test.TestRabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.mock;

@Configuration
public class RabbitMqConfiguration {

//    @Bean
//    ConnectionFactory connectionFactory() {
//        return new CachingConnectionFactory(MockConnectionFactoryFactory.build());
//    }

    @Bean
    public TestRabbitTemplate template() throws IOException {
        return new TestRabbitTemplate(connectionFactory());
    }

    @Bean
    public ConnectionFactory connectionFactory() throws IOException {
        ConnectionFactory factory = mock(ConnectionFactory.class);
        Connection connection = mock(Connection.class);
        Channel channel = mock(Channel.class);
        AMQP.Queue.DeclareOk declareOk = mock(AMQP.Queue.DeclareOk.class);
        willReturn(connection).given(factory).createConnection();
        willReturn(channel).given(connection).createChannel(anyBoolean());
        given(channel.isOpen()).willReturn(true);
        given(channel.queueDeclare(anyString(), anyBoolean(), anyBoolean(), anyBoolean(), anyMap()))
                .willReturn(declareOk);
        return factory;
    }

}
