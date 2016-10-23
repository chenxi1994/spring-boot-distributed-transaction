package com.service;

import com.Application;
import com.com.service.TransactionalService;
import com.jpa.Employee;
import com.jpa.EmployeeRepository;
import com.messaging.config.QpidMessagingConfiguration;
import com.messaging.config.TestReceiverOne;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by sanjaya on 10/23/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class, QpidMessagingConfiguration.class})
@DirtiesContext
public class TransactionalServiceTest {

    @Autowired
    TransactionalService transactionalService;

    @Autowired
    TestReceiverOne testReceiverOne;

    @Autowired
    EmployeeRepository employeeRepository;


    @Test
    public void testSaveAndSendMessage() {
        Employee employee = new Employee();
        employee.setName("Tom");
        transactionalService.saveAndSendMessage(employee);
        while (testReceiverOne.getLatch().getCount() != 0) {
        }
        assertEquals("Tom", testReceiverOne.getMessage());

        Employee employee1 = employeeRepository.findByName("Tom");
        assertEquals("Tom", employee1.getName());
        assertNotNull(employee1.getId());
    }

    @Test(expected = RuntimeException.class)
    public void testTransactionWhenDBFailed() {

        Employee employee = new Employee();
        transactionalService.saveAndSendMessage(employee);

        while (testReceiverOne.getLatch().getCount() != 0) {
        }
        assertNull(testReceiverOne.getMessage());

        Employee employee1 = employeeRepository.findByName("Tom");
        assertNull(employee1.getName());
        assertNull(employee1.getId());
    }

}
