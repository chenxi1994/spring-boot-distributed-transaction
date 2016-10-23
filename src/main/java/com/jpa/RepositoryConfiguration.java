package com.jpa;

import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by sanjaya on 10/23/16.
 */
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.jpa")
@EntityScan(basePackages = "com.jpa")
@Configuration
public class RepositoryConfiguration {
}
