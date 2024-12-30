package com.example.rabbit;

import com.example.rabbit.service.RabbitTestListener;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.amqp.rabbit.test.TestRabbitTemplate;
import org.springframework.amqp.rabbit.test.mockito.LatchCountDownAndCallRealMethodAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.willAnswer;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RabbitTestSpy {
    @Autowired
    private TestRabbitTemplate rabbitTemplate;

    @Autowired
    private Queue queue1;

    @Autowired
    private Queue queue2;

    @Autowired
    private RabbitListenerTestHarness harness;

    @Test
    public void testTwoWay() {
        assertThat(this.rabbitTemplate.convertSendAndReceive(this.queue2.getName(), "foo")).isEqualTo("FOO");

        RabbitTestListener listener = this.harness.getSpy("myQueue2");
        assertThat(listener).isNotNull();
        verify(listener).listenWithResponse("foo");
    }

    @Test
    public void testOneWay() throws Exception {
        RabbitTestListener listener = this.harness.getSpy("myQueue1");
        assertThat(listener).isNotNull();

        LatchCountDownAndCallRealMethodAnswer answer = this.harness.getLatchAnswerFor("myQueue1", 3);
        willAnswer(answer).given(listener).listen(anyString());

        this.rabbitTemplate.convertAndSend(this.queue1.getName(), "bar");
        this.rabbitTemplate.convertAndSend(this.queue1.getName(), "baz");
        this.rabbitTemplate.convertAndSend(this.queue1.getName(), "ex");

        assertThat(answer.await(10)).isTrue();
        verify(listener).listen("bar");
        verify(listener).listen("baz");
        verify(listener).listen("ex");
        willThrow(new RuntimeException("ex")).given(listener).listen("ex");
    }
}
