package com.example.rabbit;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.amqp.rabbit.test.TestRabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness.InvocationData;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext
public class RabbitTestCapture {
    @Autowired
    private TestRabbitTemplate rabbitTemplate;

    @Autowired
    private Queue queue1;

    @Autowired
    private Queue queue2;

    @Autowired
    private RabbitListenerTestHarness harness;

    @Test
    public void testTwoWay() throws Exception {
        assertThat(this.rabbitTemplate.convertSendAndReceive(this.queue2.getName(), "foo")).isEqualTo("FOO");

        InvocationData invocationData = this.harness.getNextInvocationDataFor("myQueue2", 10, TimeUnit.SECONDS);
        assertThat(invocationData).isNotNull();
        assertThat((String) invocationData.getArguments()[0]).isEqualTo("foo");
        assertThat((String) invocationData.getResult()).isEqualTo("FOO");
    }

    @Test
    public void testOneWay() throws Exception {
        this.rabbitTemplate.convertAndSend(this.queue1.getName(), "bar");
        this.rabbitTemplate.convertAndSend(this.queue1.getName(), "baz");
        this.rabbitTemplate.convertAndSend(this.queue1.getName(), "ex");

        InvocationData invocationData = this.harness.getNextInvocationDataFor("myQueue1", 10, TimeUnit.SECONDS);
        assertThat(invocationData).isNotNull();
        Object[] args = invocationData.getArguments();
        assertThat((String) args[0]).isEqualTo("bar");

        invocationData = this.harness.getNextInvocationDataFor("myQueue1", 10, TimeUnit.SECONDS);
        assertThat(invocationData).isNotNull();
        args = invocationData.getArguments();
        assertThat((String) args[0]).isEqualTo("baz");

        invocationData = this.harness.getNextInvocationDataFor("myQueue1", 10, TimeUnit.SECONDS);
        assertThat(invocationData).isNotNull();
        args = invocationData.getArguments();
        assertThat((String) args[0]).isEqualTo("ex");
        assertThat(invocationData.getThrowable()).isNotNull();
        assertThat(invocationData.getThrowable().getMessage()).isEqualTo("ex");

        invocationData = this.harness.getNextInvocationDataFor("myQueue1", 10, TimeUnit.SECONDS);
        assertThat(invocationData).isNotNull();
        args = invocationData.getArguments();
        assertThat((String) args[0]).isEqualTo("ex");
        assertThat(invocationData.getThrowable()).isNull();
    }
}
