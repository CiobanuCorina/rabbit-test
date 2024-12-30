package com.example.rabbit;

import com.example.rabbit.service.RabbitTestListener;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.amqp.rabbit.test.mockito.LatchCountDownAndCallRealMethodAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.willAnswer;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class RabbitProxyTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitListenerTestHarness harness;

    @Test
    public void testProxiedListenerSpy() throws Exception {
        RabbitTestListener listener = this.harness.getSpy("myQueue1");
        assertThat(listener).isNotNull();

        LatchCountDownAndCallRealMethodAnswer answer = this.harness.getLatchAnswerFor("myQueue1", 1);
        willAnswer(answer).given(listener).listen(anyString());

        this.rabbitTemplate.convertAndSend("myQueueTest", "foo");

        assertThat(answer.await(10)).isTrue();
        verify(listener).listen("foo");
        assertThat(answer.getExceptions()).isEmpty();
    }
}
