package com.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by sanjaya on 10/23/16.
 */
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    public Employee findByName(String name);
}