package com.messaging.config;

import com.Application;
import com.jpa.Employee;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

/**
 * Created by sanjaya on 10/22/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class, QpidMessagingConfiguration.class})
@DirtiesContext
public class QpidTest {


    @Autowired
    TestReceiverOne testReceiverOne;



    @Autowired
    private AmqpTemplate amqpTemplate;

    @Test
    public void sendMessageQueueOne() throws Exception {
        // given
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setName("Allen");;

        // when
        amqpTemplate.convertAndSend("one", employee);

        // wait
        while (testReceiverOne.getLatch().getCount() != 0) {
        }

        // assert
        assertEquals("Allen", testReceiverOne.getMessage());


    }

}
