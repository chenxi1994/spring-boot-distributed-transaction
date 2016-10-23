package com.messaging.config;

import com.jpa.Employee;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

/**
 * Created by sanjaya on 10/22/16.
 */
@Component
public class TestReceiverOne {


    private CountDownLatch latch = new CountDownLatch(1);

    private String message;

    @RabbitListener(queues = "one")
    public void receiveMessage(Employee data) {
        this.message = data.getName();
        latch.countDown();
    }

    public String getMessage() {
        return message;
    }

    public CountDownLatch getLatch() {
        return latch;
    }

}
