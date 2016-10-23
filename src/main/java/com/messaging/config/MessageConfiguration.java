package com.messaging.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.amqp.core.BindingBuilder.bind;

/**
 * Created by sanjaya on 10/22/16.
 */
@Configuration
@EnableTransactionManagement
@EnableRabbit
public class MessageConfiguration {


    @Value("${queue.name.one}")
    String queueOne;


    @Value("${dead.letter.queue.one}")
    String deadLetterQueueOne;


    @Value("${dead-letter-exchange-name}")
    String deadLetterExchange;


    @Bean
    @Value("${global.exchange}")
    DirectExchange exchange(String exchangeName) {
        return new DirectExchange(exchangeName);
    }

    @Bean
    DirectExchange deadLetterExchange() {
        return new DirectExchange(deadLetterExchange);
    }

    @Bean
    Queue deadLetterQueueOne() {
        return new Queue(deadLetterQueueOne, true);
    }

    @Bean
    Queue queueOne() {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("x-dead-letter-exchange", deadLetterExchange);
        args.put("x-dead-letter-routing-key", deadLetterQueueOne);
        return new Queue(queueOne, false, false, false, args);
    }

    @Bean
    Binding bindingDeadLetterOne(Queue deadLetterQueueOne, DirectExchange deadLetterExchange,
                                 @Value("${dead.letter.queue.one}") String routingKey) {
        return bind(deadLetterQueueOne).to(deadLetterExchange).with(routingKey);
    }


    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory cachingConnectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(cachingConnectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        factory.setChannelTransacted(true);
        return factory;
    }


    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }


}
