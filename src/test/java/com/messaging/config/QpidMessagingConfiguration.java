package com.messaging.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.List;

/**
 * Created by sanjaya on 10/22/16.
 */
@Configuration
public class QpidMessagingConfiguration {

    public final int port = 5000;
    public final String username = "guest";
    public final String password = "guest";

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public QpidBroker qpidBroker() {
        return new QpidBroker(port);
    }

    @Bean
    @DependsOn("qpidBroker")
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost", port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        return connectionFactory;
    }

    @Bean
    @DependsOn("qpidBroker")
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory,
                                   List<DirectExchange> exchanges, List<Queue> queues,
                                   List<Binding> bindings) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        exchanges.forEach(exchange -> rabbitAdmin.declareExchange(exchange));
        queues.forEach(queue -> rabbitAdmin.declareQueue(queue));
        bindings.forEach(binding -> rabbitAdmin.declareBinding(binding));
        return rabbitAdmin;
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setMessageConverter(jsonMessageConverter());
        template.setChannelTransacted(true);
        return template;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
