package com.com.service;

import com.jpa.Employee;
import com.jpa.EmployeeRepository;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by sanjaya on 10/23/16.
 */
@Component
public class TransactionalService {

    @Autowired
    AmqpTemplate amqpTemplate;

    @Autowired
    EmployeeRepository employeeRepository;


    @Transactional
    public void saveAndSendMessage(Employee employee) {
        employeeRepository.save(employee);
        amqpTemplate.convertAndSend("one", employee);
    }

}
