package com.messaging.receiver;

import com.jpa.Employee;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
public class MessageListener {


    @RabbitListener(queues = "one")
    public void receiveMessageQueueOne(Employee data) {
        System.out.println("Received data : " + data.getName());
    }


}
